package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrazerBeStupidGoal extends Goal {
	private final AbstractGrazer grazer;
	private int timer;

	public GrazerBeStupidGoal(AbstractGrazer grazer) {
		this.grazer = grazer;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return !this.grazer.hasControllingPassenger() && this.grazer.getState() == GrazerState.DEFAULT && this.grazer.getNavigation().isDone() && this.grazer.getRandom().nextInt(500) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.grazer.hasControllingPassenger() && this.grazer.getState() == GrazerState.BEING_STUPID && this.timer > 0;
	}

	@Override
	public void start() {
		this.grazer.setState(GrazerState.BEING_STUPID);
		this.timer = this.adjustedTickDelay(200 + this.grazer.getRandom().nextInt(100));
	}

	@Override
	public void stop() {
		if (this.grazer.getState() == GrazerState.BEING_STUPID)
			this.grazer.setState(GrazerState.DEFAULT);
	}

	@Override
	public void tick() {
		this.timer--;
	}
}