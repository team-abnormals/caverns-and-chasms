package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.WinchBlock;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
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
	private int holdTime;
	private float rotation;
	private float visualRotation;
	private float visualRotationOld;
	private float rewindSpeed;
	private boolean forceRollBack;

	public WinchBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.WINCH.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.holdTime = compound.getShort("HoldTime");
		this.rotation = compound.getFloat("Rotation");
		this.rewindSpeed = compound.getFloat("RewindSpeed");
		this.forceRollBack = compound.getBoolean("ForceRollBack");

		if (!compound.getBoolean("UpdateTag")) {
			this.visualRotation = this.rotation;
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putShort("HoldTime", (short) this.holdTime);
		compound.putFloat("Rotation", this.rotation);
		compound.putFloat("RewindSpeed", this.rewindSpeed);
		compound.putBoolean("ForceRollBack", this.forceRollBack);
	}

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		this.visualRotation = this.rotation;
		this.visualRotationOld = this.visualRotation;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag compound = this.saveWithoutMetadata();
		compound.putBoolean("UpdateTag", true);
		return compound;
	}

	public void setHeld() {
		if (this.holdTime <= 0) {
			this.forceRollBack = this.isFullyPowered();
		}
		this.holdTime = 2;
	}

	public float getVisualRotation(float partialTick) {
		return Mth.lerp(partialTick, this.visualRotationOld, this.visualRotation);
	}

	public int getPower() {
		return (int) (this.rotation / 24F);
	}

	public boolean isFullyPowered() {
		return this.getPower() == 15;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, WinchBlockEntity blockEntity) {
		if (level.isClientSide) {
			blockEntity.visualRotationOld = blockEntity.visualRotation;
			blockEntity.visualRotation = blockEntity.rotation;
		} else {
			int oldPower = blockEntity.getPower();
			float oldRotation = blockEntity.rotation;
			boolean isPressed = blockEntity.holdTime > 0;

			if (blockEntity.forceRollBack || (!isPressed && shouldUnwind(level, pos, state, blockEntity))) {
				blockEntity.rewindSpeed = blockEntity.rewindSpeed + 1.5F;
				blockEntity.rotation = Math.max(blockEntity.rotation - blockEntity.rewindSpeed, 0F);
			} else if (isPressed) {
				blockEntity.rewindSpeed = 0F;
				blockEntity.rotation = Math.min(blockEntity.rotation + getRewindSpeed(level, pos, state), 360F);
			}

			if (isPressed) {
				--blockEntity.holdTime;
			}

			if (blockEntity.rotation != oldRotation) {
				blockEntity.setChanged();
				level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
			}

			if (oldPower != blockEntity.getPower()) {
				WinchBlock.updateNeighbours(state, level, pos);
				if (blockEntity.isFullyPowered() && !shouldUnwind(level, pos, state, blockEntity)) {
					level.playSound(null, pos, CCSoundEvents.WINCH_LOCK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				}

				level.playSound(null, pos, CCSoundEvents.WINCH_WIND.get(), SoundSource.BLOCKS, 1.0F, 0.9F + 0.02F * blockEntity.getPower());
			}
		}
	}

	public static boolean shouldUnwind(Level level, BlockPos pos, BlockState state, WinchBlockEntity blockEntity) {
		BlockState onState = level.getBlockState(pos.relative(WinchBlock.getConnectedDirection(state).getOpposite()));
		if (blockEntity.isFullyPowered()) {
			return onState.is(CCBlockTags.WINCH_FORCES_UNWIND_ON);
		} else {
			return !onState.is(CCBlockTags.WINCH_DOES_NOT_UNWIND_ON);
		}
	}

	public static float getRewindSpeed(Level level, BlockPos pos, BlockState state) {
		BlockState onState = level.getBlockState(pos.relative(WinchBlock.getConnectedDirection(state).getOpposite()));
		if (onState.is(CCBlockTags.WINCH_WINDS_FASTER_ON)) {
			return 6F;
		} else if (onState.is(CCBlockTags.WINCH_WINDS_SLOWER_ON)) {
			return 1F;
		} else {
			return 3F;
		}
	}
}