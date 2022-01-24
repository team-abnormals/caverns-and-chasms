package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LightningBolt.class)
public final class LightningBoltMixin {

	@Inject(method = "randomStepCleaningCopper", at = @At("RETURN"), cancellable = true)
	private static void randomStepCleaningCopper(Level level, BlockPos pos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
		if (cir.getReturnValue().isPresent()) {
			BlockPos copperPos = cir.getReturnValue().get();
			BlockState copperState = level.getBlockState(copperPos);
			if (copperState.getBlock() instanceof CCWeatheringCopper) {
				CCWeatheringCopper.getPrevious(copperState).ifPresent((prevState) -> level.setBlockAndUpdate(copperPos, prevState));
			}
		}
	}

	@Redirect(method = "clearCopperOnLightningStrike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WeatheringCopper;getFirst(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"))
	private static BlockState clearCopperOnLightningStrike(BlockState state) {
		if (state.getBlock() instanceof CCWeatheringCopper) {
			return CCWeatheringCopper.getFirst(state);
		} else {
			return state;
		}
	}
}