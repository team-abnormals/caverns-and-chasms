package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractMovingDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovingDoorHeaderBlockEntity extends MovingDoorBlockEntity {
	private List<MovingDoorType> storedBlocks = Lists.newArrayList();
	private int liftTime;
	private boolean beingLifted;
	private boolean prevBeingLifted;
	private long liftUpdateTime;

	public MovingDoorHeaderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public MovingDoorHeaderBlockEntity(BlockPos pos, BlockState state) {
		this(CCBlockEntityTypes.MOVING_DOOR_HEADER.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		ListTag listTag = compound.getList("StoredDoors", 8);
		for (Tag tag : listTag) {
			MovingDoorType storedBlock = MovingDoorType.byName(tag.getAsString());
			if (storedBlock != null) {
				this.storedBlocks.add(storedBlock);
			}
		}
		this.liftTime = compound.getShort("LiftTime");
		this.beingLifted = compound.getBoolean("BeingLifted");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		ListTag listTag = new ListTag();
		for (MovingDoorType storedBlock : this.storedBlocks) {
			listTag.add(StringTag.valueOf(storedBlock.getRegistryName()));
		}
		compound.put("StoredDoors", listTag);
		compound.putShort("LiftTime", (short) this.liftTime);
		compound.putBoolean("BeingLifted", this.beingLifted);
	}

	public void setBeingLifted() {
		this.liftTime = 2;
		this.beingLifted = true;
	}

	public boolean isBeingLifted() {
		return this.beingLifted;
	}

	public static void tick(Level level, BlockPos thisPos, BlockState thisState, MovingDoorHeaderBlockEntity headerEntity) {
		if (level.isClientSide) {
			if (headerEntity.opennessUpdateTime < level.getGameTime())
				headerEntity.opennessOld = headerEntity.openness;
		} else {
			headerEntity.opennessOld = headerEntity.openness;
			headerEntity.prevBeingLifted = headerEntity.beingLifted;
		}

		headerEntity.move(level, thisPos, thisState);

		headerEntity.liftUpdateTime = level.getGameTime();
		if (headerEntity.liftTime > 0) {
			--headerEntity.liftTime;
			if (headerEntity.liftTime == 0)
				headerEntity.beingLifted = false;
		}
	}

	private void move(Level level, BlockPos thisPos, BlockState thisState) {
		if (!level.isClientSide) {
			double moveSpeed = 0.0625D;
			AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();
			int doorsBelowCount = thisBlock.countDoorsBelow(level, thisPos, thisState);

			if (this.shouldOpen()) {
				this.openness += moveSpeed;

				if (this.openness > 1.0D) {
					if (doorsBelowCount > 0) {
						this.openness -= 1.0D;
						doorsBelowCount--;
						// TODO: Set old openness in a proper way
						this.opennessOld = this.openness;
						this.retract(level, thisPos, thisState, -moveSpeed, doorsBelowCount);
						return;
					}

					this.openness = 1.0F;
					return;
				}

				this.updateOpennessInRow(level, thisPos, thisState, -moveSpeed, doorsBelowCount);
			} else {
				this.openness -= moveSpeed;

				if (this.openness < 0.0D) {
					if (!this.storedBlocks.isEmpty()) {
						BlockPos offsetpos = thisPos.relative(thisBlock.getBelowDirection(thisState), doorsBelowCount + 1);
						BlockState offsetstate = level.getBlockState(offsetpos);

						if (offsetpos.getY() >= level.getMinBuildHeight() && (offsetstate.isAir() || offsetstate.getPistonPushReaction() == PushReaction.DESTROY)) {
							this.openness += 1.0D;
							doorsBelowCount++;
							this.opennessOld = this.openness;
							this.extend(level, thisPos, thisState, moveSpeed, doorsBelowCount);
							return;
						}
					}

					this.openness = 0.0D;
					return;
				}

				this.updateOpennessInRow(level, thisPos, thisState, moveSpeed, doorsBelowCount);
			}
		}
	}

	private void updateOpennessInRow(Level level, BlockPos thisPos, BlockState thisState, double moveSpeed, int doorsBelowCount) {
		this.opennessUpdateTime = level.getGameTime();

		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();
		Direction belowDir = thisBlock.getBelowDirection(thisState);

		MutableBlockPos mutable = thisPos.mutable().move(belowDir);
		for (int i = 1; i <= doorsBelowCount; i++) {
			if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
				offsetEntity.openness = this.openness;
				offsetEntity.opennessOld = this.opennessOld;
				offsetEntity.opennessUpdateTime = this.opennessUpdateTime;
			}
			BlockState offsetState = level.getBlockState(mutable);
			level.sendBlockUpdated(mutable, offsetState, offsetState, 3);

			mutable.move(belowDir);
		}

		level.sendBlockUpdated(thisPos, thisState, thisState, 3);
	}

	private void extend(Level level, BlockPos thisPos, BlockState thisState, double moveSpeed, int doorsBelowCount) {
		this.opennessUpdateTime = level.getGameTime();

		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		Direction aboveDir = thisBlock.getAboveDirection(thisState);
		Direction belowDir = aboveDir.getOpposite();

		MutableBlockPos mutable = thisPos.mutable().move(belowDir, doorsBelowCount);
		MutableBlockPos aboveMutable = mutable.mutable().move(aboveDir);

		for (int i = 0; i <= doorsBelowCount; i++) {
			MovingDoorBlockEntity aboveEntity = i == doorsBelowCount ? null : (MovingDoorBlockEntity) level.getBlockEntity(aboveMutable);

			if (i == 0) {
				// Destroy block we move into (the block is always something also destroyable by pistons)
				level.destroyBlock(mutable, true);

				BlockState aboveState = level.getBlockState(aboveMutable);
				AbstractMovingDoorBlock aboveBlock = (AbstractMovingDoorBlock) aboveState.getBlock();
				BlockState newOffsetState = aboveBlock.copyDirectionPropertiesTo(aboveBlock.getNormalBlock().defaultBlockState(), aboveState).setValue(AbstractMovingDoorBlock.WATERLOGGED, level.getFluidState(mutable).getType() == Fluids.WATER);
				level.setBlock(mutable, newOffsetState, 2);
			}

			if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
				offsetEntity.openness = this.openness;
				offsetEntity.opennessOld = this.opennessOld;
				offsetEntity.opennessUpdateTime = this.opennessUpdateTime;
				offsetEntity.isBelowBottom = i == 1;
				offsetEntity.belowDoorType = i == 0 ? null : offsetEntity.doorType;
				offsetEntity.doorType = aboveEntity == null ? this.storedBlocks.remove(this.storedBlocks.size() - 1) : aboveEntity.doorType;

				if (offsetEntity instanceof MovingDoorHeaderBlockEntity offsetHeaderEntity) {
					offsetHeaderEntity.storedBlocks = this.storedBlocks;
					offsetHeaderEntity.liftTime = this.liftTime;
					offsetHeaderEntity.beingLifted = this.beingLifted;
					offsetHeaderEntity.prevBeingLifted = this.prevBeingLifted;
					offsetHeaderEntity.liftUpdateTime = this.liftUpdateTime;
				}
			}

			BlockState offsetState = level.getBlockState(mutable);
			level.sendBlockUpdated(mutable, offsetState, offsetState, 3);

			aboveMutable.move(aboveDir);
			mutable.move(aboveDir);
		}
	}

	private void retract(Level level, BlockPos thisPos, BlockState thisState, double moveSpeed, int doorsBelowCount) {
		this.opennessUpdateTime = level.getGameTime();

		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		// Store current top block
		this.storedBlocks.add(this.doorType);

		Direction aboveDir = thisBlock.getAboveDirection(thisState);
		Direction belowDir = aboveDir.getOpposite();

		MutableBlockPos mutable = thisPos.mutable();
		MutableBlockPos belowMutable = thisPos.mutable().move(belowDir);

		int iterateTo = doorsBelowCount + 1;
		for (int i = 0; i <= iterateTo; i++) {
			if (i == iterateTo) {
				level.setBlock(mutable, level.getBlockState(mutable).getFluidState().createLegacyBlock(), 2);
			} else {
				MovingDoorBlockEntity belowEntity = (MovingDoorBlockEntity) level.getBlockEntity(belowMutable);

				// TODO: Remove repetition
				if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
					offsetEntity.openness = this.openness;
					offsetEntity.opennessOld = this.opennessOld;
					offsetEntity.opennessUpdateTime = this.opennessUpdateTime;
					offsetEntity.isBelowBottom = i == iterateTo - 2;
					offsetEntity.doorType = belowEntity.doorType;
					offsetEntity.belowDoorType = belowEntity.belowDoorType;

					if (offsetEntity instanceof MovingDoorHeaderBlockEntity offsetHeaderEntity) {
						offsetHeaderEntity.storedBlocks = this.storedBlocks;
						offsetHeaderEntity.liftTime = this.liftTime;
						offsetHeaderEntity.beingLifted = this.beingLifted;
						offsetHeaderEntity.prevBeingLifted = this.prevBeingLifted;
						offsetHeaderEntity.liftUpdateTime = this.liftUpdateTime;
					}
				}
			}

			BlockState offsetState = level.getBlockState(mutable);
			level.sendBlockUpdated(mutable, offsetState, offsetState, 3);

			mutable.move(belowDir);
			belowMutable.move(belowDir);
		}
	}

	private void moveCollidedEntities(Level level, BlockPos thisPos, BlockState thisState, double openness, double moveSpeed, int doorsBelowCount) {
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		Direction belowDir = thisBlock.getBelowDirection(thisState);

		if (belowDir.getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
			Vec3i moveDirVector = belowDir.getNormal();
			AABB aabb = thisBlock.calculatePushAABB(openness, doorsBelowCount, thisState, moveDirVector).move(thisPos);
			AABB standAabb = aabb.expandTowards(0.0D, 0.05D, 0.0D);

			List<Entity> standingEntities;
			if (!level.isClientSide)
				standingEntities = level.getEntities((Entity) null, standAabb, entity -> entity.onGround() && entity.getY() >= aabb.maxY && !(entity instanceof ServerPlayer));
			else
				standingEntities = level.players().stream().filter(player -> player.onGround() && player.getY() >= aabb.maxY && player.getBoundingBox().intersects(standAabb)).collect(Collectors.toList());

			if (!standingEntities.isEmpty()) {
				for (Entity entity : standingEntities) {
					if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
						entity.move(MoverType.PISTON, new Vec3(moveSpeed * moveDirVector.getX(), moveSpeed * moveDirVector.getY(), moveSpeed * moveDirVector.getZ()));
						entity.setOnGround(true);
					}
				}
			}

			if (moveSpeed > 0) {
				List<Entity> insideEntities;
				if (!level.isClientSide)
					insideEntities = level.getEntities((Entity) null, aabb, entity -> !(entity instanceof ServerPlayer));
				else
					insideEntities = level.players().stream().filter(player -> player.onGround() && player.getY() >= aabb.maxY && player.getBoundingBox().intersects(aabb)).collect(Collectors.toList());
				insideEntities.removeAll(standingEntities);

				if (!insideEntities.isEmpty()) {
					double d0 = moveSpeed + 0.01D;
					for (Entity entity : insideEntities) {
						if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
							entity.move(MoverType.SELF, new Vec3(d0 * moveDirVector.getX(), d0 * moveDirVector.getY(), d0 * moveDirVector.getZ()));
						}
					}
				}
			}
		}
	}

	public List<ItemStack> getStoredBlocksAsStacks() {
		List<ItemStack> list = Lists.newArrayList();

		Map<MovingDoorType, Integer> map = new HashMap<>();
		for (MovingDoorType storedDoor : this.storedBlocks) {
			map.put(storedDoor, map.getOrDefault(storedDoor, 0) + 1);
		}

		for (MovingDoorType doorType : map.keySet()) {
			int itemCount = map.get(doorType);
			int fullStackCount = itemCount / 64;
			for (int i = 0; i < fullStackCount; i++) {
				list.add(new ItemStack(doorType.getItem(), 64));
			}
			int remaining = itemCount % 64;
			if (remaining > 0) {
				list.add(new ItemStack(doorType.getItem(), itemCount % 64));
			}
		}

		return list;
	}

	private boolean shouldOpen() {
		return this.isBeingLifted() || this.level.hasNeighborSignal(this.getBlockPos()) || this.isConnectedHeaderBeingOpened();
	}

	private boolean isConnectedHeaderBeingOpened() {
		BlockState thisState = this.getBlockState();
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		MutableBlockPos mutable = this.getBlockPos().mutable();
		boolean right = false;
		Direction direction = thisBlock.getLeftDirection(thisState);
		while (true) {
			mutable.move(direction);
			BlockState offsetState = this.level.getBlockState(mutable);
			if (this.level.getBlockEntity(mutable) instanceof MovingDoorHeaderBlockEntity offsetEntity && thisBlock.partOfSameDoor(thisState, offsetState)) {
				if (this.level.hasNeighborSignal(mutable))
					return true;
				else if (offsetEntity.liftUpdateTime == level.getGameTime() && offsetEntity.prevBeingLifted || offsetEntity.liftUpdateTime < level.getGameTime() && offsetEntity.beingLifted)
					return true;
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