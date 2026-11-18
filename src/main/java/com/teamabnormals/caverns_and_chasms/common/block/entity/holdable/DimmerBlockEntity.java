package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractDimmerBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DimmerBlockEntity extends AbstractHoldableBlockEntity {
	private static final int BUZZ_INTERVAL = 2;
	private static final double BUZZ_RANGE = 16.0D;

	public DimmerBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.DIMMER.get(), pos, state);
	}

	@Override
	public void setHeld() {
		this.holdTime = 5;
		Level level = this.level;
		if (level != null && !level.isClientSide) {
			Block block = this.getBlockState().getBlock();
			if (!level.getBlockTicks().hasScheduledTick(this.worldPosition, block)) {
				level.scheduleTick(this.worldPosition, block, 1);
			}
		}
	}

	public boolean isHeld() {
		return this.holdTime > 0;
	}

	public void ageHoldTime() {
		if (this.holdTime > 0) {
			--this.holdTime;
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, DimmerBlockEntity blockEntity) {
		if (state.getValue(AbstractDimmerBlock.POWER) > 0 && level.getGameTime() % BUZZ_INTERVAL == Math.floorMod(pos.hashCode(), BUZZ_INTERVAL)) {
			if (level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BUZZ_RANGE, entity -> true) != null) {
				level.playLocalSound(pos, CCSoundEvents.DIMMER_BUZZ.get(), SoundSource.BLOCKS, 0.2F, 0.2F, false);
			}
		}
	}
}
