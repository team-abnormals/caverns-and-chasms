package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class RollerDoorHeaderBlock extends BaseEntityBlock implements RollerDoor {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
	public static final IntegerProperty OPENNESS = IntegerProperty.create("openness", 0, 15);
	public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final Map<Direction, VoxelShape[]> WALL_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, RollerDoor.makeShapes(0, 0, 1, 16, 16, 3, box(0, 12, 0, 16, 16, 4), Direction.UP),
			Direction.SOUTH, RollerDoor.makeShapes(0, 0, 13, 16, 16, 15, box(0, 12, 12, 16, 16, 16), Direction.UP),
			Direction.WEST, RollerDoor.makeShapes(1, 0, 0, 3, 16, 16, box(0, 12, 0, 4, 16, 16), Direction.UP),
			Direction.EAST, RollerDoor.makeShapes(13, 0, 0, 15, 16, 16, box(12, 12, 0, 16, 16, 16), Direction.UP)));
	protected static final Map<Direction, VoxelShape[]> CEILING_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, box(0, 0, 0, 16, 4, 4), Direction.NORTH),
			Direction.SOUTH, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, box(0, 0, 12, 16, 4, 16), Direction.SOUTH),
			Direction.WEST, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, box(0, 0, 0, 4, 4, 16), Direction.WEST),
			Direction.EAST, RollerDoor.makeShapes(0, 1, 0, 16, 3, 16, box(12, 0, 0, 16, 4, 16), Direction.EAST)));
	protected static final Map<Direction, VoxelShape[]> FLOOR_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, box(0, 12, 0, 16, 16, 4), Direction.NORTH),
			Direction.SOUTH, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, box(0, 12, 12, 16, 16, 16), Direction.SOUTH),
			Direction.WEST, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, box(0, 12, 0, 4, 16, 16), Direction.WEST),
			Direction.EAST, RollerDoor.makeShapes(0, 13, 0, 16, 15, 16, box(12, 12, 0, 16, 16, 16), Direction.EAST)));

	public RollerDoorHeaderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(OPENNESS, 0).setValue(BOTTOM, false).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RollerDoorHeaderBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, CCBlockEntityTypes.ROLLER_DOOR_HEADER.get(), RollerDoorHeaderBlockEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		AttachFace face = state.getValue(FACE);
		Map<Direction, VoxelShape[]> map = face == AttachFace.WALL ? WALL_SHAPES : face == AttachFace.CEILING ? CEILING_SHAPES : FLOOR_SHAPES;
		return map.get(state.getValue(FACING))[state.getValue(BOTTOM) ? state.getValue(OPENNESS) : 0];
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(CCBlocks.ROLLER_DOOR.get());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		return this.handleLifting(state, level, pos, player, hand, hitResult);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof RollerDoorHeaderBlockEntity rollerDoor) {
			ObjectArrayList<ItemStack> list = new ObjectArrayList<>();
			int itemcount = rollerDoor.getBlockCount() + 1;
			int stackcount = itemcount / 64;
			for (int i = 0; i < stackcount; i++) {
				list.add(new ItemStack(CCBlocks.ROLLER_DOOR.get(), 64));
			}
			list.add(new ItemStack(CCBlocks.ROLLER_DOOR.get(), itemcount % 64));
			return list;
		}
		return super.getDrops(state, builder);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetShape, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		boolean waterlogged = state.getValue(WATERLOGGED);

		if (waterlogged)
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return this.getUpdatedState(level, pos, state.getValue(FACING), state.getValue(FACE), state.getValue(OPENNESS), waterlogged);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FACE, OPENNESS, BOTTOM, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}
}