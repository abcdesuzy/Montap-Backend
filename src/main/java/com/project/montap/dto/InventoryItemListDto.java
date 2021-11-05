package com.project.montap.dto;

import com.project.montap.enums.ItemType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemListDto {
    // inventoryItemIdx 포함한 Dto
    Long inventoryItemIdx;
    Long itemIdx;
    String name;
    String description;
    ItemType itemType;
    int itemRank;
    int hp;
    int defense;
    int damage;
    int price;
    String itemUrl;
}
