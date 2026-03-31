package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.BlockUtil;
import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public interface Sparkler {
	BooleanProperty LIT = BlockStateProperties.LIT;
	Map<DyeColor, Pair<RegistryObject<SparklerBlock>, RegistryObject<WallSparklerBlock>>> SPARKLER_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (map) -> {
		map.put(DyeColor.WHITE, CCBlocks.WHITE_SPARKLER);
		map.put(DyeColor.ORANGE, CCBlocks.ORANGE_SPARKLER);
		map.put(DyeColor.MAGENTA, CCBlocks.MAGENTA_SPARKLER);
		map.put(DyeColor.LIGHT_BLUE, CCBlocks.LIGHT_BLUE_SPARKLER);
		map.put(DyeColor.YELLOW, CCBlocks.YELLOW_SPARKLER);
		map.put(DyeColor.LIME, CCBlocks.LIME_SPARKLER);
		map.put(DyeColor.PINK, CCBlocks.PINK_SPARKLER);
		map.put(DyeColor.GRAY, CCBlocks.GRAY_SPARKLER);
		map.put(DyeColor.LIGHT_GRAY, CCBlocks.LIGHT_GRAY_SPARKLER);
		map.put(DyeColor.CYAN, CCBlocks.CYAN_SPARKLER);
		map.put(DyeColor.PURPLE, CCBlocks.PURPLE_SPARKLER);
		map.put(DyeColor.BLUE, CCBlocks.BLUE_SPARKLER);
		map.put(DyeColor.BROWN, CCBlocks.BROWN_SPARKLER);
		map.put(DyeColor.GREEN, CCBlocks.GREEN_SPARKLER);
		map.put(DyeColor.RED, CCBlocks.RED_SPARKLER);
		map.put(DyeColor.BLACK, CCBlocks.BLACK_SPARKLER);
	});

	default void onProjectileHitSparkler(Level level, BlockState state, BlockPos pos, Projectile projectile) {
		if (!level.isClientSide() && projectile.isOnFire() && projectile.mayInteract(level, pos)) {
			if (!state.getValue(LIT)) {
				level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
			} else {
				explodeSparkler(state, level, pos);
			}
		}
	}

	default InteractionResult useSparkler(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getAbilities().mayBuild && stack.isEmpty() && state.getValue(LIT)) {
			Vec3 vec3 = particlePos(state, pos);
			level.addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0D, 0.1F, 0.0D);
			level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!level.isClientSide) {
				level.setBlock(pos, state.setValue(LIT, false), 11);
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else if (stack.getItem() instanceof DyeItem dyeItem && SPARKLER_BY_DYE.get(dyeItem.getDyeColor()) != null) {
			Pair<RegistryObject<SparklerBlock>, RegistryObject<WallSparklerBlock>> pair = SPARKLER_BY_DYE.get(dyeItem.getDyeColor());
			Block newBlock = this instanceof SparklerBlock ? pair.getFirst().get() : pair.getSecond().get();
			if (!state.is(newBlock)) {
				level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				if (!level.isClientSide()) {
					level.setBlock(pos, BlockUtil.transferAllBlockStates(state, newBlock.defaultBlockState()), 11);
					if (!player.getAbilities().instabuild) {
						stack.shrink(1);
					}
				}
				level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	default void explodeSparkler(BlockState state, Level level, BlockPos pos) {
		Vec3 vec3 = particlePos(state, pos);
		CustomExplosion.spawnExplosion(level, null, vec3.x, vec3.y, vec3.z, 1.0F, false, BlockInteraction.KEEP, CCSoundEvents.SPARKLER_EXPLODE.get(), getParticleEmitter().get(), getParticleEmitter().get());
		level.setBlock(pos, state.setValue(LIT, false), 11);
	}

	default void entityInsideSparkler(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity living && !level.isClientSide() && state.getValue(LIT) && (living.xOld != living.getX() || living.zOld != living.getZ()) && living.getRandom().nextBoolean()) {
			double d0 = Math.abs(entity.getX() - entity.xOld);
			double d1 = Math.abs(entity.getZ() - entity.zOld);
			if (d0 >= (double) 0.25F || d1 >= (double) 0.25F) {
				this.explodeSparkler(state, level, pos);
			}
		} else if (entity instanceof Projectile projectile) {
			this.onProjectileHitSparkler(level, state, pos, projectile);
		}
	}

	default void animateTickSparkler(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			if (random.nextInt(12) == 0) {
				level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, CCSoundEvents.SPARKLER_SPARKLE.get(), SoundSource.BLOCKS, 0.4F, 1.0F, false);
			}

			if (random.nextInt(12) == 0) {
				level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, CCSoundEvents.SPARKLER_FIZZLE.get(), SoundSource.BLOCKS, 0.4F, 1.0F, false);
			}

			Vec3 vec3 = particlePos(state, pos);
			for (int i = 0; i < 2; i++) {
				level.addParticle(getParticle().get(), vec3.x + (random.nextFloat() - 0.5D) * 0.1D, vec3.y + (random.nextFloat() - 0.5D) * 0.05D, vec3.z + (random.nextFloat() - 0.5D) * 0.1D, 0.0D, 0.0D, 0.0D);
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

	Supplier<? extends ParticleOptions> getParticle();

	Supplier<? extends ParticleOptions> getParticleEmitter();
}
