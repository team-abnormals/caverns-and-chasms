package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.common.advancement.OpenStorageDuctTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class OpenStorageDuctTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	static final ResourceLocation ID = CavernsAndChasms.location("open_storage_duct");

	public ResourceLocation getId() {
		return ID;
	}

	public OpenStorageDuctTrigger.TriggerInstance createInstance(JsonObject p_286442_, ContextAwarePredicate p_286426_, DeserializationContext p_286750_) {
		MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(p_286442_.get("length"));
		return new OpenStorageDuctTrigger.TriggerInstance(p_286426_, minmaxbounds$ints);
	}

	public void trigger(ServerPlayer p_60113_, int p_60115_) {
		this.trigger(p_60113_, (p_60126_) -> p_60126_.matches(p_60115_));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints length;

		public TriggerInstance(ContextAwarePredicate p_286866_, MinMaxBounds.Ints p_286225_) {
			super(OpenStorageDuctTrigger.ID, p_286866_);
			this.length = p_286225_;
		}

		public static OpenStorageDuctTrigger.TriggerInstance openStorageDuct(MinMaxBounds.Ints p_286700_) {
			return new OpenStorageDuctTrigger.TriggerInstance(ContextAwarePredicate.ANY, p_286700_);
		}

		public boolean matches(int p_60145_) {
			return this.length.matches(p_60145_);
		}

		public JsonObject serializeToJson(SerializationContext p_60152_) {
			JsonObject jsonobject = super.serializeToJson(p_60152_);
			jsonobject.add("length", this.length.serializeToJson());
			return jsonobject;
		}
	}
}