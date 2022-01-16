package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCPlacedFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCGeneration {

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		Biome.BiomeCategory category = event.getCategory();
		BiomeGenerationSettingsBuilder generation = event.getGeneration();
		MobSpawnSettingsBuilder spawns = event.getSpawns();

		if (biome == null) return;

		ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, biome);

		List<Supplier<PlacedFeature>> oreFeatures = event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES);
		List<Supplier<PlacedFeature>> decorFeatures = event.getGeneration().getFeatures(Decoration.UNDERGROUND_DECORATION);

		if (DataUtil.matchesKeys(biome, Biomes.SOUL_SAND_VALLEY)) {
			removeGoldOre(decorFeatures);
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CCPlacedFeatures.ORE_SILVER_SOUL);
		}

		if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.OVERWORLD)) {
			if (event.getClimate().temperature <= 0.3D) {
				removeGoldOre(oreFeatures);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_LOWER);
			}

			if (biome.equals(Biomes.ICE_SPIKES.location())) {
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SILVER_EXTRA);
			}

			if (category == Biome.BiomeCategory.JUNGLE || category == Biome.BiomeCategory.SWAMP || DataUtil.matchesKeys(biome, Biomes.LUSH_CAVES)) {
				removeLapisOre(oreFeatures);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SPINEL);
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_SPINEL_BURIED);
				spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(CCEntityTypes.MIME.get(), 150, 1, 1));
			}

			removeDirtOre(oreFeatures);
			generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_ROCKY_DIRT);
			
			generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CCPlacedFeatures.ORE_FRAGILE_STONE);

			if (!DataUtil.matchesKeys(biome, Biomes.LUSH_CAVES) && category != Biome.BiomeCategory.OCEAN && category != Biome.BiomeCategory.BEACH)
				spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(CCEntityTypes.CAVEFISH.get(), 25, 4, 7));
		}
	}

	public static void removeGoldOre(List<Supplier<PlacedFeature>> oreFeatures) {
		List<Supplier<PlacedFeature>> toRemove = new ArrayList<>();
		for (Supplier<PlacedFeature> placedFeature : oreFeatures) {
			for (ConfiguredFeature<?, ?> feature : placedFeature.get().getFeatures().collect(Collectors.toList())) {
				if (feature.config instanceof OreConfiguration ore) {
					ore.targetStates.forEach((targetBlockState -> {
						if (targetBlockState.state.is(Blocks.GOLD_ORE) || targetBlockState.state.is(Blocks.DEEPSLATE_GOLD_ORE) || targetBlockState.state.is(Blocks.NETHER_GOLD_ORE)) {
							toRemove.add(placedFeature);
						}
					}));
				}
			}
		}
		toRemove.forEach(oreFeatures::remove);
	}

	public static void removeLapisOre(List<Supplier<PlacedFeature>> oreFeatures) {
		List<Supplier<PlacedFeature>> toRemove = new ArrayList<>();
		for (Supplier<PlacedFeature> placedFeature : oreFeatures) {
			for (ConfiguredFeature<?, ?> feature : placedFeature.get().getFeatures().collect(Collectors.toList())) {
				if (feature.config instanceof OreConfiguration ore) {
					ore.targetStates.forEach((targetBlockState -> {
						if (targetBlockState.state.is(Blocks.LAPIS_ORE) || targetBlockState.state.is(Blocks.DEEPSLATE_LAPIS_ORE)) {
							toRemove.add(placedFeature);
						}
					}));
				}
			}
		}

		toRemove.forEach(oreFeatures::remove);
	}

	public static void removeDirtOre(List<Supplier<PlacedFeature>> oreFeatures) {
		List<Supplier<PlacedFeature>> toRemove = new ArrayList<>();
		for (Supplier<PlacedFeature> placedFeature : oreFeatures) {
			for (ConfiguredFeature<?, ?> feature : placedFeature.get().getFeatures().collect(Collectors.toList())) {
				if (feature.config instanceof OreConfiguration ore) {
					ore.targetStates.forEach((targetBlockState -> {
						if (targetBlockState.state.is(Blocks.DIRT)) {
							toRemove.add(placedFeature);
						}
					}));
				}
			}
		}

		toRemove.forEach(oreFeatures::remove);
	}
}
