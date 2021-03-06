package com.project.montap.domain.repository;

import com.project.montap.domain.entity.Item;
import com.project.montap.enums.ItemRank;
import com.project.montap.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    public Optional<List<Item>> findByItemTypeAndItemRank(ItemType itemType, ItemRank itemRank);

}

