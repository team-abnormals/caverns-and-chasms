package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.HoopBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class HoopBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
	public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 3);
	public static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape[][] SHAPES = {rotatedHoopShapes(6.0D, 7.0D), rotatedHoopShapes(4.0D, 6.0D), rotatedHoopShapes(2.0D, 4.0D), rotatedHoopShapes(0.0D, 2.0D)};

	public HoopBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Axis.Y).setValue(SIZE, 3).setValue(OUTPUT_POWER, 0).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HoopBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, CCBlockEntityTypes.HOOP.get(), HoopBlockEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(OUTPUT_POWER);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return direction.getAxis() != state.getValue(AXIS) ? state.getValue(OUTPUT_POWER) : 0;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(OUTPUT_POWER) != 0) {
			level.setBlock(pos, state.setValue(OUTPUT_POWER, 0), 3);

			for (Direction direction : Direction.values()) {
				if (direction.getAxis() != state.getValue(AXIS)) {
					level.updateNeighborsAt(pos.relative(direction), state.getBlock());
				}
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		VoxelShape[] shapes = SHAPES[state.getValue(SIZE)];
		return switch (state.getValue(AXIS)) {
			case Z -> shapes[2];
			case Y -> shapes[1];
			default -> shapes[0];
		};
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.getAbilities().mayBuild && player.getItemInHand(hand).is(CCItemTags.CHANGES_HOOP_SIZE)) {
			int i = state.getValue(SIZE) - 1;
			if (i < 0) {
				i = 3;
				level.playSound(null, pos, CCSoundEvents.HOOP_EXPAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			} else {
				level.playSound(null, pos, CCSoundEvents.HOOP_SHRINK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			}
			level.setBlock(pos, state.setValue(SIZE, i), 2);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Axis axis = context.getClickedFace().getAxis();
		Direction[] directions = context.getNearestLookingDirections();
		Axis axis1 = directions[0].getAxis();
		Axis axis2 = directions[1].getAxis();

		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.getType() == Fluids.WATER;

		return this.defaultBlockState().setValue(AXIS, axis1 == axis ? axis2 : axis1).setValue(WATERLOGGED, Boolean.valueOf(flag));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED))
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return switch (rotation) {
			case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
				case X -> state.setValue(AXIS, Axis.Z);
				case Z -> state.setValue(AXIS, Axis.X);
				default -> state;
			};
			default -> state;
		};
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(AXIS, SIZE, OUTPUT_POWER, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

	private static VoxelShape[] rotatedHoopShapes(double outerDepth, double innerDepth) {
		VoxelShape[] shapes = new VoxelShape[3];
		for (int i = 0; i < 3; ++i) {
			Axis axis = Axis.values()[i];
			double x1 = axis == Axis.X ? 7.0D : outerDepth;
			double y1 = axis == Axis.Y ? 7.0D : outerDepth;
			double z1 = axis == Axis.Z ? 7.0D : outerDepth;
			double x2 = axis == Axis.X ? 7.0D : innerDepth;
			double y2 = axis == Axis.Y ? 7.0D : innerDepth;
			double z2 = axis == Axis.Z ? 7.0D : innerDepth;
			VoxelShape shape = box(x1, y1, z1, 16.0D - x1, 16.0D - y1, 16.0D - z1);
			if (outerDepth > 0.0D) {
				if (axis == Axis.X)
					shape = Shapes.or(shape, box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 16.0D), box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D));
				else if (axis == Axis.Y)
					shape = Shapes.or(shape, box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 16.0D), box(0.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D));
				else
					shape = Shapes.or(shape, box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D), box(0.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D));
			}
			shapes[i] = Shapes.join(shape, box(x2, y2, z2, 16.0D - x2, 16.0D - y2, 16.0D - z2), BooleanOp.ONLY_FIRST);
		}
		return shapes;
	}
}