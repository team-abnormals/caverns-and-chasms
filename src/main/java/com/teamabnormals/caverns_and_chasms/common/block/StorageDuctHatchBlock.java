package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.teamabnormals.caverns_and_chasms.common.block.StorageDuctBlock.DuctEnd;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctHatchBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class StorageDuctHatchBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	public static final EnumProperty<RelativeDirection> HANDLE = EnumProperty.create("handle", RelativeDirection.class);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.immutableEnumMap(Map.of(Direction.DOWN, box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Direction.UP, box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Direction.NORTH, box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D),
			Direction.SOUTH, box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
			Direction.WEST, box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Direction.EAST, box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D)));

	public StorageDuctHatchBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HANDLE, RelativeDirection.DOWN).setValue(OPEN, false).setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StorageDuctHatchBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		FluidState fluidstate = context.getLevel().getFluidState(blockpos);

		RelativeDirection handledir = calculateHandleDirection(context.getLevel(), blockpos, facing, context.getClickLocation());

		return this.defaultBlockState().setValue(FACING, facing).setValue(HANDLE, handledir).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	private static RelativeDirection calculateHandleDirection(Level level, BlockPos pos, Direction facing, Vec3 clickLocation) {
		Vec3 relativeclickloc = clickLocation.subtract(Vec3.atCenterOf(pos));
		double x;
		double y;

		if (facing == Direction.UP || facing == Direction.DOWN) {
			x = -relativeclickloc.x;
			y = relativeclickloc.z;
		} else {
			if (facing == Direction.WEST)
				x = relativeclickloc.z;
			else if (facing == Direction.EAST)
				x = -relativeclickloc.z;
			else if (facing == Direction.SOUTH)
				x = relativeclickloc.x;
			else
				x = -relativeclickloc.x;
			y = relativeclickloc.y;
		}

		RelativeDirection xdir = x < 0.0D ? RelativeDirection.LEFT : RelativeDirection.RIGHT;
		RelativeDirection ydir = y < 0.0D ? RelativeDirection.DOWN : RelativeDirection.UP;
		BlockPos offsetpos = pos.relative(facing.getOpposite());
		BlockState offsetstate = level.getBlockState(offsetpos);

		if (offsetstate.is(CCBlocks.STORAGE_DUCT.get())) {
			Direction firstend = offsetstate.getValue(StorageDuctBlock.FIRST_END);
			Direction secondend = offsetstate.getValue(StorageDuctBlock.SECOND_END);

			if (facing != firstend && facing != secondend) {
				Axis xaxis = xdir.getCardinalDirection(facing.getOpposite()).getAxis();
				Axis yaxis = ydir.getCardinalDirection(facing.getOpposite()).getAxis();
				Axis firstendaxis = firstend.getAxis();
				Axis secondendaxis = secondend.getAxis();

				if (firstendaxis != xaxis && secondendaxis != xaxis)
					return ydir;
				else if (firstendaxis != yaxis && secondendaxis != yaxis)
					return xdir;
			}
		}

		return Math.abs(x) > Math.abs(y) ? xdir : ydir;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
		if (state.getValue(WATERLOGGED))
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		Direction facing = state.getValue(FACING);
		if (direction == facing.getOpposite() && offsetState.is(CCBlocks.STORAGE_DUCT.get())) {
			RelativeDirection handledir = state.getValue(HANDLE);
			Axis handleaxis = handledir.getCardinalDirection(facing).getAxis();
			Direction firstenddir = offsetState.getValue(StorageDuctBlock.FIRST_END);
			Direction secondenddir = offsetState.getValue(StorageDuctBlock.SECOND_END);
			if (firstenddir != facing && secondenddir != facing && firstenddir.getAxis() != handleaxis && secondenddir.getAxis() != handleaxis) {
				if (facing.getAxis() == Axis.Y)
					return state.setValue(HANDLE, handledir.getClockWise());
				else if (handledir == RelativeDirection.LEFT || handledir == RelativeDirection.RIGHT)
					return state.setValue(HANDLE, handledir.getClockWise());
				else
					return state.setValue(HANDLE, handledir.getCounterClockWise());
			}
		}

		return state;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof StorageDuctHatchBlockEntity hatch)
			hatch.recheckOpen();
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (level.getBlockEntity(pos) instanceof StorageDuctHatchBlockEntity hatch) {
			BlockPos ductpos = pos.relative(state.getValue(FACING).getOpposite());
			if (level.getBlockEntity(ductpos) instanceof StorageDuctBlockEntity) {
				BlockState ductstate = level.getBlockState(ductpos);
				Direction facing = state.getValue(FACING);
				Direction readdir = state.getValue(HANDLE).getCardinalDirection(facing.getOpposite());
				Direction firstenddir = ductstate.getValue(StorageDuctBlock.FIRST_END);
				Direction secondenddir = ductstate.getValue(StorageDuctBlock.SECOND_END);

				DuctEnd startend = null;
				if (firstenddir == facing || secondenddir == readdir || firstenddir == readdir.getOpposite())
					startend = DuctEnd.FIRST;
				else if (secondenddir == facing || firstenddir == readdir || secondenddir == readdir.getOpposite())
					startend = DuctEnd.SECOND;

				if (startend != null) {
					if (!level.isClientSide) {
						StorageDuctBlock.openMenu((ServerPlayer) player, level, ductpos, startend, hatch);
						PiglinAi.angerNearbyPiglins(player, true);
					}
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		Direction facing = state.getValue(FACING);
		return state.setValue(FACING, rotation.rotate(facing)).setValue(HANDLE, state.getValue(HANDLE).rotate(rotation, facing));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		Direction facing = state.getValue(FACING);
		return state.rotate(mirror.getRotation(facing)).setValue(HANDLE, state.getValue(HANDLE).mirror(mirror, facing));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HANDLE, OPEN, WATERLOGGED);
	}
}