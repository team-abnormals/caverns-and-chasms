package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.caverns_and_chasms.common.advancement.PlayerHurtSelfTrigger.TriggerInstance;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerHurtSelfTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	static final ResourceLocation ID = CavernsAndChasms.location("player_hurt_self");

	public ResourceLocation getId() {
		return ID;
	}

	public PlayerHurtSelfTrigger.TriggerInstance createInstance(JsonObject p_286442_, ContextAwarePredicate p_286426_, DeserializationContext p_286750_) {
		DamagePredicate damagepredicate = DamagePredicate.fromJson(p_286442_.get("damage"));
		return new PlayerHurtSelfTrigger.TriggerInstance(p_286426_, damagepredicate);
	}

	public void trigger(ServerPlayer p_60113_, Entity p_60114_, DamageSource p_60115_, float p_60116_, float p_60117_, boolean p_60118_) {
		LootContext lootcontext = EntityPredicate.createContext(p_60113_, p_60114_);
		this.trigger(p_60113_, (p_60126_) -> p_60126_.matches(p_60113_, lootcontext, p_60115_, p_60116_, p_60117_, p_60118_));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final DamagePredicate damage;

		public TriggerInstance(ContextAwarePredicate p_286866_, DamagePredicate p_286225_) {
			super(PlayerHurtSelfTrigger.ID, p_286866_);
			this.damage = p_286225_;
		}

		public static PlayerHurtSelfTrigger.TriggerInstance playerHurtSelf() {
			return new PlayerHurtSelfTrigger.TriggerInstance(ContextAwarePredicate.ANY, DamagePredicate.ANY);
		}

		public static PlayerHurtSelfTrigger.TriggerInstance playerHurtSelf(DamagePredicate p_156062_) {
			return new PlayerHurtSelfTrigger.TriggerInstance(ContextAwarePredicate.ANY, p_156062_);
		}

		public static PlayerHurtSelfTrigger.TriggerInstance playerHurtSelf(DamagePredicate.Builder p_60150_) {
			return new PlayerHurtSelfTrigger.TriggerInstance(ContextAwarePredicate.ANY, p_60150_.build());
		}

		public boolean matches(ServerPlayer p_60143_, LootContext p_60144_, DamageSource p_60145_, float p_60146_, float p_60147_, boolean p_60148_) {
			return this.damage.matches(p_60143_, p_60145_, p_60146_, p_60147_, p_60148_);
		}

		public JsonObject serializeToJson(SerializationContext p_60152_) {
			JsonObject jsonobject = super.serializeToJson(p_60152_);
			jsonobject.add("damage", this.damage.serializeToJson());
			return jsonobject;
		}
	}
}