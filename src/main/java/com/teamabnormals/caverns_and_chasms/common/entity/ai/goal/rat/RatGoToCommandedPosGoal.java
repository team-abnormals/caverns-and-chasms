package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatGoToCommandedPosGoal extends Goal {
	private final Rat rat;
	private final double speedModifier;
	private int timeToRecalcPath;

	public RatGoToCommandedPosGoal(Rat rat, double speedModifier) {
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isSittingBecauseOrdered()) {
			return false;
		}

		BlockPos commandedPos = this.rat.getCommandedPos();
		return commandedPos != null && this.rat.distanceToSqr(commandedPos.getCenter()) > 36.0D;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.isSittingBecauseOrdered() && super.canContinueToUse();
	}

	@Override
	public void start() {
		this.timeToRecalcPath = 0;
	}

	@Override
	public void tick() {
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			BlockPos commandedPos = this.rat.getCommandedPos();
			if (commandedPos != null) {
				this.rat.getNavigation().moveTo(commandedPos.getX() + 0.5D, commandedPos.getY(), commandedPos.getZ() + 0.5D, this.speedModifier);
			}
		}
	}
}