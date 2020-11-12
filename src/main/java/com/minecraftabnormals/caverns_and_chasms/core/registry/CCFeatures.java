package com.minecraftabnormals.caverns_and_chasms.core.registry;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class CCFeatures {
	public static void registerFeatures() {
		ForgeRegistries.BIOMES.getValues().forEach(CCFeatures::generateFeatures);
	}

	private static void generateFeatures(Biome biome) {
		if (biome.getCategory() == Biome.Category.DESERT || biome.getCategory() == Biome.Category.JUNGLE) {
			addSugiliteOre(biome);
		}
	}

	private static void addSugiliteOre(Biome biomeIn) {
		biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CCBlocks.SUGILITE_ORE.get().getDefaultState(), 8))
				.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(12, 0, 0, 42))));
	}
}
