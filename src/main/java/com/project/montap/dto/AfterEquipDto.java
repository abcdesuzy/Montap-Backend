package com.project.montap.dto;

import com.project.montap.enums.ItemType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AfterEquipDto {
    // 아이템 장착 후 Dto, POST /equipment
    Long itemIdx;
    int hp;
    int defense;
    int damage;
}
