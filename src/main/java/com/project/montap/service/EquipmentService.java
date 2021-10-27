package com.project.montap.service;

import com.project.montap.domain.entity.Equipment;
import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.EquipmentRepository;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.EquipItemDto;
import com.project.montap.dto.UserDto;
import com.project.montap.enums.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    public UserDto equipItem(EquipItemDto equipItemDto) {
        // 1. 구매하려는 Item 을 찾는다.
        Optional<Item> optionalFindItem = itemRepository.findById(equipItemDto.getItemIdx());

        // 2. 구매하려는 User 를 찾는다.
        Optional<User> optionalFindUser = userRepository.findById(equipItemDto.getUserIdx());

        if (optionalFindItem.isEmpty() || optionalFindUser.isEmpty()) {
            return null;
        } else {
            Item findItem = optionalFindItem.get();
            User findUser = optionalFindUser.get();

            // 3. 찾은 아이템을 equipment 테이블에 넣는다.
            Equipment newEquipment = new Equipment(findUser, findItem);
            equipmentRepository.save(newEquipment);

            ItemType itemType = newEquipment.getItem().getItemType();
            if (itemType == ItemType.HELMET) {
                int hp = newEquipment.getItem().getHp();
                findUser.setHp(findUser.getHp() + hp);
                userRepository.save(findUser);
            }

            // 아이템 장착 후 유저의 상태만 보내주는 DTO 생성하면 좋음
            UserDto result = findUser.toUserDto();
            return result;
        }
    }
}
