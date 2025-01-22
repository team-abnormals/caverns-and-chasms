package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;

public class HaltRailBlock extends BaseRailBlock {
	public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
	public static final BooleanProperty TOP_POWERED = BooleanProperty.create("top_powered");
	public static final BooleanProperty BOTTOM_POWERED = BooleanProperty.create("bottom_powered");

	public HaltRailBlock(Properties properties) {
		super(true, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(TOP_POWERED, false).setValue(BOTTOM_POWERED, false).setValue(WATERLOGGED, false));
	}

	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}

	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		super.onMinecartPass(state, level, pos, cart);
		boolean top = state.getValue(TOP_POWERED);
		boolean bottom = state.getValue(BOTTOM_POWERED);

		Direction direction = switch (state.getValue(SHAPE)) {
			case EAST_WEST, ASCENDING_WEST, ASCENDING_EAST -> Direction.WEST;
			default -> Direction.SOUTH;
		};

		Direction motion = cart.getMotionDirection();

		if (top && motion.equals(direction) || bottom && motion.equals(direction.getOpposite())) {
			cart.setDeltaMovement(Vec3.ZERO);
		}
	}

	@Override
	protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
		boolean top = state.getValue(TOP_POWERED);
		boolean bottom = state.getValue(BOTTOM_POWERED);

		Direction direction = switch (state.getValue(SHAPE)) {
			case EAST_WEST, ASCENDING_WEST, ASCENDING_EAST -> Direction.NORTH;
			default -> Direction.WEST;
		};

		boolean topSignal = level.getSignal(pos.relative(direction), direction) > 0;
		boolean bottomSignal = level.getSignal(pos.relative(direction.getOpposite()), direction.getOpposite()) > 0;

		if (top != topSignal || bottom != bottomSignal) {

			if (top != topSignal) level.setBlock(pos, state.setValue(TOP_POWERED, topSignal), 3);
			if (bottom != bottomSignal) level.setBlock(pos, state.setValue(BOTTOM_POWERED, bottomSignal), 3);

			level.updateNeighborsAt(pos.below(), this);
			if (state.getValue(getShapeProperty()).isAscending()) {
				level.updateNeighborsAt(pos.above(), this);
			}
		}

	}

	@Override
	public BlockState rotate(BlockState state, Rotation railShape) {
		return switch (railShape) {
			case CLOCKWISE_180 -> switch (state.getValue(SHAPE)) {
				case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
				case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
				case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
				case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
				case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
				case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
				case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
				case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_WEST); // Forge fix: MC-196102
				case NORTH_SOUTH, EAST_WEST -> state;
			};
			case COUNTERCLOCKWISE_90 -> switch (state.getValue(SHAPE)) {
				case NORTH_SOUTH -> state.setValue(SHAPE, RailShape.EAST_WEST);
				case EAST_WEST -> state.setValue(SHAPE, RailShape.NORTH_SOUTH);
				case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
				case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
				case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
				case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
				case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
				case SOUTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
				case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
				case NORTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
			};
			case CLOCKWISE_90 -> switch (state.getValue(SHAPE)) {
				case NORTH_SOUTH -> state.setValue(SHAPE, RailShape.EAST_WEST);
				case EAST_WEST -> state.setValue(SHAPE, RailShape.NORTH_SOUTH);
				case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
				case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
				case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
				case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
				case SOUTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
				case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
				case NORTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
				case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
			};
			default -> state;
		};
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		RailShape railShape = state.getValue(SHAPE);
		switch (mirror) {
			case LEFT_RIGHT -> {
				return switch (railShape) {
					case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
					case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
					case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
					case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
					case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
					case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
					default -> super.mirror(state, mirror);
				};
			}
			case FRONT_BACK -> {
				switch (railShape) {
					case ASCENDING_EAST:
						return state.setValue(SHAPE, RailShape.ASCENDING_WEST);
					case ASCENDING_WEST:
						return state.setValue(SHAPE, RailShape.ASCENDING_EAST);
					case ASCENDING_NORTH:
					case ASCENDING_SOUTH:
					default:
						break;
					case SOUTH_EAST:
						return state.setValue(SHAPE, RailShape.SOUTH_WEST);
					case SOUTH_WEST:
						return state.setValue(SHAPE, RailShape.SOUTH_EAST);
					case NORTH_WEST:
						return state.setValue(SHAPE, RailShape.NORTH_EAST);
					case NORTH_EAST:
						return state.setValue(SHAPE, RailShape.NORTH_WEST);
				}
			}
		}

		return super.mirror(state, mirror);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getShapeProperty(), TOP_POWERED, BOTTOM_POWERED, WATERLOGGED);
	}
}
