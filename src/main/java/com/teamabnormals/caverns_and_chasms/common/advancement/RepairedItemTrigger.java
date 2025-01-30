package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.common.advancement.RepairedItemTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class RepairedItemTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(CavernsAndChasms.MOD_ID, "repaired_item");

	public ResourceLocation getId() {
		return ID;
	}

	public RepairedItemTrigger.TriggerInstance createInstance(JsonObject p_286526_, ContextAwarePredicate context, DeserializationContext p_286881_) {
		ItemPredicate input = ItemPredicate.fromJson(p_286526_.get("input"));
		ItemPredicate ingredient = ItemPredicate.fromJson(p_286526_.get("ingredient"));
		ItemPredicate output = ItemPredicate.fromJson(p_286526_.get("output"));
		return new RepairedItemTrigger.TriggerInstance(context, input, ingredient, output);
	}

	public void trigger(ServerPlayer p_27669_, ItemStack p_27670_, ItemStack ingredient, ItemStack output) {
		this.trigger(p_27669_, (p_27675_) -> {
			return p_27675_.matches(p_27670_, ingredient, output);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate input;
		private final ItemPredicate ingredient;
		private final ItemPredicate output;

		public TriggerInstance(ContextAwarePredicate p_286871_, ItemPredicate input, ItemPredicate ingredient, ItemPredicate output) {
			super(RepairedItemTrigger.ID, p_286871_);
			this.input = input;
			this.ingredient = ingredient;
			this.output = output;
		}

		public static RepairedItemTrigger.TriggerInstance repairedItem() {
			return new RepairedItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
		}

		public static RepairedItemTrigger.TriggerInstance repairedItem(ItemPredicate predicate) {
			return new RepairedItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, predicate, ItemPredicate.ANY, ItemPredicate.ANY);
		}

		public static RepairedItemTrigger.TriggerInstance repairedItemWith(ItemPredicate predicate, ItemPredicate predicate2) {
			return new RepairedItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, predicate, predicate2, ItemPredicate.ANY);
		}

		public static RepairedItemTrigger.TriggerInstance repairedItemWith(ItemPredicate predicate2) {
			return repairedItemWith(ItemPredicate.ANY, predicate2);
		}


		public boolean matches(ItemStack input, ItemStack ingredient, ItemStack output) {
			if (!this.input.matches(input)) {
				return false;
			} else if (!this.ingredient.matches(ingredient)) {
				return false;
			} else {
				return this.output.matches(output);
			}
		}

		public JsonObject serializeToJson(SerializationContext p_27695_) {
			JsonObject jsonobject = super.serializeToJson(p_27695_);
			jsonobject.add("input", this.input.serializeToJson());
			jsonobject.add("ingredient", this.ingredient.serializeToJson());
			jsonobject.add("output", this.output.serializeToJson());
			return jsonobject;
		}
	}
}