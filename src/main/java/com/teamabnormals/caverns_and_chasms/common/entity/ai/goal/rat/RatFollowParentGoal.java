package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatFollowParentGoal extends FollowParentGoal {
	public RatFollowParentGoal(Rat rat) {
		super(rat, 1.2D);
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}
}