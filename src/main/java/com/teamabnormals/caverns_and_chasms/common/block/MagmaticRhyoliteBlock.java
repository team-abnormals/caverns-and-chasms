package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MagmaticRhyoliteBlock extends MagmaBlock {

	public MagmaticRhyoliteBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void tick(BlockState p_221415_, ServerLevel p_221416_, BlockPos p_221417_, RandomSource p_221418_) {
		AmbientBubbleColumnBlock.updateColumn(p_221416_, p_221417_.above(), p_221415_);
	}
}