package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public class RatFollowOwnerGoal extends FollowOwnerGoal {
	private final Rat rat;

	public RatFollowOwnerGoal(Rat rat, double speedModifier, float startDistance, float stopDistance) {
		super(rat, speedModifier, startDistance, stopDistance);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.rat.shouldFollowOwner() && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return this.rat.shouldFollowOwner() && super.canContinueToUse();
	}
}