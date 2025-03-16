package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoor;
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class RollerDoorHeaderBlockEntity extends BlockEntity {
	private int blocks;
	private int liftTime;
	private boolean isBeingLifted;

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

	@Override
	public boolean triggerEvent(int p_58837_, int p_58838_) {
		if (p_58837_ == 1) {
			this.isBeingLifted = p_58838_ == 1;
			return true;
		} else {
			return super.triggerEvent(p_58837_, p_58838_);
		}
	}

	public int getBlockCount() {
		return this.blocks;
	}

	public void setBeingLifted() {
		this.liftTime = 5;
		this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, 1);
	}

	public boolean isBeingLifted() {
		return this.liftTime > 0;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RollerDoorHeaderBlockEntity blockEntity) {
		if (blockEntity.liftTime > 0) {
			--blockEntity.liftTime;
			if (blockEntity.liftTime == 0)
				level.blockEvent(pos, state.getBlock(), 1, 0);
		}

		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		int openness = state.getValue(RollerDoorBlock.OPENNESS);
		int columnlength = blockEntity.getColumnLength();

		if (blockEntity.shouldOpen()) {
			if (openness < 15) {
				moveCollidedEntities(level, pos, openness + 1, columnlength, columnlength, facing, face, true);

				if (!level.isClientSide)
					level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, openness + 1), 3);
			} else if (columnlength > 1) {
				BlockPos offsetpos = pos.relative(RollerDoor.getBelowDirection(facing, face), columnlength - 1);

				moveCollidedEntities(level, pos, 0, columnlength, columnlength - 1, facing, face, true);

				if (!level.isClientSide) {
					level.setBlock(offsetpos, level.getBlockState(offsetpos).getFluidState().createLegacyBlock(), 3);
					level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, 0).setValue(RollerDoorBlock.BOTTOM, columnlength == 2), 3);
				}

				++blockEntity.blocks;
			}
		} else {
			if (openness > 0) {
				moveCollidedEntities(level, pos, openness - 1, columnlength, columnlength, facing, face, false);

				if (!level.isClientSide)
					level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, openness - 1), 3);
			} else if (blockEntity.blocks > 0) {
				Direction belowdirection = RollerDoor.getBelowDirection(facing, face);
				BlockPos offsetpos = pos.relative(belowdirection, columnlength);

				if (offsetpos.getY() >= level.getMinBuildHeight()) {
					BlockState offsetstate = level.getBlockState(offsetpos);

					if (offsetstate.isAir() || offsetstate.getPistonPushReaction() == PushReaction.DESTROY) {
						moveCollidedEntities(level, pos, 15, columnlength, columnlength + 1, facing, face, false);

						if (!level.isClientSide) {
							FluidState fluidstate = level.getFluidState(offsetpos);
							level.destroyBlock(offsetpos, false);
							level.setBlock(offsetpos, CCBlocks.ROLLER_DOOR.get().defaultBlockState().setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.OPENNESS, 15).setValue(RollerDoorBlock.BOTTOM, true).setValue(RollerDoorBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER), 3);
							level.setBlock(pos, state.setValue(RollerDoorBlock.OPENNESS, 15).setValue(RollerDoorBlock.BOTTOM, false), 3);
						}
						--blockEntity.blocks;
					}
				}
			}
		}
	}

	// TODO: Fix pushing jank
	private static void moveCollidedEntities(Level level, BlockPos pos, int openness, int oldColumnLength, int columnLength, Direction facing, AttachFace face, boolean opening) {
		Direction belowdirection = RollerDoor.getBelowDirection(facing, face);
		Vec3i pushvector = belowdirection.getNormal();
		AABB aabb = calculatePushAABB(openness, columnLength, facing, face, pushvector).move(pos);

		List<Entity> standingentities = new ArrayList<>();
		if (face != AttachFace.WALL) {
			Vec3i carryvector = opening ? RollerDoor.getAboveDirection(facing, face).getNormal() : pushvector;
			MutableBlockPos mutable = pos.mutable();
			AABB standaabb = aabb.expandTowards(0.0D, 0.05D, 0.0D);
			for (int i = 0; i < oldColumnLength; i++) {
				standingentities.addAll(level.getEntities((Entity) null, standaabb, entity -> entity.onGround() && entity.getOnPos().equals(mutable)));
				mutable.move(belowdirection);
			}

			if (!standingentities.isEmpty()) {
				for (Entity entity : standingentities) {
					if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
						entity.move(MoverType.PISTON, new Vec3(0.0625D * carryvector.getX(), 0.0625D * carryvector.getY(), 0.0625D * carryvector.getZ()));
						entity.setOnGround(true);
					}
				}
			}
		}

		if (!opening) {
			List<Entity> insideentities = level.getEntities(null, aabb);
			insideentities.removeAll(standingentities);

			if (!insideentities.isEmpty()) {
				for (Entity entity : insideentities) {
					if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
						entity.move(MoverType.SELF, new Vec3(0.0725D * pushvector.getX(), 0.0725D * pushvector.getY(), 0.0725D * pushvector.getZ()));
					}
				}
			}
		}
	}

	private static AABB calculatePushAABB(int openness, int columnLength, Direction facing, AttachFace face, Vec3i pushNormal) {
		AABB aabb;
		if (face == AttachFace.WALL) {
			if (facing == Direction.EAST)
				aabb = new AABB(0.8125D, 0.0D, 0.0D, 0.9375D, 1.0D, 1.0D);
			else if (facing == Direction.WEST)
				aabb = new AABB(0.0625D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
			else if (facing == Direction.SOUTH)
				aabb = new AABB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 0.9375D);
			else
				aabb = new AABB(0.0D, 0.0D, 0.0625D, 1.0D, 1.0D, 0.1875D);
		} else if (face == AttachFace.FLOOR) {
			aabb = new AABB(0.0D, 0.8125D, 0.0D, 1.0D, 0.9375D, 1.0D);
		} else {
			aabb = new AABB(0.0D, 0.0625D, 0.0D, 1.0D, 0.1875D, 1.0D);
		}

		Vec3 vec3 = Vec3.atLowerCornerOf(pushNormal);

		if (columnLength == 1) {
			Vec3 vec31 = vec3.scale(openness * 0.0625D);
			return aabb.contract(vec31.x, vec31.y, vec31.z);
		} else {
			return aabb.expandTowards(vec3.scale(columnLength - 1.0D - openness * 0.0625D));
		}
	}

	private boolean shouldOpen() {
		return this.isBeingLifted() || this.level.hasNeighborSignal(this.getBlockPos());
	}

	private int getColumnLength() {
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