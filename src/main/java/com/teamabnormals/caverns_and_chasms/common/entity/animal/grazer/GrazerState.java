package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum GrazerState {
	DEFAULT(0, false),
	RUNNING_STILL(1, false),
	RUNNING(2, true),
	BOUNCING(3, true),
	LANDING(4, true),
	WIGGLING(5, true),
	FLIPPING_OVER(6, true),
	SLOWING_DOWN(7, false),
	BEING_STUPID(8, false);

	private static final IntFunction<GrazerState> BY_ID = ByIdMap.continuous(GrazerState::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
	private final byte id;
	private final boolean save;

	GrazerState(int id, boolean save) {
		this.id = (byte) id;
		this.save = save;
	}

	public byte getId() {
		return this.id;
	}

	public boolean getsSaved() {
		return this.save;
	}

	public static GrazerState byId(byte id) {
		return BY_ID.apply(id);
	}
}