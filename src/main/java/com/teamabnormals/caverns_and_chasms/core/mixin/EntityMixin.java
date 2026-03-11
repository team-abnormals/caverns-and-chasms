package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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
}
