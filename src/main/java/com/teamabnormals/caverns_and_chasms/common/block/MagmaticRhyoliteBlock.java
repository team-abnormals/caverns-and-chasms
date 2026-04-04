package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MagmaticRhyoliteBlock extends Block {

	public MagmaticRhyoliteBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void tick(BlockState p_221415_, ServerLevel p_221416_, BlockPos p_221417_, RandomSource p_221418_) {
		AmbientBubbleColumnBlock.updateColumn(p_221416_, p_221417_.above(), p_221415_);
	}

	@Override
	public BlockState updateShape(BlockState p_54811_, Direction p_54812_, BlockState p_54813_, LevelAccessor p_54814_, BlockPos p_54815_, BlockPos p_54816_) {
		if (p_54812_ == Direction.UP && p_54813_.is(Blocks.WATER)) {
			p_54814_.scheduleTick(p_54815_, this, 20);
		}

		return super.updateShape(p_54811_, p_54812_, p_54813_, p_54814_, p_54815_, p_54816_);
	}

	@Override
	public void onPlace(BlockState p_54823_, Level p_54824_, BlockPos p_54825_, BlockState p_54826_, boolean p_54827_) {
		p_54824_.scheduleTick(p_54825_, this, 20);
	}
}