package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class GunpowderBlock extends Block {

	public GunpowderBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level.isClientSide() && (entity.getDeltaMovement().x > 0 || entity.getDeltaMovement().z > 0)) {
			Supplier<Vec3> supplier = () -> new Vec3(Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F));
			ParticleUtils.spawnParticlesOnBlockFace(level, pos, ParticleTypes.SMOKE, ConstantInt.of(1), Direction.UP, supplier, 0.5D);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(5) == 0) {
			BlockPos belowPos = pos.below();
			if (FallingBlock.isFree(level.getBlockState(belowPos))) {
				ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.ASH);
			}
		}
	}
}
