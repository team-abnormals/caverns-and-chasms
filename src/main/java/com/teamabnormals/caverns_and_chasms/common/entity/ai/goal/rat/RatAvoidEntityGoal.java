package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

import java.util.function.Predicate;

public class RatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
	private final Rat rat;

	public RatAvoidEntityGoal(Rat rat, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> predicate) {
		super(rat, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, predicate);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.rat.shouldRunAway() && super.canUse();
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