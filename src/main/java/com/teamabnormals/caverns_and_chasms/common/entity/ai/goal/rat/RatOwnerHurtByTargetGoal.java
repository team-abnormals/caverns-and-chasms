package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;

public class RatOwnerHurtByTargetGoal extends OwnerHurtByTargetGoal {
	private final Rat rat;

	public RatOwnerHurtByTargetGoal(Rat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.isLastHurtByNewerThanCommand() && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return this.isLastHurtByNewerThanCommand() && super.canContinueToUse();
	}

	public boolean isLastHurtByNewerThanCommand() {
		LivingEntity owner = this.rat.getOwner();
		return owner != null && this.rat.getCommandedTargetOwnerTimestamp() < owner.getLastHurtByMobTimestamp();
	}
}