package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class RatFollowOwnerGoal extends FollowOwnerGoal {
	private final Rat rat;
	private final double speedModifier;
	private final PathNavigation navigation;
	private LivingEntity owner;
	private int timeToRecalcPath;

	public RatFollowOwnerGoal(Rat rat, double speedModifier, float startDistance, float stopDistance) {
		super(rat, speedModifier, startDistance, stopDistance, false);
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.navigation = rat.getNavigation();
	}

	@Override
	public boolean canUse() {
		return this.rat.getCommandedPos() == null && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return this.rat.getCommandedPos() == null && super.canContinueToUse();
	}
}