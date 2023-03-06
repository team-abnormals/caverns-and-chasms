package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;activateMinecart(IIIZ)V"))
	private void tick(AbstractMinecart cart, int x, int y, int z, boolean powered) {
		BlockState state = cart.level.getBlockState(new BlockPos(x, y, z));
		if (!state.is(CCBlocks.SPIKED_RAIL.get())) {
			cart.activateMinecart(x, y, z, powered);
		}
	}
}
