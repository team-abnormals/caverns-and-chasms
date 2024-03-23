package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

	@Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
	private void isRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && (Object) this instanceof LightningRodBlock && CCWeatheringCopper.getNext(state.getBlock()).isPresent()) {
			cir.setReturnValue(true);
		}
	}
}