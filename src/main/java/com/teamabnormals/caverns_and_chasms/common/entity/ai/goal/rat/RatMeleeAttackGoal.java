package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class RatMeleeAttackGoal extends MeleeAttackGoal {
	private final Rat rat;

	public RatMeleeAttackGoal(Rat rat, double speed, boolean followingTargetEvenIfNotSeen) {
		super(rat, speed, followingTargetEvenIfNotSeen);
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

	@Override
	protected void checkAndPerformAttack(LivingEntity target, double distanceSqr) {
		if (((RatHolder) target).getMaxRats() == 0) {
			super.checkAndPerformAttack(target, distanceSqr);
		} else if (this.rat.isFloatingInWater() && !this.rat.isAttachedToEntity() && !this.rat.isOnAttachCooldown() && distanceSqr < this.getAttackReachSqr(target)) {
			this.rat.tryToAttachToEntity(target);
		}
	}
}