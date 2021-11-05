package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InventoryItemDto {
    // InventoryItem Entity Dto
    private Long idx;
    private Long userIdx;
    private Long itemIdx;
}
