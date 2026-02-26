package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.WinchBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WinchBlockEntity extends BlockEntity {
	private int pressTime;
	private float rotation;
	private float rotationO;
	private float rewindSpeed;
	private boolean forceRollBack;

	public WinchBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.WINCH.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.pressTime = compound.getShort("PressTime");
		this.rotation = compound.getFloat("Rotation");
		// this.rotationO = this.rotation;
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putShort("PressTime", (short) this.pressTime);
		compound.putFloat("Rotation", this.rotation);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	public void setPressed() {
		if (this.pressTime <= 0) {
			this.forceRollBack = this.isFullyPowered();
		}
		this.pressTime = 2;
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

		if (blockEntity.forceRollBack || (!blockEntity.isFullyPowered() && blockEntity.pressTime <= 0)) {
			blockEntity.rewindSpeed = blockEntity.rewindSpeed + 1.5F;
			blockEntity.rotation = Math.max(blockEntity.rotation - blockEntity.rewindSpeed, 0F);
			level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
		} else if (blockEntity.pressTime > 0) {
			blockEntity.rewindSpeed = 0F;
			blockEntity.rotation = Math.min(blockEntity.rotation + 3F, 360F);
			level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
		}

		if (blockEntity.pressTime > 0) {
			--blockEntity.pressTime;
		}

		if (!level.isClientSide && oldpower != blockEntity.getPower()) {
			WinchBlock.updateNeighbours(state, level, pos);
			level.playSound(null, pos, CCSoundEvents.WINCH_WIND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}
}