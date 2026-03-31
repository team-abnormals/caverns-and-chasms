package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.client.resources.sounds.MovingDoorMoveSoundInstance;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractMovingDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.common.network.S2CMovingDoorSoundMessage;
import com.teamabnormals.caverns_and_chasms.common.network.S2CPushPlayerMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovingDoorHeaderBlockEntity extends MovingDoorBlockEntity {
	private static final double MOVE_SPEED = 0.0625D;

	private List<MovingDoorType> storedBlocks = Lists.newArrayList();
	private int holdTime;
	private long lastUpdateTick;
	private MovingDoorMoveSoundInstance soundInstance;

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
		this.holdTime = compound.getShort("HoldTime");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		ListTag listTag = new ListTag();
		for (MovingDoorType storedBlock : this.storedBlocks) {
			listTag.add(StringTag.valueOf(storedBlock.getRegistryName()));
		}
		compound.put("StoredDoors", listTag);
		compound.putShort("HoldTime", (short) this.holdTime);
	}

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		this.initSoundInstance();
	}

	@OnlyIn(Dist.CLIENT)
	protected void initSoundInstance() {
		if (this.level.isClientSide && this.soundInstance == null) {
			AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) this.getBlockState().getBlock();
			this.soundInstance = thisBlock.createSoundInstance(this);
			Minecraft.getInstance().getSoundManager().play(this.soundInstance);
		}
	}

	public void setHeld() {
		this.holdTime = 2;
	}

	public boolean isBeingLifted() {
		return this.holdTime > 0;
	}

	@OnlyIn(Dist.CLIENT)
	public MovingDoorMoveSoundInstance getSoundInstance() {
		return this.soundInstance;
	}

	public static void tick(Level level, BlockPos thisPos, BlockState thisState, MovingDoorHeaderBlockEntity thisHeader) {
		thisHeader.forceSyncVisuals = false;

		if (level.isClientSide) {
			thisHeader.setOldVisuals();
			thisHeader.syncVisuals();
		} else {
			if (thisHeader.lastUpdateTick < level.getGameTime()) {

				List<MovingDoorHeaderBlockEntity> headers = thisHeader.getConnectedHeaders();
				List<MovingDoorHeaderBlockEntity> movedHeaders = Lists.newArrayList();

				boolean shouldOpen = headers.stream().anyMatch(MovingDoorHeaderBlockEntity::shouldOpen);

				for (MovingDoorHeaderBlockEntity header : headers) {
					BlockPos headerPos = header.getBlockPos();
					BlockState headerState = header.getBlockState();
					AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) headerState.getBlock();
					int doorsBelowCount = thisBlock.countDoorsBelow(level, headerPos, headerState);

					header.lastUpdateTick = level.getGameTime();

					if (header.holdTime > 0) {
						--header.holdTime;
					}

					if (header.move(level, headerPos, headerState, doorsBelowCount, shouldOpen)) {
						movedHeaders.add(header);
					}
				}

				float volume = 1.0F / Math.min(movedHeaders.size(), 14);
				for (MovingDoorHeaderBlockEntity header : headers) {
					if (movedHeaders.contains(header)) {
						CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new S2CMovingDoorSoundMessage(header.getBlockPos(), volume));
					} else {
						CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new S2CMovingDoorSoundMessage(header.getBlockPos(), 0.0F));
					}
				}
			}
		}
	}

	protected boolean move(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount, boolean shouldOpen) {
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		if (shouldOpen) {
			this.openness += MOVE_SPEED;

			if (this.openness >= 1.0D) {
				if (doorsBelowCount > 0) {
					this.openness -= 1.0D;
					doorsBelowCount--;
					this.retract(level, thisPos, thisState, doorsBelowCount);
					this.moveCollidedEntities(level, thisPos, thisState, this.openness, -MOVE_SPEED, doorsBelowCount);
					return true;
				}

				this.openness = 1.0F;
				return false;
			}

			this.updateOpennessInRow(level, thisPos, thisState, doorsBelowCount);
			this.moveCollidedEntities(level, thisPos, thisState, this.openness, -MOVE_SPEED, doorsBelowCount);
			return true;
		} else {
			this.openness -= MOVE_SPEED;

			if (this.openness < 0.0D) {
				if (!this.storedBlocks.isEmpty()) {
					BlockPos offsetpos = thisPos.relative(thisBlock.getBelowDirection(thisState), doorsBelowCount + 1);
					BlockState offsetstate = level.getBlockState(offsetpos);

					if (offsetpos.getY() >= level.getMinBuildHeight() && (offsetstate.isAir() || offsetstate.getPistonPushReaction() == PushReaction.DESTROY)) {
						this.openness += 1.0D;
						doorsBelowCount++;
						this.extend(level, thisPos, thisState, doorsBelowCount);
						this.moveCollidedEntities(level, thisPos, thisState, this.openness, MOVE_SPEED, doorsBelowCount);
						return true;
					}
				}

				this.openness = 0.0D;
				return false;
			}

			this.updateOpennessInRow(level, thisPos, thisState, doorsBelowCount);
			this.moveCollidedEntities(level, thisPos, thisState, this.openness, MOVE_SPEED, doorsBelowCount);
			return true;
		}
	}

	private void updateOpennessInRow(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount) {
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();
		Direction belowDir = thisBlock.getBelowDirection(thisState);

		MutableBlockPos mutable = thisPos.mutable();
		for (int i = 0; i <= doorsBelowCount; i++) {
			if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
				offsetEntity.openness = this.openness;
				offsetEntity.setChanged();
			}
			BlockState offsetState = level.getBlockState(mutable);
			level.sendBlockUpdated(mutable, offsetState, offsetState, 3);

			mutable.move(belowDir);
		}

		level.sendBlockUpdated(thisPos, thisState, thisState, 3);
	}

	private void extend(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount) {
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
				offsetEntity.isBelowBottom = i == 1;
				offsetEntity.belowDoorType = i == 0 ? null : offsetEntity.doorType;
				offsetEntity.doorType = aboveEntity == null ? this.storedBlocks.remove(this.storedBlocks.size() - 1) : aboveEntity.doorType;
				if (i == 0) {
					offsetEntity.forceSyncVisuals = true;
				}
				offsetEntity.setChanged();

				if (offsetEntity instanceof MovingDoorHeaderBlockEntity offsetHeaderEntity) {
					offsetHeaderEntity.storedBlocks = this.storedBlocks;
					offsetHeaderEntity.holdTime = this.holdTime;
				}
			}

			BlockState offsetState = level.getBlockState(mutable);
			level.sendBlockUpdated(mutable, offsetState, offsetState, 3);

			aboveMutable.move(aboveDir);
			mutable.move(aboveDir);
		}
	}

	private void retract(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount) {
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

				if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
					offsetEntity.openness = this.openness;
					offsetEntity.isBelowBottom = i == iterateTo - 2;
					offsetEntity.doorType = belowEntity.doorType;
					offsetEntity.belowDoorType = belowEntity.belowDoorType;
					offsetEntity.setChanged();

					if (offsetEntity instanceof MovingDoorHeaderBlockEntity offsetHeaderEntity) {
						offsetHeaderEntity.storedBlocks = this.storedBlocks;
						offsetHeaderEntity.holdTime = this.holdTime;
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

			List<Entity> entitiesOnDoor = level.getEntities((Entity) null, standAabb, EntitySelector.NO_SPECTATORS.and(entity -> entity.onGround() && entity.getY() >= aabb.maxY));

			if (!entitiesOnDoor.isEmpty()) {
				for (Entity entity : entitiesOnDoor) {
					this.moveEntity(entity, moveSpeed * moveDirVector.getX(), moveSpeed * moveDirVector.getY(), moveSpeed * moveDirVector.getZ(), true);
				}
			}

			if (moveSpeed > 0) {
				List<Entity> entitiesInsideDoor = level.getEntities(null, aabb);
				entitiesInsideDoor.removeAll(entitiesOnDoor);

				if (!entitiesInsideDoor.isEmpty()) {
					double d0 = moveSpeed + 0.05D;
					for (Entity entity : entitiesInsideDoor) {
						this.moveEntity(entity, d0 * moveDirVector.getX(), d0 * moveDirVector.getY(), d0 * moveDirVector.getZ(), false);
					}
				}
			}
		}
	}

	private void moveEntity(Entity entity, double xMove, double yMove, double zMove, boolean onDoor) {
		if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
			if (entity instanceof ServerPlayer player) {
				CavernsAndChasms.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CPushPlayerMessage((float) xMove, (float) yMove, (float) zMove));
			} else {
				entity.move(MoverType.SELF, new Vec3(xMove, yMove, zMove));
				if (onDoor) {
					entity.setOnGround(true);
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
		return this.isBeingLifted() || this.level.hasNeighborSignal(this.getBlockPos());
	}

	private List<MovingDoorHeaderBlockEntity> getConnectedHeaders() {
		List<MovingDoorHeaderBlockEntity> list = Lists.newArrayList(this);

		BlockState thisState = this.getBlockState();
		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		MutableBlockPos mutable = this.getBlockPos().mutable();
		boolean right = false;
		Direction direction = thisBlock.getLeftDirection(thisState);
		while (true) {
			mutable.move(direction);
			BlockState offsetState = this.level.getBlockState(mutable);
			if (this.level.getBlockEntity(mutable) instanceof MovingDoorHeaderBlockEntity offsetEntity && thisBlock.partOfSameDoor(thisState, offsetState)) {
				list.add(offsetEntity);
			} else if (!right) {
				Collections.reverse(list);
				right = true;
				mutable.set(this.getBlockPos());
				direction = direction.getOpposite();
			} else {
				break;
			}
		}

		return list;
	}
}