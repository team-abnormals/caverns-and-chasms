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
			} else {
				return this.rat.distanceToSqr(livingentity) <= 16.0D && this.rat.onGround() && this.rat.getRandom().nextInt(reducedTickDelay(5)) == 0;
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (this.rat.isVehicle() || this.rat.isAttachedToEntity()) {
			return false;
		} else {
			LivingEntity livingentity = this.rat.getTarget();
			if (livingentity == null || !livingentity.isAlive()) {
				return false;
			} else if (!((RatHolder) livingentity).canHoldMoreRats()) {
				return false;
			} else {
				return !this.rat.onGround();
			}
		}
	}

	@Override
	public void start() {
		LivingEntity livingentity = this.rat.getTarget();
		Vec3 vec3 = this.rat.getDeltaMovement();
		Vec3 vec31 = new Vec3(livingentity.getX() - this.rat.getX(), 0.0D, livingentity.getZ() - this.rat.getZ());
		if (vec31.lengthSqr() > 1.0E-7D) {
			vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
		}

		this.rat.setDeltaMovement(vec31.x, 0.4D, vec31.z);
	}

	@Override
	public void tick() {
		LivingEntity livingentity = this.rat.getTarget();
		if (this.rat.getBoundingBox().inflate(0.2D).intersects(livingentity.getBoundingBox())) {
			List<Integer> availableslots = Lists.newArrayList(-1, 0, 1);
			for (Rat attachedrat : ((RatHolder) livingentity).getAttachedRats())
				availableslots.remove(Integer.valueOf(Math.round(attachedrat.getFirstPersonPos())));

			this.rat.attachToEntity(livingentity);
			this.rat.setAttachAngle(this.rat.getRandom().nextFloat() * 360.0F);
			this.rat.setAttachHeight((0.25F + this.rat.getRandom().nextFloat() * Math.max(livingentity.getEyeHeight() - 0.5F, 0.0F)) / livingentity.getBbHeight());

			int i = (availableslots.isEmpty() ? this.rat.getRandom().nextInt(3) - 1 : availableslots.get(this.rat.getRandom().nextInt(availableslots.size())));
			this.rat.setFirstPersonPos(i + (this.rat.getRandom().nextFloat() - 0.5F) * 0.6F);
		}
	}
}