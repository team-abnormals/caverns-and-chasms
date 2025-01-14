package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverItem;
import com.teamabnormals.caverns_and_chasms.core.other.CCDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;

public class SlaughterRailBlock extends BaseRailBlock {
	public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public SlaughterRailBlock(Properties properties) {
		super(true, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(POWERED, false).setValue(WATERLOGGED, false));
	}

	@Override
	public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
		cart.getPassengers().forEach((entity) -> {
			if (state.getValue(POWERED) && entity instanceof LivingEntity target) {
				if (target.hurt(CCDamageTypes.spikedRail(level), 5.0F)) {
					SilverItem.causeMagicDamageParticles(target);
				}
			}
		});
	}

	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}

	@Override
	protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
		boolean isPowered = state.getValue(POWERED);
		boolean hasNeighborSignal = level.hasNeighborSignal(pos);
		if (hasNeighborSignal != isPowered) {
			level.setBlock(pos, state.setValue(POWERED, hasNeighborSignal), 3);
			level.updateNeighborsAt(pos.below(), this);
			if (state.getValue(getShapeProperty()).isAscending()) {
				level.updateNeighborsAt(pos.above(), this);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getShapeProperty(), POWERED, WATERLOGGED);
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
}
