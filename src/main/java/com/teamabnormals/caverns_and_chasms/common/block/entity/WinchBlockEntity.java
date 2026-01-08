package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.WinchBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WinchBlockEntity extends BlockEntity {
	private int pressTime;
	private float rotation;
	private float rotationO;

	public WinchBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.WINCH.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.pressTime = compound.getShort("PressTime");
		this.rotation = compound.getFloat("Rotation");
		this.rotationO = this.rotation;
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putShort("PressTime", (short) this.pressTime);
		compound.putFloat("Rotation", this.rotation);
	}

	public void setPressed() {
		if (this.pressTime <= 0 && this.isFullyPowered()) {

		}
		this.pressTime = 5;
	}

	public float getRotation(float partialTick) {
		return Mth.lerp(partialTick, this.rotationO, this.rotation);
	}

	public int getPower() {
		return (int) (this.rotation / 24F);
	}

	public boolean isFullyPowered() {
		return this.getPower() == 15;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, WinchBlockEntity blockEntity) {
		if (level.isClientSide) {
			blockEntity.rotationO = blockEntity.rotation;
		}

		int oldpower = blockEntity.getPower();

		if (blockEntity.pressTime > 0) {
			--blockEntity.pressTime;
			blockEntity.rotation = Math.min(blockEntity.rotation + 4F, 360F);
		} else if (!blockEntity.isFullyPowered()) {
			blockEntity.rotation = Math.max(blockEntity.rotation - 2F, 0F);
		}

		if (oldpower != blockEntity.getPower()) {
			WinchBlock.updateNeighbours(state, level, pos);
		}
	}
}