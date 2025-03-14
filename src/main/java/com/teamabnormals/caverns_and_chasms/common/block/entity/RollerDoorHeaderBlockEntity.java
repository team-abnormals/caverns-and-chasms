package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoor;
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

public class RollerDoorHeaderBlockEntity extends BlockEntity {
	private int blocks;
	private int liftTime;

	public RollerDoorHeaderBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.ROLLER_DOOR_HEADER.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.blocks = compound.getInt("Blocks");
		this.liftTime = compound.getShort("LiftTime");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("Blocks", this.blocks);
		compound.putShort("LiftTime", (short) this.liftTime);
	}

	public int getBlockCount() {
		return this.blocks;
	}

	public void setLifted() {
		this.liftTime = 5;
	}

	public boolean isLifted() {
		return this.liftTime > 0;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RollerDoorHeaderBlockEntity blockEntity) {
		if (!level.isClientSide) {
			if (blockEntity.liftTime > 0)
				--blockEntity.liftTime;

			int openness = state.getValue(RollerDoorBlock.OPENNESS);
			int columnlength = blockEntity.getColumnLength();

			if (blockEntity.shouldOpen()) {
				if (openness < 15) {
					level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, openness + 1), 3);
				} else if (columnlength > 1) {
					Direction facing = state.getValue(RollerDoorBlock.FACING);
					AttachFace face = state.getValue(RollerDoorBlock.FACE);

					BlockPos offsetpos = pos.relative(RollerDoor.getBelowDirection(facing, face), columnlength - 1);
					level.setBlock(offsetpos, level.getBlockState(offsetpos).getFluidState().createLegacyBlock(), 3);

					level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, 0).setValue(RollerDoorBlock.BOTTOM, columnlength == 2), 3);

					++blockEntity.blocks;
				}
			} else {
				if (openness > 0) {
					level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, openness - 1), 3);
				} else if (blockEntity.blocks > 0) {
					Direction facing = state.getValue(RollerDoorBlock.FACING);
					AttachFace face = state.getValue(RollerDoorBlock.FACE);

					BlockPos offsetpos = pos.relative(RollerDoor.getBelowDirection(facing, face), columnlength);
					if (offsetpos.getY() >= level.getMinBuildHeight()) {
						BlockState offsetstate = level.getBlockState(offsetpos);
						if (offsetstate.isAir() || offsetstate.getPistonPushReaction() == PushReaction.DESTROY) {
							FluidState fluidstate = level.getFluidState(offsetpos);
							level.destroyBlock(offsetpos, true);
							level.setBlock(offsetpos, CCBlocks.ROLLER_DOOR.get().defaultBlockState().setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.OPENNESS, 15).setValue(RollerDoorBlock.BOTTOM, true).setValue(RollerDoorBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER), 3);

							level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, 15).setValue(RollerDoorBlock.BOTTOM, false), 3);
							--blockEntity.blocks;
						}
					}
				}
			}
		}
	}

	public boolean shouldOpen() {
		return this.isLifted() || this.level.hasNeighborSignal(this.getBlockPos());
	}

	public int getColumnLength() {
		int length = 1;

		Direction facing = this.getBlockState().getValue(RollerDoorBlock.FACING);
		AttachFace face = this.getBlockState().getValue(RollerDoorBlock.FACE);
		MutableBlockPos mutable = this.getBlockPos().mutable();

		while (true) {
			mutable.move(RollerDoor.getBelowDirection(facing, face));
			BlockState offsetstate = this.level.getBlockState(mutable);

			if (offsetstate.getBlock() instanceof RollerDoor && offsetstate.getValue(RollerDoorBlock.FACING) == facing && offsetstate.getValue(RollerDoorBlock.FACE) == face)
				++length;
			else
				break;
		}

		return length;
	}
}