package com.teamabnormals.caverns_and_chasms.common.block;

import net.minecraft.util.StringRepresentable;

public enum IngotLayer implements StringRepresentable {
	LEFT("left"),
	RIGHT("right"),
	BOTH("both");

	private final String name;

	private IngotLayer(String p_61824_) {
		this.name = p_61824_;
	}

	public String toString() {
		return this.getSerializedName();
	}

	public String getSerializedName() {
		return this.name;
	}
}