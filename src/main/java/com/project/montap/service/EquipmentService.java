package com.project.montap.service;

import com.project.montap.domain.entity.InventoryItem;
import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.User;
import com.project.montap.domain.repository.InventoryItemRepository;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.UserRepository;
import com.project.montap.dto.AfterEquipDto;
import com.project.montap.dto.EquipItemDto;
import com.project.montap.enums.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public AfterEquipDto equipItem(EquipItemDto equipItemDto) throws Exception {

        // 1. 장착하려는 User 를 찾는다.
        Optional<User> optionalFindUser = userRepository.findById(equipItemDto.getUserIdx());

        // 장착하려는 User 가 없는 경우 에러
        if (optionalFindUser.isEmpty()) {
            throw new Exception("장착하려는 User 가 없습니다.");
        } else {
            // 장착하려는 User 가 데이터베이스에 있는 경우

            // 2. 장착하려는 User 의 인벤토리에 있는 아이템 중에서 장착하려는 아이템을 찾는다.
            User findUser = optionalFindUser.get();

            // 장착하려는 User 의 인벤토리 아이템 목록
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

            // 장착하려는 아이템이 없는 경우 에러
            if (findInventoryItem == null || findItem == null) {
                throw new Exception("인벤토리에 해당 아이템이 없습니다.");
            }

            for (int i = 0; i < inventoryItemList.size(); i ++) {
                InventoryItem current = inventoryItemList.get(i);
                Item currentItem = current.getItem();
                if (current.getEquipYn() == 1 && currentItem.getItemType() == findItem.getItemType()) {
                    current.setEquipYn(0);
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

            // - 기존에 같은 타입의 아이템을 장착하고 있는지 확인
            for (int i = 0; i < inventoryItemList.size(); i++) {

                InventoryItem current = inventoryItemList.get(i);
                Item currentItem = current.getItem();

                if (current.getEquipYn() == 1 && currentItem.getItemType() == findItem.getItemType()) {
                    current.setEquipYn(0); // 1(장착)에서 0(장착해제)으로 바꿔줌

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
                    break;
                }
            }

            // 3. 찾은 아이템을 장착
            inventoryItem.setEquipYn(1); // 1(장착)로 변경
            inventoryItemRepository.save(inventoryItem);

            // 4. 해당 아이템의 스탯을 유저 스탯에 올린다.
            // 4-1. 장착하려는 아이템 타입을 파악한다.
            ItemType itemType = findItem.getItemType();

            // 4-2. 장착하려는 아이템 타입별로 분기처리 한다.
            if (itemType == ItemType.HELMET) {
                int hp = findItem.getHp();
                findUser.setHp(findUser.getHp() + hp);
            } else if (itemType == ItemType.ARMOR) {
                int defense = findItem.getDefense();
                findUser.setDefense(findUser.getDefense() + defense);
            } else {
                int damage = findItem.getDamage();
                findUser.setDamage(findUser.getDamage() + damage);
            }

            // AfterEquipDto 생성
            AfterEquipDto afterEquipDto = new AfterEquipDto();
            afterEquipDto.setItemIdx(findItem.getIdx());
            afterEquipDto.setHp(findUser.getHp());
            afterEquipDto.setDefense(findUser.getDefense());
            afterEquipDto.setDamage(findUser.getDamage());

            return afterEquipDto;
        }

    }

