package com.project.montap.dto;

import lombok.*;

// 장비장착하기, POST /equipment
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AfterEquipDto {
    Long itemIdx;
    int hp;
    int defense;
    int damage;
}
