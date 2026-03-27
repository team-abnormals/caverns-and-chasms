package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrazerBounceGoal extends Goal {
	private final AbstractGrazer grazer;
	private int wiggleTime;

	public GrazerBounceGoal(AbstractGrazer grazer) {
		this.grazer = grazer;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		GrazerState state = this.grazer.getState();
		return this.grazer.isBouncingState(state) || state == GrazerState.WIGGLING || state == GrazerState.FLIPPING_OVER;
	}

	@Override
	public void start() {
		this.grazer.getNavigation().stop();
		this.grazer.setSpeed(0.0F);
		this.wiggleTime = this.adjustedTickDelay(this.grazer.hasControllingPassenger() ? 20 : 80);
		this.grazer.setLastHurtByMob(null);
	}

	@Override
	public void stop() {
		this.grazer.setTarget(null);
	}

	@Override
	public void tick() {
		if (this.grazer.getState() == GrazerState.WIGGLING) {
			if (--this.wiggleTime <= 0) {
				this.grazer.setState(GrazerState.FLIPPING_OVER);
				if (this.grazer.onGround())
					this.grazer.getJumpControl().jump();
			}

			if (this.wiggleTime % 20 == 0)
				this.grazer.playSound(CCSoundEvents.GRAZER_STRUGGLE.get());
		}
	}
}