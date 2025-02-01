package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public class FlintBlock extends FallingBlock {

	public FlintBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void falling(FallingBlockEntity fallingBlockEntity) {
		fallingBlockEntity.setHurtsEntities(1.0F, 15);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (isFree(level.getBlockState(pos.below())) && isFree(level.getBlockState(pos.above())) && pos.getY() >= level.getMinBuildHeight()) {
			FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, pos, state);
			this.falling(fallingblockentity);
		}
	}

	@Override
	public void onLand(Level level, BlockPos pos, BlockState state, BlockState newState, FallingBlockEntity fallingBlockEntity) {
		this.spark(level, pos, fallingBlockEntity, true);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float p_152430_) {
		if (entity instanceof FallingBlockEntity) {
			FallingBlockEntity fallingblockentity = (FallingBlockEntity) entity;
			spark(level, pos, fallingblockentity, true);
		}
		super.fallOn(level, state, pos, entity, p_152430_);
	}

	public static void spark(Level level, BlockPos pos, FallingBlockEntity fallingBlockEntity, boolean checkHeight) {
		if (fallingBlockEntity.getStartPos().getY() - pos.getY() > 2 || !checkHeight) {
			for (int k = 0; k < level.random.nextInt(5); ++k) {
				int i = level.random.nextIntBetweenInclusive(-1, 1);
				int j = level.random.nextIntBetweenInclusive(-1, 1);
				if (i != 0 || j != 0 ) {
					BlockPos randomPos = pos.offset(i, 0, j);
					BlockState firestate = BaseFireBlock.getState(level, randomPos);
					Vec3 direction = new Vec3(randomPos.getX() - pos.getX(), randomPos.getY(), randomPos.getZ() - pos.getZ()).normalize();
					for (int l = 0; l < 10; ++l) {
						double d1 = direction.x * 0.4D + level.random.nextGaussian() * 0.05D;
						double d2 = direction.y * 0.4D + level.random.nextGaussian() * 0.05D;
						double d3 = direction.z * 0.4D + level.random.nextGaussian() * 0.05D;
						
						NetworkUtil.spawnParticle("caverns_and_chasms:spark", pos.getX(), pos.getY(), pos.getZ(), d1, d2, d3);
					}
					if (level.getBlockState(randomPos).isAir() && firestate.canSurvive(level, randomPos)) {
						level.setBlockAndUpdate(randomPos, firestate);
					}
				}
			}
		}
	}

	@Override
	public int getDustColor(BlockState p_53238_, BlockGetter p_53239_, BlockPos p_53240_) {
		return -8356741;
	}
}