package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.advancement.RatKilledEntityTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class RatKilledEntityTrigger extends SimpleCriterionTrigger<TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Rat rat, Entity target, DamageSource damage) {
		LootContext ratContext = EntityPredicate.createContext(player, rat);
		LootContext targetContext = EntityPredicate.createContext(player, target);
		this.trigger(player, (instance) -> instance.matches(player, ratContext, targetContext, damage));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> ratPredicate, Optional<ContextAwarePredicate> entityPredicate, Optional<DamageSourcePredicate> killingBlow) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("rat").forGetter(TriggerInstance::ratPredicate),
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entityPredicate),
						DamageSourcePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(TriggerInstance::killingBlow)
				).apply(instance, TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> ratKilledEntity() {
			return CCCriteriaTriggers.RAT_KILLED_ENTITY.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
		}

		public static Criterion<TriggerInstance> ratKilledEntity(EntityPredicate.Builder target) {
			return CCCriteriaTriggers.RAT_KILLED_ENTITY.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(target)), Optional.empty()));
		}

		public boolean matches(ServerPlayer player, LootContext ratContext, LootContext context, DamageSource source) {
			if (this.killingBlow.isPresent() && !this.killingBlow.get().matches(player, source)) {
				return false;
			} else if (this.ratPredicate.isPresent() && !this.ratPredicate.get().matches(ratContext)) {
				return false;
			}
			return this.entityPredicate.isPresent() && this.entityPredicate.get().matches(context);
		}

		@Override
		public void validate(CriterionValidator validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			validator.validateEntity(this.ratPredicate, ".rat");
			validator.validateEntity(this.entityPredicate, ".entity");
		}
	}
}