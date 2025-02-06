package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.common.advancement.AtonedItemTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class AtonedItemTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation(CavernsAndChasms.MOD_ID, "atoned_item");

	public ResourceLocation getId() {
		return ID;
	}

	public AtonedItemTrigger.TriggerInstance createInstance(JsonObject p_286526_, ContextAwarePredicate p_286279_, DeserializationContext p_286881_) {
		ItemPredicate itempredicate = ItemPredicate.fromJson(p_286526_.get("item"));
		MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(p_286526_.get("levels"));
		return new AtonedItemTrigger.TriggerInstance(p_286279_, itempredicate, minmaxbounds$ints);
	}

	public void trigger(ServerPlayer p_27669_, ItemStack p_27670_, int p_27671_) {
		this.trigger(p_27669_, (p_27675_) -> {
			return p_27675_.matches(p_27670_, p_27671_);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate item;
		private final MinMaxBounds.Ints levels;

		public TriggerInstance(ContextAwarePredicate p_286871_, ItemPredicate p_286640_, MinMaxBounds.Ints p_286367_) {
			super(AtonedItemTrigger.ID, p_286871_);
			this.item = p_286640_;
			this.levels = p_286367_;
		}

		public static AtonedItemTrigger.TriggerInstance atonedItem() {
			return new AtonedItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.ANY, MinMaxBounds.Ints.ANY);
		}

		public static AtonedItemTrigger.TriggerInstance brokenAtonement() {
			return new AtonedItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(ItemStack.EMPTY.getItem()).build(), MinMaxBounds.Ints.ANY);
		}

		public boolean matches(ItemStack p_27692_, int p_27693_) {
			if (!this.item.matches(p_27692_)) {
				return false;
			} else {
				return this.levels.matches(p_27693_);
			}
		}

		public JsonObject serializeToJson(SerializationContext p_27695_) {
			JsonObject jsonobject = super.serializeToJson(p_27695_);
			jsonobject.add("item", this.item.serializeToJson());
			jsonobject.add("levels", this.levels.serializeToJson());
			return jsonobject;
		}
	}
}