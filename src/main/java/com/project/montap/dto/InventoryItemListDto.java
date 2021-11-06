package com.project.montap.dto;

import com.project.montap.enums.ItemType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemListDto {
    // inventoryItemIdx 포함한 인벤토리 리스트 Dto
    Long inventoryItemIdx;
    Long itemIdx;
    String name;
    ItemType itemType;
    int itemRank;
    int price;
    int hp;
    int defense;
    int damage;
    int equipYn;
    String description;
    String itemUrl;
}
