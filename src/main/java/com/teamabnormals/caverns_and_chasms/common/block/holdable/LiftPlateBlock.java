package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.LiftPlateBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.CCProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class LiftPlateBlock extends PressurePlateBlock implements EntityBlock {
	public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");
	protected final WeatherState weatherState;
	private final int ticksToStayPressed;

	public LiftPlateBlock(WeatherState weatherState, int ticks, BlockBehaviour.Properties properties) {
		super(CCProperties.COPPER_BLOCK_SET.get(), properties);
		this.registerDefaultState(this.defaultBlockState().setValue(PRESSED, false));
		this.weatherState = weatherState;
		this.ticksToStayPressed = ticks;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LiftPlateBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return HoldButtonBlock.createTickerHelper(entityType, CCBlockEntityTypes.LIFT_PLATE.get(), LiftPlateBlockEntity::tick);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(PRESSED) ? PRESSED_AABB : AABB;
	}

	@Override
	protected BlockState setSignalForState(BlockState state, int strength) {
		return state.setValue(PRESSED, strength > 0);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(POWERED)) {
			level.setBlock(pos, state.setValue(POWERED, false), 2);
			this.updateNeighbours(level, pos);
		}
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!level.isClientSide) {
			this.checkPressed(entity, level, pos, state, -1);
		}
	}

	@Override
	public void checkPressed(@Nullable Entity entity, Level level, BlockPos pos, BlockState state, int signalStrength) {
		int i = this.getSignalStrength(level, pos);
		boolean isPressed = state.getValue(PRESSED);
		boolean shouldBePressed = i > 0;
		if (!isPressed && shouldBePressed) {
			BlockState newState = this.setSignalForState(state, i);
			level.setBlock(pos, newState, 2);
			this.updateNeighbours(level, pos);
			level.setBlocksDirty(pos, state, newState);
			level.playSound(null, pos, this.type.pressurePlateClickOn(), SoundSource.BLOCKS);
			level.gameEvent(entity, GameEvent.BLOCK_ACTIVATE, pos);
		}
	}

	public void deactivate(@Nullable Entity entity, Level level, BlockPos pos, BlockState state) {
		int i = this.getSignalStrength(level, pos);
		boolean isPressed = state.getValue(PRESSED);
		boolean shouldBePressed = i > 0;
		if (isPressed && !shouldBePressed) {
			BlockState newState = this.setSignalForState(state, i).setValue(POWERED, true);
			level.setBlock(pos, newState, 2);
			this.updateNeighbours(level, pos);
			level.setBlocksDirty(pos, state, newState);
			level.scheduleTick(new BlockPos(pos), this, this.ticksToStayPressed);
			level.playSound(null, pos, this.type.pressurePlateClickOff(), SoundSource.BLOCKS);
			level.gameEvent(entity, GameEvent.BLOCK_DEACTIVATE, pos);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED, PRESSED);
	}

	@Override
	public int getSignalStrength(Level level, BlockPos pos) {
		return super.getSignalStrength(level, pos);
	}
}