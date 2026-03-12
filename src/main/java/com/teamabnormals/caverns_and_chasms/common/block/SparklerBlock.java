package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SparklerBlock extends TorchBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D);

	public SparklerBlock(Properties properties) {
		super(properties, ParticleTypes.FLAME);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			double x = (double) pos.getX() + 0.5D;
			double y = (double) pos.getY() + 0.8D;
			double z = (double) pos.getZ() + 0.5D;
			level.addParticle(CCParticleTypes.SPARKLER_SPARK.get(),
					x + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.05D, random),
					y + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.025D, random),
					z + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.05D, random),
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}
}