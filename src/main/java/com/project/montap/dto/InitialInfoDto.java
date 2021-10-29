package com.project.montap.dto;

import com.project.montap.domain.entity.InventoryItem;
import com.project.montap.enums.UserType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InitialInfoDto {

    // 1. 로그인 성공시 프론트에 내려줄 정보 정의
    Long userIdx;
    String userId;
    String nickname;
    String email;
    int money;
    int stage;
    int hp;
    int defense;
    int damage;
    String role;
    List<InventoryItem> inventoryItemList = new ArrayList<>();

    // 2. 프론트개발자랑 이야기한 후에, 필요할 때마다 추가
}
