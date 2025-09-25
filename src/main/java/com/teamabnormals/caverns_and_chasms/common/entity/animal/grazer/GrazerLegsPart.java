package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

public class GrazerLegsPart extends GrazerPart {
	private final double yOffsetTucked;

	public GrazerLegsPart(AbstractGrazer parent, float size, double zOffset, double yOffset, double yOffsetTucked) {
		super(parent, size, zOffset, yOffset);
		this.yOffsetTucked = yOffsetTucked / 16D;
	}

	@Override
	protected double getYOffset() {
		AbstractGrazer grazer = this.getParent();
		return grazer.isBouncingState(grazer.getState()) ? this.yOffsetTucked : this.yOffset;
	}

	@Override
	public boolean deflectsAttacks() {
		AbstractGrazer grazer = this.getParent();
		return grazer.isBouncingState(grazer.getState());
	}
}