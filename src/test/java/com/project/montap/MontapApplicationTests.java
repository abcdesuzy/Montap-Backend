package com.project.montap;

import com.project.montap.domain.entity.Item;
import com.project.montap.domain.entity.Stage;
import com.project.montap.domain.repository.ItemRepository;
import com.project.montap.domain.repository.StageLogRepository;
import com.project.montap.domain.repository.StageRepository;
import com.project.montap.enums.IsBoss;
import com.project.montap.enums.ItemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MontapApplicationTests {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    StageRepository stageRepository;

    @Test
    public void createDummyItem() {

        List<Item> newItemList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                Item item = new Item();
                if (i == 0) {
                    item.setName("helmet" + (j + 1));
                    item.setDescription("This is helmet" + (j + 1));
                    item.setItemType(ItemType.HELMET);
                    item.setItemRank(j % 4);
                    item.setHp((j + 1) * 10);
                    item.setItemUrl(null);
                    item.setPrice((j + 1) * 1000);
                }
                if (i == 1) {
                    item.setName("armor" + (j + 1));
                    item.setDescription("This is armor" + (j + 1));
                    item.setItemType(ItemType.ARMOR);
                    item.setItemRank(j % 4);
                    item.setDefense((j + 1) * 10);
                    item.setItemUrl(null);
                    item.setPrice((j + 1) * 1000);
                }
                if (i == 2) {
                    item.setName("weapon" + (j + 1));
                    item.setDescription("This is weapon" + (j + 1));
                    item.setItemType(ItemType.WEAPON);
                    item.setItemRank(j % 4);
                    item.setDamage((j + 1) * 10);
                    item.setItemUrl(null);
                    item.setPrice((j + 1) * 1000);
                }
                newItemList.add(item);
            }
        }

        itemRepository.saveAll(newItemList);

    }

    @Test
    public void createDummyStage() {

        List<Stage> newStageList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Stage newStage = Stage
                    .builder()
                    .stageCount(i + 1)
                    .monsterName("monster" + (i + 1))
                    .monsterHp(1 * (i + 1))
                    .monsterDamage(100 * (i + 1))
                    .isBoss(IsBoss.N)
                    .dropMoney(100 * (i + 1))
                    .monsterUrl(null)
                    .build();
            newStageList.add(newStage);
        }
        stageRepository.saveAll(newStageList);

    }
}
