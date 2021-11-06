package com.project.montap.controller;

import com.project.montap.domain.entity.Item;
import com.project.montap.dto.*;
import com.project.montap.dto.GetItemDto;
import com.project.montap.dto.ItemDto;
import com.project.montap.dto.SellingItemDto;
import com.project.montap.dto.InventoryItemListDto;

import com.project.montap.exception.Error;
import com.project.montap.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    // 아이템 획득하기
    @PostMapping( "/inventory/item" )
    public ResponseEntity getItemToMyInventory(@RequestBody GetItemDto getItemDto) {
        try {
            GetItemDto result = inventoryService.getItemToInventory(getItemDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 내 인벤토리 전체 아이템 리스트
    @GetMapping( "/inventory/item/all" )
    public ResponseEntity inventoryItemAllList() {
        try {
            List<InventoryItemListDto> result = inventoryService.inventoryItemAllList();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 인벤토리 미장착 아이템 리스트
    @GetMapping( "/inventory/item" )
    public ResponseEntity inventoryItemList() {
        try {
            List<InventoryItemListDto> result = inventoryService.inventoryItemList();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 내 인벤토리 top 10 아이템 리스트
    @GetMapping( "/inventory/item/get" )
    public ResponseEntity inventoryItemTopList() {
        try {
            List<InventoryItemListDto> result = inventoryService.inventoryItemTopList();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 아이템 상세정보
    @GetMapping( "/item/{itemIdx}" )
    public ResponseEntity getItemInfo(@PathVariable Long itemIdx) {
        try {
            ItemDto itemDto = inventoryService.getItemInfo(itemIdx);
            return ResponseEntity.status(HttpStatus.OK).body(itemDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }

    // 아이템 판매
    @PostMapping( "/item/sell" )
    public ResponseEntity sellItem(@RequestBody SellingItemDto sellingItemDto) {
        try {
            int result = inventoryService.sellItem(sellingItemDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }

    // 아이템 뽑기
    @PostMapping( "/draw" )
    public ResponseEntity drawItem(@RequestBody DrawingItemDto drawingItemDto) {
        try {
            List<Item> count = inventoryService.drawItem(drawingItemDto);
            return ResponseEntity.status(HttpStatus.OK).body(count);
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }


}
