package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.SpikedRailBlock;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity {

	public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;activateMinecart(IIIZ)V"))
	private void tick(AbstractMinecart cart, int x, int y, int z, boolean powered) {
		BlockState state = cart.level().getBlockState(new BlockPos(x, y, z));
		if (!(state.getBlock() instanceof SpikedRailBlock)) {
			cart.activateMinecart(x, y, z, powered);
		}
	}

	@Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
	private void getBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
		BlockState blockstate = this.level().getBlockState(this.blockPosition());
		if (blockstate.is(CCBlockTags.COPPER_RAILS)) {
			cir.setReturnValue(0.985F);
		}
	}
}
