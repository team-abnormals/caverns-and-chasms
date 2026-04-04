package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class RatBreedGoal extends BreedGoal {
	private final Rat rat;

	public RatBreedGoal(Rat rat, double speed) {
		super(rat, speed);
		this.rat = rat;
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