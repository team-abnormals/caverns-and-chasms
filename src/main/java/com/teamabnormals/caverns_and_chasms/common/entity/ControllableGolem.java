package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

public interface ControllableGolem {

    default boolean canBeTuningForkControlled(Player player) { return true; }

    default void onTuningForkControlStart(Player player) {}

    default void onTuningForkControlEnd(Player player) {}

    default boolean shouldMoveToTuningForkPos(BlockPos pos, Player player) { return true; }

    default boolean shouldAttackTuningForkTarget(LivingEntity target, Player player) { return false; }

    default void setBeingTuningForkControlled(boolean controlled) {
        IDataManager data = (IDataManager) this;
        data.setValue(CCDataProcessors.IS_BEING_CONTROLLED, controlled);
    }

    default boolean isBeingTuningForkControlled() {
        return ((IDataManager) this).getValue(CCDataProcessors.IS_BEING_CONTROLLED);
    }

    default void setTuningForkPos(BlockPos pos) {
        ((IDataManager) this).setValue(CCDataProcessors.TUNING_FORK_POS, pos != null ? Optional.of(pos) : Optional.empty());
    }

    default BlockPos getTuningForkPos() {
        IDataManager data = (IDataManager) this;
        if (data.getValue(CCDataProcessors.TUNING_FORK_POS).isPresent()) {
            return data.getValue(CCDataProcessors.TUNING_FORK_POS).get();
        }
        return null;
    }

    default void setTuningForkTarget(LivingEntity target) {
        ((IDataManager) this).setValue(CCDataProcessors.TUNING_FORK_TARGET_UUID, target != null ? Optional.of(target.getUUID()) : Optional.empty());
    }

    default boolean isTuningForkTarget(LivingEntity target) {
        IDataManager data = (IDataManager) this;
        if (data.getValue(CCDataProcessors.TUNING_FORK_TARGET_UUID).isPresent()) {
            return target.getUUID() == data.getValue(CCDataProcessors.TUNING_FORK_TARGET_UUID).get();
        }
        return false;
    }

    default Player getTuningForkController() {
        for (Player player : ((Mob) this).level.players()) {
            if (this.isTuningForkControlledBy(player)) {
                return player;
            }
        }
        return null;
    }

    default boolean isTuningForkControlledBy(Player player) {
        Optional<UUID> uuid = ((IDataManager) player).getValue(CCDataProcessors.CONTROLLED_GOLEM_UUID);
        return uuid.isPresent() && uuid.get() == ((Mob) this).getUUID();
    }
}
