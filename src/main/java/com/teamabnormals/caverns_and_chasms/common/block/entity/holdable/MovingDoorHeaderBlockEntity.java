package com.teamabnormals.caverns_and_chasms.common.block.entity.holdable;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractMovingDoorBlock;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovingDoorHeaderBlockEntity extends MovingDoorBlockEntity {
	private List<AbstractMovingDoorBlock> storedBlocks = Lists.newArrayList();
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
		ListTag listtag = compound.getList("StoredBlocks", 10);
		for (Tag tag : listtag) {
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getAsString()));
			if (block instanceof AbstractMovingDoorBlock doorBlock) {
				this.storedBlocks.add(doorBlock);
			}
		}
		this.liftTime = compound.getShort("LiftTime");
		this.beingLifted = compound.getBoolean("BeingLifted");
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		ListTag listtag = new ListTag();
		for (Block block : this.storedBlocks) {
			ResourceLocation location = ForgeRegistries.BLOCKS.getKey(block);
			if (location != null) {
				listtag.add(StringTag.valueOf(location.toString()));
			}
		}
		compound.put("StoredBlocks", listtag);
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

		// System.out.println((level.isClientSide ? "Client: " : "Server: ") + headerEntity.openness + ", " + headerEntity.isBeingLifted());

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
						this.retract(level, thisPos, thisState, doorsBelowCount);
						return;
					}

					this.openness = 1.0F;
					return;
				}
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
							this.extend(level, thisPos, thisState, doorsBelowCount);
							return;
						}
					}

					this.openness = 0.0D;
					return;
				}
			}

			this.updateOpennessInRow(level, thisPos, thisState, doorsBelowCount);
		}
	}

	private void updateOpennessInRow(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount) {
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

	private void extend(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount) {
		this.opennessUpdateTime = level.getGameTime();

		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		Direction aboveDir = thisBlock.getAboveDirection(thisState);
		Direction belowDir = aboveDir.getOpposite();

		MutableBlockPos aboveMutable = thisPos.mutable().move(belowDir, doorsBelowCount - 1);
		MutableBlockPos mutable = aboveMutable.mutable().move(belowDir);
		for (int i = 0; i <= doorsBelowCount; i++) {
			// Destroy block we move into (the block is always something also destroyable by pistons)
			if (i == 0) {
				level.destroyBlock(mutable, true);
			}

			BlockState oldOffsetState = level.getBlockState(mutable);
			BlockState newOffsetState;

			if (i == doorsBelowCount) {
				newOffsetState = thisBlock.copyDirectionPropertiesTo(this.storedBlocks.remove(this.storedBlocks.size() - 1).getHeaderBlock().defaultBlockState(), thisState);
			} else {
				BlockState aboveState = level.getBlockState(aboveMutable);
				AbstractMovingDoorBlock aboveBlock = (AbstractMovingDoorBlock) aboveState.getBlock();
				newOffsetState = aboveBlock.copyDirectionPropertiesTo(aboveBlock.getNormalBlock().defaultBlockState(), aboveState);
			}

			newOffsetState = newOffsetState.setValue(AbstractMovingDoorBlock.WATERLOGGED, level.getFluidState(mutable).getType() == Fluids.WATER);

			level.setBlock(mutable, newOffsetState, 2);

			if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
				offsetEntity.openness = this.openness;
				offsetEntity.opennessOld = this.opennessOld;
				offsetEntity.opennessUpdateTime = this.opennessUpdateTime;
				offsetEntity.isBottom = i == 0;
				offsetEntity.isBelowBottom = i == 1;
				offsetEntity.belowBlock = i == 0 ? null : ((AbstractMovingDoorBlock) oldOffsetState.getBlock()).getNormalBlock();

				if (offsetEntity instanceof MovingDoorHeaderBlockEntity offsetHeaderEntity) {
					offsetHeaderEntity.storedBlocks = this.storedBlocks;
					offsetHeaderEntity.liftTime = this.liftTime;
					offsetHeaderEntity.beingLifted = this.beingLifted;
					offsetHeaderEntity.prevBeingLifted = this.prevBeingLifted;
					offsetHeaderEntity.liftUpdateTime = this.liftUpdateTime;
				}
			}

			level.sendBlockUpdated(mutable, newOffsetState, newOffsetState, 3);

			aboveMutable.move(aboveDir);
			mutable.move(aboveDir);
		}
	}

	private void retract(Level level, BlockPos thisPos, BlockState thisState, int doorsBelowCount) {
		this.opennessUpdateTime = level.getGameTime();

		AbstractMovingDoorBlock thisBlock = (AbstractMovingDoorBlock) thisState.getBlock();

		// Store current top block
		this.storedBlocks.add(thisBlock.getNormalBlock());

		Direction aboveDir = thisBlock.getAboveDirection(thisState);
		Direction belowDir = aboveDir.getOpposite();

		MutableBlockPos mutable = thisPos.mutable();
		MutableBlockPos belowMutable = thisPos.mutable().move(belowDir);
		int iterateTo = doorsBelowCount + 1;
		for (int i = 0; i <= iterateTo; i++) {
			BlockState newOffsetState;
			MovingDoorBlockEntity belowEntity;

			if (i == iterateTo) {
				newOffsetState = level.getBlockState(mutable).getFluidState().createLegacyBlock();
				belowEntity = null;
			} else {
				BlockState belowState = level.getBlockState(belowMutable);
				AbstractMovingDoorBlock belowBlock = (AbstractMovingDoorBlock) belowState.getBlock();
				if (i == 0) {
					newOffsetState = belowBlock.copyDirectionPropertiesTo(belowBlock.getHeaderBlock().defaultBlockState(), belowState);
				} else {
					newOffsetState = belowBlock.copyDirectionPropertiesTo(belowBlock.getNormalBlock().defaultBlockState(), belowState);
				}
				newOffsetState = newOffsetState.setValue(AbstractMovingDoorBlock.WATERLOGGED, level.getFluidState(mutable).getType() == Fluids.WATER);
				belowEntity = (MovingDoorBlockEntity) level.getBlockEntity(belowMutable);
			}

			level.setBlock(mutable, newOffsetState, 2);

			// TODO: Remove repetition
			if (level.getBlockEntity(mutable) instanceof MovingDoorBlockEntity offsetEntity) {
				offsetEntity.openness = this.openness;
				offsetEntity.opennessOld = this.opennessOld;
				offsetEntity.opennessUpdateTime = this.opennessUpdateTime;
				offsetEntity.isBottom = i == iterateTo - 1;
				offsetEntity.isBelowBottom = i == iterateTo - 2;
				offsetEntity.belowBlock = belowEntity.belowBlock;

				if (offsetEntity instanceof MovingDoorHeaderBlockEntity offsetHeaderEntity) {
					offsetHeaderEntity.storedBlocks = this.storedBlocks;
					offsetHeaderEntity.liftTime = this.liftTime;
					offsetHeaderEntity.beingLifted = this.beingLifted;
					offsetHeaderEntity.prevBeingLifted = this.prevBeingLifted;
					offsetHeaderEntity.liftUpdateTime = this.liftUpdateTime;
				}
			}

			level.sendBlockUpdated(mutable, newOffsetState, newOffsetState, 3);

			mutable.move(belowDir);
			belowMutable.move(belowDir);
		}
	}

	// TODO: Add this back
	/*
	private void moveCollidedEntities(Level level, BlockPos pos, BlockState state, boolean extending, double openness, double moveSpeed, int doorsBelowCount) {
		Direction belowdir = RollerDoorBlock.getBelowDirection(facing, face);
		Vec3i movevector = belowdir.getNormal();
		AABB aabb = calculatePushAABB(openness, doorsBelowCount, facing, face, movevector).move(pos);
		double pushamount = extending ? -moveSpeed : moveSpeed;

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

			if (!extending) {
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
	*/

	public List<ItemStack> getStoredBlocksAsStacks() {
		List<ItemStack> list = Lists.newArrayList();

		Map<Block, Integer> map = new HashMap<>();
		for (Block block : this.storedBlocks) {
			map.put(block, map.getOrDefault(block, 0) + 1);
		}

		for (Block block : map.keySet()) {
			int itemCount = map.get(block);
			int fullStackCount = itemCount / 64;
			for (int i = 0; i < fullStackCount; i++) {
				list.add(new ItemStack(block, 64));
			}
			int remaining = itemCount % 64;
			if (remaining > 0) {
				list.add(new ItemStack(block, itemCount % 64));
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