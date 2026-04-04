package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;

public class RatOwnerHurtTargetGoal extends OwnerHurtTargetGoal {
	private final Rat rat;

	public RatOwnerHurtTargetGoal(Rat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.isLastHurtNewerThanCommand() && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return this.isLastHurtNewerThanCommand() && super.canContinueToUse();
	}

	public boolean isLastHurtNewerThanCommand() {
		LivingEntity owner = this.rat.getOwner();
		return owner != null && this.rat.getCommandedTargetOwnerTimestamp() < owner.getLastHurtMobTimestamp();
	}
}