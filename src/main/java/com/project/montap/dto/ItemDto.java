package com.project.montap.dto;

import com.project.montap.enums.ItemType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    // Item Entity Dto
    Long itemIdx;
    String name;
    ItemType itemType;
    int itemRank;
    int price;
    int hp;
    int defense;
    int damage;
    String description;
    String itemUrl;
}

