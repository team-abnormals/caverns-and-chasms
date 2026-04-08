package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.advancement.OpenStorageDuctTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class OpenStorageDuctTrigger extends SimpleCriterionTrigger<TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, int length) {
		this.trigger(player, (instance) -> instance.matches(length));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints length) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("length", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::length)
		).apply(instance, TriggerInstance::new));

		public static Criterion<TriggerInstance> openStorageDuct(MinMaxBounds.Ints length) {
			return CCCriteriaTriggers.OPEN_STORAGE_DUCT.get().trigger(new OpenStorageDuctTrigger.TriggerInstance(Optional.empty(), length));
		}

		public boolean matches(int length) {
			return this.length.matches(length);
		}
	}
}