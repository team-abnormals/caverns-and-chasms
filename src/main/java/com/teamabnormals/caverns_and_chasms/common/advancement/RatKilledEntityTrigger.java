package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.common.advancement.RatKilledEntityTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class RatKilledEntityTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	static final ResourceLocation ID = CavernsAndChasms.location("rat_killed_entity");

	public ResourceLocation getId() {
		return ID;
	}

	public RatKilledEntityTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate playerPredicate, DeserializationContext context) {
		return new RatKilledEntityTrigger.TriggerInstance(playerPredicate, EntityPredicate.fromJson(json, "rat", context), EntityPredicate.fromJson(json, "entity", context), DamageSourcePredicate.fromJson(json.get("killing_blow")));
	}

	public void trigger(ServerPlayer player, Rat rat, Entity target, DamageSource damage) {
		LootContext ratContext = EntityPredicate.createContext(player, rat);
		LootContext targetContext = EntityPredicate.createContext(player, target);
		this.trigger(player, (instance) -> instance.matches(player, ratContext, targetContext, damage));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ContextAwarePredicate ratPredicate;
		private final ContextAwarePredicate targetPredicate;
		private final DamageSourcePredicate killingBlow;

		public TriggerInstance(ContextAwarePredicate playerPredicate, ContextAwarePredicate ratPredicate, ContextAwarePredicate targetPredicate, DamageSourcePredicate damagePredicate) {
			super(ID, playerPredicate);
			this.ratPredicate = ratPredicate;
			this.targetPredicate = targetPredicate;
			this.killingBlow = damagePredicate;
		}

		public static RatKilledEntityTrigger.TriggerInstance ratKilledEntity(EntityPredicate targetPredicate) {
			return new RatKilledEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, EntityPredicate.wrap(targetPredicate), DamageSourcePredicate.ANY);
		}

		public static RatKilledEntityTrigger.TriggerInstance ratKilledEntity(EntityPredicate.Builder targetBuilder) {
			return new RatKilledEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, EntityPredicate.wrap(targetBuilder.build()), DamageSourcePredicate.ANY);
		}

		public boolean matches(ServerPlayer player, LootContext ratContext, LootContext targetContext, DamageSource source) {
			return this.killingBlow.matches(player, source) && this.ratPredicate.matches(ratContext) && this.targetPredicate.matches(targetContext);
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			json.add("rat", this.ratPredicate.toJson(context));
			json.add("entity", this.targetPredicate.toJson(context));
			json.add("killing_blow", this.killingBlow.serializeToJson());
			return json;
		}
	}
}