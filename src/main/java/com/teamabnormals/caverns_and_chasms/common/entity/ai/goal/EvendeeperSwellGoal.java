package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Evendeeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class EvendeeperSwellGoal extends Goal {
	private final Evendeeper evendeeper;
	private LivingEntity target;

	public EvendeeperSwellGoal(Evendeeper evendeeper) {
		this.evendeeper = evendeeper;
	}

	public boolean canUse() {
		LivingEntity livingentity = this.evendeeper.getTarget();
		return this.evendeeper.getSwellDir() > 0 || livingentity != null && this.evendeeper.distanceToSqr(livingentity) < 9.0D;
	}

	public void start() {
		this.target = this.evendeeper.getTarget();
	}

	public void stop() {
		this.target = null;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		if (this.target == null) {
			this.evendeeper.setSwellDir(-1);
		} else if (this.evendeeper.distanceToSqr(this.target) > 49.0D) {
			this.evendeeper.setSwellDir(-1);
		} else if (!this.evendeeper.getSensing().hasLineOfSight(this.target)) {
			this.evendeeper.setSwellDir(-1);
		} else {
			this.evendeeper.setSwellDir(1);
		}
	}
}