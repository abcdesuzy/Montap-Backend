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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();
        Optional<User> optionalUser = userRepository.findById(authUserDto.getUserIdx());
        // 2. 획득할 아이템을 찾는다.
        Optional<Item> optionalItem = itemRepository.findById(getItemDto.getItemIdx());

        // 2-1

        // - 유저나 얻을 아이템이 없는 경우 >>> 에러
        if (optionalItem.isEmpty() || optionalUser.isEmpty()) {
            throw new Exception("사용자 혹은 해당하는 아이템이 없습니다.");
        } else {
            User findUser = optionalUser.get();
            Item findItem = optionalItem.get();

            // 인벤토리 300칸 제한 체크
            if (findUser.getInventoryItemList().size() >= 300) {
                throw new Exception("인벤토리가 꽉찼습니다.");
            }

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
    @Transactional
    public List<InventoryItemListDto> inventoryItemAllList() throws Exception {

        // 인증된 사용자
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();
        Long userIdx = authUserDto.getUserIdx();

        // inventoryItemRepository 의 List
        Optional<List<InventoryItem>> optionalInventoryItemList = inventoryItemRepository.findByUserIdx(userIdx);
        if (optionalInventoryItemList.isEmpty()) {
            throw new Exception("내 인벤토리에 해당 아이템이 없습니다.");
        }

        List<InventoryItem> inventoryItemList = optionalInventoryItemList.get();
        List<InventoryItemListDto> resultList = new ArrayList<>();
        for (int i = 0; i < inventoryItemList.size(); i++) {
            Item item = inventoryItemList.get(i).getItem();

            // InventoryItemListDto 만들기
            InventoryItemListDto inventoryItemListDto = new InventoryItemListDto();
            inventoryItemListDto.setInventoryItemIdx(inventoryItemList.get(i).getIdx());
            inventoryItemListDto.setItemIdx(item.getIdx());
            inventoryItemListDto.setName(item.getName());
            inventoryItemListDto.setItemType(item.getItemType());
            inventoryItemListDto.setItemRank(item.getItemRank());
            inventoryItemListDto.setHp(item.getHp());
            inventoryItemListDto.setDefense(item.getDefense());
            inventoryItemListDto.setDamage(item.getDamage());
            inventoryItemListDto.setPrice(item.getPrice());
            inventoryItemListDto.setEquipYn(inventoryItemList.get(i).getEquipYn());
            inventoryItemListDto.setDescription(item.getDescription());
            inventoryItemListDto.setItemUrl(item.getItemUrl());

            resultList.add(inventoryItemListDto);
        }
        return resultList;
    }

    // 내 인벤토리 아이템 미장착 리스트
    @Transactional
    public List<InventoryItemListDto> inventoryItemList() throws Exception {

        // 인증된 사용자
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();
        Long userIdx = authUserDto.getUserIdx();

        // inventoryItemRepository 의 List
        Optional<List<InventoryItem>> optionalInventoryItemList = inventoryItemRepository.findByUserIdx(userIdx);
        if (optionalInventoryItemList.isEmpty()) {
            throw new Exception("내 인벤토리에 해당 아이템이 없습니다.");
        }

        List<InventoryItem> inventoryItemList = optionalInventoryItemList.get();
        List<InventoryItemListDto> resultList = new ArrayList<>();
        for (int i = 0; i < inventoryItemList.size(); i++) {
            if (inventoryItemList.get(i).getEquipYn() == 0) {
                Item item = inventoryItemList.get(i).getItem();

                // InventoryItemListDto 만들기
                InventoryItemListDto inventoryItemListDto = new InventoryItemListDto();
                inventoryItemListDto.setInventoryItemIdx(inventoryItemList.get(i).getIdx());
                inventoryItemListDto.setItemIdx(item.getIdx());
                inventoryItemListDto.setName(item.getName());
                inventoryItemListDto.setItemType(item.getItemType());
                inventoryItemListDto.setItemRank(item.getItemRank());
                inventoryItemListDto.setHp(item.getHp());
                inventoryItemListDto.setDefense(item.getDefense());
                inventoryItemListDto.setDamage(item.getDamage());
                inventoryItemListDto.setPrice(item.getPrice());
                inventoryItemListDto.setEquipYn(inventoryItemList.get(i).getEquipYn());
                inventoryItemListDto.setDescription(item.getDescription());
                inventoryItemListDto.setItemUrl(item.getItemUrl());

                resultList.add(inventoryItemListDto);
            }
        }
        return resultList;
    }

    // 최근 획득장비 10개 가져오기
    public List<InventoryItemListDto> inventoryItemTopList() throws Exception {

        // 인증된 사용자
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal();
        Optional<User> optionalUser = userRepository.findById(authUserDto.getUserIdx());
        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }
        User user = optionalUser.get();

        Optional<List<InventoryItem>> optionalInventoryItemList = inventoryItemRepository.findTop10ByUserIdxOrderByDropDateDesc(user.getIdx());
        List<InventoryItem> inventoryItemList = optionalInventoryItemList.get();
        List<InventoryItemListDto> resultList = new ArrayList<>();
        for (int i = 0; i < inventoryItemList.size(); i++) {
            Item item = inventoryItemList.get(i).getItem();

            // InventoryItemListDto 만들기
            InventoryItemListDto inventoryItemListDto = new InventoryItemListDto();
            inventoryItemListDto.setInventoryItemIdx(inventoryItemList.get(i).getIdx());
            inventoryItemListDto.setItemIdx(item.getIdx());
            inventoryItemListDto.setName(item.getName());
            inventoryItemListDto.setItemType(item.getItemType());
            inventoryItemListDto.setItemRank(item.getItemRank());
            inventoryItemListDto.setHp(item.getHp());
            inventoryItemListDto.setDefense(item.getDefense());
            inventoryItemListDto.setDamage(item.getDamage());
            inventoryItemListDto.setPrice(item.getPrice());
            inventoryItemListDto.setEquipYn(inventoryItemList.get(i).getEquipYn());
            inventoryItemListDto.setDescription(item.getDescription());
            inventoryItemListDto.setItemUrl(item.getItemUrl());

            resultList.add(inventoryItemListDto);
        }
        return resultList;
    }

    // 아이템 판매
    @Transactional
    public Integer sellItem(SellingItemDto sellingItemDto) throws Exception {

        Long userIdx = sellingItemDto.getUserIdx();
        List<Long> inventoryItemIdxList = sellingItemDto.getInventoryItemIdxList();

        User user = null;

        // 1. 사용자를 찾는다.
        Optional<User> optionalUser = userRepository.findById(userIdx);
        if (optionalUser.isEmpty()) {
            throw new Exception("해당 유저가 없습니다.");
        }
        // 해당하는 사용자가 있으면 꺼냄
        user = optionalUser.get();

        // 2. 내 인벤토리에서 해당 아이템리스트의 Idx 를 찾는다.
        Optional<List<InventoryItem>> optionalInventoryItemList = inventoryItemRepository.findByIdxInAndEquipYn(inventoryItemIdxList, 0);
        if (optionalInventoryItemList.isEmpty()) {
            throw new Exception("인벤토리에 해당 아이템이 없습니다.");
        }
        // 해당하는 아이템이 있으면 꺼냄
        List<InventoryItem> inventoryItemList = optionalInventoryItemList.get();

        // 3. 인벤토리에서 가져온 아이템의 개수와 판매하려는 아이템의 개수가 일치하지 않는다면 에러
        if (inventoryItemIdxList.size() != inventoryItemList.size()) {
            throw new Exception("인벤토리에 해당 아이템이 없습니다.");
        }

        // 4. 판매 금액을 계산하고, 해당 유저의 소지금액을 증가시킨다.
        int money = 0;
        for (int i = 0; i < inventoryItemList.size(); i++) {
            money += inventoryItemList.get(i).getItem().getPrice();
        }
        user.setMoney(user.getMoney() + money);
        userRepository.save(user);

        // 5. 인벤토리아이템에서 판매한 아이템을 삭제한다.
        inventoryItemRepository.deleteByUserIdxAndIdxIn(sellingItemDto.getUserIdx(), sellingItemDto.getInventoryItemIdxList());

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
        itemDto.setItemIdx(item.getIdx());
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

        int count = drawingItemDto.getCount();
        // 뽑기 횟수 제한 1 or 10
        List<Item> resultList = new ArrayList<>();
        if (count != 1 && count != 10) {
            throw new Exception("1회 또는 10회만 가능합니다.");
        }

        // 현재 로그인 한 유저의 정보 찾기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환
        Optional<User> optionalUser = userRepository.findById(authUserDto.getUserIdx());
        if (optionalUser.isEmpty()) {
            throw new Exception("해당하는 유저가 없습니다.");
        }
        User user = optionalUser.get();

        // 인벤토리 한도 체크
        List<InventoryItem> inventoryItemList = user.getInventoryItemList();
        if (inventoryItemList.size() + count > 300) {
            throw new Exception("인벤토리가 가득 찼습니다.");
        }

        // 뽑기 금액 계산
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
            // 아이템 등급 0(S),1(A),2(B),3(C)
            number = (int) (Math.random() * 100);
            if (0 <= number && number < 2) itemRank = 0; // 0~1
            if (2 <= number && number < 16) itemRank = 1; // 2~15
            if (16 <= number && number < 51) itemRank = 2; // 16~50
            if (51 <= number && number < 100) itemRank = 3; // 51~99

            Optional<Item> optionalItem = itemRepository.findByItemTypeAndItemRank(itemType, itemRank);
            if (optionalItem.isEmpty()) {
                throw new Exception("해당하는 아이템이 없습니다.");
            }
            Item item = optionalItem.get();

            // 인벤토리 아이템 임시박스에 넣어주기
            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setUser(user);
            inventoryItem.setItem(item);
            // 그리고 저장
            inventoryItemRepository.save(inventoryItem);

            resultList.add(item);
        }

        // 재화 차감 - 저장
        user.setMoney(user.getMoney() - drawMoney);
        userRepository.save(user);

        return resultList;
    }
}
