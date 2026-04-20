package com.cafeflow.repositories;

import com.cafeflow.entities.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    List<MenuItemEntity> findByActiveTrueOrderByCategoryAscNameAsc();
}
