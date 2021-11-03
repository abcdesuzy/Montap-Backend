package com.project.montap.controller;

import com.project.montap.domain.entity.Item;
import com.project.montap.dto.*;
import com.project.montap.dto.DrawingItemDto;
import com.project.montap.dto.GetItemDto;
import com.project.montap.dto.ItemDto;
import com.project.montap.dto.SellingItemDto;

import com.project.montap.exception.Error;
import com.project.montap.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    // 획득한 아이템을 [inventory_item] 에 넣어준다.
    @PostMapping( "/inventory/item" )
    public ResponseEntity getItemToMyInventory(@RequestBody GetItemDto getItemDto) throws Exception {
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
    public ResponseEntity getItemInventoryAllList(@AuthenticationPrincipal AuthUserDto authUserDto) throws Exception {
        // 서비스를 호출해서 내 인벤토리 목록을 받아온다.
        List<Item> result = inventoryService.getItemInventoryList(authUserDto.getUserIdx());//
        // 클라이언트에게 내 인벤토리 목록을 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 인벤토리 미장착 아이템 리스트
    @GetMapping( "/inventory/item" )
    public ResponseEntity getItemInventoryList(@AuthenticationPrincipal AuthUserDto authUserDto) throws Exception {
        // 서비스를 호출해서 내 인벤토리 목록을 받아온다.
        List<Item> result = inventoryService.getItemInventoryList(authUserDto.getUserIdx());//
        // 클라이언트에게 내 인벤토리 목록을 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(result);
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

    // 아이템 뽑기
    @PostMapping( "/draw" )
    public ResponseEntity drawItem(@RequestBody Map<String, Integer> param) {
        try {
            Integer count = param.get("count");
            List<Item> resultList = inventoryService.drawItem(count);
            return ResponseEntity.status(HttpStatus.OK).body(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }
}
