package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public interface Sparkler {
	BooleanProperty LIT = BlockStateProperties.LIT;

	static InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (player.getAbilities().mayBuild && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
			Vec3 vec3 = particlePos(state, pos);
			level.addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0D, 0.1F, 0.0D);
			level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!level.isClientSide) {
				if (level.random.nextFloat() < 0.25F) {
					level.setBlock(pos, state.setValue(LIT, false), 11);
				} else {
					explode(level, pos, vec3);
				}
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	static void explode(Level level, BlockPos pos, Vec3 vec3) {
		CustomExplosion.spawnExplosion(level, null, vec3.x, vec3.y, vec3.z, 1.0F, false, BlockInteraction.KEEP, CCSoundEvents.SPARKLER_EXPLODE.get(), CCParticleTypes.SPARKLER_SPARK_EMITTER.get(), CCParticleTypes.SPARKLER_SPARK_EMITTER.get());
		level.destroyBlock(pos, false);
	}

	static void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity living && !level.isClientSide() && state.getValue(LIT) && (living.xOld != living.getX() || living.zOld != living.getZ()) && living.getRandom().nextFloat() < 0.1F) {
			explode(level, pos, particlePos(state, pos));
		}
	}

	static void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			if (random.nextInt(24) == 0) {
				level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, CCSoundEvents.SPARKLER_SPARKLE.get(), SoundSource.BLOCKS, 0.4F, 1.0F, false);
			}

			Vec3 vec3 = particlePos(state, pos);
			for (int i = 0; i < 2; i++) {
				level.addParticle(CCParticleTypes.SPARKLER_SPARK.get(), vec3.x + (random.nextFloat() - 0.5D) * 0.1D, vec3.y + (random.nextFloat() - 0.5D) * 0.05D, vec3.z + (random.nextFloat() - 0.5D) * 0.1D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	static Vec3 particlePos(BlockState state, BlockPos pos) {
		double x = (double) pos.getX() + 0.5D;
		double y = (double) pos.getY() + 0.78D;
		double z = (double) pos.getZ() + 0.5D;

		if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
			Direction opposite = facing.getOpposite();

			double offsetY = 0.15D;
			double offsetXZ = 0.2D;

			return new Vec3(x + offsetXZ * (double) opposite.getStepX(), y + offsetY, z + offsetXZ * (double) opposite.getStepZ());
		} else {
			return new Vec3(x, y, z);
		}
	}
}
