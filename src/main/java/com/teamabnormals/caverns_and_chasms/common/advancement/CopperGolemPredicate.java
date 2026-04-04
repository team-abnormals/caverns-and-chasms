package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.decoration.OxidizedCopperGolem;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CopperGolemPredicate implements EntitySubPredicate {
	private final MinMaxBounds.Ints oxidation;
	private final boolean waxed;

	private CopperGolemPredicate(MinMaxBounds.Ints oxidation, boolean waxed) {
		this.oxidation = oxidation;
		this.waxed = waxed;
	}

	public static CopperGolemPredicate copperGolem(MinMaxBounds.Ints oxidation, boolean waxed) {
		return new CopperGolemPredicate(oxidation, waxed);
	}

	public static CopperGolemPredicate fromJson(JsonObject json) {
		MinMaxBounds.Ints oxidation = MinMaxBounds.Ints.fromJson(json.get("oxidation"));
		boolean waxed = GsonHelper.convertToBoolean(json.get("waxed"), "waxed");
		return new CopperGolemPredicate(oxidation, waxed);
	}

	@Override
	public JsonElement serialize() {
		JsonObject json = this.serializeCustomData();
		json.addProperty("type", "copper_golem");
		return json;
	}

	@Override
	public JsonObject serializeCustomData() {
		JsonObject json = new JsonObject();
		json.add("oxidation", this.oxidation.serializeToJson());
		json.add("waxed", new JsonPrimitive(this.waxed));
		return json;
	}

	@Override
	public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 vec3) {
		if (entity instanceof CopperGolem copperGolem) {
			return this.oxidation.matches(copperGolem.getOxidation().getId()) && this.waxed == copperGolem.isWaxed();
		} else if (entity instanceof OxidizedCopperGolem copperGolem) {
			return this.oxidation.matches(3) && this.waxed == copperGolem.isWaxed();
		} else {
			return false;
		}
	}

	public EntitySubPredicate.Type type() {
		return CCCriteriaTriggers.COPPER_GOLEM;
	}
}