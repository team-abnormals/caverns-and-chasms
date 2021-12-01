package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.google.gson.Gson;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.loot.modification.LootModifiers;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier.Config;
import com.teamabnormals.blueprint.core.util.modification.ConfiguredModifier;
import com.teamabnormals.blueprint.core.util.modification.ModifierDataProvider;
import com.teamabnormals.blueprint.core.util.modification.TargetedModifier;
import com.teamabnormals.blueprint.core.util.modification.targeting.ConditionedModifierTargetSelector;
import com.teamabnormals.blueprint.core.util.modification.targeting.ModifierTargetSelectorRegistry;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CCLootModifiersProvider {
	public static ModifierDataProvider<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createLootModifierDataProvider(DataGenerator dataGenerator) {
		return LootModifiers.createDataProvider(dataGenerator, "Caverns & Chasms Loot Modifiers", CavernsAndChasms.MOD_ID,
				createModifierEntry("shipwreck_map", false, 1, CCItems.DEPTH_GAUGE.get(), 1, BuiltInLootTables.SHIPWRECK_MAP),
				createModifierEntry("shipwreck_treasure", Collections.singletonList(createModifier(false, 1, CCItems.SPINEL.get(), 20, 1, 10)), BuiltInLootTables.SHIPWRECK_TREASURE),
				createModifierEntry("buried_treasure", Collections.singletonList(createModifier(false, 1, CCItems.SILVER_INGOT.get(), 10, 1, 4)), BuiltInLootTables.BURIED_TREASURE),

				createModifierEntry("abandoned_mineshaft", Arrays.asList(
						createModifier(Arrays.asList(
								createLootEntry(Items.BUNDLE, 5),
								createLootEntry(CCItems.DEPTH_GAUGE.get(), 5))),
						createModifier(false, 1, Arrays.asList(
								createLootEntry(CCItems.SILVER_INGOT.get(), 5, 1, 3),
								createLootEntry(CCItems.SPINEL.get(), 5, 6, 11))),
						createModifier(false, 2, CCBlocks.SPIKED_RAIL.get(), 5, 1, 4)
				), BuiltInLootTables.ABANDONED_MINESHAFT),

				createModifierEntry("jungle_temple", Collections.singletonList(createModifier(Arrays.asList(
						createLootEntry(CCItems.SILVER_INGOT.get(), 15, 2, 7),
						createLootEntry(CCItems.SPINEL.get(), 15, 4, 9)))
				), BuiltInLootTables.JUNGLE_TEMPLE),


				createModifierEntry("ruined_portal", Collections.singletonList(
						createModifier(Arrays.asList(
								createLootEntry(CCItems.SPINEL_CROWN.get(), 10),
								createLootEntry(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16),
								createLootEntry(CCBlocks.GOLDEN_LANTERN.get(), 5),
								createLootEntry(CCItems.GOLDEN_BUCKET.get(), 1)
						))
				), BuiltInLootTables.RUINED_PORTAL),

				createModifierEntry("silver_horse_armor_misc", CCItems.SILVER_HORSE_ARMOR.get(), 1, BuiltInLootTables.STRONGHOLD_CORRIDOR, BuiltInLootTables.VILLAGE_WEAPONSMITH, BuiltInLootTables.END_CITY_TREASURE, BuiltInLootTables.JUNGLE_TEMPLE),
				createModifierEntry("silver_horse_armor_dungeon", CCItems.SILVER_HORSE_ARMOR.get(), 10, BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.DESERT_PYRAMID),
				createModifierEntry("silver_horse_armor_nether_bridge", CCItems.SILVER_HORSE_ARMOR.get(), 6, BuiltInLootTables.NETHER_BRIDGE)
		);
	}

	private static ModifierDataProvider.ProviderEntry<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createModifierEntry(String name, ItemLike item, int weight, ResourceLocation... lootTables) {
		return createModifierEntry(name, false, 0, item, weight, lootTables);
	}

	private static ModifierDataProvider.ProviderEntry<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createModifierEntry(String name, boolean replace, int index, ItemLike item, int weight, ResourceLocation... lootTables) {
		return createModifierEntry(name, Collections.singletonList(createModifier(replace, index, item, weight)), lootTables);
	}

	private static ModifierDataProvider.ProviderEntry<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createModifierEntry(String name, List<ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?>> modifiers, ResourceLocation... lootTables) {
		return new ModifierDataProvider.ProviderEntry<>(
				new TargetedModifier<>(new ConditionedModifierTargetSelector<>(ModifierTargetSelectorRegistry.NAMES.withConfiguration(Arrays.asList(lootTables))), modifiers),
				new ResourceLocation(CavernsAndChasms.MOD_ID, name)
		);
	}

	private static ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?> createModifier(boolean replace, int index, List<LootPoolEntryContainer> entries) {
		return new ConfiguredModifier<>(LootModifiers.ENTRIES_MODIFIER, new Config(replace, index, entries));
	}

	private static ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?> createModifier(List<LootPoolEntryContainer> entries) {
		return new ConfiguredModifier<>(LootModifiers.ENTRIES_MODIFIER, new Config(false, 0, entries));
	}

	private static ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?> createModifier(boolean replace, int index, ItemLike item, int weight) {
		return createModifier(replace, index, Collections.singletonList(createLootEntry(item, weight)));
	}

	private static ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?> createModifier(boolean replace, int index, ItemLike item, int weight, int min, int max) {
		return createModifier(replace, index, Collections.singletonList(createLootEntry(item, weight, min, max)));
	}

	private static ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?> createModifier(ItemLike item, int weight, int min, int max) {
		return createModifier(false, 0, item, weight, min, max);
	}

	private static ConfiguredModifier<LootTableLoadEvent, ?, Gson, Pair<Gson, PredicateManager>, ?> createModifier(Item item, int weight) {
		return createModifier(false, 0, Collections.singletonList(createLootEntry(item, weight)));
	}

	private static LootPoolEntryContainer createLootEntry(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer createLootEntry(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}