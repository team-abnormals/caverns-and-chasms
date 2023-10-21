package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.MovingPlayer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PeeperSwellGoal extends Goal {
	private final Peeper peeper;
	@Nullable
	private LivingEntity target;

	public PeeperSwellGoal(Peeper peeper) {
		this.peeper = peeper;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	public boolean canUse() {
		LivingEntity livingentity = this.peeper.getTarget();
		return this.peeper.getSwellDir() >= 0 || livingentity != null && this.peeper.distanceToSqr(livingentity) < 9.0D;
	}

	public void start() {
		this.peeper.getNavigation().stop();
		this.target = this.peeper.getTarget();
	}

	public void stop() {
		this.target = null;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		if (this.target == null) {
			this.peeper.setSwellDir(-1);
		} else if (this.peeper.distanceToSqr(this.target) > 49.0D) {
			this.peeper.setSwellDir(-1);
		} else if (!this.peeper.getSensing().hasLineOfSight(this.target)) {
			this.peeper.setSwellDir(-1);
		} else if (this.peeper.distanceToSqr(this.target) >= 9.0D && this.target instanceof MovingPlayer player && player.isMoving()) {
			this.peeper.setSwellDir(-1);
		} else if (this.target instanceof MovingPlayer player && !player.isMoving()) {
			this.peeper.setSwellDir(0);
		} else {
			if (this.peeper.getSwellDir() == 0) {
				this.peeper.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
			}
			this.peeper.setSwellDir(1);
		}
	}
}