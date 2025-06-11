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
import net.minecraft.server.level.ServerPlayer;
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
import java.util.stream.Collectors;

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

	public void setBlockCount(int count) {
		this.blocks = count;
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
		if (level.isClientSide) {
			if (blockEntity.opennessUpdateTime < level.getGameTime())
				blockEntity.opennessOld = blockEntity.openness;
		}

		blockEntity.opennessOld = blockEntity.openness;
		blockEntity.prevBeingLifted = blockEntity.beingLifted;

		double speed = 0.0625D;

		Direction facing = state.getValue(RollerDoorBlock.FACING);
		AttachFace face = state.getValue(RollerDoorBlock.FACE);
		Direction belowdirection = RollerDoorBlock.getBelowDirection(facing, face);

		int columnlength = RollerDoorBlock.calculateColumnLength(level, pos, facing, face);
		double newopenness = blockEntity.openness;
		boolean addblock = false;
		boolean removeblock = false;

		boolean opening = blockEntity.shouldOpen();
		boolean updatedoor = true;

		if (opening) {
			newopenness += speed;

			if (newopenness > 1.0D) {
				if (columnlength > 0) {
					newopenness -= 1.0D;
					columnlength--;
					if (!level.isClientSide) {
						blockEntity.opennessOld = newopenness;
						blockEntity.blocks++;
						removeblock = true;
					}
				} else {
					newopenness = 1.0F;
					updatedoor = false;
				}
			}
		} else {
			newopenness -= speed;

			if (newopenness < 0.0D) {
				if (blockEntity.blocks > 0) {
					BlockPos offsetpos = pos.relative(belowdirection, columnlength + 1);
					BlockState offsetstate = level.getBlockState(offsetpos);

					if (offsetpos.getY() >= level.getMinBuildHeight() && (offsetstate.isAir() || offsetstate.getPistonPushReaction() == PushReaction.DESTROY)) {
						newopenness += 1.0D;
						columnlength++;
						if (!level.isClientSide) {
							blockEntity.opennessOld = newopenness;
							blockEntity.blocks--;
							addblock = true;
						}
					} else {
						newopenness = 0.0D;
						updatedoor = false;
					}
				} else {
					newopenness = 0.0D;
					updatedoor = false;
				}
			}
		}

		if (!level.isClientSide)
			blockEntity.openness = newopenness;

		if (updatedoor) {
			moveCollidedEntities(level, pos, opening, blockEntity.openness, speed, columnlength, facing, face);

			if (!level.isClientSide) {
				blockEntity.bottom = columnlength == 0;
				blockEntity.bottomBelow = columnlength == 1;
				blockEntity.opennessUpdateTime = level.getGameTime();

				MutableBlockPos mutable = pos.mutable();
				for (int i = 0; i < columnlength; i++) {
					mutable.move(belowdirection);

					boolean bottom = i == columnlength - 1;
					if (bottom && addblock) {
						FluidState fluidstate = level.getFluidState(mutable);
						level.destroyBlock(mutable, true);
						level.setBlock(mutable, CCBlocks.ROLLER_DOOR.get().defaultBlockState().setValue(RollerDoorBlock.FACING, facing).setValue(RollerDoorBlock.FACE, face).setValue(RollerDoorBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER), 2);
						BlockState offsetstate = level.getBlockState(mutable);
						level.sendBlockUpdated(mutable, offsetstate, offsetstate, 3);
					}

					if (level.getBlockEntity(mutable) instanceof RollerDoorBlockEntity offsetentity) {
						offsetentity.openness = blockEntity.openness;
						offsetentity.opennessOld = blockEntity.opennessOld;
						offsetentity.opennessUpdateTime = blockEntity.opennessUpdateTime;
						offsetentity.bottom = bottom;
						offsetentity.bottomBelow = i == columnlength - 2;
						BlockState offsetstate = level.getBlockState(mutable);
						level.sendBlockUpdated(mutable, offsetstate, offsetstate, 3);
					}
				}

				if (removeblock) {
					mutable.move(belowdirection);
					level.setBlock(mutable, level.getBlockState(mutable).getFluidState().createLegacyBlock(), 2);
				}

				level.sendBlockUpdated(pos, state, state, 3);
			}
		}

		blockEntity.liftUpdateTime = level.getGameTime();
		if (blockEntity.liftTime > 0) {
			--blockEntity.liftTime;
			if (blockEntity.liftTime == 0)
				blockEntity.beingLifted = false;
		}
	}

	// TODO: Fix pushing jank
	private static void moveCollidedEntities(Level level, BlockPos pos, boolean opening, double openness, double moveSpeed, int columnLength, Direction facing, AttachFace face) {
		Direction belowdir = RollerDoorBlock.getBelowDirection(facing, face);
		Vec3i movevector = belowdir.getNormal();
		AABB aabb = calculatePushAABB(openness, columnLength, facing, face, movevector).move(pos);
		double pushamount = opening ? -moveSpeed : moveSpeed;

		if (face != AttachFace.WALL) {
			AABB standaabb = aabb.expandTowards(0.0D, 0.05D, 0.0D);

			List<Entity> standingentities;
			if (!level.isClientSide)
				standingentities = level.getEntities((Entity) null, standaabb, entity -> entity.onGround() && entity.getY() >= aabb.maxY && !(entity instanceof ServerPlayer));
			else
				standingentities = level.players().stream().filter(player -> player.onGround() && player.getY() >= aabb.maxY && player.getBoundingBox().intersects(standaabb)).collect(Collectors.toList());

			if (!standingentities.isEmpty()) {
				for (Entity entity : standingentities) {
					if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
						entity.move(MoverType.PISTON, new Vec3(pushamount * movevector.getX(), pushamount * movevector.getY(), pushamount * movevector.getZ()));
						entity.setOnGround(true);
					}
				}
			}

			if (!opening) {
				List<Entity> insideentities;
				if (!level.isClientSide)
					insideentities = level.getEntities((Entity) null, aabb, entity -> !(entity instanceof ServerPlayer));
				else
					insideentities = level.players().stream().filter(player -> player.onGround() && player.getY() >= aabb.maxY && player.getBoundingBox().intersects(aabb)).collect(Collectors.toList());
				insideentities.removeAll(standingentities);

				if (!insideentities.isEmpty()) {
					double d0 = pushamount + 0.01D;
					for (Entity entity : insideentities) {
						if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
							entity.move(MoverType.SELF, new Vec3(d0 * movevector.getX(), d0 * movevector.getY(), d0 * movevector.getZ()));
						}
					}
				}
			}
		}
	}

	private static AABB calculatePushAABB(double openness, int columnLength, Direction facing, AttachFace face, Vec3i moveVector) {
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

		Vec3 vec3 = Vec3.atLowerCornerOf(moveVector);

		if (columnLength == 0) {
			Vec3 vec31 = vec3.scale(openness);
			return aabb.contract(vec31.x, vec31.y, vec31.z);
		} else {
			return aabb.expandTowards(vec3.scale(columnLength - openness));
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
			if (blockstate.getBlock() instanceof RollerDoorHeaderBlock && RollerDoorBlock.isDoorParallel(blockstate, facing, face)) {
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
}