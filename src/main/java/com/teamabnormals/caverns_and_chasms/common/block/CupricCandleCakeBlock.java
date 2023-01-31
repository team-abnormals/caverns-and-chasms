package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CupricCandleCakeBlock extends CandleCakeBlock {

	public CupricCandleCakeBlock(Block candle, Properties properties) {
		super(candle, properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			this.getParticleOffsets(state).forEach((vec3) -> CupricCandleBlock.addCupricParticlesAndSound(level, vec3.add(pos.getX(), pos.getY(), pos.getZ()), random));
		}
	}
}