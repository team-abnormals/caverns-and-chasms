package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatStopAttackingGoal extends Goal {
	private final Rat rat;

	public RatStopAttackingGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		return this.rat.getTarget() != null && !this.rat.hasBraveryToFight();
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		this.rat.setTarget(null);
	}
}