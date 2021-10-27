package com.project.montap.controller;

import com.project.montap.dto.EquipItemDto;
import com.project.montap.dto.UserDto;
import com.project.montap.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

    @PostMapping("/equipment")
    public ResponseEntity equipItem(@RequestBody EquipItemDto equipItemDto) {
        UserDto result = equipmentService.equipItem(equipItemDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
