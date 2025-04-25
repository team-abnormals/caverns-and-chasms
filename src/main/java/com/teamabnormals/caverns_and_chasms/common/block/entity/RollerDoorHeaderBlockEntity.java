package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorHeaderBlock;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class RollerDoorHeaderBlockEntity extends RollerDoorBlockEntity {
	private int blocks;
	private int liftTime;
	private boolean beingLifted;
	private boolean prevBeingLifted;
	private long liftUpdateTime;

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

	public void setBeingLifted() {
		this.liftTime = 5;
		this.beingLifted = true;
	}

	public boolean isBeingLifted() {
		return this.beingLifted;
	}

	// TODO: Check the setblock flags
	public static void tick(Level level, BlockPos pos, BlockState state, RollerDoorHeaderBlockEntity blockEntity) {
		if (!level.isClientSide) {
		blockEntity.prevBeingLifted = blockEntity.beingLifted;

		double speed = 0.0625D;

		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);

		Direction belowdirection = RollerDoorBlock.getBelowDirection(facing, face);
		int columnlength = blockEntity.getColumnLength();
		boolean addblock = false;
		boolean removeblock = false;

		if (blockEntity.shouldOpen()) {
			blockEntity.openness += speed;
			if (blockEntity.openness > 1.0D) {
				if (columnlength > 0) {
					blockEntity.openness -= 1.0D;
					blockEntity.blocks++;
					columnlength--;
					removeblock = true;
				} else {
					blockEntity.openness = 1.0F;
				}
			}
		} else {
			blockEntity.openness -= speed;
			if (blockEntity.openness < 0.0D) {
				if (blockEntity.blocks > 0) {
					BlockPos offsetpos = pos.relative(belowdirection, columnlength + 1);
					BlockState offsetstate = level.getBlockState(offsetpos);

					if (offsetpos.getY() >= level.getMinBuildHeight() && (offsetstate.isAir() || offsetstate.getPistonPushReaction() == PushReaction.DESTROY)) {
						blockEntity.openness += 1.0D;
						blockEntity.blocks--;
						columnlength++;
						addblock = true;
					} else {
						blockEntity.openness = 0.0D;
					}
				} else {
					blockEntity.openness = 0.0D;
				}
			}
		}

		blockEntity.bottom = columnlength == 0;

		MutableBlockPos mutable = pos.mutable();
		int j = addblock ? columnlength - 1 : columnlength;
		for (int i = 0; i < j; i++) {
			mutable.move(belowdirection);
			if (level.getBlockEntity(mutable) instanceof RollerDoorBlockEntity offsetentity) {
				offsetentity.openness = blockEntity.openness;
				offsetentity.bottom = i == j - 1 && !addblock;
				BlockState offsetstate = level.getBlockState(mutable);
				level.sendBlockUpdated(mutable, offsetstate, offsetstate, 3);
			}
		}

		if (addblock) {
			mutable.move(belowdirection);
			FluidState fluidstate = level.getFluidState(mutable);
			level.destroyBlock(mutable, false);
			level.setBlock(mutable, CCBlocks.ROLLER_DOOR.get().defaultBlockState().setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER), 2);
			if (level.getBlockEntity(mutable) instanceof RollerDoorBlockEntity offsetentity) {
				offsetentity.openness = blockEntity.openness;
				offsetentity.bottom = true;
				BlockState offsetstate = level.getBlockState(mutable);
				level.sendBlockUpdated(mutable, offsetstate, offsetstate, 3);
			}
		} else if (removeblock) {
			mutable.move(belowdirection);
			level.setBlock(mutable, level.getBlockState(mutable).getFluidState().createLegacyBlock(), 2);
		}

		blockEntity.liftUpdateTime = level.getGameTime();
		if (blockEntity.liftTime > 0) {
			--blockEntity.liftTime;
			if (blockEntity.liftTime == 0) {
				blockEntity.beingLifted = false;
				if (!level.isClientSide)
					level.blockEvent(pos, state.getBlock(), 1, 0);
			}
		}

		level.sendBlockUpdated(pos, state, state, 3);
		}
	}

	// TODO: Fix pushing jank
	private static void moveCollidedEntities(Level level, BlockPos pos, float openness, int oldColumnLength, int columnLength, Direction facing, AttachFace face, boolean opening) {
		Direction belowdirection = RollerDoorBlock.getBelowDirection(facing, face);
		Vec3i pushvector = belowdirection.getNormal();
		AABB aabb = calculatePushAABB(openness, columnLength, facing, face, pushvector).move(pos);

		List<Entity> standingentities = new ArrayList<>();
		if (face != AttachFace.WALL) {
			Vec3i carryvector = opening ? RollerDoorBlock.getAboveDirection(facing, face).getNormal() : pushvector;
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

	private static AABB calculatePushAABB(float openness, int columnLength, Direction facing, AttachFace face, Vec3i pushNormal) {
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
		return this.isBeingLifted() || this.level.hasNeighborSignal(this.getBlockPos()) || this.isConnectedHeaderBeingOpened();
	}

	private boolean isConnectedHeaderBeingOpened() {
		MutableBlockPos mutable = this.getBlockPos().mutable();
		boolean right = false;
		Direction facing = this.getBlockState().getValue(RollerDoorBlock.FACING);
		AttachFace face = this.getBlockState().getValue(RollerDoorBlock.FACE);
		Direction direction = RollerDoorBlock.getLeftDirection(facing, face);
		while (true) {
			mutable.move(direction);
			BlockState blockstate = this.level.getBlockState(mutable);
			if (blockstate.getBlock() instanceof RollerDoorHeaderBlock && RollerDoorBlock.isParallelDoor(blockstate, facing, face)) {
				if (this.level.hasNeighborSignal(mutable))
					return true;
				else if (this.level.getBlockEntity(mutable) instanceof RollerDoorHeaderBlockEntity blockentity) {
					if (blockentity.liftUpdateTime == level.getGameTime() && blockentity.prevBeingLifted)
						return true;
					else if (blockentity.liftUpdateTime < level.getGameTime() && blockentity.beingLifted)
						return true;
				}
			} else {
				if (!right) {
					right = true;
					mutable.set(this.getBlockPos());
					direction = direction.getOpposite();
				} else {
					return false;
				}
			}
		}
	}

	private int getColumnLength() {
		int length = 0;

		Direction facing = this.getBlockState().getValue(RollerDoorBlock.FACING);
		AttachFace face = this.getBlockState().getValue(RollerDoorBlock.FACE);
		MutableBlockPos mutable = this.getBlockPos().mutable();

		while (true) {
			mutable.move(RollerDoorBlock.getBelowDirection(facing, face));
			BlockState offsetstate = this.level.getBlockState(mutable);

			if (offsetstate.getBlock() instanceof RollerDoorBlock && offsetstate.getValue(RollerDoorBlock.FACING) == facing && offsetstate.getValue(RollerDoorBlock.FACE) == face)
				++length;
			else
				break;
		}

		return length;
	}

	/*
	private BlockState getUpdatedState(LevelAccessor level, BlockPos pos, Direction facing, AttachFace face, int openness, boolean waterlogged) {
		BlockPos abovepos = pos.relative(RollerDoorBlock.getAboveDirection(facing, face));
		BlockPos belowpos = pos.relative(RollerDoorBlock.getBelowDirection(facing, face));
		BlockState abovestate = level.getBlockState(abovepos);
		BlockState belowstate = level.getBlockState(belowpos);
		boolean connectsabove = RollerDoorBlock.isParallelDoor(abovestate, facing, face);
		boolean connectsbelow = RollerDoorBlock.isParallelDoor(belowstate, facing, face);

		BlockState newstate = connectsabove ? CCBlocks.ROLLER_DOOR.get().defaultBlockState() : CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState();
		int newopenness = connectsabove ? abovestate.getValue(RollerDoorBlock.OPENNESS) : connectsbelow ? belowstate.getValue(RollerDoorBlock.OPENNESS) : openness;

		if (connectsabove && this instanceof RollerDoorHeaderBlock && abovestate.getBlock() instanceof RollerDoorHeaderBlock)
			if (level.getBlockEntity(pos) instanceof RollerDoorBlockEntity door && level.getBlockEntity(abovepos) instanceof RollerDoorBlockEntity aboveDoor)
				aboveDoor.deserializeNBT(door.serializeNBT());

		return newstate.setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.BOTTOM, !connectsbelow).setValue(RollerDoorBlock.WATERLOGGED, waterlogged);
	}

	 */
}