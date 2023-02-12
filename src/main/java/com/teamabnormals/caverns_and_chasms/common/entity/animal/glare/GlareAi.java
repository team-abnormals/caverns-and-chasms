package com.teamabnormals.caverns_and_chasms.common.entity.animal.glare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.behavior.GetMadAtDarkness;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.behavior.GoToDarkBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.behavior.GoToSporeBlossom;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.behavior.SpinAtSporeBlossom;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.player.Player;
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
		p_218426_.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(1.5F), new LookAtTargetSink(45, 90), new MoveToTargetSink()));
	}

	private static void initIdleActivity(Brain<Glare> brain) {
		brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
				Pair.of(0, new StayCloseToTarget<>(GlareAi::getLikedPlayerPositionTracker, 4, 12, 2.25F)),
				Pair.of(1, new SpinAtSporeBlossom<>()),
				Pair.of(1, new RunSometimes<>(new GoToSporeBlossom<>(CCMemoryModuleTypes.NEAREST_SPORE_BLOSSOM.get(), 0, 1.25F), UniformInt.of(400, 600))),
				Pair.of(2, new StayCloseToTarget<>(GlareAi::getNewLikedPlayerPositionTracker, 4, 12, 1.75F)),
				Pair.of(3, new GetMadAtDarkness<>()),
				Pair.of(3, new RunSometimes<>(new GoToDarkBlock<>(CCMemoryModuleTypes.NEAREST_DARK_BLOCK.get(), 0, 1.25F), UniformInt.of(100, 300))),
				Pair.of(4, new RunSometimes<>(new SetEntityLookTarget((p_218434_) -> true, 6.0F), UniformInt.of(30, 60))),
				Pair.of(5, new RunOne<>(ImmutableList.of(Pair.of(new FlyingRandomStroll(1.0F), 2), Pair.of(new SetWalkTargetFromLookTarget(1.0F, 3), 2), Pair.of(new DoNothing(30, 60), 1))))
		), ImmutableSet.of());
	}

	public static void updateActivity(Glare glare) {
		glare.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}

	private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity entity) {
		return getLikedPlayer(entity).map((p_218409_) -> new EntityTracker(p_218409_, true));
	}

	private static Optional<PositionTracker> getNewLikedPlayerPositionTracker(LivingEntity entity) {
		return getNewLikedPlayer(entity).map((p_218409_) -> new EntityTracker(p_218409_, true));
	}

	public static Optional<ServerPlayer> getNewLikedPlayer(LivingEntity entity) {
		Level level = entity.getLevel();
		if (!level.isClientSide() && level instanceof ServerLevel serverLevel && entity.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).isEmpty()) {
			Optional<LivingEntity> optional = entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty()).findClosest(GlareAi::isPlayer);
			if (optional.isPresent()) {
				if (optional.get() instanceof ServerPlayer serverPlayer) {
					IDataManager manager = ((IDataManager) optional.get());
					Optional<UUID> ownedGlare = manager.getValue(CCDataProcessors.OWNED_GLARE_UUID);
					if ((ownedGlare.isEmpty() || serverLevel.getEntity(ownedGlare.get()) == null) && (serverPlayer.gameMode.isSurvival() || serverPlayer.gameMode.isCreative()) && serverPlayer.closerThan(entity, 4.0D)) {
						entity.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, serverPlayer.getUUID());
						manager.setValue(CCDataProcessors.OWNED_GLARE_UUID, Optional.of(entity.getUUID()));
						return Optional.of(serverPlayer);
					}
				}

				return Optional.empty();
			}
		}

		return Optional.empty();
	}

	private static boolean isPlayer(LivingEntity entity) {
		return entity instanceof Player;
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