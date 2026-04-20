package com.cafeflow.config;

import com.cafeflow.entities.MenuItemEntity;
import com.cafeflow.repositories.MenuItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;

    public DataSeeder(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) {
        if (menuItemRepository.count() > 0) {
            return;
        }

        List<MenuItemEntity> menus = Arrays.asList(
                createMenu("Espresso", "Kopi", new BigDecimal("18000"), 50, "https://images.unsplash.com/photo-1510707577719-ae7c14805e3a"),
                createMenu("Cappuccino", "Kopi", new BigDecimal("26000"), 40, "https://images.unsplash.com/photo-1534778101976-62847782c213"),
                createMenu("Caramel Latte", "Kopi", new BigDecimal("30000"), 35, "https://images.unsplash.com/photo-1461023058943-07fcbe16d735"),
                createMenu("Iced Americano", "Non-Kopi", new BigDecimal("22000"), 45, "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085"),
                createMenu("Matcha Latte", "Non-Kopi", new BigDecimal("28000"), 30, "https://images.unsplash.com/photo-1515823064-d6e0c04616a7"),
                createMenu("Chocolate", "Non-Kopi", new BigDecimal("25000"), 40, "https://images.unsplash.com/photo-1572490122747-3968b75cc699"),
                createMenu("Croissant", "Snack", new BigDecimal("17000"), 20, "https://images.unsplash.com/photo-1555507036-ab794f4afe5a"),
                createMenu("French Fries", "Snack", new BigDecimal("19000"), 25, "https://images.unsplash.com/photo-1576107232684-1279f390859f"),
                createMenu("Brownies", "Snack", new BigDecimal("21000"), 22, "https://images.unsplash.com/photo-1606313564200-e75d5e30476b")
        );

        menuItemRepository.saveAll(menus);
    }

    private MenuItemEntity createMenu(String name, String category, BigDecimal price, int stock, String imageUrl) {
        MenuItemEntity menu = new MenuItemEntity();
        menu.setName(name);
        menu.setCategory(category);
        menu.setPrice(price);
        menu.setStock(stock);
        menu.setImageUrl(imageUrl);
        menu.setActive(true);
        return menu;
    }
}
