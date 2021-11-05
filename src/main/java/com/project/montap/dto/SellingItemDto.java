package com.project.montap.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellingItemDto {
    List<Long> inventoryItemIdxList = new ArrayList<>();
    Long userIdx;
}
