package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RicochetPredicate implements EntitySubPredicate {
	private final MinMaxBounds.Ints ricochets;

	private RicochetPredicate(MinMaxBounds.Ints p_223420_) {
		this.ricochets = p_223420_;
	}

	public static RicochetPredicate ricochets(MinMaxBounds.Ints p_223427_) {
		return new RicochetPredicate(p_223427_);
	}

	public static RicochetPredicate fromJson(JsonObject p_223429_) {
		MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(p_223429_.get("count"));
		return new RicochetPredicate(minmaxbounds$ints);
	}

	@Override
	public JsonElement serialize() {
		JsonObject jsonobject = this.serializeCustomData();
		jsonobject.addProperty("type", "ricochets");
		return jsonobject;
	}

	public JsonObject serializeCustomData() {
		JsonObject jsonobject = new JsonObject();
		jsonobject.add("count", this.ricochets.serializeToJson());
		return jsonobject;
	}

	public boolean matches(Entity p_223423_, ServerLevel p_223424_, @Nullable Vec3 p_223425_) {
		if (p_223423_ instanceof IDataManager data) {
			return this.ricochets.matches(data.getValue(CCDataProcessors.RICOCHETS));
		} else {
			return false;
		}
	}

	public EntitySubPredicate.Type type() {
		return CCCriteriaTriggers.RICOCHETS;
	}
}