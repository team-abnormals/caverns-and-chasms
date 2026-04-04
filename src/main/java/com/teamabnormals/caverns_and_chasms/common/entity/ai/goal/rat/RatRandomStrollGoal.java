package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RatRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
	private final Rat rat;

	public RatRandomStrollGoal(Rat rat) {
		super(rat, 1.0D);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return !this.rat.isSittingBecauseOrdered() && (!this.rat.isWounded() || this.rat.getRandom().nextInt(3) == 0) && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.isSittingBecauseOrdered() && super.canContinueToUse();
	}

	@Nullable
	@Override
	protected Vec3 getPosition() {
		if (this.rat.isInWaterOrBubble()) {
			Vec3 vec3 = LandRandomPos.getPos(this.rat, 15, 7);
			return vec3 == null ? super.getPosition() : vec3;
		} else {
			boolean flag = this.rat.isTame() || this.rat.hasPack();
			int max = flag ? 6 : 10;
			int min = flag ? 3 : 7;
			return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, max, min) : DefaultRandomPos.getPos(this.mob, max, min);
		}
	}
}