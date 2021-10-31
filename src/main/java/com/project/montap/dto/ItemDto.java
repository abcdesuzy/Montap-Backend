package com.project.montap.dto;

import com.project.montap.enums.ItemType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long idx;
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

