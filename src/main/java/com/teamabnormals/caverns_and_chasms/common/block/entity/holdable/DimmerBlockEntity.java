package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractDimmerBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DimmerBlockEntity extends BlockEntity {
	private int holdTime;
	private boolean unpowerTick;

	public DimmerBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.DIMMER.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.holdTime = compound.getShort("HoldTime");
		this.unpowerTick = compound.getBoolean("UnpowerTick");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putShort("HoldTime", (short) this.holdTime);
		compound.putBoolean("UnpowerTick", this.unpowerTick);
	}

	public void setHeld() {
		this.holdTime = 5;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, DimmerBlockEntity blockEntity) {
		if (!level.isClientSide()) {
			int i = blockEntity.holdTime > 0 ? 15 : level.getBestNeighborSignal(pos);
			int j = state.getValue(AbstractDimmerBlock.POWER);
			if (i < j) {
				if (!blockEntity.unpowerTick) {
					blockEntity.unpowerTick = true;
				} else {
					level.setBlock(pos, state.setValue(AbstractDimmerBlock.POWER, j - 1), 3);
					level.playSound(null, pos, CCSoundEvents.DIMMER_BUZZ.get(), SoundSource.BLOCKS, 0.75F + (j - 1) / 15F * 0.5F, 0.8F);
					blockEntity.unpowerTick = false;
				}
			} else {
				if (i > j) {
					level.setBlock(pos, state.setValue(AbstractDimmerBlock.POWER, j + 1), 3);
					level.playSound(null, pos, CCSoundEvents.DIMMER_BUZZ.get(), SoundSource.BLOCKS, 0.75F + (j + 1) / 15F * 0.5F, 0.8F);
				}
				blockEntity.unpowerTick = false;
			}

			if (blockEntity.holdTime > 0) {
				--blockEntity.holdTime;
			}
		} else if (state.getValue(AbstractDimmerBlock.POWER) > 0 && level.getGameTime() % 2 == 0) {
			level.playLocalSound(pos, CCSoundEvents.DIMMER_BUZZ.get(), SoundSource.BLOCKS, 0.2F, 0.2F, false);
		}
	}
}