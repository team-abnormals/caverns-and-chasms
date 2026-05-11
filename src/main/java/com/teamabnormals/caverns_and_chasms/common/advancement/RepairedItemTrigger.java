package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.advancement.RepairedItemTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class RepairedItemTrigger extends SimpleCriterionTrigger<TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack input, ItemStack ingredient, ItemStack output) {
		this.trigger(player, (p_27675_) -> p_27675_.matches(input, ingredient, output));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> input, Optional<ItemPredicate> ingredient, Optional<ItemPredicate> output) implements SimpleCriterionTrigger.SimpleInstance {

		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
						ItemPredicate.CODEC.optionalFieldOf("input").forGetter(TriggerInstance::input),
						ItemPredicate.CODEC.optionalFieldOf("ingredient").forGetter(TriggerInstance::ingredient),
						ItemPredicate.CODEC.optionalFieldOf("output").forGetter(TriggerInstance::output)
				).apply(instance, TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> repairedItem() {
			return CCCriteriaTriggers.REPAIRED_ITEM.get().createCriterion(new RepairedItemTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
		}

		public static Criterion<TriggerInstance> repairedItemWith(ItemPredicate predicate, ItemPredicate predicate2) {
			return CCCriteriaTriggers.REPAIRED_ITEM.get().createCriterion(new RepairedItemTrigger.TriggerInstance(Optional.empty(), Optional.of(predicate), Optional.of(predicate2), Optional.empty()));
		}

		public static Criterion<TriggerInstance> repairedItemWith(ItemPredicate predicate2) {
			return CCCriteriaTriggers.REPAIRED_ITEM.get().createCriterion(new RepairedItemTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(predicate2), Optional.empty()));
		}

		public boolean matches(ItemStack input, ItemStack ingredient, ItemStack output) {
			if (this.input.isPresent() && !this.input.get().test(input)) {
				return false;
			} else if (this.ingredient.isPresent() && !this.ingredient.get().test(ingredient)) {
				return false;
			} else {
				return this.output.isEmpty() || this.output.get().test(output);
			}
		}
	}
}