package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractMovingDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MovingDoorBlockEntity extends BlockEntity {
	protected double openness;
	protected double opennessOld;
	protected boolean isBelowBottom;
	protected MovingDoorType doorType;
	protected MovingDoorType belowDoorType;
	protected long lastUpdateTick;

	public MovingDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.doorType = ((AbstractMovingDoorBlock) state.getBlock()).getDefaultDoorType();
	}

	public MovingDoorBlockEntity(BlockPos pos, BlockState state) {
		this(CCBlockEntityTypes.MOVING_DOOR.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.openness = compound.getDouble("Openness");
		this.isBelowBottom = compound.getBoolean("BelowIsBottom");
		MovingDoorType thisType = MovingDoorType.byName((compound.getString("DoorType")));
		if (thisType != null) {
			this.doorType = thisType;
		}
		MovingDoorType belowType = MovingDoorType.byName((compound.getString("BelowDoorType")));
		this.belowDoorType = belowType;

		// Only in update tag
		this.lastUpdateTick = compound.getLong("LastUpdateTick");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putDouble("Openness", this.openness);
		compound.putBoolean("BelowIsBottom", this.isBelowBottom);
		if (this.doorType != null) {
			compound.putString("DoorType", this.doorType.getRegistryName());
		}
		if (this.belowDoorType != null) {
			compound.putString("BelowDoorType", this.belowDoorType.getRegistryName());
		}
	}

	public void onPlace(MovingDoorType doorType) {
		this.doorType = doorType;

		BlockPos thisPos = this.getBlockPos();
		BlockState thisState = this.getBlockState();
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		Direction aboveDir = thisBlock.getAboveDirection(thisState);
		BlockPos abovePos = thisPos.relative(aboveDir);
		BlockState aboveState = this.level.getBlockState(abovePos);

		Direction belowDir = aboveDir.getOpposite();
		BlockPos belowPos = thisPos.relative(belowDir);
		BlockState belowState = this.level.getBlockState(belowPos);

		boolean hasDoorAbove = false;

		// Copy the openness of the door above
		if (this.level.getBlockEntity(abovePos) instanceof MovingDoorBlockEntity aboveEntity && thisBlock.partOfSameDoor(thisState, aboveState)) {
			this.openness = aboveEntity.openness;
			this.opennessOld = aboveEntity.opennessOld;
			aboveEntity.belowDoorType = doorType;
			this.level.sendBlockUpdated(abovePos, aboveState, aboveState, 3);
			hasDoorAbove = true;
		}

		// Merge with door below (assumed to be a header)
		if (this.level.getBlockEntity(belowPos) instanceof MovingDoorHeaderBlockEntity belowEntity && thisBlock.partOfSameDoor(thisState, belowState)) {
			MovingDoorHeaderBlockEntity aboveHeaderEntity = thisBlock.findHeaderBlockEntity(this.level, thisState, thisPos);
			this.belowDoorType = belowEntity.doorType;
			if (aboveHeaderEntity != null) {
				// Merged header below drops all its stored blocks because nothing else makes any spatial sense
				for (ItemStack itemStack : belowEntity.getStoredBlocksAsStacks()) {
					Block.popResource(this.level, belowPos, itemStack);
				}

				if (!hasDoorAbove) {
					this.openness = belowEntity.openness;
					this.opennessOld = belowEntity.opennessOld;
				}

				AbstractMovingDoorBlock belowBlock = (AbstractMovingDoorBlock) belowState.getBlock();
				this.level.setBlock(belowPos, belowBlock.copyDirectionPropertiesTo(belowBlock.getNormalBlock().defaultBlockState(), thisState).setValue(AbstractMovingDoorBlock.WATERLOGGED, belowState.getValue(AbstractMovingDoorBlock.WATERLOGGED)), 2);

				if (this.level.getBlockEntity(belowPos) instanceof MovingDoorBlockEntity newBelowEntity) {
					newBelowEntity.openness = belowEntity.openness;
					newBelowEntity.opennessOld = belowEntity.opennessOld;
					newBelowEntity.isBelowBottom = belowEntity.isBelowBottom;
					newBelowEntity.doorType = belowEntity.doorType;
					newBelowEntity.belowDoorType = belowEntity.belowDoorType;
					this.level.sendBlockUpdated(belowPos, belowState, belowState, 3);
				}
			}
		}

		// Set the openness and bottom values of all doors below
		int doorsBelowCount = thisBlock.countDoorsBelow(this.level, thisPos, thisState);
		MutableBlockPos mutable = thisPos.relative(belowDir, doorsBelowCount).mutable();

		for (int i = 0; i <= doorsBelowCount + 2; i++) {
			BlockState offsetState = this.level.getBlockState(mutable);
			if (this.level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity && thisBlock.partOfSameDoor(thisState, offsetState)) {
				if (hasDoorAbove) {
					offsetEntity.openness = this.openness;
					offsetEntity.opennessOld = this.opennessOld;
				}
				offsetEntity.isBelowBottom = i == 1;
				this.level.sendBlockUpdated(mutable, offsetState, offsetState, 3);
			} else {
				break;
			}
			mutable.move(aboveDir);
		}
	}

	public void onRemove(BlockState newState) {
		BlockPos thisPos = this.getBlockPos();
		BlockState thisState = this.getBlockState();
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		boolean isNewStateAlignedDoor = newState.getBlock() instanceof AbstractMovingDoorBlock && thisBlock.partOfSameDoor(thisState, newState);

		Direction belowDir = thisBlock.getBelowDirection(thisState);
		BlockPos belowPos = thisPos.relative(belowDir);
		BlockState belowState = this.level.getBlockState(belowPos);

		// Turn door below into header door
		if (!isNewStateAlignedDoor && this.level.getBlockEntity(belowPos) instanceof MovingDoorBlockEntity belowEntity && thisBlock.partOfSameDoor(thisState, belowState)) {
			AbstractMovingDoorBlock belowBlock = (AbstractMovingDoorBlock) belowState.getBlock();
			this.level.setBlock(belowPos, belowBlock.copyDirectionPropertiesTo(belowBlock.getHeaderBlock().defaultBlockState(), thisState).setValue(AbstractMovingDoorBlock.WATERLOGGED, belowState.getValue(AbstractMovingDoorBlock.WATERLOGGED)), 2);

			if (this.level.getBlockEntity(belowPos) instanceof MovingDoorHeaderBlockEntity newBelowEntity) {
				newBelowEntity.openness = belowEntity.openness;
				newBelowEntity.opennessOld = belowEntity.opennessOld;
				newBelowEntity.isBelowBottom = belowEntity.isBelowBottom;
				newBelowEntity.doorType = belowEntity.doorType;
				newBelowEntity.belowDoorType = belowEntity.belowDoorType;
				this.level.sendBlockUpdated(belowPos, belowState, belowState, 3);
			}
		}

		// TODO: Why does this check for if new state was a header?
		// Set the bottom and bottomBelow fields of the door blocks above
		if (!isNewStateAlignedDoor || (newState.getBlock() instanceof AbstractMovingDoorBlock newBlock && newBlock.isHeader())) {
			Direction aboveDir = belowDir.getOpposite();
			BlockPos abovePos = thisPos.relative(aboveDir);
			BlockState aboveState = this.level.getBlockState(abovePos);

			if (this.level.getBlockEntity(abovePos) instanceof MovingDoorBlockEntity aboveEntity && thisBlock.partOfSameDoor(thisState, aboveState)) {
				aboveEntity.isBelowBottom = false;
				aboveEntity.belowDoorType = null;
				this.level.sendBlockUpdated(abovePos, aboveState, aboveState, 3);

				BlockPos abovePos1 = abovePos.relative(aboveDir);
				BlockState aboveState1 = level.getBlockState(abovePos1);
				if (this.level.getBlockEntity(abovePos1) instanceof MovingDoorBlockEntity aboveEntity1 && thisBlock.partOfSameDoor(thisState, aboveState1)) {
					aboveEntity1.isBelowBottom = true;
					this.level.sendBlockUpdated(abovePos1, aboveState1, aboveState1, 3);
				}
			}
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, MovingDoorBlockEntity thisEntity) {
		if (thisEntity.lastUpdateTick < level.getGameTime()) {
			thisEntity.opennessOld = thisEntity.openness;
		}
	}

	public double getOpenness(float partialTick) {
		double d0 = this.openness - this.opennessOld;
		if (d0 > 0.5D) {
			d0 -= 1.0D;
		} else if (d0 < -0.5D) {
			d0 += 1.0D;
		}

		double d1 = this.opennessOld + partialTick * d0;

		if (d1 > 1.0D) {
			d1 -= 1.0D;
		} else if (d1 < 0.0D) {
			d1 += 1.0D;
		}

		return d1;
	}

	public boolean isBottom() {
		return this.belowDoorType == null;
	}

	public boolean isBelowBottom() {
		return this.isBelowBottom;
	}

	public MovingDoorType getDoorType() {
		return this.doorType;
	}

	public void setDoorType(MovingDoorType doorType) {
		this.doorType = doorType;
	}

	public MovingDoorType getBelowDoorType() {
		return this.belowDoorType;
	}
}