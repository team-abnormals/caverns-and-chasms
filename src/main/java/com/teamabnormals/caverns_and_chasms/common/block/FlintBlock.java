package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.common.block.BlueprintFallingBlock;
import com.teamabnormals.blueprint.common.entity.BlueprintFallingBlockEntity;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;


public class FlintBlock extends BlueprintFallingBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public FlintBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}

	@Override
	protected void falling(FallingBlockEntity fallingBlockEntity) {
		fallingBlockEntity.setHurtsEntities(1.0F, 15);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.hasProperty(LIT)) {
			return;
		}

		if (state.getValue(LIT)) {
			level.setBlock(pos, CCBlocks.FLINT_BLOCK.get().defaultBlockState(), 3);
			return;
		}

		if (isFree(level.getBlockState(pos.below())) && isFree(level.getBlockState(pos.above())) && pos.getY() >= level.getMinBuildHeight()) {
			BlueprintFallingBlockEntity fallingblockentity = BlueprintFallingBlockEntity.fall(level, pos, state);
			this.falling(fallingblockentity);
		}
	}

	@Override
	public void onLand(Level level, BlockPos pos, BlockState state, BlockState newState, FallingBlockEntity fallingBlockEntity) {
		spark(level, pos, false);
		level.playSound(null, pos, CCSoundEvents.FLINT_BLOCK_LAND.get(), SoundSource.BLOCKS, 0.3F, level.random.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float p_152430_) {
		if (entity instanceof FallingBlockEntity) {
			spark(level, pos, false);
		}
		super.fallOn(level, state, pos, entity, p_152430_);
	}

	public static void spark(Level level, BlockPos pos, boolean grazing) {
		for (int k = 0; k < level.random.nextIntBetweenInclusive(1, 5); ++k) {
			int i = level.random.nextIntBetweenInclusive(-1, 1);
			int j = level.random.nextIntBetweenInclusive(-1, 1);
			if (i != 0 || j != 0) {
				BlockPos randomPos = pos.offset(i, 0, j);
				BlockState firestate = BaseFireBlock.getState(level, randomPos);

				Vec3 direction = new Vec3(randomPos.getX() - pos.getX(), randomPos.getY(), randomPos.getZ() - pos.getZ()).normalize();
				for (int l = 0; l < 10; ++l) {
					double d0 = pos.getX() + level.random.nextDouble() * 0.8D;
					double d1 = pos.getY() + level.random.nextDouble() * 0.2D;
					double d2 = pos.getZ() + level.random.nextDouble() * 0.8D;
					double d3 = direction.x * 0.4D + level.random.nextGaussian() * 0.05D;
					double d4 = direction.y * 0.4D + level.random.nextGaussian() * 0.05D;
					double d5 = direction.z * 0.4D + level.random.nextGaussian() * 0.05D;

					NetworkUtil.spawnParticle(CCParticleTypes.TIN_SPARK.getId().toString(), d0, d1, d2, d3, d4, d5);
				}
				for (int m = 0; m < (!grazing ? 25 : 10); ++m) {
					double d0 = pos.getX() + level.random.nextDouble() * 0.8D;
					double d1 = pos.getY() + level.random.nextDouble() * 0.2D;
					double d2 = pos.getZ() + level.random.nextDouble() * 0.8D;
					double d3 = direction.x + level.random.nextGaussian() * 0.02D;
					double d4 = direction.y * 0.3D + level.random.nextGaussian() * 0.02D;
					double d5 = direction.z + level.random.nextGaussian() * 0.02D;

					NetworkUtil.spawnParticle(CCParticleTypes.FLINT.getId().toString(), d0, d1, d2, d3, d4, d5);
				}
				if (!grazing) {
					level.playSound(null, pos, CCSoundEvents.FLINT_BLOCK_STRIKE.get(), SoundSource.BLOCKS, 1F, 1F);
					if (level.getBlockState(pos).is(CCBlocks.FLINT_BLOCK.get())) {
						BlockState lit = FlintBlock.litState(level.getBlockState(pos));
						level.setBlock(pos, lit, 3);
					}
				} else {
					if (level.random.nextFloat() < 0.6) {
						level.playSound(null, pos, CCSoundEvents.FLINT_BLOCK_STRIKE.get(), SoundSource.BLOCKS, 0.4F, 1F);
					}
					if (level.getBlockState(pos).is(CCBlocks.FLINT_BLOCK.get())) {
						BlockState lit = FlintBlock.litState(level.getBlockState(pos));
						level.setBlock(pos, lit, 3);
					}
				}
				if (level.getBlockState(randomPos).isAir() && firestate.canSurvive(level, randomPos) && !level.getBlockState(randomPos).is(Blocks.TNT)) {
					level.setBlockAndUpdate(randomPos, firestate);
				}
			}
		}
		for (Direction dir : Direction.values()) {
			BlockPos tntPos = pos.relative(dir);
			if (level.getBlockState(tntPos).getBlock() instanceof TntBlock tnt) {
				tnt.onCaughtFire(level.getBlockState(tntPos), level, tntPos, null, null);
				level.removeBlock(tntPos, false);
			}
		}
		level.scheduleTick(pos, CCBlocks.FLINT_BLOCK.get(), 10);
	}

	public static BlockState litState(BlockState state) {
		return state.setValue(LIT, true);
	}

	@Override
	public int getDustColor(BlockState p_53238_, BlockGetter p_53239_, BlockPos p_53240_) {
		return -8356741;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.4) {
			BlockPos blockpos = pos.below();
			if (isFree(level.getBlockState(blockpos))) {
				double d0 = pos.getX() + random.nextDouble();
				double d1 = pos.getY() + 0.7;
				double d2 = pos.getZ() + random.nextDouble();
				level.addParticle(CCParticleTypes.FLINT.get(), d0, d1, d2, 0.0, 0.0, 0.0);

				if (random.nextInt(50) == 0) {
					level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, CCSoundEvents.FLINT_BLOCK_RATTLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
				}
			}
		}
	}
}