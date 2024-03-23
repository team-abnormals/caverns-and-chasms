package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if ((Object) this instanceof LightningRodBlock && this instanceof CCWeatheringCopper weatheringCopper) {
			weatheringCopper.onRandomTick(state, level, pos, random);
			ci.cancel();
		}
	}
}