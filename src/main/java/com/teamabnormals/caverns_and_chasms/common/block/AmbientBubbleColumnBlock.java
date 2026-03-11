package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;

public class AmbientBubbleColumnBlock extends BubbleColumnBlock {
	public static final BooleanProperty DRAG_DOWN = BlockStateProperties.DRAG;

	public AmbientBubbleColumnBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		BlockState aboveState = level.getBlockState(pos.above());
		if (aboveState.isAir()) {
			if (!level.isClientSide) {
				ServerLevel serverLevel = (ServerLevel) level;
				for (int i = 0; i < 2; ++i) {
					serverLevel.sendParticles(ParticleTypes.SPLASH, (double) pos.getX() + level.random.nextDouble(), pos.getY() + 1, (double) pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
					serverLevel.sendParticles(ParticleTypes.BUBBLE, (double) pos.getX() + level.random.nextDouble(), pos.getY() + 1, (double) pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.01D, 0.0D, 0.2D);
				}
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		updateColumn(level, pos, state, level.getBlockState(pos.below()));
	}

	public static void updateColumn(LevelAccessor level, BlockPos pos, BlockState state) {
		updateColumn(level, pos, level.getBlockState(pos), state);
	}

	public static void updateColumn(LevelAccessor level, BlockPos pos, BlockState state, BlockState belowState) {
		if (canExistIn(state)) {
			BlockState blockstate = getColumnState(belowState);
			level.setBlock(pos, blockstate, 2);
			MutableBlockPos mutablePos = pos.mutable().move(Direction.UP);

			while (canExistIn(level.getBlockState(mutablePos))) {
				if (!level.setBlock(mutablePos, blockstate, 2)) {
					return;
				}
				mutablePos.move(Direction.UP);
			}

		}
	}

	private static boolean canExistIn(BlockState state) {
		return state.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get()) || state.is(Blocks.WATER) && state.getFluidState().getAmount() >= 8 && state.getFluidState().isSource();
	}

	private static BlockState getColumnState(BlockState state) {
		if (state.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get())) {
			return state;
		} else if (state.is(Blocks.SOUL_SOIL)) {
			return CCBlocks.AMBIENT_BUBBLE_COLUMN.get().defaultBlockState().setValue(DRAG_DOWN, Boolean.valueOf(false));
		} else {
			return state.is(CCBlocks.MAGMATIC_RHYOLITE.get()) ? CCBlocks.AMBIENT_BUBBLE_COLUMN.get().defaultBlockState().setValue(DRAG_DOWN, Boolean.valueOf(true)) : Blocks.WATER.defaultBlockState();
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
		level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		if (!state.canSurvive(level, pos) || dir == Direction.DOWN || dir == Direction.UP && !state.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get()) && canExistIn(state)) {
			level.scheduleTick(pos, this, 5);
		}

		return state;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos.below());
		return blockstate.is(CCBlocks.AMBIENT_BUBBLE_COLUMN.get()) || blockstate.is(CCBlocks.MAGMATIC_RHYOLITE.get()) || blockstate.is(Blocks.SOUL_SOIL);
	}
}