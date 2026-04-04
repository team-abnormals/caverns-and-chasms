package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class RatAvoidEntityGoal extends AvoidEntityGoal<LivingEntity> {
	private final Rat rat;

	public RatAvoidEntityGoal(Rat rat, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
		super(rat, LivingEntity.class, maxDist, walkSpeedModifier, sprintSpeedModifier, rat::isScaredOf);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return !this.rat.isSittingBecauseOrdered() && !this.rat.hasBraveryToFight() && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.isSittingBecauseOrdered() && super.canContinueToUse();
	}

	@Override
	public void start() {
		super.start();
		this.rat.setRunningAway(true);
	}

	@Override
	public void stop() {
		super.stop();
		this.rat.setRunningAway(false);
	}
}