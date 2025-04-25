package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RollerDoorBlockEntity extends BlockEntity {
	protected double openness;
	protected double opennessOld;
	protected boolean bottom;

	public RollerDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RollerDoorBlockEntity(BlockPos pos, BlockState state) {
		this(CCBlockEntityTypes.ROLLER_DOOR.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.openness = compound.getDouble("Openness");
		this.bottom = compound.getBoolean("Bottom");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putDouble("Openness", this.openness);
		compound.putBoolean("Bottom", this.bottom);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RollerDoorBlockEntity blockEntity) {
		// blockEntity.opennessOld = blockEntity.openness;
	}

	public double getOpenness(float partialTick) {
		return Mth.lerp(partialTick, this.opennessOld, this.openness);
	}

	/*
	public void setOpenness(double openness) {
		this.openness = openness;
		this.level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, p_58835_.get3DDataValue());
	}
	*/

	public boolean isBottom() {
		return this.bottom;
	}

	public VoxelShape getDoorShape(BlockState state) {
		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		double openness = this.bottom ? this.openness : 0.0D;

		if (face == AttachFace.WALL) {
			if (facing == Direction.NORTH)
				return Block.box(0.0D, 16.0D * openness, 1.0D, 16.0D, 16.0D, 3.0D);
			else if (facing == Direction.SOUTH)
				return Block.box(0.0D, 16.0D * openness, 13.0D, 16.0D, 16.0D, 15.0D);
			else if (facing == Direction.WEST)
				return Block.box(1.0D, 16.0D * openness, 0.0D, 3.0D, 16.0D, 16.0D);
			else
				return Block.box(13.0D, 16.0D * openness, 0.0D, 15.0D, 16.0D, 16.0D);
		} else {
			int offset = face == AttachFace.FLOOR ? 12 : 0;
			if (facing == Direction.NORTH)
				return Block.box(0.0D, 1.0D + offset, 0.0D, 16.0D, 3.0D + offset, 16.0D - 16.0D * openness);
			else if (facing == Direction.SOUTH)
				return Block.box(0.0D, 1.0D + offset, 16.0D * openness, 16.0D, 3.0D + offset, 16.0D);
			else if (facing == Direction.WEST)
				return Block.box(0.0D, 1.0D + offset, 0.0D, 16.0D - 16.0D * openness, 3.0D + offset, 16.0D);
			else
				return Block.box(16.0D * openness, 1.0D + offset, 0.0D, 16.0D, 3.0D + offset, 16.0D);
		}
	}
}