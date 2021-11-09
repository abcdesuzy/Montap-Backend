package com.project.montap.domain.repository;

import com.project.montap.domain.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    public Optional<List<InventoryItem>> findByItemIdxInAndUserIdx(List<Long> itemIdxList, Long userIdx);

    public Optional<List<InventoryItem>> findByUserIdx(Long userIdx);

    public Optional<List<InventoryItem>> findByIdxInAndEquipYn(List<Long> inventoryItemIdxList, int equipYn);

    public Optional<InventoryItem> findByIdxAndEquipYn(Long idx, int equipYn);

    public void deleteByUserIdxAndIdxIn(Long userIdx, List<Long> inventoryItemIdxList);

    public Optional<List<InventoryItem>> findTop20ByUserIdxOrderByDropDateDesc(Long userIdx);
}