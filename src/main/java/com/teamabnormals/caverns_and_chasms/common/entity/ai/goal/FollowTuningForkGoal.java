package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal;

import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import java.util.EnumSet;

public class FollowTuningForkGoal extends Goal {
    private final ControllableGolem golem;
    private final PathNavigation navigation;

    public FollowTuningForkGoal(ControllableGolem golemIn) {
        this.golem = golemIn;
        this.navigation = ((Mob) golem).getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return golem.getTuningForkTargetPos() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !navigation.isDone() && golem.getTuningForkTargetPos() == null;
    }

    @Override
    public void start() {
        BlockPos pos = golem.getTuningForkTargetPos();
        navigation.moveTo(navigation.createPath(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0), 1.0D);
        golem.setTuningForkTargetPos(null);
    }

    @Override
    public void stop() {
        navigation.stop();
    }
}