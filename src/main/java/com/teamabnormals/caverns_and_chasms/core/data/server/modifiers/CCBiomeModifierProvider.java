package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.mojang.serialization.JsonOps;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBiomeModifierTypes.BlacklistedAddFeaturesBiomeModifier;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBiomeModifierTypes.BlacklistedAddSpawnsBiomeModifier;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.RemoveFeaturesBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CCBiomeModifierProvider {
	private static final RegistryAccess ACCESS = RegistryAccess.builtinCopy();
	private static final Registry<Biome> BIOMES = ACCESS.registryOrThrow(Registry.BIOME_REGISTRY);
	private static final Registry<PlacedFeature> PLACED_FEATURES = ACCESS.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
	private static final HashMap<ResourceLocation, BiomeModifier> MODIFIERS = new HashMap<>();

	public static JsonCodecProvider<BiomeModifier> create(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		addSpawn("mime", CCBiomeTags.HAS_MIME, new MobSpawnSettings.SpawnerData(CCEntityTypes.MIME.get(), 150, 1, 1));
		addSpawn("glare", CCBiomeTags.HAS_GLARE, new MobSpawnSettings.SpawnerData(CCEntityTypes.GLARE.get(), 20, 1, 1));

		addFeature("base_ores", BiomeTags.IS_OVERWORLD, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_GOLD_AND_SILVER_LOWER, CCPlacedFeatures.ORE_SPINEL_BURIED);
		addFeature("spinel_ore", CCBiomeTags.HAS_SPINEL_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SPINEL);
		addFeature("silver_ore", CCBiomeTags.HAS_SILVER_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_BURIED_WITH_GOLD);
		addFeatureBlacklisted("gold_ore", CCBiomeTags.HAS_SILVER_ORE, BiomeTags.IS_OVERWORLD, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_GOLD_BURIED_WITH_SILVER);

		addFeature("extra_silver_ore", CCBiomeTags.HAS_EXTRA_SILVER_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_EXTRA);
		addFeature("soul_silver_ore", CCBiomeTags.HAS_SOUL_SILVER_ORE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_SOUL);
		addFeature("rocky_dirt", CCBiomeTags.HAS_ROCKY_DIRT, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_ROCKY_DIRT);
		addFeature("fragile_stone", CCBiomeTags.HAS_FRAGILE_STONE, Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_FRAGILE_STONE, CCPlacedFeatures.ORE_FRAGILE_STONE_BURIED);

		removeFeature("gold_ore", BiomeTags.IS_OVERWORLD, Set.of(Decoration.UNDERGROUND_ORES), OrePlacements.ORE_GOLD, OrePlacements.ORE_GOLD_LOWER);
		removeFeature("lapis_ore", BiomeTags.IS_OVERWORLD, Set.of(Decoration.UNDERGROUND_ORES), OrePlacements.ORE_LAPIS, OrePlacements.ORE_LAPIS_BURIED);
		removeFeature("dirt_ore", CCBiomeTags.HAS_ROCKY_DIRT, Set.of(Decoration.UNDERGROUND_ORES), OrePlacements.ORE_DIRT);

		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, CavernsAndChasms.MOD_ID, RegistryOps.create(JsonOps.INSTANCE, ACCESS), ForgeRegistries.Keys.BIOME_MODIFIERS, MODIFIERS);
	}

	@SafeVarargs
	private static void removeFeature(String name, TagKey<Biome> biomes, Set<GenerationStep.Decoration> steps, Holder<PlacedFeature>... features) {
		addModifier("remove_feature/" + name, new RemoveFeaturesBiomeModifier(new HolderSet.Named<>(BIOMES, biomes), featureSet(features), steps));
	}

	@SafeVarargs
	private static void addFeature(String name, TagKey<Biome> biomes, GenerationStep.Decoration step, RegistryObject<PlacedFeature>... features) {
		addModifier("add_feature/" + name, new AddFeaturesBiomeModifier(new HolderSet.Named<>(BIOMES, biomes), featureSet(features), step));
	}

	@SafeVarargs
	private static void addFeatureBlacklisted(String name, TagKey<Biome> biomes, TagKey<Biome> blacklistedBiomes, GenerationStep.Decoration step, RegistryObject<PlacedFeature>... features) {
		addModifier("add_feature/" + name, new BlacklistedAddFeaturesBiomeModifier(new HolderSet.Named<>(BIOMES, biomes), new HolderSet.Named<>(BIOMES, blacklistedBiomes), featureSet(features), step));
	}

	private static void addSpawn(String name, TagKey<Biome> biomes, MobSpawnSettings.SpawnerData... spawns) {
		addModifier("add_spawn/" + name, new AddSpawnsBiomeModifier(new HolderSet.Named<>(BIOMES, biomes), List.of(spawns)));
	}

	private static void addSpawnBlacklisted(String name, TagKey<Biome> biomes, TagKey<Biome> blacklistedBiomes, MobSpawnSettings.SpawnerData... spawns) {
		addModifier("add_spawn/" + name, new BlacklistedAddSpawnsBiomeModifier(new HolderSet.Named<>(BIOMES, biomes), new HolderSet.Named<>(BIOMES, blacklistedBiomes), List.of(spawns)));
	}

	private static void addModifier(String name, BiomeModifier modifier) {
		MODIFIERS.put(new ResourceLocation(CavernsAndChasms.MOD_ID, name), modifier);
	}

	@SafeVarargs
	private static HolderSet<PlacedFeature> featureSet(RegistryObject<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(registryObject -> PLACED_FEATURES.getOrCreateHolderOrThrow(registryObject.getKey())).collect(Collectors.toList()));
	}

	@SafeVarargs
	private static HolderSet<PlacedFeature> featureSet(Holder<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(registryObject -> PLACED_FEATURES.getOrCreateHolderOrThrow(registryObject.unwrapKey().get())).collect(Collectors.toList()));
	}
}