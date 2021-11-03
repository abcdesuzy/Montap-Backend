package com.project.montap.domain.repository;

import com.project.montap.domain.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    public Optional<InventoryItem> findByItemIdx(Long itemIdx);
    public Optional<List<InventoryItem>> findByItemIdxInAndUserIdx(List<Long> itemIdxList, Long userIdx);
    public void deleteByItemIdxInAndUserIdx(List<Long> itemIdxList, Long userIdx);
    public Optional<List<InventoryItem>> findByUserIdx(Long userIdx);
}