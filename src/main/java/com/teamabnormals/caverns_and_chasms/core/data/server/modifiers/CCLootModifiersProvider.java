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
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraftforge.event.LootTableLoadEvent;

import java.util.Arrays;
import java.util.Collections;

public class CCLootModifiersProvider {
	public static ModifierDataProvider<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createLootModifierDataProvider(DataGenerator dataGenerator) {
		return LootModifiers.createDataProvider(dataGenerator, "Caverns & Chasms Loot Modifiers", CavernsAndChasms.MOD_ID,
				createBasicModifier("ore_detector_shipwreck", false, 1, CCItems.ORE_DETECTOR.get(), 1, BuiltInLootTables.SHIPWRECK_MAP),
				createBasicModifier("ore_detector_buried_treasure", false, 3, CCItems.ORE_DETECTOR.get(), 1, BuiltInLootTables.BURIED_TREASURE),
				createBasicModifier("silver_buried_treasure", false, 1, CCItems.SILVER_INGOT.get(), 10, BuiltInLootTables.BURIED_TREASURE),

				createBasicModifier("silver_horse_armor_misc", CCItems.SILVER_HORSE_ARMOR.get(), 1, BuiltInLootTables.STRONGHOLD_CORRIDOR, BuiltInLootTables.VILLAGE_WEAPONSMITH, BuiltInLootTables.END_CITY_TREASURE, BuiltInLootTables.JUNGLE_TEMPLE),
				createBasicModifier("silver_horse_armor_dungeon", CCItems.SILVER_HORSE_ARMOR.get(), 10, BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.DESERT_PYRAMID),
				createBasicModifier("silver_horse_armor_nether_bridge", CCItems.SILVER_HORSE_ARMOR.get(), 6, BuiltInLootTables.NETHER_BRIDGE)
		);
	}

	private static ModifierDataProvider.ProviderEntry<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createBasicModifier(String name, Item item, int weight, ResourceLocation... lootTables) {
		return createBasicModifier(name, false, 0, item, weight, lootTables);
	}

	private static ModifierDataProvider.ProviderEntry<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createBasicModifier(String name, boolean replace, int index, Item item, int weight, ResourceLocation... lootTables) {
		return new ModifierDataProvider.ProviderEntry<>(
				new TargetedModifier<>(
						new ConditionedModifierTargetSelector<>(ModifierTargetSelectorRegistry.NAMES.withConfiguration(Arrays.asList(lootTables))),
						Collections.singletonList(new ConfiguredModifier<>(LootModifiers.ENTRIES_MODIFIER, new Config(replace, index, Collections.singletonList(LootItem.lootTableItem(item).setWeight(weight).build()))))
				),
				new ResourceLocation(CavernsAndChasms.MOD_ID, name)
		);
	}
}