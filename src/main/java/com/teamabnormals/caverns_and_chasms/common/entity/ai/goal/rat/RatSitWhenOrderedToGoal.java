package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.Goal;

public class RatSitWhenOrderedToGoal extends Goal {
	private final Rat rat;

	public RatSitWhenOrderedToGoal(Rat rat) {
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.rat.isTame() && this.rat.isOrderedToSit() && this.rat.canSit();
	}

	@Override
	public void start() {
		this.rat.getNavigation().stop();
		this.rat.setSittingBecauseOrdered(true);
	}

	@Override
	public void stop() {
		this.rat.setSittingBecauseOrdered(false);
	}
}