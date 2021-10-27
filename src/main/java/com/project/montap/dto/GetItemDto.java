package com.project.montap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetItemDto {

    Long itemIdx;
    Long userIdx;

}
