package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class RatJumpAtTargetGoal extends Goal {
	private final Rat rat;
	private LivingEntity target;

	public RatJumpAtTargetGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isOnAttachCooldown() || this.rat.isVehicle() || this.rat.isAttachedToEntity()) {
			return false;
		} else {
			LivingEntity livingentity = this.rat.getTarget();
			if (livingentity == null || !livingentity.isAlive()) {
				return false;
			} else if (!((RatHolder) livingentity).canHoldMoreRats()) {
				return false;
			} else if (this.rat.distanceToSqr(livingentity) <= 16.0D && this.rat.onGround() && this.rat.getRandom().nextInt(reducedTickDelay(5)) == 0) {
				this.target = livingentity;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.rat.isVehicle() || this.rat.isAttachedToEntity()) {
			return false;
		} else {
			if (this.target == null || !this.target.isAlive() || this.target != this.rat.getTarget()) {
				return false;
			} else if (!((RatHolder) this.target).canHoldMoreRats()) {
				return false;
			} else {
				return !this.rat.onGround();
			}
		}
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
		if (this.rat.getBoundingBox().inflate(0.2D).intersects(this.target.getBoundingBox())) {
			List<Integer> availableslots = Lists.newArrayList(-1, 0, 1);
			for (Rat attachedrat : ((RatHolder) this.target).getAttachedRats())
				availableslots.remove(Integer.valueOf(Math.round(attachedrat.getFirstPersonPos())));

			this.rat.attachToEntity(this.target);
			this.rat.setAttachAngle(this.rat.getRandom().nextFloat() * 360.0F);
			this.rat.setAttachHeight((0.25F + this.rat.getRandom().nextFloat() * Math.max(this.target.getEyeHeight() - 0.5F, 0.0F)) / this.target.getBbHeight());

			int i = (availableslots.isEmpty() ? this.rat.getRandom().nextInt(3) - 1 : availableslots.get(this.rat.getRandom().nextInt(availableslots.size())));
			this.rat.setFirstPersonPos(i + (this.rat.getRandom().nextFloat() - 0.5F) * 0.6F);
		}
	}
}