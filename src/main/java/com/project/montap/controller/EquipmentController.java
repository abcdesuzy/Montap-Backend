package com.project.montap.controller;

import com.project.montap.dto.AfterEquipDto;
import com.project.montap.dto.EquipItemDto;
import com.project.montap.dto.InventoryItemListDto;
import com.project.montap.exception.Error;
import com.project.montap.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

    // 아이템 장착하기
    @PostMapping("/equipment")
    public ResponseEntity equipItem(@RequestBody EquipItemDto equipItemDto) {
        try {
            AfterEquipDto result = equipmentService.equipItem(equipItemDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }

    // 장착한 아이템 리스트
    @GetMapping("/equipment")
    public ResponseEntity getEquipment() throws Exception {
        try {
            // 서비스를 호출해서 장착한 장비 목록을 받아온다.
            List<InventoryItemListDto> result = equipmentService.getEquipment();
            // 클라이언트에게 장착한 장비 목록을 반환한다.
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }

    // 아이템 장착 해제
    @DeleteMapping("/equipment")
    public ResponseEntity deleteEquipment(@RequestBody EquipItemDto equipItemDto) {
        try {
            // 서비스를 호출해서 장착된 장비를 장착 해제 시킨다.
            AfterEquipDto result = equipmentService.deleteEquipment(equipItemDto);
            // 클라이언트에게 장착 해제한 아이템의 인덱스를 반환한다.
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.getMessage()));
        }
    }
}