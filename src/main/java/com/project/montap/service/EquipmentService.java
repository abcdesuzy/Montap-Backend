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

        // 1. 장착하려는 유저를 찾는다.
        Optional<User> optionalFindUser = userRepository.findById(equipItemDto.getUserIdx());
        // - 장착하려는 유저가 없는 경우 에러
        if (optionalFindUser.isEmpty()) {
            throw new Exception("장착하려는 User 가 없습니다.");
        } else { // - 장착하려는 유저가 데이터베이스에 있는 경우

            // 2. 장착하려는 유저의. 인벤토리에 있는 아이템. 중에서 장착하려는 아이템. 을 찾는다.
            User findUser = optionalFindUser.get();

            // - 장착하려는 유저의 인벤토리 아이템 목록
            List<InventoryItem> inventoryItemList = findUser.getInventoryItemList();

            InventoryItem findInventoryItem = null;
            Item findItem = null;
            for (int i = 0; i < inventoryItemList.size(); i++) {
                if (inventoryItemList.get(i).getItem().getIdx() == equipItemDto.getItemIdx()) {
                    findInventoryItem = inventoryItemList.get(i);
                    findItem = inventoryItemList.get(i).getItem();
                    break;
                }
            }

            // - 장착하려는 아이템이 없는 경우 에러
            if (findInventoryItem == null || findItem == null) {
                throw new Exception("인벤토리에 해당 아이템이 없습니다.");
            }

            // - 기존에 장착된 아이템과 교체
            for (int i = 0; i < inventoryItemList.size(); i++) {
                InventoryItem current = inventoryItemList.get(i);
                Item currentItem = current.getItem();
                if (current.getEquipYn() == 1 && currentItem.getItemType() == findItem.getItemType()) {
                    current.setEquipYn(0); // 아이템 장착 여부를 1 > 0
                    ItemType itemType = currentItem.getItemType();
                    if (itemType == ItemType.HELMET) {
                        int hp = currentItem.getHp();
                        findUser.setHp(findUser.getHp() - hp);
                    } else if (itemType == ItemType.ARMOR) {
                        int defense = currentItem.getDefense();
                        findUser.setDefense(findUser.getDefense() - defense);
                    } else {
                        int damage = currentItem.getDamage();
                        findUser.setDamage(findUser.getDamage() - damage);
                    }
                }
            }

            // 3. 찾은 아이템을 장착 처리하고 능력치를 올린다.
            findInventoryItem.setEquipYn(1);

            ItemType itemType = findItem.getItemType();
            if (itemType == ItemType.HELMET) {
                int hp = findItem.getHp();
                findUser.setHp(findUser.getHp() + hp);
                userRepository.save(findUser);
            } else if (itemType == ItemType.ARMOR) {
                int defense = findItem.getDefense();
                findUser.setDefense(findUser.getDefense() + defense);
                userRepository.save(findUser);
            } else {
                int damage = findItem.getDamage();
                findUser.setDamage(findUser.getDamage() + damage);
                userRepository.save(findUser);
            }

            // 아이템 장착 후 유저의 상태만 보내주는 DTO
            AfterEquipDto result = new AfterEquipDto();
            result.setItemIdx(findInventoryItem.getIdx());
            result.setHp(findUser.getHp());
            result.setDefense(findUser.getDefense());
            result.setDamage(findUser.getDamage());
            return result;
        }
    }

    // 장착한 아이템 리스트
    @Transactional
    public List<Item> getEquipment() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto authUserDto = (AuthUserDto) auth.getPrincipal(); // 강제 형변환
        Long userIdx = authUserDto.getUserIdx();
        Optional<List<InventoryItem>> optionalInventoryItemList = inventoryItemRepository.findByUserIdx(userIdx);

        if (optionalInventoryItemList.isEmpty()) {
            throw new Exception("인벤토리에 아이템이 없습니다.");
        }
        List<InventoryItem> inventoryItemList = optionalInventoryItemList.get();
        List<Item> resultList = new ArrayList<>();
        for (int i = 0; i < inventoryItemList.size(); i++) {
            if (inventoryItemList.get(i).getEquipYn() == 1) {
                resultList.add(inventoryItemList.get(i).getItem());
            }
        }
        return resultList;
    }

    // 아이템 장착 해제
    @Transactional
    public AfterEquipDto deleteEquipment(EquipItemDto equipItemDto) throws Exception {

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

            // 인벤토리 한도 체크
            List<InventoryItem> inventoryItemList = findUser.getInventoryItemList();
            if (inventoryItemList.size() >= 300) {
                throw new Exception("인벤토리가 가득 찼습니다.");
            }

            // 2. 그 유저의 인벤토리에 있는 아이템 중에서 장착해제 하려는 아이템을 찾는다.
            InventoryItem findInventoryItem = null;
            Item findItem = null;
            for (int i = 0; i < inventoryItemList.size(); i++) {
                if (inventoryItemList.get(i).getItem().getIdx() == equipItemDto.getItemIdx()) {
                    findInventoryItem = inventoryItemList.get(i);
                    findItem = inventoryItemList.get(i).getItem();
                    break;
                }
            }
            // - 장착하려는 아이템이 없는 경우 에러
            if (findInventoryItem == null || findItem == null) {
                throw new Exception("인벤토리에 해당 아이템이 없습니다.");
            }

            // - 기존에 장착된 아이템 해제
            for (int i = 0; i < inventoryItemList.size(); i++) {
                InventoryItem current = inventoryItemList.get(i);
                Item currentItem = current.getItem();
                if (current.getEquipYn() == 1 && currentItem.getItemType() == findItem.getItemType()) { // 아이템장착여부가 1(장착)인 경우
                    current.setEquipYn(0); // 0으로 바꾸기

                    ItemType itemType = currentItem.getItemType();
                    if (itemType == ItemType.HELMET) {
                        int hp = currentItem.getHp();
                        findUser.setHp(findUser.getHp() - hp);
                        userRepository.save(findUser);
                    } else if (itemType == ItemType.ARMOR) {
                        int defense = currentItem.getDefense();
                        findUser.setDefense(findUser.getDefense() - defense);
                        userRepository.save(findUser);
                    } else {
                        int damage = currentItem.getDamage();
                        findUser.setDamage(findUser.getDamage() - damage);
                        userRepository.save(findUser);
                    }
                }
            }
            // 유저의 상태만 보내주는 DTO
            AfterEquipDto result = new AfterEquipDto();
            result.setItemIdx(findInventoryItem.getIdx());
            result.setHp(findUser.getHp());
            result.setDefense(findUser.getDefense());
            result.setDamage(findUser.getDamage());

            return result;
        }
    }
}
