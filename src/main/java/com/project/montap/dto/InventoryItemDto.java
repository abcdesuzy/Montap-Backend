package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InventoryItemDto {
    private Long idx;
    private Long itemIdx;
}
