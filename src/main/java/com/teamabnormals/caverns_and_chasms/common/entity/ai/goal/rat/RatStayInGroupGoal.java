package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RatStayInGroupGoal extends Goal {
	private final Rat rat;
	private Vec3 groupCenter;
	private int timeToRecalcPath;

	public RatStayInGroupGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.rat.isTame() && !this.rat.isBaby() && this.rat.hasPack()) {
			this.setGroupCenter();
			return this.rat.distanceToSqr(this.groupCenter) > 16.0D;
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		double d0 = this.rat.distanceToSqr(this.groupCenter);
		return !(d0 < 9.0D) && !(d0 > 256.0D);
	}

	@Override
	public void start() {
		this.timeToRecalcPath = 0;
	}

	@Override
	public void stop() {
		if (this.rat.getRandom().nextBoolean()) {
			this.rat.getNavigation().stop();
		}
	}

	@Override
	public void tick() {
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			this.setGroupCenter();
			this.rat.getNavigation().moveTo(this.groupCenter.x, this.groupCenter.y, this.groupCenter.z, 1.0D);
		}
	}

	private void setGroupCenter() {
		this.groupCenter = this.rat.findPackCenter(this.rat.getPack());
	}
}