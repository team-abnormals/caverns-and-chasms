package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

public class RefractorBlock extends DiodeBlock {
	public static final IntegerProperty LEFT = IntegerProperty.create("left", 0, 3);
	public static final IntegerProperty CENTER = IntegerProperty.create("center", 0, 3);
	public static final IntegerProperty RIGHT = IntegerProperty.create("right", 0, 3);
	public static final EnumProperty<RefractorState> POWERED = EnumProperty.create("powered", RefractorState.class);

	public RefractorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(POWERED, RefractorState.OFF)
				.setValue(LEFT, 2).setValue(CENTER, 2).setValue(RIGHT, 2)
		);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 2;
	}


	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
		Direction facing = state.getValue(FACING);
		if (facing.getCounterClockWise() == direction && state.getValue(LEFT) == 0) {
			return false;
		} else if (facing == direction && state.getValue(CENTER) == 0) {
			return false;
		} else if (facing.getClockWise() == direction && state.getValue(RIGHT) == 0) {
			return false;
		}

		return true;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		} else {
			Optional<IntegerProperty> prop = getHitProperty(getRelativeCoordinates(result, state.getValue(FACING)));
			if (prop.isPresent() && state.getValue(POWERED) != getStateFromProperty(prop.get())) {
				level.setBlock(pos, ResistorBlock.cycleOrReverse(state, player, prop.get(), 0, 3), 3);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				return InteractionResult.PASS;
			}
		}
	}

	public static RefractorState getStateFromProperty(IntegerProperty property) {
		return property == LEFT ? RefractorState.LEFT : property == CENTER ? RefractorState.CENTER : property == RIGHT ? RefractorState.RIGHT : RefractorState.NONE;
	}

	private static Optional<Vec2> getRelativeCoordinates(BlockHitResult result, Direction facing) {
		Direction direction = result.getDirection();
		if (direction != Direction.UP) {
			return Optional.empty();
		} else {
			BlockPos pos = result.getBlockPos();
			Vec3 vec3 = result.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
			double x = vec3.x();
			double z = vec3.z();
			return switch (facing) {
				case NORTH -> Optional.of(new Vec2((float) (1.0D - x), (float) z));
				case SOUTH -> Optional.of(new Vec2((float) x, (float) (1.0D - z)));
				case WEST -> Optional.of(new Vec2((float) z, (float) x));
				case EAST -> Optional.of(new Vec2((float) (1.0D - z), (float) (1.0D - x)));
				case DOWN, UP -> Optional.empty();
			};
		}
	}

	private static Optional<IntegerProperty> getHitProperty(Optional<Vec2> optional) {
		if (optional.isPresent()) {
			Vec2 vec2 = optional.get();
			float x = vec2.x;
			float y = vec2.y;

			if (x >= div(12) && y >= div(4) && y <= div(12))
				return Optional.of(RIGHT);
			if (x >= 0 && x <= div(4) && y >= div(4) && y <= div(12))
				return Optional.of(LEFT);
			if (x >= div(4) && x <= div(12) && y >= div(12))
				return Optional.of(CENTER);
		}

		return Optional.empty();
	}

	private static float div(int pos) {
		return pos / 16.0F;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!this.isLocked(level, pos, state)) {
			boolean isPowered = state.getValue(POWERED) != RefractorState.OFF;
			boolean shouldBePowered = this.shouldTurnOn(level, pos, state);
			if (isPowered && !shouldBePowered) {
				level.setBlock(pos, state.setValue(POWERED, RefractorState.OFF), 2);
			} else if (!isPowered) {
				level.setBlock(pos, state.setValue(POWERED, this.getRandomOutput(state, random)), 2);
				if (!shouldBePowered) {
					level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
				}
			}
		}
	}

	@Override
	protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
		if (!this.isLocked(level, pos, state)) {
			boolean isPowered = state.getValue(POWERED) != RefractorState.OFF;
			boolean shouldBePowered = this.shouldTurnOn(level, pos, state);
			if (isPowered != shouldBePowered && !level.getBlockTicks().willTickThisTick(pos, this)) {
				TickPriority tickpriority = TickPriority.HIGH;
				if (this.shouldPrioritize(level, pos, state)) {
					tickpriority = TickPriority.EXTREMELY_HIGH;
				} else if (isPowered) {
					tickpriority = TickPriority.VERY_HIGH;
				}

				level.scheduleTick(pos, this, this.getDelay(state), tickpriority);
			}

		}
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
		if (state.getValue(POWERED) == RefractorState.OFF) {
			return 0;
		} else {
			Direction facing = state.getValue(FACING);
			int signal = this.getOutputSignal(level, pos, state);
			return switch (state.getValue(POWERED)) {
				case LEFT -> facing.getCounterClockWise() == dir ? signal : 0;
				case CENTER -> facing == dir ? signal : 0;
				case RIGHT -> facing.getClockWise() == dir ? signal : 0;
				default -> 0;
			};
		}
	}

	public RefractorState getRandomOutput(BlockState state, RandomSource random) {
		ArrayList<RefractorState> states = Lists.newArrayList();

		for (int i = 0; i < state.getValue(LEFT); i++) states.add(RefractorState.LEFT);
		for (int i = 0; i < state.getValue(CENTER); i++) states.add(RefractorState.CENTER);
		for (int i = 0; i < state.getValue(RIGHT); i++) states.add(RefractorState.RIGHT);

		if (states.isEmpty()) {
			return RefractorState.NONE;
		} else {
			return states.get(random.nextInt(states.size()));
		}
	}

	@Override
	protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
		Direction dir = state.getValue(FACING);
		this.updateNeighborInDirection(level, pos, dir, dir.getClockWise(), dir.getCounterClockWise());
	}

	public void updateNeighborInDirection(Level level, BlockPos pos, Direction... directions) {
		for (Direction direction : directions) {
			BlockPos blockpos = pos.relative(direction.getOpposite());
			if (ForgeEventFactory.onNeighborNotify(level, pos, level.getBlockState(pos), EnumSet.of(direction.getOpposite()), false).isCanceled())
				return;
			level.neighborChanged(blockpos, this, pos);
			level.updateNeighborsAtExceptFromFacing(blockpos, this, direction);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LEFT, CENTER, RIGHT, POWERED);
	}

	public enum RefractorState implements StringRepresentable {
		OFF("off"),
		NONE("none"),
		LEFT("left"),
		CENTER("center"),
		RIGHT("right");

		private final String name;

		RefractorState(String name) {
			this.name = name;
		}

		public String toString() {
			return this.getSerializedName();
		}

		public String getSerializedName() {
			return this.name;
		}
	}
}
