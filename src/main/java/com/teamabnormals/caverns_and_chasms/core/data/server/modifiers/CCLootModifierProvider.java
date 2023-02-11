package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolsModifier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CCLootModifierProvider extends LootModifierProvider {

	public CCLootModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, CavernsAndChasms.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		this.entry("simple_dungeon").selects(BuiltInLootTables.SIMPLE_DUNGEON)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 10))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 4))));
		this.entry("abandoned_mineshaft").selects(BuiltInLootTables.ABANDONED_MINESHAFT)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(Items.BUNDLE, 5), lootPool(CCItems.DEPTH_GAUGE.get(), 5))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SPINEL.get(), 5, 6, 11))))
				.addModifier(new LootPoolEntriesModifier(false, 2, List.of(lootPool(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4))));
		this.entry("stronghold_corridor").selects(BuiltInLootTables.STRONGHOLD_CORRIDOR).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1))));
		this.entry("stronghold_crossing").selects(BuiltInLootTables.STRONGHOLD_CROSSING).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3))));

		this.entry("shipwreck_map").selects(BuiltInLootTables.SHIPWRECK_MAP)
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.BAROMETER.get(), 1), lootPool(CCItems.DEPTH_GAUGE.get(), 1))));
		this.entry("shipwreck_treasure").selects(BuiltInLootTables.SHIPWRECK_TREASURE)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(CCItems.SILVER_INGOT.get(), 10, 1, 5))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_NUGGET.get(), 10, 1, 10), lootPool(CCItems.SPINEL.get(), 20, 1, 10))));
		this.entry("buried_treasure").selects(BuiltInLootTables.BURIED_TREASURE)
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_INGOT.get(), 10, 1, 4))));

		this.entry("desert_pyramid").selects(BuiltInLootTables.DESERT_PYRAMID).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 1, 5), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 10))));
		this.entry("jungle_temple").selects(BuiltInLootTables.JUNGLE_TEMPLE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 2, 7), lootPool(CCItems.SPINEL.get(), 15, 4, 9), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1))));
		this.entry("woodland_mansion").selects(BuiltInLootTables.WOODLAND_MANSION).addModifier(new LootPoolEntriesModifier(false, 1,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 4))));

		this.entry("village_fisher").selects(BuiltInLootTables.VILLAGE_FISHER)
				.addModifier(new LootPoolsModifier(List.of(LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":barometer").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(CCItems.BAROMETER.get())).build()), false));
		this.entry("village_weaponsmith").selects(BuiltInLootTables.VILLAGE_WEAPONSMITH).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1))));
		this.entry("village_toolsmith").selects(BuiltInLootTables.VILLAGE_TOOLSMITH).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 1, 1, 3))));
		this.entry("village_temple").selects(BuiltInLootTables.VILLAGE_TEMPLE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SPINEL.get(), 1, 1, 4), lootPool(CCItems.SILVER_INGOT.get(), 1, 1, 4))));

		this.entry("ruined_portal").selects(BuiltInLootTables.RUINED_PORTAL).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16), lootPool(CCBlocks.LAVA_LAMP.get(), 5), lootPool(CCItems.GOLDEN_BUCKET.get(), 1))));
		this.entry("nether_bridge").selects(BuiltInLootTables.NETHER_BRIDGE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 5), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 6))));

		this.entry("end_city_treasure").selects(BuiltInLootTables.END_CITY_TREASURE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 2, 7), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1))));
	}

	private static LootPoolEntryContainer lootPool(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer lootPool(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}