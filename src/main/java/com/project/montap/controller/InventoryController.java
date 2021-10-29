package com.project.montap.controller;

import com.project.montap.dto.GetItemDto;
import com.project.montap.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    // 내가 획득한 아이템울 내 [inventory_item]에 넣어준다.
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
}
