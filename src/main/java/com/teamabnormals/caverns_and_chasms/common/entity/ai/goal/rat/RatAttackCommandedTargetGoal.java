package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class RatAttackCommandedTargetGoal extends TargetGoal {
	private final Rat rat;

	public RatAttackCommandedTargetGoal(Rat rat) {
		super(rat, false);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isTame() && !this.rat.isOrderedToSit()) {
			LivingEntity owner = this.rat.getOwner();
			if (owner == null) {
				return false;
			} else {
				LivingEntity commandedTarget = this.rat.getCommandedTarget();
				return commandedTarget != null && this.canAttack(commandedTarget, TargetingConditions.DEFAULT) && this.rat.wantsToAttack(commandedTarget, owner);
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.targetMob == this.rat.getCommandedTarget() && super.canContinueToUse();
	}

	@Override
	public void start() {
		LivingEntity target = this.rat.getCommandedTarget();
		this.rat.setTarget(target);
		this.targetMob = target;
		super.start();
	}
}