package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class RatHurtByTargetGoal extends HurtByTargetGoal {
	private final Rat rat;

	public RatHurtByTargetGoal(Rat rat) {
		super(rat);
		this.rat = rat;
	}

	public void start() {
		super.start();
		if (!this.rat.canFightAgainst(this.targetMob)) {
			this.stop();
		}
	}

	@Override
	protected void alertOthers() {
		this.rat.alertOthers(this.targetMob);
	}
}