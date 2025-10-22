package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataProcessor;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder.AttachedRat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
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
	public static final IDataProcessor<List<AttachedRat>> ATTACHED_RAT_DATA_LIST = new IDataProcessor<>() {
		@Override
		public CompoundTag write(List<AttachedRat> list) {
			CompoundTag compound = new CompoundTag();
			ListTag listtag = new ListTag();
			for (AttachedRat data : list) {
				listtag.add(data.save());
			}
			compound.put("Entries", listtag);
			return compound;
		}

		@Override
		public List<AttachedRat> read(CompoundTag nbt) {
			List<AttachedRat> list = Lists.newArrayList();
			nbt.getList("Entries", 10).forEach(tag -> list.add(AttachedRat.load(((CompoundTag) tag))));
			return list;
		}
	};

	public static final TrackedData<Optional<UUID>> CONTROLLED_GOLEM_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
	public static final TrackedData<Boolean> IS_BEING_CONTROLLED = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();
	public static final TrackedData<Integer> FORGET_GOLEM_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> 0).build();
	public static final TrackedData<Optional<BlockPos>> TUNING_FORK_POS = TrackedData.Builder.create(OPTIONAL_POS, () -> Optional.empty()).build();
	public static final TrackedData<Optional<UUID>> TUNING_FORK_TARGET_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
	public static final TrackedData<ResourceLocation> REWIND_DIMENSION = TrackedData.Builder.create(DataProcessors.RESOURCE_LOCATION, () -> Level.OVERWORLD.location()).enableSaving().build();
	public static final TrackedData<Double> REWIND_X = TrackedData.Builder.create(DataProcessors.DOUBLE, () -> 0.0D).enableSaving().build();
	public static final TrackedData<Double> REWIND_Y = TrackedData.Builder.create(DataProcessors.DOUBLE, () -> 0.0D).enableSaving().build();
	public static final TrackedData<Double> REWIND_Z = TrackedData.Builder.create(DataProcessors.DOUBLE, () -> 0.0D).enableSaving().build();
	public static final TrackedData<Boolean> SHOULD_DEFLECT = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().build();
	public static final TrackedData<Double> DEFLECT_X = TrackedData.Builder.create(DataProcessors.DOUBLE, () -> 0.0D).enableSaving().build();
	public static final TrackedData<Double> DEFLECT_Y = TrackedData.Builder.create(DataProcessors.DOUBLE, () -> 0.0D).enableSaving().build();
	public static final TrackedData<Double> DEFLECT_Z = TrackedData.Builder.create(DataProcessors.DOUBLE, () -> 0.0D).enableSaving().build();
	public static final TrackedData<List<AttachedRat>> ATTACHED_RATS = TrackedData.Builder.create(ATTACHED_RAT_DATA_LIST, ArrayList::new).enableSaving().build();
	public static final TrackedData<ItemStack> UNICORN_HORN = TrackedData.Builder.create(DataProcessors.STACK, () -> ItemStack.EMPTY).enableSaving().build();
	public static final TrackedData<Boolean> OBSCURITY_INVISIBILITY = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().build();

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("controlled_golem_uuid"), CONTROLLED_GOLEM_UUID);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("is_being_controlled"), IS_BEING_CONTROLLED);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("forget_golem_time"), FORGET_GOLEM_TIME);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("tuning_fork_pos"), TUNING_FORK_POS);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("tuning_fork_target_uuid"), TUNING_FORK_TARGET_UUID);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("rewind_dimension"), REWIND_DIMENSION);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("rewind_x"), REWIND_X);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("rewind_y"), REWIND_Y);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("rewind_z"), REWIND_Z);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("should_deflect"), SHOULD_DEFLECT);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("deflect_x"), DEFLECT_X);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("deflect_y"), DEFLECT_Y);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("deflect_z"), DEFLECT_Z);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("attached_rats"), ATTACHED_RATS);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("unicorn_horn"), UNICORN_HORN);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("obscurity_invisibility"), OBSCURITY_INVISIBILITY);
	}
}