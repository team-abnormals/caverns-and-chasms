package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RatAttachToTargetGoal extends Goal {
	private final Rat rat;
	private LivingEntity target;

	public RatAttachToTargetGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isVehicle()) {
			return false;
		} else {
			this.target = this.rat.getTarget();
			if (this.target == null || !this.target.isAlive()) {
				return false;
			} else if (!((RatHolder) this.target).canHoldMoreRats()) {
				return false;
			} else {
				double d0 = this.rat.distanceToSqr(this.target);
				if (d0 <= 16.0D) {
					if (!this.rat.onGround()) {
						return false;
					} else {
						return this.rat.getRandom().nextInt(reducedTickDelay(5)) == 0;
					}
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.isVehicle() && !this.rat.onGround() && this.target != null && this.target.isAlive() && ((RatHolder) this.target).canHoldMoreRats();
	}

	@Override
	public void start() {
		Vec3 vec3 = this.rat.getDeltaMovement();
		Vec3 vec31 = new Vec3(this.target.getX() - this.rat.getX(), 0.0D, this.target.getZ() - this.rat.getZ());
		if (vec31.lengthSqr() > 1.0E-7D) {
			vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
		}

		this.rat.setDeltaMovement(vec31.x, 0.4D, vec31.z);
	}

	@Override
	public void tick() {
		if (this.rat.getBoundingBox().inflate(0.2D).intersects(this.target.getBoundingBox()))
			((RatHolder) this.target).attachRat(this.rat);
	}
}