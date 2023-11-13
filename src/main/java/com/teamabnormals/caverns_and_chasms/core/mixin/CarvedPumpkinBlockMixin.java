package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CarvedPumpkinBlock.class)
public final class CarvedPumpkinBlockMixin {

	@Inject(method = "canSpawnGolem", at = @At("RETURN"), cancellable = true)
	private void canSpawnGolem(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState abovestate = level.getBlockState(pos.above());
		if (abovestate.getBlock() instanceof LightningRodBlock && abovestate.getValue(LightningRodBlock.FACING) == Direction.UP)
			cir.setReturnValue(true);
	}

	@Inject(method = "trySpawnGolem", at = @At("HEAD"), cancellable = true)
	private void trySpawnGolem(Level level, BlockPos pos, CallbackInfo ci) {
		BlockState abovestate = level.getBlockState(pos.above());
		if (abovestate.getBlock() instanceof LightningRodBlock && abovestate.getValue(LightningRodBlock.FACING) == Direction.UP) {
			CopperGolem.createGolem(level, pos, level.getBlockState(pos));
			ci.cancel();
		}
	}
}