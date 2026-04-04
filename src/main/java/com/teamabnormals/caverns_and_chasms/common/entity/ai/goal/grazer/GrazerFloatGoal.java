package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class GrazerFloatGoal extends FloatGoal {
	private final AbstractGrazer grazer;

	public GrazerFloatGoal(AbstractGrazer grazer) {
		super(grazer);
		this.grazer = grazer;
	}

	@Override
	public void start() {
		super.start();

		if (this.grazer.isBouncingState(this.grazer.getState()) || this.grazer.getState() == GrazerState.WIGGLING) {
			this.grazer.setState(GrazerState.FLIPPING_OVER);
		}
	}
}