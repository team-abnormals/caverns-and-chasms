package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractHoldableBlockEntity extends BlockEntity implements HoldableBlockEntity {
	protected int holdTime;

	public AbstractHoldableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		this.holdTime = tag.getShort("HoldTime");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putShort("HoldTime", (short) this.holdTime);
	}

	@Override
	public void setHeld() {
		this.holdTime = 2;
	}
}