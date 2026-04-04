package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.common.advancement.HoopTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class HoopTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	static final ResourceLocation ID = CavernsAndChasms.location("hoop_entered");

	public ResourceLocation getId() {
		return ID;
	}

	public HoopTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
		MinMaxBounds.Ints hoopSize = MinMaxBounds.Ints.fromJson(json.get("hoop_size"));
		MinMaxBounds.Ints signalStrength = MinMaxBounds.Ints.fromJson(json.get("signal_strength"));
		ContextAwarePredicate projectile = EntityPredicate.fromJson(json, "projectile", context);
		return new HoopTrigger.TriggerInstance(player, hoopSize, signalStrength, projectile);
	}

	public void trigger(ServerPlayer player, Entity projectile, int hoopSize, int signalStrength) {
		LootContext projectileContext = EntityPredicate.createContext(player, projectile);
		this.trigger(player, (instance) -> instance.matches(projectileContext, hoopSize, signalStrength));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints hoopSize;
		private final MinMaxBounds.Ints signalStrength;
		private final ContextAwarePredicate projectile;

		public TriggerInstance(ContextAwarePredicate player, MinMaxBounds.Ints hoopSize, MinMaxBounds.Ints signalStrength, ContextAwarePredicate projectile) {
			super(HoopTrigger.ID, player);
			this.hoopSize = hoopSize;
			this.signalStrength = signalStrength;
			this.projectile = projectile;
		}

		public static HoopTrigger.TriggerInstance hoopEntered(MinMaxBounds.Ints hoopSize, MinMaxBounds.Ints signalStrength, ContextAwarePredicate projectile) {
			return new HoopTrigger.TriggerInstance(ContextAwarePredicate.ANY, hoopSize, signalStrength, projectile);
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			json.add("hoop_size", this.hoopSize.serializeToJson());
			json.add("signal_strength", this.signalStrength.serializeToJson());
			json.add("projectile", this.projectile.toJson(context));
			return json;
		}

		public boolean matches(LootContext projectileContext, int hoopSize, int signalStrength) {
			if (!this.hoopSize.matches(hoopSize) && !this.signalStrength.matches(signalStrength)) {
				return false;
			} else {
				return this.projectile.matches(projectileContext);
			}
		}
	}
}