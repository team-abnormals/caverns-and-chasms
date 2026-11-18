package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.DimmerBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class AbstractDimmerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, HoldableBlock {
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public AbstractDimmerBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DimmerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		// only dimmers that are on need a client ticker for the buzz; the server ramp runs on scheduled ticks
		return level.isClientSide && state.getValue(POWER) > 0 ? createTickerHelper(entityType, CCBlockEntityTypes.DIMMER.get(), DimmerBlockEntity::clientTick) : null;
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		// onPlace fires for every state change, including the ramp's own steps
		if (!level.isClientSide && !oldState.is(state.getBlock())) {
			this.schedulePowerChange(level, pos, state);
		}
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (!level.isClientSide) {
			this.schedulePowerChange(level, pos, state);
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (!(blockEntity instanceof DimmerBlockEntity dimmer)) {
			return;
		}

		boolean held = dimmer.isHeld();
		int target = held ? 15 : level.getBestNeighborSignal(pos);
		int power = state.getValue(POWER);

		if (target != power) {
			int newPower = power + (target > power ? 1 : -1);
			level.setBlock(pos, state.setValue(POWER, newPower), 3);
			level.playSound(null, pos, CCSoundEvents.DIMMER_BUZZ.get(), SoundSource.BLOCKS, 0.75F + newPower / 15F * 0.5F, 0.8F);
			power = newPower;
		}

		dimmer.ageHoldTime();

		if (held || target != power) {
			level.scheduleTick(pos, this, target < power ? 2 : 1);
		}
	}

	private void schedulePowerChange(Level level, BlockPos pos, BlockState state) {
		// scheduling the same position twice with different delays queues two ticks
		if (level.getBlockTicks().hasScheduledTick(pos, this)) {
			return;
		}

		int power = state.getValue(POWER);
		int target = this.getTargetPower(level, pos);
		if (target != power) {
			level.scheduleTick(pos, this, target < power ? 2 : 1);
		}
	}

	private int getTargetPower(Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof DimmerBlockEntity dimmer && dimmer.isHeld() ? 15 : level.getBestNeighborSignal(pos);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof DimmerBlockEntity dimmerBlockEntity) {
			this.setHeld(level, dimmerBlockEntity);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean shouldResetRightClickDelay() {
		return false;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return state.getValue(POWER);
	}
}
