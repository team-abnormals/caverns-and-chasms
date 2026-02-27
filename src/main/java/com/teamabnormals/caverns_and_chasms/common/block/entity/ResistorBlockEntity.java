package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ResistorBlockEntity extends BlockEntity {
	private int output;

	public ResistorBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.RESISTOR.get(), pos, state);
	}

	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("OutputSignal", this.output);
	}

	public void load(CompoundTag tag) {
		super.load(tag);
		this.output = tag.getInt("OutputSignal");
	}

	public int getOutputSignal() {
		return this.output;
	}

	public void setOutputSignal(int signal) {
		this.output = signal;
	}
}