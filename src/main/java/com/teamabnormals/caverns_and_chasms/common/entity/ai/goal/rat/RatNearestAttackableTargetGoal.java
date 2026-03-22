package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import java.util.function.Predicate;

public class RatNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
	private final Rat rat;

	public RatNearestAttackableTargetGoal(Rat rat, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, Predicate<LivingEntity> predicate) {
		super(rat, targetType, randomInterval, mustSee, mustReach, predicate);
		this.rat = rat;
	}

	public boolean canUse() {
		return this.rat.hasBraveryToFight() && super.canUse();
	}
}