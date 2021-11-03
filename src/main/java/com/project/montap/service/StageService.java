package com.project.montap.service;

import com.project.montap.domain.entity.Stage;
import com.project.montap.domain.entity.StageLog;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.StageRepository;
import com.project.montap.domain.repository.StageLogRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.ClearStageDto;
import com.project.montap.dto.MyStageDto;
import com.project.montap.dto.StageDto;
import com.project.montap.security.service.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    StageLogRepository stageLogRepository;


    @Transactional
    public ClearStageDto clearStage(ClearStageDto clearStageDto) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();
        Long userIdx = authUserDto.getUserIdx();

        Optional<User> optionalFindUser = userRepository.findById(userIdx);
        Optional<Stage> optionalFindStage = stageRepository.findById(clearStageDto.getStageIdx());

        if (optionalFindUser.isEmpty() || optionalFindStage.isEmpty()) {
            throw new Exception("클리어한 스테이지가 없습니다.");
        } else {
            if (optionalFindStage.get().getMonsterHp() != 0 && optionalFindUser.get().getHp() == 0) {
                throw new Exception("게임에서 졌습니다");
            } else {
                User findUser = optionalFindUser.get();
                Stage findStage = optionalFindStage.get();

                StageLog newStageLog = new StageLog();
                newStageLog.setUser(findUser);
                newStageLog.setStage(findStage);
                newStageLog.setClearDate(LocalDateTime.now());
                newStageLog.setIsClear(1);
                stageLogRepository.save(newStageLog);

                int clearMoney = optionalFindUser.get().getMoney() + optionalFindStage.get().getDropMoney();
                optionalFindUser.get().setMoney(clearMoney);

                if (optionalFindUser.get().getStage() < optionalFindStage.get().getStageCount()) {
                    optionalFindUser.get().setStage(optionalFindStage.get().getStageCount());
                }
            }
        }


        return clearStageDto;
    }

    @Transactional
    public List<MyStageDto> getMyStage(Long userIdx) throws Exception {

        Optional<User> optionalUser = userRepository.findById(userIdx);

        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }
        User user = optionalUser.get();
        List<MyStageDto> myStage = stageRepository.findByMyStage(user.getIdx());

        return myStage;
    }

    @Transactional
    public StageDto getStage(Long stageIdx) throws Exception {
        Optional<Stage> optionalStage = stageRepository.findById(stageIdx);
        if (optionalStage.isEmpty()) {
            throw new Exception("해당 스테이지가 없습니다.");
        }
        Stage stage = optionalStage.get();
        StageDto stageDto = new StageDto();
        stageDto.setIdx(stage.getIdx());
        stageDto.setStageCount(stage.getStageCount());
        stageDto.setMonsterName(stage.getMonsterName());
        stageDto.setMonsterDamage(stage.getMonsterDamage());
        stageDto.setIsBoss(stage.getIsBoss());
        stageDto.setDropMoney(stage.getDropMoney());
        stageDto.setMonsterUrl(stage.getMonsterUrl());
        return stageDto;
    }

}




