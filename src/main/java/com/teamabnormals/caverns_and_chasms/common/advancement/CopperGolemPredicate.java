package com.teamabnormals.caverns_and_chasms.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.decoration.OxidizedCopperGolem;
import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record CopperGolemPredicate(MinMaxBounds.Ints oxidation, boolean waxed) implements EntitySubPredicate {
	public static final MapCodec<CopperGolemPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					MinMaxBounds.Ints.CODEC.optionalFieldOf("oxidation", MinMaxBounds.Ints.ANY).forGetter(CopperGolemPredicate::oxidation),
					Codec.BOOL.optionalFieldOf("waxed", false).forGetter(CopperGolemPredicate::waxed)
			).apply(instance, CopperGolemPredicate::new)
	);

	public static CopperGolemPredicate copperGolem(MinMaxBounds.Ints oxidation, boolean waxed) {
		return new CopperGolemPredicate(oxidation, waxed);
	}

	@Override
	public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
		if (entity instanceof CopperGolem copperGolem) {
			return this.oxidation.matches(copperGolem.getOxidation().getId()) && this.waxed == copperGolem.isWaxed();
		} else if (entity instanceof OxidizedCopperGolem copperGolem) {
			return this.oxidation.matches(3) && this.waxed == copperGolem.isWaxed();
		} else {
			return false;
		}
	}

	@Override
	public MapCodec<CopperGolemPredicate> codec() {
		return CCCriteriaTriggers.COPPER_GOLEM.get();
	}
}