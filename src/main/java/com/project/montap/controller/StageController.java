package com.project.montap.controller;

import com.project.montap.domain.entity.Stage;
import com.project.montap.domain.entity.StageLog;
import com.project.montap.domain.repository.StageLogRepository;
import com.project.montap.dto.ClearStageDto;
import com.project.montap.dto.MyStageDto;
import com.project.montap.dto.StageDto;
import com.project.montap.exception.Error;
import com.project.montap.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StageController {

    @Autowired
    StageService stageService;

    @Autowired
    StageLogRepository stageLogRepository;


    @GetMapping("/mystage/{userIdx}")
    public ResponseEntity getMyStage(@PathVariable Long userIdx) throws Exception{
       List<MyStageDto> result = stageService.getMyStage(userIdx);
        return ResponseEntity.status(HttpStatus.OK).body(result);
       // return stageService.getMyStage(userIdx);
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
