package com.teamabnormals.caverns_and_chasms.common.block.holdable;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.client.resources.sounds.MovingDoorMoveSoundInstance;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public abstract class AbstractMovingDoorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, HoldableBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private final boolean isHeader;
	private final MovingDoorType defaultDoorType;

	public AbstractMovingDoorBlock(boolean isHeader, MovingDoorType defaultDoorType, Properties properties) {
		super(properties);
		this.isHeader = isHeader;
		this.defaultDoorType = defaultDoorType;
	}

	public boolean isHeader() {
		return this.isHeader;
	}

	public MovingDoorType getDefaultDoorType() {
		return this.defaultDoorType;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof MovingDoorBlockEntity doorEntity) {
			VoxelShape doorShape = this.getDoorShape(doorEntity, state);
			return this.isHeader() ? Shapes.or(doorShape, this.getHeaderShape(state)) : doorShape;
		} else {
			return Shapes.empty();
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof MovingDoorBlockEntity blockEntity) {
			return new ItemStack(blockEntity.getDoorType().getItem());
		}
		return new ItemStack(this.defaultDoorType.getItem());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof MovingDoorBlockEntity blockEntity) {
			List<ItemStack> list = Lists.newArrayList();
			list.add(new ItemStack(blockEntity.getDoorType().getItem(), 1));
			if (blockEntity instanceof MovingDoorHeaderBlockEntity headerEntity) {
				list.addAll(headerEntity.getStoredBlocksAsStacks());
			}
			return list;
		}
		return super.getDrops(state, builder);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		MovingDoorHeaderBlockEntity headerEntity = this.findHeaderBlockEntity(level, state, pos);
		// boolean isItemCompatibleDoor = player.getItemInHand(hand).getItem() instanceof MovingDoorBlockItem doorItem && this.canBePartOfSameDoor(doorItem.getBlock());
		if (headerEntity != null && player.getItemInHand(hand).is(CCItemTags.ROLLER_DOOR_LIFT_ITEMS) && level.getBlockEntity(pos) instanceof MovingDoorBlockEntity doorEntity && (doorEntity.isBottom() || (doorEntity.isBelowBottom() && this.isHitResultInLiftArea(state, doorEntity, pos, hitResult)))) {
			if (!level.isClientSide)
				headerEntity.setHeld();

			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		if (state.getValue(WATERLOGGED))
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return state;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof MovingDoorBlockEntity blockEntity)
			blockEntity.onRemove(newState);

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

	public abstract AbstractMovingDoorBlock getNormalBlock();

	public abstract AbstractMovingDoorBlock getHeaderBlock();

	public abstract Direction getAboveDirection(BlockState state);

	public Direction getBelowDirection(BlockState state) {
		return this.getAboveDirection(state).getOpposite();
	}

	public abstract Direction getRightDirection(BlockState state);

	public Direction getLeftDirection(BlockState state) {
		return this.getRightDirection(state).getOpposite();
	}

	public abstract boolean doDoorsAlign(BlockState state, BlockState otherState);

	public abstract boolean canBePartOfSameDoor(Block block);

	public boolean partOfSameDoor(BlockState state, BlockState otherState) {
		if (state.getBlock() instanceof AbstractMovingDoorBlock block) {
			return block.canBePartOfSameDoor(otherState.getBlock()) && this.doDoorsAlign(state, otherState);
		} else {
			return false;
		}
	}

	public abstract BlockState copyDirectionPropertiesTo(BlockState state, BlockState copyFromState);

	public abstract VoxelShape getHeaderShape(BlockState state);

	public abstract VoxelShape getDoorShape(MovingDoorBlockEntity blockEntity, BlockState state);

	public abstract AABB calculatePushAABB(double openness, int doorsBelowCount, BlockState state, Vec3i moveVector);

	public abstract MovingDoorMoveSoundInstance createSoundInstance(MovingDoorHeaderBlockEntity headerEntity);

	public boolean isHitResultInLiftArea(BlockState state, MovingDoorBlockEntity doorEntity, BlockPos pos, BlockHitResult hitResult) {
		Direction direction = this.getBelowDirection(state);
		Axis axis = direction.getAxis();
		double d0 = hitResult.getLocation().get(axis) - pos.get(axis);
		if (direction.getAxisDirection() == AxisDirection.POSITIVE)
			d0 = 1.0D - d0;
		return d0 < doorEntity.getOpenness();
	}

	public MovingDoorHeaderBlockEntity findHeaderBlockEntity(LevelAccessor level, BlockState state, BlockPos pos) {
		Direction aboveDir = this.getAboveDirection(state);
		MutableBlockPos mutable = pos.mutable();
		while (true) {
			if (!this.partOfSameDoor(state, level.getBlockState(mutable)))
				return null;
			else if (level.getBlockEntity(mutable) instanceof MovingDoorHeaderBlockEntity headerEntity)
				return headerEntity;
			mutable.move(aboveDir);
		}
	}

	public int countDoorsBelow(Level level, BlockPos pos, BlockState state) {
		int doors = 0;

		MutableBlockPos mutable = pos.mutable();
		Direction belowDir = this.getBelowDirection(state);

		while (true) {
			mutable.move(belowDir);
			BlockState offsetState = level.getBlockState(mutable);

			if (offsetState.getBlock() instanceof AbstractMovingDoorBlock offsetDoorBlock && !offsetDoorBlock.isHeader() && this.partOfSameDoor(state, level.getBlockState(mutable)))
				++doors;
			else
				break;
		}

		return doors;
	}
}