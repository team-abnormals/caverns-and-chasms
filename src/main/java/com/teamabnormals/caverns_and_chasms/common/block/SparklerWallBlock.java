package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class SparklerWallBlock extends WallTorchBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 15.0D, 16.0D),
			Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 15.0D, 5.0D),
			Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 15.0D, 10.5D),
			Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 15.0D, 10.5D)
	));

	public SparklerWallBlock(Properties properties) {
		super(properties, ParticleTypes.FLAME);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABBS.get(state.getValue(FACING));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			if (random.nextInt(24) == 0) {
				level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, CCSoundEvents.SPARKLER_SPARKLE.get(), SoundSource.BLOCKS, 0.4F, 1.0F, false);
			}

			Direction facing = state.getValue(FACING);
			Direction opposite = facing.getOpposite();
			double x = (double) pos.getX() + 0.5D;
			double y = (double) pos.getY() + 0.8D;
			double z = (double) pos.getZ() + 0.5D;
			double offsetY = 0.15D;
			double offsetXZ = 0.2D;
			level.addParticle(CCParticleTypes.SPARKLER_SPARK.get(),
					x + offsetXZ * (double) opposite.getStepX() + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.05D, random),
					y + offsetY + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.025D, random),
					z + offsetXZ * (double) opposite.getStepZ() + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.05D, random),
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}
}