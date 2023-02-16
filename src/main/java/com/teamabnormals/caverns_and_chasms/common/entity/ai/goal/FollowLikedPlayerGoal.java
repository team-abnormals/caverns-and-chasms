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

	public FollowLikedPlayerGoal(Glare glare, double speed) {
		this.glare = glare;
		this.speedModifier = speed;
	}

	public boolean canUse() {
		if (this.glare.getLikedPlayerUUID() == null) {
			return false;
		} else {
			this.likedPlayer = glare.level.getPlayerByUUID(glare.getLikedPlayerUUID());
			return this.glare.distanceToSqr(this.likedPlayer) >= 64.0D;
		}
	}

	public boolean canContinueToUse() {
		if (this.glare.getLikedPlayerUUID() == null) {
			return false;
		} else if (!this.likedPlayer.isAlive()) {
			return false;
		} else {
			double distance = this.glare.distanceToSqr(this.likedPlayer);
			return distance >= 64.0D;
		}
	}

	public void start() {
		this.timeToRecalcPath = 0;
	}

	public void stop() {
		this.likedPlayer = null;
	}

	public void tick() {
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			this.glare.getNavigation().moveTo(this.likedPlayer, this.speedModifier);
		}
	}
}