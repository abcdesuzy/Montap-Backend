package com.project.montap.dto;

import com.project.montap.domain.entity.Item;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EquipmentListDto {
    // 장착한 아이템 리스트 Dto
    List<Item> equipmentItemList = new ArrayList<>();
}