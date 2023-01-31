package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;

public class InductorBlock extends DirectionalBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty COOLDOWN = BooleanProperty.create("cooldown");
	public static final IntegerProperty INDUCTION_POWER = IntegerProperty.create("induction_power", 0, 5);

	public InductorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false).setValue(COOLDOWN, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getNearestLookingDirection().getOpposite();
		boolean powered = context.getLevel().hasNeighborSignal(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(FACING, direction).setValue(POWERED, powered);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!level.isClientSide() && state.getValue(POWERED)) {
			this.induceNearbyCoils(level, pos);
		}
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return state.getValue(INDUCTION_POWER) * 3;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean powered = level.hasNeighborSignal(pos);
		if (powered != state.getValue(POWERED) && !state.getValue(COOLDOWN) && !hasInductionSignal(state)) {
			level.setBlock(pos, state.setValue(POWERED, powered), 3);
			this.induceNearbyCoils(level, pos);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (hasInductionSignal(state)) {
			level.setBlock(pos, state.setValue(COOLDOWN, true).setValue(INDUCTION_POWER, 0), 3);
			level.scheduleTick(pos, state.getBlock(), 4);
		} else if (state.getValue(COOLDOWN)) {
			boolean powered = level.hasNeighborSignal(pos);
			level.setBlock(pos, state.setValue(COOLDOWN, false).setValue(POWERED, powered), 3);
		}
	}

	private void induceNearbyCoils(Level level, BlockPos pos) {
		for (BlockPos blockpos : BlockPos.betweenClosed(pos.getX() - 8, pos.getY() - 8, pos.getZ() - 8, pos.getX() + 8, pos.getY() + 8, pos.getZ() + 8)) {
			BlockState blockstate = level.getBlockState(blockpos);
			if (!blockpos.equals(pos) && blockstate.is(this)) {
				int xdist = Math.abs(blockpos.getX() - pos.getX());
				int ydist = Math.abs(blockpos.getY() - pos.getY());
				int zdist = Math.abs(blockpos.getZ() - pos.getZ());
				int power = Math.min(5, 9 - Math.max(Math.max(xdist, ydist), zdist));
				this.induceCoil(level, blockstate, blockpos, power);
			}
		}
	}

	private void induceCoil(Level level, BlockState state, BlockPos pos, int power) {
		if (!state.getValue(POWERED) && !state.getValue(COOLDOWN) && !hasInductionSignal(state)) {
			level.setBlock(pos, state.setValue(INDUCTION_POWER, power), 3);
			level.scheduleTick(pos, state.getBlock(), 8);
		}
	}

	private boolean hasInductionSignal(BlockState state) {
		return state.getValue(INDUCTION_POWER) > 0;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_57407_) {
		p_57407_.add(FACING, POWERED, COOLDOWN, INDUCTION_POWER);
	}
}
