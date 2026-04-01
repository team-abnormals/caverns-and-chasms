package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.ResistorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

public class ResistorBlock extends DiodeBlock implements EntityBlock {
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
			this.refreshOutputState(level, pos, level.getBlockState(pos));
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
	protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
		int oldoutput = level.getBlockEntity(pos) instanceof ResistorBlockEntity resistorblockentity ? resistorblockentity.getOutputSignal() : 0;
		int newoutput = this.calculateOutputSignal(level, pos, state);

		if (newoutput != oldoutput && !level.getBlockTicks().willTickThisTick(pos, this)) {
			TickPriority tickpriority = TickPriority.HIGH;
			if (this.shouldPrioritize(level, pos, state)) {
				tickpriority = TickPriority.EXTREMELY_HIGH;
			} else if (newoutput < oldoutput) {
				tickpriority = TickPriority.VERY_HIGH;
			}

			level.scheduleTick(pos, this, this.getDelay(state), tickpriority);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		this.refreshOutputState(level, pos, state);
	}

	@Override
	protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
		return level.getBlockEntity(pos) instanceof ResistorBlockEntity resistorblockentity ? resistorblockentity.getOutputSignal() : 0;
	}

	@Override
	protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
		return this.calculateOutputSignal(level, pos, state) > 0;
	}

	private int calculateOutputSignal(Level level, BlockPos pos, BlockState state) {
		return Math.max(this.getInputSignal(level, pos, state) - state.getValue(RESISTANCE), 0);
	}

	private void refreshOutputState(Level level, BlockPos pos, BlockState state) {
		int oldoutput = 0;
		int newoutput = this.calculateOutputSignal(level, pos, state);
		if (level.getBlockEntity(pos) instanceof ResistorBlockEntity resistorblockentity) {
			oldoutput = resistorblockentity.getOutputSignal();
			resistorblockentity.setOutputSignal(newoutput);
		}

		if (newoutput != oldoutput) {
			boolean oldpowered = state.getValue(POWERED);
			boolean newpowered = this.shouldTurnOn(level, pos, state);
			if (oldpowered && !newpowered) {
				level.setBlock(pos, state.setValue(POWERED, false), 2);
			} else if (!oldpowered && newpowered) {
				level.setBlock(pos, state.setValue(POWERED, true), 2);
				/*
				if (!newpowered) {
					level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
				}
				*/
			}

			this.updateNeighborsInFront(level, pos, state);
		}
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

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ResistorBlockEntity(pos, state);
	}
}
