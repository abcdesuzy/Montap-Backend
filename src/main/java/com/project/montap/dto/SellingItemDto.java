package com.project.montap.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SellingItemDto {
    Long userIdx;
    List<Long> itemIdxList;
}
