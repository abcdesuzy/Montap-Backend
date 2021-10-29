package com.project.montap.dto;

import com.project.montap.domain.entity.Item;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EquipmentListDto {
    List<Item> equipmentItemList = new ArrayList<>();
}