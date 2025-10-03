package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public enum RatType {
	BLUE(0),
	GRAY(1),
	BROWN(2),
	WHITE(3);

	private static final RatType[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(RatType::getId)).toArray(RatType[]::new);

	private final int id;
	private final LazyLoadedValue<ResourceLocation> textureLocation = new LazyLoadedValue<>(() -> CavernsAndChasms.location("textures/entity/rat/rat_" + this.name().toLowerCase(Locale.ROOT) + ".png"));

	RatType(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public ResourceLocation getTextureLocation() {
		return this.textureLocation.get();
	}

	public static RatType byId(int id) {
		if (id < 0 || id >= VALUES.length) {
			id = 0;
		}
		return VALUES[id];
	}
}