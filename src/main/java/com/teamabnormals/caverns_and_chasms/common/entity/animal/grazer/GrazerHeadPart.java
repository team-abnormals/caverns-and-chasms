package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;

public class GrazerHeadPart extends GrazerPart {
	protected final EntityDimensions babyDimensions;
	private final double zOffsetBaby;
	private final double yOffsetBaby;

	public GrazerHeadPart(AbstractGrazer parent, float size, float babySize, double zOffset, double yOffset, double zOffsetBaby, double yOffsetBaby) {
		super(parent, size, zOffset, yOffset);
		float f = babySize / 16F;
		this.babyDimensions = EntityDimensions.scalable(f, f);
		this.zOffsetBaby = zOffsetBaby / 16D;
		this.yOffsetBaby = yOffsetBaby / 16D;
	}

	@Override
	protected double getYOffset() {
		return this.getParent().isBaby() ? this.yOffsetBaby : this.yOffset;
	}

	@Override
	protected double getZOffset() {
		return this.getParent().isBaby() ? this.zOffsetBaby : this.zOffset;
	}

	@Override
	public boolean deflectsAttacks() {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		EntityDimensions dimensions = this.getParent().isBaby() ? this.babyDimensions : this.dimensions;
		return dimensions.scale(this.getScale());
	}
}