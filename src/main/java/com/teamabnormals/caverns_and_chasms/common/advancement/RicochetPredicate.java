package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record RicochetPredicate(MinMaxBounds.Ints ricochets) implements EntitySubPredicate {
	public static final MapCodec<RicochetPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
			.group(MinMaxBounds.Ints.CODEC.optionalFieldOf("ricochets", MinMaxBounds.Ints.ANY).forGetter(RicochetPredicate::ricochets))
			.apply(instance, RicochetPredicate::new)
	);

	public static RicochetPredicate ricochets(MinMaxBounds.Ints ricochets) {
		return new RicochetPredicate(ricochets);
	}

	@Override
	public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
		return entity instanceof IDataManager data && this.ricochets.matches(data.getValue(CCDataProcessors.RICOCHETS));
	}

	@Override
	public MapCodec<RicochetPredicate> codec() {
		return CCCriteriaTriggers.RICOCHETS.get();
	}
}