package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum GrazerRunPhase {
	DEFAULT(0),
	RUNNING(1),
	ROLLING(2),
	WIGGLING(3);

	private static final IntFunction<GrazerRunPhase> BY_ID = ByIdMap.continuous(GrazerRunPhase::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
	private final int id;

	GrazerRunPhase(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static GrazerRunPhase byId(int id) {
		return BY_ID.apply(id);
	}
}