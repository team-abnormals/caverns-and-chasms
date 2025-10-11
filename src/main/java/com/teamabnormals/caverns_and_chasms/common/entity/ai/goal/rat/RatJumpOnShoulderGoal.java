package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatJumpOnShoulderGoal extends Goal {
	private final Rat rat;
	private ServerPlayer owner;

	public RatJumpOnShoulderGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return !this.rat.isOrderedToSit() && this.rat.canSitOnShoulder() && this.shouldJumpOnShoulder();
	}

	@Override
	public void start() {
		this.owner = (ServerPlayer) this.rat.getOwner();
	}

	@Override
	public void stop() {
		this.owner = null;
	}

	@Override
	public void tick() {
		if (!this.rat.isInSittingPose() && !this.rat.isLeashed() && this.rat.getBoundingBox().intersects(this.owner.getBoundingBox())) {
			this.rat.setEntityOnShoulder(this.owner);
		} else {
			this.rat.getLookControl().setLookAt(this.owner, 10.0F, this.rat.getMaxHeadXRot());
			this.rat.getNavigation().moveTo(this.owner, 1.0D);
		}
	}

	private boolean shouldJumpOnShoulder() {
		ServerPlayer owner = (ServerPlayer) this.rat.getOwner();
		return owner != null && owner.isCrouching() && !owner.isSpectator() && !owner.getAbilities().flying && !owner.isInWater() && !owner.isInPowderSnow;
	}
}