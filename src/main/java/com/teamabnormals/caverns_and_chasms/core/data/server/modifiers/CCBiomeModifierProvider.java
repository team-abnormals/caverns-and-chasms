package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.mojang.serialization.JsonOps;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
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

	public static JsonCodecProvider<BiomeModifier> create(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		RegistryAccess access = RegistryAccess.builtinCopy();
		Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
		Registry<PlacedFeature> placedFeatures = access.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
		HashMap<ResourceLocation, BiomeModifier> modifiers = new HashMap<>();

		addModifier(modifiers, "add_spawn/cavefish", new AddSpawnsBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_CAVEFISH), List.of(new MobSpawnSettings.SpawnerData(CCEntityTypes.CAVEFISH.get(), 25, 4, 7))));
		addModifier(modifiers, "add_spawn/mime", new AddSpawnsBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_MIME), List.of(new MobSpawnSettings.SpawnerData(CCEntityTypes.MIME.get(), 150, 1, 1))));

		addModifier(modifiers, "add_feature/spinel_ore", new AddFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_SPINEL_ORE), of(placedFeatures, CCPlacedFeatures.ORE_SPINEL, CCPlacedFeatures.ORE_SPINEL_BURIED), Decoration.UNDERGROUND_ORES));
		addModifier(modifiers, "add_feature/silver_ore", new AddFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_SILVER_ORE), of(placedFeatures, CCPlacedFeatures.ORE_SILVER, CCPlacedFeatures.ORE_SILVER_LOWER), Decoration.UNDERGROUND_ORES));
		addModifier(modifiers, "add_feature/extra_silver_ore", new AddFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_EXTRA_SILVER_ORE), of(placedFeatures, CCPlacedFeatures.ORE_SILVER_EXTRA), Decoration.UNDERGROUND_ORES));
		addModifier(modifiers, "add_feature/soul_silver_ore", new AddFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_SOUL_SILVER_ORE), of(placedFeatures, CCPlacedFeatures.ORE_SILVER_SOUL), Decoration.UNDERGROUND_ORES));
		addModifier(modifiers, "add_feature/rocky_dirt", new AddFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_ROCKY_DIRT), of(placedFeatures, CCPlacedFeatures.ORE_ROCKY_DIRT), Decoration.UNDERGROUND_ORES));
		addModifier(modifiers, "add_feature/fragile_stone", new AddFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_FRAGILE_STONE), of(placedFeatures, CCPlacedFeatures.ORE_FRAGILE_STONE, CCPlacedFeatures.ORE_FRAGILE_STONE_BURIED), Decoration.UNDERGROUND_ORES));

		addModifier(modifiers, "remove_feature/gold_ore", new RemoveFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_SILVER_ORE), of(placedFeatures, OrePlacements.ORE_GOLD, OrePlacements.ORE_GOLD_LOWER, OrePlacements.ORE_GOLD_EXTRA), Set.of(Decoration.UNDERGROUND_ORES)));
		addModifier(modifiers, "remove_feature/nether_gold_ore", new RemoveFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_SOUL_SILVER_ORE), of(placedFeatures, OrePlacements.ORE_GOLD_NETHER, OrePlacements.ORE_GOLD_DELTAS), Set.of(Decoration.UNDERGROUND_DECORATION)));
		addModifier(modifiers, "remove_feature/lapis_ore", new RemoveFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_SPINEL_ORE), of(placedFeatures, OrePlacements.ORE_LAPIS, OrePlacements.ORE_LAPIS_BURIED), Set.of(Decoration.UNDERGROUND_ORES)));
		addModifier(modifiers, "remove_feature/dirt_ore", new RemoveFeaturesBiomeModifier(tag(biomeRegistry, CCBiomeTags.HAS_ROCKY_DIRT), of(placedFeatures, OrePlacements.ORE_DIRT), Set.of(Decoration.UNDERGROUND_ORES)));

		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, CavernsAndChasms.MOD_ID, RegistryOps.create(JsonOps.INSTANCE, access), ForgeRegistries.Keys.BIOME_MODIFIERS, modifiers);
	}

	private static HolderSet<Biome> tag(Registry<Biome> biomeRegistry, TagKey<Biome> tagKey) {
		return new HolderSet.Named<>(biomeRegistry, tagKey);
	}

	private static void addModifier(HashMap<ResourceLocation, BiomeModifier> modifiers, String name, BiomeModifier modifier) {
		modifiers.put(new ResourceLocation(CavernsAndChasms.MOD_ID, name), modifier);
	}

	@SafeVarargs
	@SuppressWarnings("ConstantConditions")
	private static HolderSet<PlacedFeature> of(Registry<PlacedFeature> placedFeatures, RegistryObject<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(registryObject -> placedFeatures.getOrCreateHolderOrThrow(registryObject.getKey())).collect(Collectors.toList()));
	}

	@SafeVarargs
	@SuppressWarnings("ConstantConditions")
	private static HolderSet<PlacedFeature> of(Registry<PlacedFeature> placedFeatures, Holder<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(registryObject -> placedFeatures.getOrCreateHolderOrThrow(registryObject.unwrapKey().get())).collect(Collectors.toList()));
	}
}