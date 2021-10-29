package com.project.montap.dto;

import lombok.*;

// 장비 장착하기에서 사용하는 Dto
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AfterEquipDto {
    Long itemIdx;
    int hp;
    int defense;
    int damage;
}
