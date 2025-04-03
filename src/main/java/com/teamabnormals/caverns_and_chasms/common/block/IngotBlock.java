package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class IngotBlock extends Block implements SimpleWaterloggedBlock {
	private final Supplier<Item> item;

	public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 0, 3);
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
	public static final EnumProperty<IngotLayer> TOP_INGOT = EnumProperty.create("top_ingot", IngotLayer.class);

	public static final VoxelShape LAYER_1_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 4.0D, 16.0D);
	public static final VoxelShape LAYER_2_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 8.0D, 16.0D);
	public static final VoxelShape LAYER_3_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 12.0D, 16.0D);
	public static final VoxelShape LAYER_4_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 16.0D, 16.0D);

	public static final VoxelShape LEFT_X_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 8.0D, 4.0D, 16.0D);
	public static final VoxelShape RIGHT_X_SHAPE = Block.box(9.0D, 0.0D, 1.0D, 16.0D, 4.0D, 16.0D);
	public static final VoxelShape LEFT_Z_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 4.0D, 8.0D);
	public static final VoxelShape RIGHT_Z_SHAPE = Block.box(1.0D, 0.0D, 9.0D, 16.0D, 4.0D, 16.0D);

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public IngotBlock(Supplier<Item> item, Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(TOP_INGOT, IngotLayer.LEFT)
				.setValue(LAYERS, 0)
				.setValue(AXIS, Axis.X)
				.setValue(WATERLOGGED, false));
		this.item = item;
	}

	public static int countIngots(BlockState state) {
		return 2 * state.getValue(LAYERS) + (state.getValue(TOP_INGOT) == IngotLayer.BOTH ? 2 : 1);
	}

	public static BlockState increaseIngots(BlockState state, Vec3 clickPos) {
		if (countIngots(state) < 8) {
			IngotLayer topIngot = state.getValue(TOP_INGOT);
			int layers = state.getValue(LAYERS);

			if (topIngot == IngotLayer.BOTH) {
				return state
						.setValue(TOP_INGOT, getLayerForAxis(clickPos, getOpposite(getAxisForLayer(layers, state.getValue(AXIS)))))
						.setValue(LAYERS, layers + 1);
			} else {
				return state.setValue(TOP_INGOT, IngotLayer.BOTH);
			}
		}

		return state;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());

		Vec3 clickPos = context.getClickLocation().subtract(Vec3.atLowerCornerOf(context.getClickedPos()));

		if (state.is(this) && countIngots(state) < 8) {
			return increaseIngots(state, clickPos);
		} else {
			FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
			Axis axis = context.getHorizontalDirection().getClockWise().getAxis();
			boolean flag = fluidstate.getType() == Fluids.WATER;
			return super.getStateForPlacement(context)
					.setValue(WATERLOGGED, flag)
					.setValue(AXIS, axis)
					.setValue(TOP_INGOT, getLayerForAxis(clickPos, axis));
		}
	}

	public static IngotLayer getLayerForAxis(Vec3 clickPos, Axis axis) {
		return (axis == Axis.Z ? clickPos.x < 0.5F : clickPos.z > 0.5F) ? IngotLayer.RIGHT : IngotLayer.LEFT;
	}

	public static Axis getOpposite(Axis axis) {
		return axis == Axis.X ? Axis.Z : Axis.X;
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return (!state.getCollisionShape(level, pos).getFaceShape(Direction.UP).isEmpty() || state.isFaceSturdy(level, pos, Direction.UP)) && (!(state.getBlock() instanceof IngotBlock) || countIngots(state) == 8);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos belowPos = pos.below();
		return this.mayPlaceOn(level.getBlockState(belowPos), level, belowPos);
	}

	@Override
	public BlockState updateShape(BlockState p_56113_, Direction p_56114_, BlockState p_56115_, LevelAccessor p_56116_, BlockPos p_56117_, BlockPos p_56118_) {
		if (!p_56113_.canSurvive(p_56116_, p_56117_)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if (p_56113_.getValue(WATERLOGGED)) {
				p_56116_.scheduleTick(p_56117_, Fluids.WATER, Fluids.WATER.getTickDelay(p_56116_));
			}

			return super.updateShape(p_56113_, p_56114_, p_56115_, p_56116_, p_56117_, p_56118_);
		}
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext p_56102_) {
		return !p_56102_.isSecondaryUseActive() && p_56102_.getItemInHand().is(this.asItem()) && countIngots(state) < 8 || super.canBeReplaced(state, p_56102_);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_56123_, BlockPos p_56124_, CollisionContext p_56125_) {
		if (countIngots(state) == 8) {
			return LAYER_4_SHAPE;
		} else {
			return Shapes.or(getBaseShape(state), getTopShape(state).move(0.0F, 0.25F * state.getValue(LAYERS), 0.0F));
		}
	}

	public static VoxelShape getBaseShape(BlockState state) {
		int layers = state.getValue(LAYERS);
		if (state.getValue(TOP_INGOT) == IngotLayer.BOTH) {
			layers += 1;
		}

		return switch (layers) {
			case 4 -> LAYER_4_SHAPE;
			case 3 -> LAYER_3_SHAPE;
			case 2 -> LAYER_2_SHAPE;
			case 1 -> LAYER_1_SHAPE;
			default -> Shapes.empty();
		};
	}

	public static Axis getAxisForLayer(int layer, Axis original) {
		return layer % 2 != 0 ? getOpposite(original) : original;
	}

	public static VoxelShape getTopShape(BlockState state) {
		IngotLayer layer = state.getValue(TOP_INGOT);
		Axis axis = getAxisForLayer(state.getValue(LAYERS), state.getValue(AXIS));
		if (layer != IngotLayer.BOTH) {
			if (axis == Axis.X) {
				return layer == IngotLayer.LEFT ? LEFT_Z_SHAPE : RIGHT_Z_SHAPE;
			} else {
				return layer == IngotLayer.LEFT ? RIGHT_X_SHAPE : LEFT_X_SHAPE;
			}
		}
		return Shapes.empty();
	}

	@Override
	public FluidState getFluidState(BlockState p_56131_) {
		return p_56131_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_56131_);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56120_) {
		p_56120_.add(LAYERS, TOP_INGOT, AXIS, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState p_56104_, BlockGetter p_56105_, BlockPos p_56106_, PathComputationType p_56107_) {
		return false;
	}

	@Override
	public Item asItem() {
		return this.item.get();
	}

	@Override
	public String getDescriptionId() {
		return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this.asItem()));
	}
}