package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCFeatures;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {

	@Redirect(method = "withEmeraldOre", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;withFeature(Lnet/minecraft/world/gen/GenerationStage$Decoration;Lnet/minecraft/world/gen/feature/ConfiguredFeature;)Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;"))
	private static BiomeGenerationSettings.Builder addExtraEmeraldOre(BiomeGenerationSettings.Builder builder, GenerationStage.Decoration stage, ConfiguredFeature<?, ?> feature) {
//		if (CCConfig.COMMON.largeEmeraldVeins.get()) {
//			return builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CCFeatures.Configured.ORE_EMERALD_CHUNK);
//		} else {
			return builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.ORE_EMERALD);
//		}
	}
}
