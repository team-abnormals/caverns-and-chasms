package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GunpowderBlock extends TntBlock {

	public GunpowderBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
		explode(level, pos, explosion.getIndirectSourceEntity());
	}

	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		explode(world, pos, igniter);
	}

	public static void explode(Level level, BlockPos pos) {
		explode(level, pos, null);
	}

	public static void explode(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
		if (!level.isClientSide) {
			CustomExplosion.spawnExplosion(level, igniter, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 4.0F, false, BlockInteraction.DESTROY, SoundEvents.GENERIC_EXPLODE, CCParticleTypes.LARGE_SMOKE_EMITTER.get(), ParticleTypes.LARGE_SMOKE);
		}
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
