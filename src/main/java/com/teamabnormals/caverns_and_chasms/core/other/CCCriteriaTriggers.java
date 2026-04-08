package com.teamabnormals.caverns_and_chasms.core.other;

import com.mojang.serialization.MapCodec;
import com.teamabnormals.caverns_and_chasms.common.advancement.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger.TriggerInstance;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class CCCriteriaTriggers {
	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<CriterionTrigger<?>, AtonedItemTrigger> ATONED_ITEM = TRIGGERS.register("atoned_item", AtonedItemTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, HoopTrigger> HOOP_ENTERED = TRIGGERS.register("hoop_entered", HoopTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, OpenStorageDuctTrigger> OPEN_STORAGE_DUCT = TRIGGERS.register("open_storage_duct", OpenStorageDuctTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, PlayerHurtSelfTrigger> PLAYER_HURT_SELF = TRIGGERS.register("player_hurt_self", PlayerHurtSelfTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, RatKilledEntityTrigger> RAT_KILLED_ENTITY = TRIGGERS.register("rat_killed_entity", RatKilledEntityTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, RepairedItemTrigger> REPAIRED_ITEM = TRIGGERS.register("repaired_item", RepairedItemTrigger::new);

	public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> USE_TUNING_FORK = TRIGGERS.register("use_tuning_fork", PlayerTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> SPOTTED_BY_PEEPER = TRIGGERS.register("spotted_by_peeper", PlayerTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> DISMANTLED_ITEM = TRIGGERS.register("dismantled_item", PlayerTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> HATCH_SADDLED_GRAZER = TRIGGERS.register("hatch_saddled_grazer", PlayerTrigger::new);

	public static Criterion<TriggerInstance> useTuningFork() {
		return USE_TUNING_FORK.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
	}

	public static Criterion<TriggerInstance> spottedByPeeper() {
		return SPOTTED_BY_PEEPER.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
	}

	public static Criterion<TriggerInstance> dismantledItem() {
		return DISMANTLED_ITEM.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
	}

	public static Criterion<TriggerInstance> hatchSaddledGrazer() {
		return HATCH_SADDLED_GRAZER.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
	}

	public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<RicochetPredicate>> RICOCHETS = ENTITY_SUB_PREDICATE_TYPES.register("ricochets", () -> RicochetPredicate.CODEC);
	public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<CopperGolemPredicate>> COPPER_GOLEM = ENTITY_SUB_PREDICATE_TYPES.register("copper_golem", () -> CopperGolemPredicate.CODEC);
}