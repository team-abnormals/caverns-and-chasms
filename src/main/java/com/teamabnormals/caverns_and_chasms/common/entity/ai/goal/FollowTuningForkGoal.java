package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import java.util.EnumSet;

public class FollowTuningForkGoal extends Goal {
	private final ControllableGolem golem;
	private final PathNavigation navigation;
	private final double speedModifier;

	public FollowTuningForkGoal(ControllableGolem golemIn, double speedModifierIn) {
		this.golem = golemIn;
		this.navigation = ((Mob) golem).getNavigation();
		this.speedModifier = speedModifierIn;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return golem.getController() != null && golem.getTuningForkPos() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return !navigation.isDone() && golem.getTuningForkPos() == null;
	}

	@Override
	public void start() {
		BlockPos pos = golem.getTuningForkPos();
		navigation.moveTo(navigation.createPath(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0), this.speedModifier);
		golem.setTuningForkPos(null);
	}

	@Override
	public void stop() {
		navigation.stop();
	}
}