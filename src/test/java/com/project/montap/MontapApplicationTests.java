package com.project.montap;

import com.project.montap.domain.entity.Item;
import com.project.montap.domain.repository.ItemRepository;
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

	@Test
	public void createDummyItem() {

		List<Item> newItemList = new ArrayList<>();

		for (int i = 0; i < 10; i ++) {
			Item newItem = Item
					.builder()
					.name("helmet" + (i + 1))
					.description("This is helmet" + (i + 1))
					.itemType(ItemType.HELMET)
					.itemRank(i % 4)
					.hp(i + 1)
					.itemUrl(null)
					.price((i + 1) * 1000)
					.build();
			newItemList.add(newItem);
		}

		for (int i = 0; i < 10; i ++) {
			Item newItem = Item
					.builder()
					.name("Armor" + (i + 1))
					.description("This is Armor" + (i + 1))
					.itemType(ItemType.ARMOR)
					.itemRank(i % 4)
					.defense(i + 1)
					.itemUrl(null)
					.price((i + 1) * 1000)
					.build();
			newItemList.add(newItem);
		}

		for (int i = 0; i < 10; i ++) {
			Item newItem = Item
					.builder()
					.name("Weapon" + (i + 1))
					.description("This is Weapon" + (i + 1))
					.itemType(ItemType.WEAPON)
					.itemRank(i % 4)
					.damage(i + 1)
					.itemUrl(null)
					.price((i + 1) * 1000)
					.build();
			newItemList.add(newItem);
		}

		itemRepository.saveAll(newItemList);

	}
}
