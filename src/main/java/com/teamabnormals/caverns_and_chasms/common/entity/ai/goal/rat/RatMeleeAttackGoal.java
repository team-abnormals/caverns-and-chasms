package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class RatMeleeAttackGoal extends MeleeAttackGoal {
	public RatMeleeAttackGoal(Rat rat, double speed, boolean followingTargetEvenIfNotSeen) {
		super(rat, speed, followingTargetEvenIfNotSeen);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity target, double distance) {
		if (((RatHolder) target).getMaxRats() == 0)
			super.checkAndPerformAttack(target, distance);
	}
}