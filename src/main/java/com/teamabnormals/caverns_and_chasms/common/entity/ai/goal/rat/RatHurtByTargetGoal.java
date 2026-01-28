package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class RatHurtByTargetGoal extends HurtByTargetGoal {
	private final Rat rat;

	public RatHurtByTargetGoal(Rat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.rat.shouldAttack(this.rat.getLastHurtByMob()) && super.canUse();
	}

	public void start() {
		super.start();
	}

	@Override
	protected void alertOther(Mob mob, LivingEntity target) {
		if (mob instanceof Rat && ((Rat) mob).shouldAttack(this.targetMob)) {
			super.alertOther(mob, target);
		}
	}
}