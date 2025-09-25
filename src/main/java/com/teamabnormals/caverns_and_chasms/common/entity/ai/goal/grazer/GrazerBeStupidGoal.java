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
		return this.grazer.getState() == GrazerState.DEFAULT && this.grazer.getNavigation().isDone() && this.grazer.getRandom().nextInt(500) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return this.grazer.getState() == GrazerState.DEFAULT && this.timer > 0;
	}

	@Override
	public void start() {
		this.grazer.level().broadcastEntityEvent(this.grazer, (byte) 4);
		this.timer = this.adjustedTickDelay(200 + this.grazer.getRandom().nextInt(100));
	}

	@Override
	public void stop() {
		this.grazer.level().broadcastEntityEvent(this.grazer, (byte) 5);
	}

	@Override
	public void tick() {
		this.timer--;
	}
}