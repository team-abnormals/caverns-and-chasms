package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.GrazerState;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrazerRollGoal extends Goal {
	private final Grazer grazer;

	public GrazerRollGoal(Grazer grazer) {
		this.grazer = grazer;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		return this.grazer.getState() == GrazerState.ROLLING || this.grazer.getState() == GrazerState.WIGGLING;
	}

	@Override
	public void start() {
		this.grazer.getNavigation().stop();
		this.grazer.setSpeed(0.0F);
	}

	@Override
	public void tick() {
		if (this.grazer.getState() == GrazerState.WIGGLING) {
			float xrot = this.grazer.getXRot();
			if (xrot < 0F)
				this.grazer.setXRot(Math.min(xrot + 5F, 0F));
			else if (xrot > 0F)
				this.grazer.setXRot(Math.max(xrot - 5F, 0F));
			else
				this.grazer.setState(GrazerState.DEFAULT);
		}
	}
}