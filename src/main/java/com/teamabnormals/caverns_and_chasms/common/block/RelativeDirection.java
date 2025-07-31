package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public enum RelativeDirection implements StringRepresentable {
	LEFT("left"), // Also east
	RIGHT("right"), // Also west
	UP("up"), // Also north
	DOWN("down"); // Also south

	private final String name;

	RelativeDirection(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public Direction getCardinalDirection(Direction forwardDir) {
		if (forwardDir.getAxis() == Axis.Y) {
			return switch (this) {
				case LEFT -> Direction.EAST;
				case RIGHT -> Direction.WEST;
				case UP -> Direction.SOUTH;
				case DOWN -> Direction.NORTH;
			};
		} else {
			return switch (this) {
				case LEFT -> forwardDir.getCounterClockWise();
				case RIGHT -> forwardDir.getClockWise();
				case UP -> Direction.UP;
				case DOWN -> Direction.DOWN;
			};
		}
	}

	public RelativeDirection getClockWise() {
		return switch (this) {
			case LEFT -> UP;
			case RIGHT -> DOWN;
			case UP -> RIGHT;
			case DOWN -> LEFT;
		};
	}

	public RelativeDirection getCounterClockWise() {
		return switch (this) {
			case LEFT -> DOWN;
			case RIGHT -> UP;
			case UP -> LEFT;
			case DOWN -> RIGHT;
		};
	}

	public RelativeDirection getOpposite() {
		return switch (this) {
			case LEFT -> RIGHT;
			case RIGHT -> LEFT;
			case UP -> DOWN;
			case DOWN -> UP;
		};
	}

	public RelativeDirection rotate(Rotation rotation, Direction forward) {
		if (forward.getAxis() == Direction.Axis.Y) {
			return switch (rotation) {
				case CLOCKWISE_90 -> this.getClockWise();
				case CLOCKWISE_180 -> this.getOpposite();
				case COUNTERCLOCKWISE_90 -> this.getCounterClockWise();
				default -> this;
			};
		} else {
			return this;
		}
	}

	public RelativeDirection mirror(Mirror mirror, Direction forward) {
		if (forward.getAxis() == Direction.Axis.Y) {
			if (mirror == Mirror.FRONT_BACK && (this == LEFT || this == RIGHT))
				return this.getOpposite();
			else if (mirror == Mirror.LEFT_RIGHT && (this == UP || this == DOWN))
				return this.getOpposite();
		} else if (this != UP && this != DOWN) {
			return this.getOpposite();
		}
		return this;
	}
}