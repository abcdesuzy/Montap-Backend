package com.project.montap.service;

import com.project.montap.domain.entity.InventoryItem;
import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.GetItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    InventoryItemRepository inventoryItemRepository;

    @Autowired
    UserRepository userRepository;

    // 아이템 획득
    public GetItemDto getItemToInventory(GetItemDto getItemDto) throws Exception {

        // 1. 현재 유저를 찾는다.
        Optional<User> optionalFindUser = userRepository.findById(getItemDto.getUserIdx());
        // 2. 획득할 아이템을 찾는다.
        Optional<Item> optionalFindItem = itemRepository.findById(getItemDto.getItemIdx());

        // - 유저나 얻을 아이템이 없는 경우 >>> 에러
        if (optionalFindItem.isEmpty() || optionalFindUser.isEmpty()) {
            throw new Exception("사용자 혹은 해당하는 아이템이 없습니다.");
        } else {
            User findUser = optionalFindUser.get();
            Item findItem = optionalFindItem.get();

            // 3. 찾은 아이템을 [inventory_item] 테이블에 넣는다.
            InventoryItem newInventoryItem = new InventoryItem();
            newInventoryItem.setUser(findUser);
            newInventoryItem.setItem(findItem);
            newInventoryItem.setEquipYn(0);
            inventoryItemRepository.save(newInventoryItem);
            return getItemDto;
        }
    }

    // 획득한 아이템 리스트
    @Transactional
    public List<Item> getItemInventoryList(Long userIdx) throws Exception {

        // 1. 장착한 유저의 정보를 가져온다.
        Optional<User> optionalUser = userRepository.findById(userIdx);
        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }

        User user = optionalUser.get();
        List<InventoryItem> inventoryItemList = user.getInventoryItemList();
        List<Item> resultList = new ArrayList<>();
        for (int i = 0; i < inventoryItemList.size(); i++) {
            if (inventoryItemList.get(i).getEquipYn() == 0) {
                resultList.add(inventoryItemList.get(i).getItem());
            }
        }
        return resultList;
    }
}
