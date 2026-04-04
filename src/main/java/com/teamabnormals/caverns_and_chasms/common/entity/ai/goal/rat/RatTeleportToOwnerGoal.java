package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class RatTeleportToOwnerGoal extends Goal {
	private final Rat rat;
	private final LevelReader level;
	private LivingEntity owner;

	public RatTeleportToOwnerGoal(Rat rat) {
		this.rat = rat;
		this.level = rat.level();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		LivingEntity owner = this.rat.getOwner();
		if (owner == null) {
			return false;
		} else if (owner.isSpectator()) {
			return false;
		} else if (!this.rat.shouldFollowOwner()) {
			return false;
		} else if (this.rat.isPassenger() || this.rat.isLeashed()) {
			return false;
		} else if (this.rat.distanceToSqr(owner) < 144.0D) {
			return false;
		} else {
			this.owner = owner;
			return true;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		this.teleportToOwner();
	}

	protected void teleportToOwner() {
		BlockPos blockpos = this.owner.blockPosition();

		for (int i = 0; i < 10; ++i) {
			int j = this.randomIntInclusive(-3, 3);
			int k = this.randomIntInclusive(-1, 1);
			int l = this.randomIntInclusive(-3, 3);
			boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			if (flag) {
				return;
			}
		}
	}

	private boolean maybeTeleportTo(int x, int y, int z) {
		if (Math.abs(x - this.owner.getX()) < 2.0D && Math.abs(z - this.owner.getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
			return false;
		} else {
			this.rat.moveTo(x + 0.5D, y, z + 0.5D, this.rat.getYRot(), this.rat.getXRot());
			this.rat.getNavigation().stop();
			return true;
		}
	}

	private boolean canTeleportTo(BlockPos pos) {
		BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pos.mutable());
		if (blockpathtypes != BlockPathTypes.WALKABLE) {
			return false;
		} else {
			BlockState blockstate = this.level.getBlockState(pos.below());
			if (blockstate.getBlock() instanceof LeavesBlock) {
				return false;
			} else {
				BlockPos blockpos = pos.subtract(this.rat.blockPosition());
				return this.level.noCollision(this.rat, this.rat.getBoundingBox().move(blockpos));
			}
		}
	}

	private int randomIntInclusive(int p_25301_, int p_25302_) {
		return this.rat.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
	}
}