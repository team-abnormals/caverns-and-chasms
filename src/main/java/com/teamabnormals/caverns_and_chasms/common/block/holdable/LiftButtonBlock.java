package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.LiftButtonBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class LiftButtonBlock extends AbstractHoldableButtonBlock implements EntityBlock, HoldableBlock {
	public static final BooleanProperty SIGNAL = LiftPlateBlock.SIGNAL;
	protected final WeatherState weatherState;
	private final int ticksToStayPressed;

	public LiftButtonBlock(WeatherState weatherState, int ticks, Properties properties) {
		super(CCProperties.COPPER_BLOCK_SET.get(), ticks, properties);
		this.registerDefaultState(this.defaultBlockState().setValue(SIGNAL, false));
		this.weatherState = weatherState;
		this.ticksToStayPressed = ticks;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LiftButtonBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return HoldButtonBlock.createTickerHelper(entityType, CCBlockEntityTypes.LIFT_BUTTON.get(), LiftButtonBlockEntity::tick);
	}

	@Override
	public void deactivate(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
		super.deactivate(state, level, pos, player);
		level.scheduleTick(new BlockPos(pos), this, this.ticksToStayPressed);
	}

	@Override
	public BlockState getDeactivationState(BlockState state) {
		return super.getDeactivationState(state).setValue(LiftButtonBlock.SIGNAL, true);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(SIGNAL)) {
			level.setBlock(pos, state.setValue(SIGNAL, false), 3);
			this.updateNeighbours(state, level, pos);
		}
	}

	@Override
	protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(SIGNAL) ? 15 : 0;
	}

	@Override
	protected int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(SIGNAL) && getConnectedDirection(blockState) == side ? 15 : 0;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, SIGNAL, FACE);
	}
}