package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class CharcoalBlock extends RotatedPillarBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public CharcoalBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(LIT, shouldBeLit(context.getLevel(), context.getClickedPos()));
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide()) {
			boolean flag = state.getValue(LIT);
			if (flag != shouldBeLit(level, pos)) {
				level.setBlock(pos, state.cycle(LIT), 2);
			}
		}
	}

	public static boolean shouldBeLit(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos.above());
		if (state.is(Blocks.FIRE) || state.is(Blocks.LAVA)) {
			return true;
		}
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}
}