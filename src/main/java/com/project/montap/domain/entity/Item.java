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
    Long idx; // 아이템 Id
    String name; // 아이템 이름
    ItemType itemType; // 아이템 타입 : 0(Helmet), 1(Armor), 2(Weapon)
    int itemRank; // 아이템 등급 : 0(S), 1(A), 2(B), 3(C)
    int price; // 아이템 가격
    int hp; // 아이템 체력
    int defense; // 아이템 방어력
    int damage; // 아이템 공격력
    String description; // 아이템 설명
    String itemUrl; // 아이템 이미지 주소

}
