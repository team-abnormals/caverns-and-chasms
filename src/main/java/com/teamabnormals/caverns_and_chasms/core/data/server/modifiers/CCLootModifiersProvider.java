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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraftforge.event.LootTableLoadEvent;

import java.util.Arrays;
import java.util.Collections;

public class CCLootModifiersProvider {
	public static ModifierDataProvider<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> createLootModifierDataProvider(DataGenerator dataGenerator) {
		return LootModifiers.createDataProvider(dataGenerator, "Caverns & Chasms Loot Modifiers", CavernsAndChasms.MOD_ID,
				silverHorseArmorModifier("silver_horse_armor_misc", 1, BuiltInLootTables.STRONGHOLD_CORRIDOR, BuiltInLootTables.VILLAGE_WEAPONSMITH, BuiltInLootTables.END_CITY_TREASURE, BuiltInLootTables.JUNGLE_TEMPLE),
				silverHorseArmorModifier("silver_horse_armor_dungeon", 10, BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.DESERT_PYRAMID),
				silverHorseArmorModifier("silver_horse_armor_nether_bridge", 6, BuiltInLootTables.NETHER_BRIDGE)
		);
	}

	private static ModifierDataProvider.ProviderEntry<LootTableLoadEvent, Gson, Pair<Gson, PredicateManager>> silverHorseArmorModifier(String name, int weight, ResourceLocation... lootTables) {
		return new ModifierDataProvider.ProviderEntry<>(
				new TargetedModifier<>(
						new ConditionedModifierTargetSelector<>(ModifierTargetSelectorRegistry.NAMES.withConfiguration(Arrays.asList(lootTables))),
						Collections.singletonList(new ConfiguredModifier<>(LootModifiers.ENTRIES_MODIFIER, new Config(false, 0, Collections.singletonList(LootItem.lootTableItem(CCItems.SILVER_HORSE_ARMOR.get()).setWeight(weight).build()))))
				),
				new ResourceLocation(CavernsAndChasms.MOD_ID, name)
		);
	}
}