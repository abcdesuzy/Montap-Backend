package com.project.montap.controller;

import com.project.montap.domain.repository.StageLogRepository;
import com.project.montap.dto.*;
import com.project.montap.exception.Error;
import com.project.montap.security.service.AccountContext;
import com.project.montap.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class StageController {

    @Autowired
    StageService stageService;

    @Autowired
    StageLogRepository stageLogRepository;

    @GetMapping("/mystage")
    public ResponseEntity getMyStage(@AuthenticationPrincipal AuthUserDto authUserDto) throws Exception{
        List<MyStageDto> result = stageService.getMyStage(authUserDto.getUserIdx());
        System.out.println("확인" + authUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/stage/{stageIdx}")
    public ResponseEntity getStage(@PathVariable Long stageIdx){
        try {
            StageDto stageDto = stageService.getStage(stageIdx);
            return ResponseEntity.status(HttpStatus.OK).body(stageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }

    @PostMapping("/mystage/clear")
    public ResponseEntity clearStage(@RequestBody ClearStageDto clearStageDto){
        try {
            ClearStageDto result = stageService.clearStage(clearStageDto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(e.getMessage()));
        }
    }

}
