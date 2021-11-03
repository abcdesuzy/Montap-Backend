package com.project.montap.service;

import com.project.montap.domain.entity.InventoryItem;
import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.*;
import com.project.montap.enums.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.project.montap.dto.DrawingItemDto;
import com.project.montap.dto.GetItemDto;
import com.project.montap.dto.ItemDto;
import com.project.montap.dto.SellingItemDto;
import org.springframework.stereotype.Service;

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

        // 2-1

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

    // 내 인벤토리 아이템 전체 리스트
    //    @Transactional
    //    public List<Item> getItemInventoryAllList(Long userIdx) throws Exception {
    //
    //        // 1. 사용자를 가져온다.
    //        Optional<User> optionalUser = userRepository.findById(userIdx);
    //        if (optionalUser.isEmpty()) {
    //            throw new Exception("해당하는 유저가 없습니다.");
    //        }
    //
    //        User user = optionalUser.get();
    //        List<InventoryItem> inventoryItemList = user.getInventoryItemList();
    //        List<Item> resultList = new ArrayList<>();
    //        for (int i = 0; i < inventoryItemList.size(); i++) {
    //            resultList.add(inventoryItemList.get(i).getItem());
    //        }
    //        return resultList;
    //}

    // 내 인벤토리 미장착 아이템 리스트
    @Transactional
    public List<Item> getItemInventoryList(Long userIdx) throws Exception {

        // 1. 사용자를 가져온다.
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

        // 현재 로그인 한 유저의 정보 찾기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환
        Optional<User> optionalUser = userRepository.findById(authUserDto.getUserIdx());
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
        if (optionalItem.isEmpty()) {
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

    // 아이템 뽑기
    @Transactional
    public List<Item> drawItem(DrawingItemDto drawingItemDto) throws Exception {
        Long userIdx = drawingItemDto.getUserIdx();
        int count = drawingItemDto.getCount();
        List<Item> resultList = new ArrayList<>();
        if (count != 1 && count != 10) {
            throw new Exception("1회 또는 10회만 가능합니다.");
        }

        // 유저 찾기
        // 현재 로그인 한 유저의 정보 찾기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환
        Optional<User> optionalUser = userRepository.findById(authUserDto.getUserIdx());
        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }

        // 유저와 유저의 재화 꺼내기
        User user = optionalUser.get();
        int money = user.getMoney();
        int drawMoney = 0;

        // 뽑기 금액
        if (count == 1) drawMoney = 1000;
        else drawMoney = 9000;

        if (money < drawMoney) {
            throw new Exception("재화가 부족합니다.");
        }

        // 뽑기 확률
        for (int i = 0; i < count; i++) {
            ItemType itemType = null;
            int itemRank = 0;
            // 아이템 타입 33% 0,1,2
            int number = (int) (Math.random() * 10) % 3;
            if (number == 0) itemType = ItemType.HELMET;
            if (number == 1) itemType = ItemType.ARMOR;
            if (number == 2) itemType = ItemType.WEAPON;
            // 아이템 등급 0,1,2,3
            number = (int) (Math.random() * 100);
            if (0 <= number && number < 5) itemRank = 0;
            if (5 <= number && number < 21) itemRank = 1;
            if (21 <= number && number < 51) itemRank = 2;
            if (51 <= number && number < 100) itemRank = 3;

            Optional<Item> optionalItem = itemRepository.findByItemTypeAndItemRank(itemType, itemRank);
            if (optionalItem.isEmpty()) {
                throw new Exception("해당하는 아이템이 없습니다.");
            }
            Item item = optionalItem.get();

            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setUser(user);
            inventoryItem.setItem(item);
            inventoryItemRepository.save(inventoryItem);

            resultList.add(item);
        }

        user.setMoney(user.getMoney() - drawMoney);
        userRepository.save(user);

        return resultList;
    }
}
