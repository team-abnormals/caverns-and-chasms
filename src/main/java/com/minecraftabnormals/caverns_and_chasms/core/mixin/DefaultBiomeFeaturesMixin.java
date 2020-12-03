package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {

	@Redirect(method = "addExtraEmeraldOre", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;addFeature(Lnet/minecraft/world/gen/GenerationStage$Decoration;Lnet/minecraft/world/gen/feature/ConfiguredFeature;)V"))
	private static void addExtraEmeraldOre(Biome biome, GenerationStage.Decoration stage, ConfiguredFeature<?, ?> feature) {
		if (CCConfig.COMMON.largeEmeraldVeins.get()) {
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
					.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.EMERALD_ORE.getDefaultState(), 19))
					.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 32))));
		} else {
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.EMERALD_ORE
					.withConfiguration(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(), Blocks.EMERALD_ORE.getDefaultState()))
					.withPlacement(Placement.EMERALD_ORE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		}
	}
}
