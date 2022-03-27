package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.loot.modification.LootModificationManager;
import com.teamabnormals.blueprint.common.loot.modification.LootModifierSerializers;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import com.teamabnormals.blueprint.core.util.modification.ObjectModifierGroup;
import com.teamabnormals.blueprint.core.util.modification.ObjectModifierProvider;
import com.teamabnormals.blueprint.core.util.modification.selection.selectors.NamesResourceSelector;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CCLootModifiersProvider extends ObjectModifierProvider<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> {

	public CCLootModifiersProvider(DataGenerator dataGenerator) {
		super(dataGenerator, CavernsAndChasms.MOD_ID, true, LootModificationManager.TARGET_DIRECTORY, new GsonBuilder().setPrettyPrinting().create(), LootModifierSerializers.REGISTRY, (group) -> Deserializers.createLootTableSerializer().create());
	}

	@Override
	protected void registerEntries() {
		this.registerEntry("shipwreck_map", new LootPoolEntriesModifier(false, 1, Collections.singletonList(createLootEntry(CCItems.DEPTH_GAUGE.get(), 1))), BuiltInLootTables.SHIPWRECK_MAP);
		this.registerEntry("shipwreck_treasure", new LootPoolEntriesModifier(false, 1, Collections.singletonList(createLootEntry(CCItems.SPINEL.get(), 20, 1, 10))), BuiltInLootTables.SHIPWRECK_TREASURE);
		this.registerEntry("buried_treasure", new LootPoolEntriesModifier(false, 1, Collections.singletonList(createLootEntry(CCItems.SILVER_INGOT.get(), 10, 1, 4))), BuiltInLootTables.BURIED_TREASURE);

		this.registerEntry("abandoned_mineshaft", new ObjectModifierGroup<>(new NamesResourceSelector(BuiltInLootTables.ABANDONED_MINESHAFT), List.of(
				new LootPoolEntriesModifier(false, 0, Arrays.asList(createLootEntry(Items.BUNDLE, 5), createLootEntry(CCItems.DEPTH_GAUGE.get(), 5))),
				new LootPoolEntriesModifier(false, 1, Arrays.asList(createLootEntry(CCItems.SILVER_INGOT.get(), 5, 1, 3), createLootEntry(CCItems.SPINEL.get(), 5, 6, 11))),
				new LootPoolEntriesModifier(false, 2, Collections.singletonList(createLootEntry(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4)))
		)));

		this.registerEntry("jungle_temple", new LootPoolEntriesModifier(false, 0, Arrays.asList(createLootEntry(CCItems.SILVER_INGOT.get(), 15, 2, 7), createLootEntry(CCItems.SPINEL.get(), 15, 4, 9))), BuiltInLootTables.JUNGLE_TEMPLE);
		this.registerEntry("ruined_portal", new LootPoolEntriesModifier(false, 0, Arrays.asList(createLootEntry(CCItems.SPINEL_CROWN.get(), 10), createLootEntry(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16), createLootEntry(CCBlocks.LAVA_LAMP.get(), 5), createLootEntry(CCItems.GOLDEN_BUCKET.get(), 1))), BuiltInLootTables.RUINED_PORTAL);

		this.registerEntry("silver_horse_armor_misc", new LootPoolEntriesModifier(false, 0, Collections.singletonList(createLootEntry(CCItems.SILVER_HORSE_ARMOR.get(), 1))), BuiltInLootTables.STRONGHOLD_CORRIDOR, BuiltInLootTables.VILLAGE_WEAPONSMITH, BuiltInLootTables.END_CITY_TREASURE, BuiltInLootTables.JUNGLE_TEMPLE);
		this.registerEntry("silver_horse_armor_dungeon", new LootPoolEntriesModifier(false, 0, Collections.singletonList(createLootEntry(CCItems.SILVER_HORSE_ARMOR.get(), 10))), BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.DESERT_PYRAMID);
		this.registerEntry("silver_horse_armor_nether_bridge", new LootPoolEntriesModifier(false, 0, Collections.singletonList(createLootEntry(CCItems.SILVER_HORSE_ARMOR.get(), 6))), BuiltInLootTables.NETHER_BRIDGE);
	}

	private static LootPoolEntryContainer createLootEntry(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer createLootEntry(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}