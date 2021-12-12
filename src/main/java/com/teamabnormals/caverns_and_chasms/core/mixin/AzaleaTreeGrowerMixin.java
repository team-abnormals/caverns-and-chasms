package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCFeatures.CCConfiguredFeatures;
import net.minecraft.world.level.block.grower.AzaleaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(AzaleaTreeGrower.class)
public final class AzaleaTreeGrowerMixin {

	@Inject(at = @At("HEAD"), method = "getConfiguredFeature", cancellable = true)
	private void getConfiguredFeature(Random random, boolean bool, CallbackInfoReturnable<ConfiguredFeature<?, ?>> cir) {
		cir.setReturnValue(CCConfiguredFeatures.AZALEA_TREE);
	}
}