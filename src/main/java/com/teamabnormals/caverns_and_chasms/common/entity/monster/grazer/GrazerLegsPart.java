package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

public class GrazerLegsPart extends GrazerPart {
	private final double yOffsetTucked;

	public GrazerLegsPart(Grazer parent, float size, double zOffset, double yOffset, double yOffsetTucked) {
		super(parent, size, zOffset, yOffset);
		this.yOffsetTucked = yOffsetTucked / 16D;
	}

	@Override
	protected double getYOffset() {
		Grazer grazer = this.getParent();
		return grazer.isBouncingState(grazer.getState()) ? this.yOffsetTucked : this.yOffset;
	}

	public boolean deflectsDamage() {
		Grazer grazer = this.getParent();
		return grazer.isBouncingState(grazer.getState());
	}
}