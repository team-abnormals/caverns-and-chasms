package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Glare;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class FollowLikedPlayerGoal extends Goal {
	private final Glare glare;
	@Nullable
	private Player likedPlayer;
	private final double speedModifier;
	private int timeToRecalcPath;
	private final double followDistance = 64.0D;

	public FollowLikedPlayerGoal(Glare glare, double speed) {
		this.glare = glare;
		this.speedModifier = speed;
	}

	public boolean canUse() {
		if (this.glare.getOwnerUUID() == null || this.glare.isLeashed()) {
			return false;
		} else {
			this.likedPlayer = glare.level().getPlayerByUUID(glare.getOwnerUUID());
			return this.likedPlayer != null && this.glare.distanceToSqr(this.likedPlayer) >= followDistance;
		}
	}

	public boolean canContinueToUse() {
		if (this.glare.getOwnerUUID() == null) {
			return false;
		} else if (this.likedPlayer == null || !this.likedPlayer.isAlive()) {
			return false;
		} else if (this.glare.isLeashed()) {
			return false;
		} else {
			double distance = this.glare.distanceToSqr(this.likedPlayer);
			if (distance < followDistance)
				this.glare.getNavigation().stop();
			return distance >= followDistance;
		}
	}

	public void start() {
		this.timeToRecalcPath = 0;
	}

	public void stop() {
		this.likedPlayer = null;
	}

	public void tick() {
		if (--this.timeToRecalcPath <= 0 && this.likedPlayer != null) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			this.glare.getNavigation().moveTo(this.likedPlayer, this.speedModifier);
		}
	}
}