package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatFollowParentGoal extends FollowParentGoal {
	private final Rat rat;

	public RatFollowParentGoal(Rat rat) {
		super(rat, 1.2D);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return !this.rat.isSittingBecauseOrdered() && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.isSittingBecauseOrdered() && super.canContinueToUse();
	}
}