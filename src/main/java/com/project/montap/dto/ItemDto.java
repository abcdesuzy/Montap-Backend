package com.project.montap.dto;

import com.project.montap.enums.ItemRank;
import com.project.montap.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    // Item Entity Dto
    Long itemIdx;
    String name;
    ItemType itemType;
    ItemRank itemRank;
    int price;
    int hp;
    int defense;
    int damage;
    String description;
    String itemUrl;
}

