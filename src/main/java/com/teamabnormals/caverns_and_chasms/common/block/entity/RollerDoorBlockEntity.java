package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
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
	protected long opennessUpdateTime;
	protected boolean bottom;
	protected boolean bottomBelow;

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
		this.opennessOld = compound.getDouble("OpennessOld");
		this.bottom = compound.getBoolean("Bottom");
		this.bottomBelow = compound.getBoolean("BottomBelow");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putDouble("Openness", this.openness);
		compound.putDouble("OpennessOld", this.opennessOld);
		compound.putBoolean("Bottom", this.bottom);
		compound.putBoolean("BottomBelow", this.bottomBelow);
	}

	public void onPlace() {
		BlockPos blockpos = this.getBlockPos();
		BlockState blockstate = this.getBlockState();
		Direction facing = blockstate.getValue(RollerDoorBlock.FACING);
		AttachFace face = blockstate.getValue(RollerDoorBlock.FACE);

		Direction abovedirection = RollerDoorBlock.getAboveDirection(facing, face);
		BlockPos abovepos = blockpos.relative(abovedirection);
		BlockState abovestate = level.getBlockState(abovepos);

		Direction belowdirection = abovedirection.getOpposite();
		BlockPos belowpos = blockpos.relative(belowdirection);
		BlockState belowstate = level.getBlockState(belowpos);

		boolean hasdoorabove = false;

		if (this.level.getBlockEntity(abovepos) instanceof RollerDoorBlockEntity aboveentity && RollerDoorBlock.isDoorParallel(abovestate, facing, face)) {
			this.openness = aboveentity.openness;
			this.opennessOld = aboveentity.opennessOld;
			this.level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
			hasdoorabove = true;
		}

		if (this.level.getBlockEntity(belowpos) instanceof RollerDoorBlockEntity belowentity && RollerDoorBlock.isDoorParallel(belowstate, facing, face) && belowentity instanceof RollerDoorHeaderBlockEntity belowheaderentity) {
			RollerDoorHeaderBlockEntity headerentity = RollerDoorBlock.findHeaderBlockEntity(this.level, blockstate, blockpos);
			if (headerentity != null) {
				headerentity.setBlockCount(headerentity.getBlockCount() + belowheaderentity.getBlockCount());
				if (!hasdoorabove) {
					this.openness = belowentity.openness;
					this.opennessOld = belowentity.opennessOld;
					this.level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
				}
				this.level.setBlock(belowpos, CCBlocks.ROLLER_DOOR.get().defaultBlockState().setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.WATERLOGGED, belowstate.getValue(RollerDoorBlock.WATERLOGGED)), 2);
				if (this.level.getBlockEntity(belowpos) instanceof RollerDoorBlockEntity newbelowentity) {
					newbelowentity.openness = belowentity.openness;
					newbelowentity.opennessOld = belowentity.opennessOld;
					this.level.sendBlockUpdated(belowpos, belowstate, belowstate, 3);
				}
			}
		}

		int columnlength = RollerDoorBlock.getColumnLength(this.level, blockpos, facing, face);
		MutableBlockPos mutable = blockpos.relative(belowdirection, columnlength).mutable();

		for (int i = 0; i <= columnlength + 2; i++) {
			if (this.level.getBlockEntity(mutable) instanceof RollerDoorBlockEntity offsetEntity) {
				if (hasdoorabove) {
					offsetEntity.openness = this.openness;
					offsetEntity.opennessOld = this.opennessOld;
				}
				offsetEntity.bottom = i == 0;
				offsetEntity.bottomBelow = i == 1;
				BlockState offsetstate = level.getBlockState(mutable);
				this.level.sendBlockUpdated(mutable, offsetstate, offsetstate, 3);
			} else {
				break;
			}
			mutable.move(abovedirection);
		}
	}

	public void onRemove(BlockState newState) {
		BlockPos blockpos = this.getBlockPos();
		BlockState blockstate = this.getBlockState();

		Direction facing = blockstate.getValue(RollerDoorBlock.FACING);
		AttachFace face = blockstate.getValue(RollerDoorBlock.FACE);

		boolean isnewstateparalleldoor = newState.getBlock() instanceof RollerDoorBlock && RollerDoorBlock.isDoorParallel(newState, facing, face);

		Direction belowdirection = RollerDoorBlock.getBelowDirection(facing, face);
		BlockPos belowpos = blockpos.relative(belowdirection);
		BlockState belowstate = this.level.getBlockState(belowpos);

		if (!isnewstateparalleldoor && belowstate.is(CCBlocks.ROLLER_DOOR.get()) && this.level.getBlockEntity(belowpos) instanceof RollerDoorBlockEntity belowentity && RollerDoorBlock.isDoorParallel(belowstate, facing, face)) {
			this.level.setBlock(belowpos, CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState().setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.WATERLOGGED, belowstate.getValue(RollerDoorBlock.WATERLOGGED)), 2);
			if (this.level.getBlockEntity(belowpos) instanceof RollerDoorBlockEntity newbelowentity) {
				newbelowentity.openness = belowentity.openness;
				newbelowentity.opennessOld = belowentity.opennessOld;
				newbelowentity.bottom = belowentity.bottom;
				newbelowentity.bottomBelow = belowentity.bottomBelow;
				this.level.sendBlockUpdated(belowpos, belowstate, belowstate, 3);
			}
		}

		if (!isnewstateparalleldoor || newState.is(CCBlocks.ROLLER_DOOR_HEADER.get())) {
			Direction abovedirection = belowdirection.getOpposite();
			BlockPos abovepos = blockpos.relative(abovedirection);
			BlockState abovestate = this.level.getBlockState(abovepos);

			if (abovestate.getBlock() instanceof RollerDoorBlock && this.level.getBlockEntity(abovepos) instanceof RollerDoorBlockEntity aboveentity && RollerDoorBlock.isDoorParallel(abovestate, facing, face)) {
				aboveentity.bottom = true;
				this.level.sendBlockUpdated(abovepos, abovestate, abovestate, 3);

				BlockPos abovepos1 = abovepos.relative(abovedirection);
				BlockState abovestate1 = level.getBlockState(abovepos1);
				if (abovestate1.getBlock() instanceof RollerDoorBlock && this.level.getBlockEntity(abovepos1) instanceof RollerDoorBlockEntity aboveentity1 && RollerDoorBlock.isDoorParallel(abovestate1, facing, face)) {
					aboveentity1.bottomBelow = true;
					this.level.sendBlockUpdated(abovepos1, abovestate1, abovestate1, 3);
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

	public static void tick(Level level, BlockPos pos, BlockState state, RollerDoorBlockEntity blockEntity) {
		if (level.isClientSide && blockEntity.opennessUpdateTime < level.getGameTime())
			blockEntity.opennessOld = blockEntity.openness;
	}

	public double getOpenness(float partialTick) {
		return Mth.lerp(partialTick, this.opennessOld, this.openness);
	}

	public boolean isBottom() {
		return this.bottom;
	}

	public boolean hasBottomBelow() {
		return this.bottomBelow;
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