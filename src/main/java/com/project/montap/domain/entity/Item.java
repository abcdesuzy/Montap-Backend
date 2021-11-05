package com.project.montap.domain.entity;

import com.project.montap.enums.ItemType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_IDX")
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
