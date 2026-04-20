package com.cafeflow.services;

import com.cafeflow.dto.CartItemPayload;
import com.cafeflow.dto.CheckoutRequest;
import com.cafeflow.dto.CheckoutResponse;
import com.cafeflow.dto.InventoryItemResponse;
import com.cafeflow.dto.MenuItemResponse;
import com.cafeflow.dto.MidtransNotification;
import com.cafeflow.dto.TransactionResponse;
import com.cafeflow.entities.MenuItemEntity;
import com.cafeflow.entities.OrderEntity;
import com.cafeflow.entities.OrderItemEntity;
import com.cafeflow.repositories.MenuItemRepository;
import com.cafeflow.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final DateTimeFormatter ORDER_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter MIDTRANS_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final MidtransService midtransService;

    public PaymentService(
            OrderRepository orderRepository,
            MenuItemRepository menuItemRepository,
            MidtransService midtransService
    ) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.midtransService = midtransService;
    }

    @Transactional
    public CheckoutResponse createCheckout(CheckoutRequest request) {
        validateStock(request.items());

        BigDecimal grossAmount = request.items().stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String orderId = generateOrderId();

        OrderEntity order = new OrderEntity();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setGrossAmount(grossAmount);
        order.setStatus("PENDING");

        for (CartItemPayload item : request.items()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setMenuId(item.menuId());
            orderItem.setMenuName(item.name());
            orderItem.setQuantity(item.quantity());
            orderItem.setPrice(item.price());
            orderItem.setSubtotal(item.price().multiply(BigDecimal.valueOf(item.quantity())));
            order.addItem(orderItem);
        }

        orderRepository.save(order);

        MidtransService.SnapTokenResult snapResult = midtransService.requestSnapToken(
                orderId,
                request.customerName(),
                grossAmount,
                request.items()
        );

        return new CheckoutResponse(orderId, snapResult.token(), snapResult.redirectUrl(), order.getStatus());
    }

    @Transactional
    public void applyMidtransNotification(MidtransNotification notification) {
        if (notification.orderId() == null || notification.orderId().trim().isEmpty()) {
            return;
        }

        orderRepository.findByOrderId(notification.orderId()).ifPresent(order -> {
            String previousStatus = order.getStatus();
            String mappedStatus = mapStatus(notification.transactionStatus(), notification.fraudStatus());

            order.setStatus(mappedStatus);
            order.setPaymentType(notification.paymentType());
            order.setTransactionTime(parseMidtransTime(notification.transactionTime()));

            if (!"PAID".equalsIgnoreCase(previousStatus) && "PAID".equalsIgnoreCase(mappedStatus)) {
                decreaseInventory(order);
            }
        });
    }

    public List<MenuItemResponse> getMenuCatalog() {
        return menuItemRepository.findByActiveTrueOrderByCategoryAscNameAsc().stream()
                .map(item -> new MenuItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getCategory(),
                        item.getPrice(),
                        item.getStock(),
                        item.getImageUrl()
                ))
            .collect(Collectors.toList());
    }

    public List<InventoryItemResponse> getInventorySnapshot() {
        return menuItemRepository.findByActiveTrueOrderByCategoryAscNameAsc().stream()
                .map(item -> new InventoryItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getCategory(),
                        item.getStock(),
                        item.getPrice()
                ))
            .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTodayTransactions() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        return orderRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .map(order -> new TransactionResponse(
                        order.getOrderId(),
                        order.getCustomerName(),
                        order.getGrossAmount(),
                        order.getStatus(),
                        order.getPaymentType(),
                        order.getCreatedAt()
                ))
                    .collect(Collectors.toList());
    }

    private void validateStock(List<CartItemPayload> items) {
        for (CartItemPayload item : items) {
            if (item.menuId() == null) {
                continue;
            }

            MenuItemEntity menu = menuItemRepository.findById(item.menuId())
                    .orElseThrow(() -> new IllegalArgumentException("Menu dengan id " + item.menuId() + " tidak ditemukan."));

            if (menu.getStock() < item.quantity()) {
                throw new IllegalArgumentException("Stok menu " + menu.getName() + " tidak mencukupi.");
            }
        }
    }

    private void decreaseInventory(OrderEntity order) {
        for (OrderItemEntity orderItem : order.getItems()) {
            if (orderItem.getMenuId() == null) {
                continue;
            }

            menuItemRepository.findById(orderItem.getMenuId()).ifPresent(menu -> {
                int remainingStock = Math.max(0, menu.getStock() - orderItem.getQuantity());
                menu.setStock(remainingStock);
            });
        }
    }

    private LocalDateTime parseMidtransTime(String transactionTime) {
        if (transactionTime == null || transactionTime.trim().isEmpty()) {
            return LocalDateTime.now();
        }

        try {
            return LocalDateTime.parse(transactionTime, MIDTRANS_TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            return LocalDateTime.now();
        }
    }

    private String mapStatus(String transactionStatus, String fraudStatus) {
        if (transactionStatus == null || transactionStatus.trim().isEmpty()) {
            return "PENDING";
        }

        String normalized = transactionStatus.toLowerCase(Locale.ROOT);
        switch (normalized) {
            case "capture":
                if ("challenge".equalsIgnoreCase(fraudStatus)) {
                    return "CHALLENGE";
                }
                return "PAID";
            case "settlement":
                return "PAID";
            case "pending":
                return "PENDING";
            case "deny":
            case "cancel":
            case "expire":
                return "FAILED";
            case "refund":
            case "partial_refund":
                return "REFUNDED";
            default:
                return "PENDING";
        }
    }

    private String generateOrderId() {
        int randomSuffix = ThreadLocalRandom.current().nextInt(100, 1000);
        String timestamp = LocalDateTime.now().format(ORDER_TIME_FORMATTER);
        return "CAFEAJA-" + timestamp + "-" + randomSuffix;
    }
}
