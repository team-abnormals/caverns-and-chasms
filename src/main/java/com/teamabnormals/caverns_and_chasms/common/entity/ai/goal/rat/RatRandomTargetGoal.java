package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;

import java.util.function.Predicate;

public class RatRandomTargetGoal<T extends LivingEntity> extends NonTameRandomTargetGoal<T> {
	private final Rat rat;

	public RatRandomTargetGoal(Rat rat, Class<T> targetType, boolean mustReach, Predicate<LivingEntity> predicate) {
		super(rat, targetType, mustReach, predicate);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return super.canUse() && this.rat.shouldAttack(this.target);
	}

	@Override
	public void start() {
		for (Rat friend : this.rat.getPack()) {
			if (friend != this.rat && friend.shouldAttack(this.target) && friend.getTarget() == null) {
				friend.setTarget(this.target);
			}
		}
		super.start();
	}
}