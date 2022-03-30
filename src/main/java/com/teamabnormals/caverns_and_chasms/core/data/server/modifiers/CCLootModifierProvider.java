package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.Collections;

public class CCLootModifierProvider extends LootModifierProvider {

	public CCLootModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, CavernsAndChasms.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		this.entry("shipwreck_map").selects(BuiltInLootTables.SHIPWRECK_MAP).addModifier(new LootPoolEntriesModifier(false, 1, Collections.singletonList(lootPool(CCItems.DEPTH_GAUGE.get(), 1))));
		this.entry("shipwreck_treasure").selects(BuiltInLootTables.SHIPWRECK_TREASURE).addModifier(new LootPoolEntriesModifier(false, 1, Collections.singletonList(lootPool(CCItems.SPINEL.get(), 20, 1, 10))));
		this.entry("buried_treasure").selects(BuiltInLootTables.BURIED_TREASURE).addModifier(new LootPoolEntriesModifier(false, 1, Collections.singletonList(lootPool(CCItems.SILVER_INGOT.get(), 10, 1, 4))));

		this.entry("abandoned_mineshaft").selects(BuiltInLootTables.ABANDONED_MINESHAFT)
				.addModifier(new LootPoolEntriesModifier(false, 0, Arrays.asList(lootPool(Items.BUNDLE, 5), lootPool(CCItems.DEPTH_GAUGE.get(), 5))))
				.addModifier(new LootPoolEntriesModifier(false, 1, Arrays.asList(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SPINEL.get(), 5, 6, 11))))
				.addModifier(new LootPoolEntriesModifier(false, 2, Collections.singletonList(lootPool(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4))));

		this.entry("jungle_temple").selects(BuiltInLootTables.JUNGLE_TEMPLE).addModifier(new LootPoolEntriesModifier(false, 0, Arrays.asList(lootPool(CCItems.SILVER_INGOT.get(), 15, 2, 7), lootPool(CCItems.SPINEL.get(), 15, 4, 9))));
		this.entry("ruined_portal").selects(BuiltInLootTables.RUINED_PORTAL).addModifier(new LootPoolEntriesModifier(false, 0, Arrays.asList(lootPool(CCItems.SPINEL_CROWN.get(), 10), lootPool(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16), lootPool(CCBlocks.LAVA_LAMP.get(), 5), lootPool(CCItems.GOLDEN_BUCKET.get(), 1))));

		this.entry("silver_horse_armor_misc").selects(BuiltInLootTables.STRONGHOLD_CORRIDOR, BuiltInLootTables.VILLAGE_WEAPONSMITH, BuiltInLootTables.END_CITY_TREASURE, BuiltInLootTables.JUNGLE_TEMPLE).addModifier(new LootPoolEntriesModifier(false, 0, Collections.singletonList(lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1))));
		this.entry("silver_horse_armor_dungeon").selects(BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.DESERT_PYRAMID).addModifier(new LootPoolEntriesModifier(false, 0, Collections.singletonList(lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 10))));
		this.entry("silver_horse_armor_nether_bridge").selects(BuiltInLootTables.NETHER_BRIDGE).addModifier(new LootPoolEntriesModifier(false, 0, Collections.singletonList(lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 6))));
	}

	private static LootPoolEntryContainer lootPool(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer lootPool(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}