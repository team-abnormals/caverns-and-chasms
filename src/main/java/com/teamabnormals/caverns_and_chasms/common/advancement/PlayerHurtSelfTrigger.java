package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.advancement.PlayerHurtSelfTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class PlayerHurtSelfTrigger extends SimpleCriterionTrigger<TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Entity entity, DamageSource source, float amountDealt, float amountTaken, boolean blocked) {
		LootContext lootcontext = EntityPredicate.createContext(player, entity);
		this.trigger(player, instance -> instance.matches(player, lootcontext, source, amountDealt, amountTaken, blocked));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DamagePredicate> damage) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
				DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(TriggerInstance::damage)
		).apply(instance, TriggerInstance::new));

		public static Criterion<TriggerInstance> playerHurtSelf() {
			return CCCriteriaTriggers.PLAYER_HURT_SELF.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
		}

		public static Criterion<TriggerInstance> playerHurtSelfWithDamage(Optional<DamagePredicate> damage) {
			return CCCriteriaTriggers.PLAYER_HURT_SELF.get().createCriterion(new TriggerInstance(Optional.empty(), damage));
		}

		public static Criterion<TriggerInstance> playerHurtSelfWithDamage(DamagePredicate.Builder damage) {
			return CCCriteriaTriggers.PLAYER_HURT_SELF.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build())));
		}

		public boolean matches(ServerPlayer player, LootContext context, DamageSource damage, float dealt, float taken, boolean blocked) {
			return this.damage.isPresent() && !this.damage.get().matches(player, damage, dealt, taken, blocked);
		}
	}
}