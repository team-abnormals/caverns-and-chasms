package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class RatFloatGoal extends FloatGoal {
	private final Rat rat;

	public RatFloatGoal(Rat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public void start() {
		super.start();
		this.rat.setFloatingInWater(true);
	}

	@Override
	public void stop() {
		super.stop();
		this.rat.setFloatingInWater(false);
	}
}