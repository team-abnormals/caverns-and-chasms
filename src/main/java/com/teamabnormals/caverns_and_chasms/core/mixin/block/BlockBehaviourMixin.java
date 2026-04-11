package com.teamabnormals.caverns_and_chasms.core.mixin.block;

import com.teamabnormals.caverns_and_chasms.common.block.AmbientBubbleColumnBlock;
import com.teamabnormals.caverns_and_chasms.core.other.CCCompat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
	@Shadow
	protected abstract Block asBlock();

	@Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
	private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
		if (CCCompat.POLISHED_SHALE_SOUNDS.contains(this.asBlock())) {
			cir.setReturnValue(CCSoundTypes.POLISHED_SHALE);
		} else if (CCCompat.SHALE_BRICKS_SOUNDS.contains(this.asBlock())) {
			cir.setReturnValue(CCSoundTypes.SHALE_BRICKS);
		}
	}

	@Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
	private void isRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && (Object) this instanceof LightningRodBlock && WeatheringCopper.getNext(state.getBlock()).isPresent()) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if ((Object) this instanceof LightningRodBlock && this instanceof WeatheringCopper weatheringCopper) {
			weatheringCopper.changeOverTime(state, level, pos, random);
			ci.cancel();
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (state.is(Blocks.SOUL_SOIL)) {
			AmbientBubbleColumnBlock.updateColumn(level, pos.above(), state);
		}
	}

	@Inject(method = "onPlace", at = @At("HEAD"))
	private void onPlace(BlockState state, Level level, BlockPos pos, BlockState otherState, boolean b, CallbackInfo ci) {
		if (state.is(Blocks.SOUL_SOIL) && (Object) this instanceof Block block) {
			level.scheduleTick(pos, block, 20);
		}
	}

	@Inject(method = "updateShape", at = @At("RETURN"))
	private void updateShape(BlockState state, Direction dir, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> cir) {
		if (state.is(Blocks.SOUL_SOIL) && dir == Direction.UP && otherState.is(Blocks.WATER) && (Object) this instanceof Block block) {
			level.scheduleTick(pos, block, 20);
		}
	}
}