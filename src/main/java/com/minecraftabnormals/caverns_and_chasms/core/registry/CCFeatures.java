package com.minecraftabnormals.caverns_and_chasms.core.registry;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CCFeatures {
	public static void registerFeatures() {
		ForgeRegistries.BIOMES.getValues().forEach(CCFeatures::generateFeatures);
	}

	private static void generateFeatures(Biome biome) {
		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD)) {
			if (biome.getDefaultTemperature() <= 0.3D) {
				removeGold(biome);
				addSilverOre(biome);
				if (biome == Biomes.ICE_SPIKES)
					addExtraSilverOre(biome);
			}

			if (biome.getCategory() == Biome.Category.DESERT || biome.getCategory() == Biome.Category.JUNGLE) {
				addSugiliteOre(biome);
			}
		}

		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
			if (biome == Biomes.SOUL_SAND_VALLEY) {
				removeGold(biome);
				addNetherSilverOre(biome);
			}
		}
	}

	private static void addSugiliteOre(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CCBlocks.SUGILITE_ORE.get().getDefaultState(), 8))
				.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(9, 0, 0, 42))));
	}

	public static void addSilverOre(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CCBlocks.SILVER_ORE.get().getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
	}

	public static void addExtraSilverOre(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CCBlocks.SILVER_ORE.get().getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 32, 32, 80))));
	}

	public static void addNetherSilverOre(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, CCBlocks.NETHER_SILVER_ORE.get().getDefaultState(), 10)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 10, 20, 128))));
	}

	public static void removeGold(Biome biome) {
		for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
			List<ConfiguredFeature<?, ?>> features = biome.getFeatures(stage);
			for (ConfiguredFeature<?, ?> configuredFeature : features) {
				if (configuredFeature.config instanceof DecoratedFeatureConfig) {
					DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) configuredFeature.config;
					if (decorated.feature.config instanceof OreFeatureConfig) {
						OreFeatureConfig ore = (OreFeatureConfig) decorated.feature.config;
						if (ore.state == Blocks.GOLD_ORE.getDefaultState() || ore.state == Blocks.NETHER_GOLD_ORE.getDefaultState()) {
							biome.getFeatures(stage).remove(configuredFeature);
						}
					}
				}
			}
		}
	}
}
