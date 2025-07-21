package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GrazerBeBabyGoal extends Goal {
	private final Grazer grazer;

	public GrazerBeBabyGoal(Grazer grazer) {
		this.grazer = grazer;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return this.grazer.isBaby();
	}
}