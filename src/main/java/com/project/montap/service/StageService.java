package com.project.montap.service;

import com.project.montap.domain.entity.Stage;
import com.project.montap.domain.entity.StageLog;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.StageRepository;
import com.project.montap.domain.repository.StageLogRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.ClearStageDto;
import com.project.montap.dto.StageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Optional<User> optionalFindUser = userRepository.findById(clearStageDto.getUserIdx());
        Optional<Stage> optionalFindStage = stageRepository.findById(clearStageDto.getStageIdx());

        if (optionalFindUser.isEmpty() || optionalFindStage.isEmpty()) {
            throw new Exception("클리어한 스테이지가 없습니다.");
        } else {
            //if (optionalFindStage.get().getMonsterHp() == 0) {
            User findUser = optionalFindUser.get();
            Stage findStage = optionalFindStage.get();

            StageLog newStageLog = new StageLog();
            newStageLog.setUser(findUser);
            newStageLog.setStage(findStage);
            newStageLog.setIsClear(1);
            stageLogRepository.save(newStageLog);
            //}

            return clearStageDto;
        }
    }

    @Transactional
    public List<Stage> getMyStage(Long userIdx) throws Exception {

        Optional<User> optionalUser = userRepository.findById(userIdx);
        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }

        User user = optionalUser.get();
        List<StageLog> stageLogList = user.getStageLogList();

        List<Stage> stageList = new ArrayList<>();

        for (int i = 0; i < stageLogList.size(); i++) {
            stageList.add(stageLogList.get(i).getStage());
        }
        return stageList;
    }
}
