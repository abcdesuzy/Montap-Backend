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

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    InventoryItemRepository inventoryItemRepository;

    @Autowired
    UserRepository userRepository;

    public GetItemDto getItemToInventory(GetItemDto getItemDto) throws Exception {

        // 1. 획득할 아이템을 찾는다.
        Optional<Item> optionalFindItem = itemRepository.findById(getItemDto.getItemIdx());

        // 2. 현재 접속중인 유저를 찾는다.
        Optional<User> optionalFindUser = userRepository.findById(getItemDto.getUserIdx());

        // - 유저나 획득할 아이템이 없는 경우 -> 에러
        if (optionalFindItem.isEmpty() || optionalFindUser.isEmpty()) {
            throw new Exception("사용자 혹은 해당하는 아이템이 없습니다.");
        } else {
            User findUser = optionalFindUser.get();
            Item findItem = optionalFindItem.get();

            // 3. 찾은 아이템을 inventory_item 테이블에 넣는다.
            InventoryItem newInventoryItem = new InventoryItem(findUser, findItem);
            inventoryItemRepository.save(newInventoryItem);
            return getItemDto;
        }
    }
}
