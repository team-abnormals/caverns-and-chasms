package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class RatLookAtPlayerGoal extends LookAtPlayerGoal {
	private final Rat rat;

	public RatLookAtPlayerGoal(Rat rat, Class<? extends LivingEntity> lookAtType, float lookDistance) {
		super(rat, lookAtType, lookDistance);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return !this.rat.isWounded() && super.canUse();
	}
}