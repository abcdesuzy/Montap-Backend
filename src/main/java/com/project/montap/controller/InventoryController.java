package com.project.montap.controller;

import com.project.montap.domain.entity.Item;
import com.project.montap.dto.GetItemDto;
import com.project.montap.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    // 획득한 아이템을 [inventory_item] 에 넣어준다.
    @PostMapping("/inventory/item")
    public ResponseEntity getItemToMyInventory(@RequestBody GetItemDto getItemDto) {
        try {
            GetItemDto result = inventoryService.getItemToInventory(getItemDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 사용자 인벤토리 아이템 조회하기
    @GetMapping("/inventory/item/{userIdx}")
    public ResponseEntity getItemInventoryList(@PathVariable Long userIdx) throws Exception {
        // 서비스를 호출해서 내 인벤토리 목록을 받아온다.
        List<Item> result = inventoryService.getItemInventoryList(userIdx);
        // 클라이언트에게 내 인벤토리 목록을 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
