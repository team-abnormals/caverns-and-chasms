package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.SaddledGrazer;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;
import java.util.function.Predicate;

public class GrazerRunGoal extends Goal {
	private final AbstractGrazer grazer;
	private final double range;
	private final TargetingConditions avoidEntityTargeting;
	private int runStillTime;

	private int cooldown;

	public GrazerRunGoal(AbstractGrazer grazer, Predicate<LivingEntity> avoidPredicate, double range) {
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
		}

		LivingEntity attacker = this.grazer.getLastHurtByMob();
		if (attacker == null && !(this.grazer instanceof SaddledGrazer))
			attacker = findNearestScaryEntity();

		if (attacker != null || this.grazer.isFreezing() || this.grazer.isOnFire()) {
			if (attacker != null && !(this.grazer instanceof SaddledGrazer))
				this.grazer.setTarget(attacker);
			this.grazer.setState(GrazerState.RUNNING_STILL);
			this.runStillTime = 10;
			return true;
		}

		return false;
	}

	private LivingEntity findNearestScaryEntity() {
		return this.grazer.level().getNearestEntity(LivingEntity.class, this.avoidEntityTargeting, this.grazer, this.grazer.getX(), this.grazer.getY(), this.grazer.getZ(), this.grazer.getBoundingBox().inflate(this.range, 4.0D, this.range));
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
		if (this.grazer.getState() == GrazerState.RUNNING) {
			this.grazer.setState(GrazerState.DEFAULT);
			this.grazer.setTarget(null);
		}
	}

	@Override
	public void tick() {
		if (this.runStillTime > 0) {
			this.runStillTime--;
		} else if (this.grazer.getState() == GrazerState.RUNNING_STILL) {
			this.grazer.setState(GrazerState.RUNNING);
			this.grazer.playSound(CCSoundEvents.GRAZER_CHARGE.get(), 1.0F, 1.0F);
		}
	}
}