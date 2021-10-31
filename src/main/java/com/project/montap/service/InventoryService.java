package com.project.montap.service;

import com.project.montap.domain.entity.InventoryItem;
import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.GetItemDto;
import com.project.montap.dto.ItemDto;
import com.project.montap.dto.SellingItemDto;
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

    // 아이템 판매
    @Transactional
    public Integer sellItem(SellingItemDto sellingItemDto) throws Exception {

        // 1. 전달받은 userIdx 에 해당하는 user 를 찾는다.
        List<Long> itemIdxList = sellingItemDto.getItemIdxList();
        Long userIdx = sellingItemDto.getUserIdx();
        Optional<User> optionalUser = userRepository.findById(userIdx);
        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }
        User user = optionalUser.get();

        // 2. 배열로 받은 아이템들이 이 유저의 인벤토리에 있는지 확인한다.
        Optional<List<InventoryItem>> optionalInventoryItemList = inventoryItemRepository.findByItemIdxInAndUserIdx(itemIdxList, userIdx);
        if (optionalInventoryItemList.isEmpty()) {
            throw new Exception("해당하는 아이템이 없습니다.");
        }


        List<InventoryItem> resultInventoryItemList = optionalInventoryItemList.get();
        if (itemIdxList.size() != resultInventoryItemList.size()) {
            throw new Exception("판매하려는 아이템이 없습니다.");
        }
        for (int i = 0; i < resultInventoryItemList.size(); i++) {
            if (resultInventoryItemList.get(i).getEquipYn() == 1) {
                throw new Exception("장착된 아이템은 판매할 수 없습니다.");
            }
        }

        int total = 0;
        for (int i = 0; i < resultInventoryItemList.size(); i++) {
            int money = resultInventoryItemList.get(i).getItem().getPrice();
            total = total + money;
        }
        user.setMoney(user.getMoney() + total);
        userRepository.save(user);
        inventoryItemRepository.deleteByItemIdxInAndUserIdx(itemIdxList, userIdx);

        return user.getMoney();
    }

    // 아이템 상세정보
    @Transactional
    public ItemDto getItemInfo(Long itemIdx) throws Exception {
      Optional<Item> optionalItem = itemRepository.findById(itemIdx);
      if (optionalItem.isEmpty()){
          throw new Exception("없는 아이템 입니다.");
      }
        Item item = optionalItem.get();
        ItemDto itemDto = new ItemDto();
        itemDto.setIdx(item.getIdx());
        itemDto.setName(item.getName());
        itemDto.setItemRank(item.getItemRank());
        itemDto.setItemType(item.getItemType());
        itemDto.setDamage(item.getDamage());
        itemDto.setDefense(item.getDefense());
        itemDto.setDescription(item.getDescription());
        itemDto.setHp(item.getHp());
        itemDto.setPrice(item.getPrice());
        itemDto.setItemUrl(item.getItemUrl());
        return itemDto;
    }
}
