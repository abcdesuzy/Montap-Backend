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
    // 아이템 판매 Dto
    Long userIdx; // userIdx
    List<Long> inventoryItemIdxList = new ArrayList<>(); // 판매하려는 아이템 리스트 : inventoryItemIdx 로 입력
}
