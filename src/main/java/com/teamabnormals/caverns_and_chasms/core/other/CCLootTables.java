package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class CCLootTables {
	public static final ResourceKey<LootTable> FORGE_COMMON = create("archaeology/forge_common");
	public static final ResourceKey<LootTable> FORGE_RARE = create("archaeology/forge_rare");

	public static final ResourceKey<LootTable> RAT_SPAWN_ITEMS = create("equipment/rat_spawn_items");

	public static final ResourceKey<LootTable> FORGE_DISPENSER = create("chests/forge_dispenser");
	public static final ResourceKey<LootTable> VAULT = create("chests/vault");

	public static final ResourceKey<LootTable> SPAWNER_TRIAL_CHAMBER_TOKEN = create("spawners/trial_chamber/token");
	public static final ResourceKey<LootTable> SPAWNER_OMINOUS_TRIAL_CHAMBER_TOKEN = create("spawners/ominous/trial_chamber/token");

	private static ResourceKey<LootTable> create(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, CavernsAndChasms.location(name));
	}
}