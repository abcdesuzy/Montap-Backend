package com.project.montap.service;

import com.project.montap.domain.entity.InventoryItem;
import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.AfterEquipDto;
import com.project.montap.dto.AuthUserDto;
import com.project.montap.dto.EquipItemDto;
import com.project.montap.dto.InventoryItemListDto;
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
public class EquipmentService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InventoryItemRepository inventoryItemRepository;

    // 아이템 장착하기
    @Transactional
    public AfterEquipDto equipItem(EquipItemDto equipItemDto) throws Exception {

        // 1. 장착 해제하려는 유저 찾기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환
        Optional<User> optionalFindUser = userRepository.findById(authUserDto.getUserIdx());

        // - 유저가 없는 경우
        if (optionalFindUser.isEmpty()) {
            throw new Exception("장착해제 하려는 유저가 없습니다.");
        } else { // - 유저가 DB에 있는 경우

            // 유저 꺼내기
            User findUser = optionalFindUser.get();

            // 전달 받은 inventoryItemIdx, equipYn = 0 인 인벤토리 아이템을 찾아서 장착한다.
            Optional<InventoryItem> optionalInventoryItem = inventoryItemRepository.findByIdxAndEquipYn(equipItemDto.getInventoryItemIdx(), 0);
            if (optionalInventoryItem.isEmpty()) throw new Exception("장착할 수 있는 아이템이 없습니다.");
            InventoryItem inventoryItem = optionalInventoryItem.get();
            inventoryItem.setEquipYn(1); // 0 >> 1 로 변경

            // 장착한 아이템의 능력치만큼 유저의 능력치을 올린다.
            Item item = inventoryItem.getItem();
            ItemType itemType = item.getItemType();
            if (itemType == ItemType.HELMET) findUser.setHp(findUser.getHp() + item.getHp());
            if (itemType == ItemType.WEAPON) findUser.setDamage(findUser.getDamage() + item.getDamage());
            if (itemType == ItemType.ARMOR) findUser.setDefense(findUser.getDefense() + item.getDefense());
            userRepository.save(findUser);

            // 아이템 장착 후 유저의 상태만 보내주는 DTO
            AfterEquipDto result = new AfterEquipDto();
            result.setItemIdx(item.getIdx());
            result.setHp(findUser.getHp());
            result.setDefense(findUser.getDefense());
            result.setDamage(findUser.getDamage());

            return result;
        }
    }

    // 장착한 아이템 리스트
    @Transactional
    public List<InventoryItemListDto> getEquipment() throws Exception {

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
            if (inventoryItemList.get(i).getEquipYn() == 1) {
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

    // 아이템 장착 해제
    @Transactional
    public AfterEquipDto deleteEquipment(EquipItemDto equipItemDto) throws Exception {

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
        if (inventoryItemList.size() >= 300) {
            throw new Exception("인벤토리가 가득 찼습니다.");
        }

        // 2. 전달받은 inventoryItemIdx, equipYn = 1 인 인벤토리 아이템을 찾아서 장착해제 한다.
        Optional<InventoryItem> optionalInventoryItem = inventoryItemRepository.findByIdxAndEquipYn(equipItemDto.getInventoryItemIdx(), 1);
        if (optionalInventoryItem.isEmpty()) throw new Exception("해당 아이템이 없습니다.");
        InventoryItem inventoryItem = optionalInventoryItem.get();
        inventoryItem.setEquipYn(0);

        Item currentItem = inventoryItem.getItem();
        ItemType itemType = currentItem.getItemType();
        if (itemType == ItemType.HELMET) {
            int hp = currentItem.getHp();
            user.setHp(user.getHp() - hp);
            userRepository.save(user);
        } else if (itemType == ItemType.ARMOR) {
            int defense = currentItem.getDefense();
            user.setDefense(user.getDefense() - defense);
            userRepository.save(user);
        } else {
            int damage = currentItem.getDamage();
            user.setDamage(user.getDamage() - damage);
            userRepository.save(user);
        }

        // 유저의 상태만 보내주는 DTO
        AfterEquipDto result = new AfterEquipDto();
        result.setItemIdx(inventoryItem.getIdx());
        result.setHp(user.getHp());
        result.setDefense(user.getDefense());
        result.setDamage(user.getDamage());

        return result;
    }

}
