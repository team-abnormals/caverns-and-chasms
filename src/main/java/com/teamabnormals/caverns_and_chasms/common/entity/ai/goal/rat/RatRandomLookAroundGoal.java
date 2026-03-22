package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class RatRandomLookAroundGoal extends RandomLookAroundGoal {
	private final Rat rat;

	public RatRandomLookAroundGoal(Rat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return (!this.rat.isWounded() || this.rat.getRandom().nextInt(3) == 0) && super.canUse();
	}
}