package com.teamabnormals.caverns_and_chasms.common.entity.animal.glare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class GlareAi {
	protected static Brain<?> makeBrain(Brain<Glare> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<Glare> p_218426_) {
		p_218426_.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.5F), new LookAtTargetSink(45, 90), new MoveToTargetSink()));
	}

	private static void initIdleActivity(Brain<Glare> brain) {
		brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(Pair.of(0, new GoToWantedItem<>((p_218428_) -> {
			return true;
		}, 1.75F, true, 32)), Pair.of(1, new StayCloseToTarget<>(GlareAi::getLikedPlayerPositionTracker, 4, 16, 2.25F)), Pair.of(2, new RunSometimes<>(new SetEntityLookTarget((p_218434_) -> {
			return true;
		}, 6.0F), UniformInt.of(30, 60))), Pair.of(4, new RunOne<>(ImmutableList.of(Pair.of(new FlyingRandomStroll(1.0F), 2), Pair.of(new SetWalkTargetFromLookTarget(1.0F, 3), 2), Pair.of(new DoNothing(30, 60), 1))))), ImmutableSet.of());
	}

	public static void updateActivity(Glare glare) {
		glare.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}

	private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity entity) {
		return getLikedPlayer(entity).map((p_218409_) -> new EntityTracker(p_218409_, true));
	}

	public static Optional<ServerPlayer> getLikedPlayer(LivingEntity entity) {
		Level level = entity.getLevel();
		if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
			Optional<UUID> optional = entity.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
			if (optional.isPresent()) {
				if (serverLevel.getEntity(optional.get()) instanceof ServerPlayer serverPlayer) {
					if ((serverPlayer.gameMode.isSurvival() || serverPlayer.gameMode.isCreative()) && serverPlayer.closerThan(entity, 64.0D)) {
						return Optional.of(serverPlayer);
					}
				}

				return Optional.empty();
			}
		}

		return Optional.empty();
	}
}