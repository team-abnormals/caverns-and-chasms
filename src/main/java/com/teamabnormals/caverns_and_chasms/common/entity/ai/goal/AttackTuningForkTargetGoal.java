package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class AttackTuningForkTargetGoal extends TargetGoal {
	private final ControllableGolem golem;

	public AttackTuningForkTargetGoal(ControllableGolem golemIn) {
		super((Mob) golemIn, false);
		this.golem = golemIn;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (golem.getController() == null) {
			return false;
		} else {
			LivingEntity target = this.golem.getTuningForkTarget();
			if (target != null) {
				return this.canAttack(target, TargetingConditions.DEFAULT);
			} else {
				return false;
			}
		}
	}

	@Override
	public void start() {
		((Mob) this.golem).setTarget(this.golem.getTuningForkTarget());
		this.golem.setTuningForkTarget(null);
		super.start();
	}
}