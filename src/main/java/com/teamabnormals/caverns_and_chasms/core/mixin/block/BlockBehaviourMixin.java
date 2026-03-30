package com.teamabnormals.caverns_and_chasms.core.mixin.block;

import com.teamabnormals.caverns_and_chasms.common.block.AmbientBubbleColumnBlock;
import com.teamabnormals.caverns_and_chasms.common.block.weathering.CCWeatheringCopper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if ((Object) this instanceof LightningRodBlock && this instanceof CCWeatheringCopper weatheringCopper) {
			weatheringCopper.onRandomTick(state, level, pos, random);
			ci.cancel();
		}
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (state.is(Blocks.SOUL_SOIL)) {
			AmbientBubbleColumnBlock.updateColumn(level, pos.above(), state);
		}
	}

	@Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
	private void onPlace(BlockState state, Level level, BlockPos pos, BlockState otherState, boolean b, CallbackInfo ci) {
		if (state.is(Blocks.SOUL_SOIL) && (Object) this instanceof Block block) {
			level.scheduleTick(pos, block, 20);
		}
	}

	@Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
	private void updateShape(BlockState state, Direction dir, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> cir) {
		if (state.is(Blocks.SOUL_SOIL) && dir == Direction.UP && otherState.is(Blocks.WATER) && (Object) this instanceof Block block) {
			level.scheduleTick(pos, block, 20);
		}
	}
}