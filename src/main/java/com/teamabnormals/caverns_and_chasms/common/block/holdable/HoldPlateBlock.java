package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.HoldPlateBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;

public class HoldPlateBlock extends PressurePlateBlock implements EntityBlock {

	public HoldPlateBlock(BlockSetType type, Properties properties) {
		super(type, properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HoldPlateBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return HoldButtonBlock.createTickerHelper(entityType, CCBlockEntityTypes.HOLD_PRESSURE_PLATE.get(), HoldPlateBlockEntity::tick);
	}

	public static int getOutputSpeed(BlockState state) {
		if (state.is(CCBlockTags.HOLDS_FASTER_ON)) {
			return 10;
		} else if (state.is(CCBlockTags.HOLDS_SLOWER_ON)) {
			return 40;
		} else {
			return 20;
		}
	}

	@Override
	protected int getPressedTime() {
		return 0;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) ? this.getSignalBasedOnTime(level, pos) : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return direction == Direction.UP && state.getValue(POWERED) ? this.getSignalBasedOnTime(level, pos) : 0;
	}

	public int getSignalBasedOnTime(BlockGetter level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof HoldPlateBlockEntity blockEntity) {
			return Math.min(1 + blockEntity.getTimePressed() / getOutputSpeed(level.getBlockState(pos.below())), 15);
		}
		return 0;
	}
}