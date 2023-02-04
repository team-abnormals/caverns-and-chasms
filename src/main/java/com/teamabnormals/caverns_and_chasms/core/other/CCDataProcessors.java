package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataProcessor;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

public class CCDataProcessors {

    public static final IDataProcessor<Optional<UUID>> OPTIONAL_UUID = new IDataProcessor<>() {
        @Override
        public CompoundTag write(Optional<UUID> optionalUUID) {
            CompoundTag compound = new CompoundTag();
            if (optionalUUID.isPresent())
                compound.putUUID("OptionalUUID", optionalUUID.get());
            return compound;
        }

        @Override
        public Optional<UUID> read(CompoundTag nbt) {
            return nbt.hasUUID("OptionalUUID") ? Optional.of(nbt.getUUID("OptionalUUID")) : Optional.empty();
        }
    };
    public static final IDataProcessor<Optional<BlockPos>> OPTIONAL_POS = new IDataProcessor<>() {
        @Override
        public CompoundTag write(Optional<BlockPos> optionalPos) {
            CompoundTag compound = new CompoundTag();
            if (optionalPos.isPresent())
                compound.putLong("OptionalPos", optionalPos.get().asLong());
            return compound;
        }

        @Override
        public Optional<BlockPos> read(CompoundTag nbt) {
            try {
                return nbt.contains("OptionalPos", 99) ? Optional.of(BlockPos.of(nbt.getLong("OptionalPos"))) : Optional.empty();
            } catch (ClassCastException classcastexception) {
            }
            return Optional.empty();
        }
    };

    public static final TrackedData<Optional<UUID>> CONTROLLER_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
    public static final TrackedData<Integer> FORGET_CONTROLLER_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> 0).build();
    public static final TrackedData<Optional<BlockPos>> TUNING_FORK_TARGET_POS = TrackedData.Builder.create(OPTIONAL_POS, () -> Optional.empty()).build();

    public static void registerTrackedData() {
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "controller_uuid"), CONTROLLER_UUID);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "forget_controller_time"), FORGET_CONTROLLER_TIME);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "tuning_fork_target_pos"), TUNING_FORK_TARGET_POS);
    }
}