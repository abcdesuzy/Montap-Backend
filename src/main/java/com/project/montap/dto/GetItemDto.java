package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetItemDto {
    Long userIdx;
    Long itemIdx;
}
