package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.advancement.HoopTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class HoopTrigger extends SimpleCriterionTrigger<TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Entity projectile, int hoopSize, int signalStrength) {
		LootContext projectileContext = EntityPredicate.createContext(player, projectile);
		this.trigger(player, (instance) -> instance.matches(projectileContext, hoopSize, signalStrength));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints hoopSize, MinMaxBounds.Ints signalStrength, Optional<ContextAwarePredicate> projectile) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
						MinMaxBounds.Ints.CODEC.optionalFieldOf("hoop_size", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::hoopSize),
						MinMaxBounds.Ints.CODEC.optionalFieldOf("signal_strength", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::signalStrength),
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("projectile").forGetter(TriggerInstance::projectile)
				).apply(instance, TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> hoopEntered(MinMaxBounds.Ints hoopSize, MinMaxBounds.Ints signalStrength, Optional<ContextAwarePredicate> projectile) {
			return CCCriteriaTriggers.HOOP_ENTERED.get().createCriterion(new HoopTrigger.TriggerInstance(Optional.empty(), hoopSize, signalStrength, projectile));
		}

		public boolean matches(LootContext projectileContext, int hoopSize, int signalStrength) {
			if (!this.hoopSize.matches(hoopSize) || !this.signalStrength.matches(signalStrength)) {
				return false;
			} else {
				return this.projectile.isEmpty() || this.projectile.get().matches(projectileContext);
			}
		}

		@Override
		public void validate(CriterionValidator validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			validator.validateEntity(this.projectile, ".projectile");
		}
	}
}