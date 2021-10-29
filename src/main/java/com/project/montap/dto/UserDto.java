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
public class UserDto {
    Long idx;
    String userId;
    String userPwd;
    String nickname;
    String email;
    int money;
    int stage;
    int hp;
    int defense;
    int damage;
    String role;
    List<InventoryItem> inventoryItemDtoList = new ArrayList<>();
}
