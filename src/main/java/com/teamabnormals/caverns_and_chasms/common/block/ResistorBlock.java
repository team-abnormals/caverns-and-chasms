package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ResistorBlock extends DiodeBlock {
	public static final IntegerProperty RESISTANCE = IntegerProperty.create("resistance", 1, 14);

	public ResistorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(RESISTANCE, 1));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		} else {
			level.setBlock(pos, cycleOrReverse(state, player, RESISTANCE, 1, 14), 3);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static BlockState cycleOrReverse(BlockState state, Player player, IntegerProperty property, int min, int max) {
		BlockState newState = state.cycle(property);
		if (player.isSecondaryUseActive()) {
			int val = state.getValue(property) - 1;
			if (val < min) val = max;
			newState = state.setValue(property, val);
		}
		return newState;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean b) {
		if (state.canSurvive(level, pos) && !level.isClientSide()) {
			Direction dir = state.getValue(FACING).getCounterClockWise();
			BlockPos dirPos = pos.relative(dir);
			if (dirPos.equals(otherPos) && level.hasSignal(dirPos, dir)) {
				level.setBlock(pos, state.cycle(RESISTANCE), 2);
			}
		}

		super.neighborChanged(state, level, pos, block, otherPos, b);
	}

	@Override
	protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
		int max = level instanceof Level ? this.getInputSignal((Level) level, pos, state) : 15;
		return Math.max(max - state.getValue(RESISTANCE), 0);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 2;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, RESISTANCE, POWERED);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
		Direction facing = state.getValue(RepeaterBlock.FACING);
		return direction != facing.getCounterClockWise();
	}
}
