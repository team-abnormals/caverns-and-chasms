package com.teamabnormals.caverns_and_chasms.common.block.roller_door;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.entity.DimmerBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.RollerDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class RollerDoorHeaderBlock extends BaseEntityBlock implements RollerDoor {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final IntegerProperty OPENNESS = IntegerProperty.create("openness", 0, 15);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final Map<Direction, VoxelShape[]> SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, makeShapes(0.0D, 1.0D, 16.0D, 3.0D),
			Direction.SOUTH, makeShapes(0.0D, 13.0D, 16.0D, 15.0D),
			Direction.WEST, makeShapes(1.0D, 0.0D, 3.0D, 16.0D),
			Direction.EAST, makeShapes(13.0D, 0.0D, 15.0D, 16.0D)));

	private static VoxelShape[] makeShapes(double x1, double z1, double x2, double z2) {
		VoxelShape[] shapes = new VoxelShape[16];
		for (int i = 0; i < 16; i++) {
			shapes[i] = Shapes.or(box(x1, i, z1, x2, 16.0D, z2), box(Math.max(x1 - 1.0D, 0.0D), 12.0D, Math.max(z1 - 1.0D, 0.0D), Math.min(x2 + 1.0D, 16.0D), 16.0D, Math.min(z2 + 1.0D, 16.0D)));
		}
		return shapes;
	}

	public RollerDoorHeaderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPENNESS, 0).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DimmerBlockEntity(pos, state);
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
	public boolean isHeader() {
		return true;
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING))[0];
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(CCBlocks.ROLLER_DOOR.get());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetShape, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		Direction facing = state.getValue(FACING);
		boolean waterlogged = state.getValue(WATERLOGGED);

		if (waterlogged)
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return RollerDoor.getCorrectDoorAndOpenness(level, pos, facing, state.getValue(OPENNESS)).setValue(FACING, facing).setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPENNESS, WATERLOGGED);
	}
}