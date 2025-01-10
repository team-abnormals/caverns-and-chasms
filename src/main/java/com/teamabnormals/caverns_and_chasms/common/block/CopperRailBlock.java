package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class CopperRailBlock extends BaseRailBlock {
	private final WeatheringCopper.WeatherState weatherState;

	public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

	public CopperRailBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
		super(true, properties);
		this.weatherState = weatherState;
		this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false));
	}

	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}

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

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> properties) {
		properties.add(SHAPE, WATERLOGGED);
	}

	public WeatherState getWeatherState() {
		return this.weatherState;
	}
}