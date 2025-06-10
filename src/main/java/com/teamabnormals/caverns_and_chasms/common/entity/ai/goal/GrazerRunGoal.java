package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.GrazerState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;
import java.util.function.Predicate;

public class GrazerRunGoal extends Goal {
	private final Grazer grazer;
	private final double range;
	private final TargetingConditions avoidEntityTargeting;
	private int runStillTime;

	private int cooldown;

	public GrazerRunGoal(Grazer grazer, Predicate<LivingEntity> avoidPredicate, double range) {
		this.grazer = grazer;
		this.range = range;
		this.avoidEntityTargeting = TargetingConditions.forCombat().range(range).selector(avoidPredicate);
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.grazer.getState() == GrazerState.RUNNING_STILL || this.grazer.getState() == GrazerState.RUNNING) {
			return true;
		} else if (this.grazer.getState() != GrazerState.DEFAULT) {
			return false;
		} else if (this.cooldown-- > 0) {
			return false;
		} else if (this.grazer.getLastHurtByMob() != null || this.grazer.isFreezing() || this.grazer.isOnFire() || this.isNearEntityToAvoid()) {
			this.grazer.setState(GrazerState.RUNNING_STILL);
			this.runStillTime = 10;
			return true;
		}

		return false;
	}

	private boolean isNearEntityToAvoid() {
		return this.grazer.level().getNearestEntity(LivingEntity.class, this.avoidEntityTargeting, this.grazer, this.grazer.getX(), this.grazer.getY(), this.grazer.getZ(), this.grazer.getBoundingBox().inflate(this.range, 4.0D, this.range)) != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.grazer.getState() == GrazerState.RUNNING_STILL || this.grazer.getState() == GrazerState.RUNNING;
	}

	@Override
	public void start() {
		this.grazer.getNavigation().stop();
	}

	@Override
	public void stop() {
		if (this.grazer.getState() == GrazerState.RUNNING)
			this.grazer.setState(GrazerState.DEFAULT);
	}

	@Override
	public void tick() {
		if (this.runStillTime > 0) {
			this.runStillTime--;
		} else if (this.grazer.getState() == GrazerState.RUNNING_STILL) {
			this.grazer.setState(GrazerState.RUNNING);
		}
	}
}