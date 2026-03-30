package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.teamabnormals.caverns_and_chasms.common.block.FloodlightBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public abstract Level level();

	@Shadow
	public abstract BlockPos blockPosition();

	@Inject(method = "isInBubbleColumn", at = @At("RETURN"), cancellable = true)
	private void isInBubbleColumn(CallbackInfoReturnable<Boolean> cir) {
		if (this.level().getBlockState(this.blockPosition()).is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get())) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "shouldPlayAmethystStepSound", at = @At("HEAD"), cancellable = true)
	private void shouldPlayAmethystStepSound(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.getBlock() instanceof FloodlightBlock && state.getValue(FloodlightBlock.FACING) != Direction.UP) {
			cir.setReturnValue(false);
		}
	}
}
