package com.project.montap.dto;

import com.project.montap.domain.entity.InventoryItem;
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
    // 로그인 성공시 프론트에 내려줄 유저 정보 리스트 (시큐리티 미적용)
    Long userIdx;
    String userId;
    String nickname;
    String email;
    int emailYn;
    int money;
    int stage;
    int hp;
    int defense;
    int damage;
    String userProfileUrl;

    // + 프론트개발자랑 이야기한 후에, 필요할 때마다 추가
}
