package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.advancement.AtonedItemTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AtonedItemTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	@Override
	public Codec<AtonedItemTrigger.TriggerInstance> codec() {
		return AtonedItemTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack item, int levelsSpent) {
		this.trigger(player, instance -> instance.matches(item, levelsSpent));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item, MinMaxBounds.Ints levels) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<AtonedItemTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AtonedItemTrigger.TriggerInstance::player),
						ItemPredicate.CODEC.optionalFieldOf("item").forGetter(AtonedItemTrigger.TriggerInstance::item),
						MinMaxBounds.Ints.CODEC.optionalFieldOf("levels", MinMaxBounds.Ints.ANY).forGetter(AtonedItemTrigger.TriggerInstance::levels)
				).apply(instance, AtonedItemTrigger.TriggerInstance::new)
		);

		public static Criterion<AtonedItemTrigger.TriggerInstance> atonedItem() {
			return CCCriteriaTriggers.ATONED_ITEM.get().createCriterion(new AtonedItemTrigger.TriggerInstance(Optional.empty(), Optional.empty(), MinMaxBounds.Ints.ANY));
		}

		public static Criterion<AtonedItemTrigger.TriggerInstance> brokenAtonement() {
			return CCCriteriaTriggers.ATONED_ITEM.get().createCriterion(new AtonedItemTrigger.TriggerInstance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(ItemStack.EMPTY.getItem()).build()), MinMaxBounds.Ints.ANY));
		}

		public boolean matches(ItemStack item, int levels) {
			return (this.item.isEmpty() || this.item.get().test(item)) && this.levels.matches(levels);
		}
	}
}