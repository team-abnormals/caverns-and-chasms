package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCConfiguredFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AzaleaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AzaleaTreeGrower.class)
public final class AzaleaTreeGrowerMixin {

	@Inject(at = @At("HEAD"), method = "getConfiguredFeature", cancellable = true)
	private void getConfiguredFeature(RandomSource random, boolean hasFlowers, CallbackInfoReturnable<ResourceKey<ConfiguredFeature<?, ?>>> cir) {
		cir.setReturnValue(CCConfiguredFeatures.AZALEA_TREE);
	}
}