package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatAttachedToMobGoal extends Goal {
	private final Rat rat;
	private int biteTimer;

	public RatAttachedToMobGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.rat.isAttachedToEntity();
	}

	@Override
	public void start() {
		this.biteTimer = this.adjustedTickDelay(this.rat.getRandom().nextInt(30));
	}

	@Override
	public void tick() {
		if (this.biteTimer-- <= 0) {
			this.biteTimer = this.adjustedTickDelay(20 + this.rat.getRandom().nextInt(10));
			this.rat.doHurtTarget(this.rat.getAttachedEntity());
			this.rat.playSound(CCSoundEvents.RAT_ATTACK.get(), 1.0F, 1.0F);
		}
	}
}