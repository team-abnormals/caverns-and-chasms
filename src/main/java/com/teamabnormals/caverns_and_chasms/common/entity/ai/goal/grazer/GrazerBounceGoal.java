package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.GrazerState;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrazerBounceGoal extends Goal {
	private final Grazer grazer;
	private int wiggleTime;

	public GrazerBounceGoal(Grazer grazer) {
		this.grazer = grazer;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		return this.grazer.getState() == GrazerState.BOUNCING || this.grazer.getState() == GrazerState.LANDING || this.grazer.getState() == GrazerState.WIGGLING || this.grazer.getState() == GrazerState.FLIPPING_OVER;
	}

	@Override
	public void start() {
		this.grazer.getNavigation().stop();
		this.grazer.setSpeed(0.0F);
		this.wiggleTime = 0;
	}

	@Override
	public void stop() {
		this.grazer.setTarget(null);
	}

	@Override
	public void tick() {
		if (this.grazer.getState() == GrazerState.WIGGLING && ++this.wiggleTime >= this.adjustedTickDelay(80)) {
			this.grazer.setState(GrazerState.FLIPPING_OVER);
			if (this.grazer.onGround())
				this.grazer.getJumpControl().jump();
		}
	}
}