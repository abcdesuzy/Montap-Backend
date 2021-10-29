package com.project.montap.domain.repository;

import com.project.montap.domain.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    public Optional<InventoryItem> findByItemIdx(Long itemIdx);
}