package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataProcessor;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.swing.text.html.Option;
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

    public static final TrackedData<Optional<UUID>> CONTROLLED_GOLEM_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
    public static final TrackedData<Boolean> IS_BEING_CONTROLLED = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();
    public static final TrackedData<Integer> FORGET_GOLEM_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> 0).build();
    public static final TrackedData<Optional<BlockPos>> TUNING_FORK_POS = TrackedData.Builder.create(OPTIONAL_POS, () -> Optional.empty()).build();
    public static final TrackedData<Optional<UUID>> TUNING_FORK_TARGET_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
    public static final TrackedData<Optional<UUID>> OWNED_GLARE_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).enableSaving().build();

    public static void registerTrackedData() {
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "controlled_golem_uuid"), CONTROLLED_GOLEM_UUID);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "is_being_controlled"), IS_BEING_CONTROLLED);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "forget_golem_time"), FORGET_GOLEM_TIME);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "tuning_fork_pos"), TUNING_FORK_POS);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "tuning_fork_target_uuid"), TUNING_FORK_TARGET_UUID);
        TrackedDataManager.INSTANCE.registerData(new ResourceLocation(CavernsAndChasms.MOD_ID, "owned_glare_uuid"), OWNED_GLARE_UUID);
    }
}