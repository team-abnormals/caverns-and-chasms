package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum GrazerState {
	DEFAULT(0),
	RUNNING_STILL(1),
	RUNNING(2),
	BOUNCING(3),
	LANDING(4),
	WIGGLING(5),
	FLIPPING_OVER(6);

	private static final IntFunction<GrazerState> BY_ID = ByIdMap.continuous(GrazerState::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
	private final int id;

	GrazerState(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static GrazerState byId(int id) {
		return BY_ID.apply(id);
	}
}