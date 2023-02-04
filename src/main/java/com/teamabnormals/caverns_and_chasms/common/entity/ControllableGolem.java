package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public interface ControllableGolem {

    boolean canBeControlled(Player controller);

    void onTuningForkControl(Player controller);

    boolean shouldMoveToTuningForkPos(BlockPos pos, Player controller);

    default void moveToTuningForkPos(BlockPos pos) {
        this.setTuningForkTargetPos(pos);
    }

    boolean shouldAttackTuningForkTarget(LivingEntity target, Player controller);

    void attackTuningForkTarget(LivingEntity target);

    @Nullable
    default BlockPos getTuningForkTargetPos() {
        IDataManager data = (IDataManager) this;
        if (data.getValue(CCDataProcessors.TUNING_FORK_TARGET_POS).isPresent()) {
            return data.getValue(CCDataProcessors.TUNING_FORK_TARGET_POS).get();
        }
        return null;
    }

    default void setTuningForkTargetPos(BlockPos pos) {
        IDataManager data = (IDataManager) this;
        data.setValue(CCDataProcessors.TUNING_FORK_TARGET_POS, pos != null ? Optional.of(pos) : Optional.empty());
    }

    static Player getController(ControllableGolem golem) {
        IDataManager data = ((IDataManager) golem);
        if (data.getValue(CCDataProcessors.CONTROLLER_UUID).isPresent()) {
            UUID uuid = data.getValue(CCDataProcessors.CONTROLLER_UUID).get();
            Player player = ((LivingEntity) golem).level.getPlayerByUUID(uuid);
            if (golem.canBeControlled(player))
                return ((LivingEntity) golem).level.getPlayerByUUID(uuid);
        }
        return null;
    }
}
