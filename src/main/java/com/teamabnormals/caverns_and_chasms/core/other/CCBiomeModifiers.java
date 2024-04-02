package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBiomeModifierTypes.BlacklistedAddFeaturesBiomeModifier;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBiomeModifierTypes.BlacklistedAddSpawnsBiomeModifier;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCPlacedFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.RemoveFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.RemoveSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CCBiomeModifiers {

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		addSpawn(context, "mime", CCBiomeTags.HAS_MIME, new MobSpawnSettings.SpawnerData(CCEntityTypes.MIME.get(), 100, 1, 1));
		addSpawn(context, "glare", CCBiomeTags.HAS_GLARE, new MobSpawnSettings.SpawnerData(CCEntityTypes.GLARE.get(), 20, 1, 1));
		addSpawn(context, "lost_goat", CCBiomeTags.HAS_LOST_GOAT, new MobSpawnSettings.SpawnerData(CCEntityTypes.LOST_GOAT.get(), 1, 1, 1));

		addFeature(context, "base_ores", BiomeTags.IS_OVERWORLD, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_GOLD_AND_SILVER_LOWER, CCPlacedFeatures.ORE_SPINEL_BURIED);
		addFeature(context, "spinel_ore", CCBiomeTags.HAS_SPINEL_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SPINEL);
		addFeature(context, "silver_ore", CCBiomeTags.HAS_SILVER_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_BURIED_WITH_GOLD);
		addFeatureBlacklisted(context, "gold_ore", CCBiomeTags.HAS_SILVER_ORE, BiomeTags.IS_OVERWORLD, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_GOLD_BURIED_WITH_SILVER);

		addFeature(context, "extra_silver_ore", CCBiomeTags.HAS_EXTRA_SILVER_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_EXTRA);
		addFeature(context, "soul_silver_ore", CCBiomeTags.HAS_SOUL_SILVER_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_SOUL);
		addFeature(context, "rocky_dirt", CCBiomeTags.HAS_ROCKY_DIRT, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_ROCKY_DIRT);
		addFeature(context, "fragile_stone", CCBiomeTags.HAS_FRAGILE_STONE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_FRAGILE_STONE, CCPlacedFeatures.ORE_FRAGILE_STONE_BURIED);

		removeFeature(context, "gold_ore", BiomeTags.IS_OVERWORLD, Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GOLD, OrePlacements.ORE_GOLD_LOWER);
		removeFeature(context, "dirt_ore", CCBiomeTags.HAS_ROCKY_DIRT, Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIRT);
	}

	@SafeVarargs
	private static void removeFeature(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, Decoration step, ResourceKey<PlacedFeature>... features) {
		register(context, "remove_feature/" + name, () -> new RemoveFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), featureSet(context, features), Set.of(step)));
	}

	@SafeVarargs
	private static void addFeature(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, Decoration step, ResourceKey<PlacedFeature>... features) {
		register(context, "add_feature/" + name, () -> new AddFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), featureSet(context, features), step));
	}

	@SafeVarargs
	private static void addFeatureBlacklisted(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, TagKey<Biome> blacklistedBiomes, Decoration step, ResourceKey<PlacedFeature>... features) {
		register(context, "add_feature/" + name, () -> new BlacklistedAddFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), context.lookup(Registries.BIOME).getOrThrow(blacklistedBiomes), featureSet(context, features), step));
	}

	private static void addSpawn(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, MobSpawnSettings.SpawnerData... spawns) {
		register(context, "add_spawn/" + name, () -> new AddSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), List.of(spawns)));
	}

	private static void addSpawnBlackListed(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, TagKey<Biome> blacklistedBiomes, MobSpawnSettings.SpawnerData... spawns) {
		register(context, "add_spawn/" + name, () -> new BlacklistedAddSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), context.lookup(Registries.BIOME).getOrThrow(blacklistedBiomes), List.of(spawns)));
	}

	private static void removeSpawn(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, EntityType<?>... types) {
		register(context, "remove_spawn/" + name, () -> new RemoveSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), HolderSet.direct(Stream.of(types).map(type -> ForgeRegistries.ENTITY_TYPES.getHolder(type).get()).collect(Collectors.toList()))));
	}

	private static void register(BootstapContext<BiomeModifier> context, String name, Supplier<? extends BiomeModifier> modifier) {
		context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CavernsAndChasms.MOD_ID, name)), modifier.get());
	}

	@SafeVarargs
	private static HolderSet<PlacedFeature> featureSet(BootstapContext<?> context, ResourceKey<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(placedFeatureKey -> context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeatureKey)).collect(Collectors.toList()));
	}
}