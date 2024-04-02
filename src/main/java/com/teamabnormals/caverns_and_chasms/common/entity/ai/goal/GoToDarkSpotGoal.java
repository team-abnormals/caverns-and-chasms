package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Glare;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GoToDarkSpotGoal extends WaterAvoidingRandomFlyingGoal {

	public GoToDarkSpotGoal(PathfinderMob mob, double p_25982_) {
		super(mob, p_25982_);
	}

	@Nullable
	@Override
	protected Vec3 getPosition() {
		for (int i = 0; i < 32; i++) {
			Vec3 vec3 = this.getModifiedPosition();
			if (vec3 != null) {
				BlockPos pos = BlockPos.containing(vec3);
				if (!GoalUtils.isSolid(this.mob, pos) && !GoalUtils.isSolid(this.mob, pos.above()) && Glare.shouldBeGrumpy(this.mob.level(), pos)) {
					return vec3;
				}
			}
		}

		return this.getModifiedPosition();
	}

	protected Vec3 getModifiedPosition() {
		Vec3 vec3 = this.mob.getViewVector(0.0F);
		Vec3 vec31 = HoverRandomPos.getPos(this.mob, 8, 7, vec3.x, vec3.z, ((float) Math.PI / 2F), 1, 0);
		return vec31 != null ? vec31 : AirAndWaterRandomPos.getPos(this.mob, 8, 4, -12, vec3.x, vec3.z, (float) Math.PI / 2F);
	}
}