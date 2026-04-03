package com.teamabnormals.caverns_and_chasms.core.other;

import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class CCDataProcessors {
	public static final StreamCodec<ByteBuf, Optional<UUID>> OPTIONAL_UUID = new StreamCodec<>() {
		public Optional<UUID> decode(ByteBuf buf) {
			return Optional.ofNullable(FriendlyByteBuf.readUUID(buf));
		}

		public void encode(ByteBuf buf, Optional<UUID> optional) {
			FriendlyByteBuf.writeUUID(buf, optional.orElse(null));
		}
	};

	public static final StreamCodec<ByteBuf, Optional<BlockPos>> OPTIONAL_BLOCK_POS = new StreamCodec<>() {
		public Optional<BlockPos> decode(ByteBuf buf) {
			return Optional.ofNullable(FriendlyByteBuf.readBlockPos(buf));
		}

		public void encode(ByteBuf buf, Optional<BlockPos> optional) {
			FriendlyByteBuf.writeBlockPos(buf, optional.orElse(null));
		}
	};

	public static final TrackedData<Optional<UUID>> CONTROLLED_GOLEM_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
	public static final TrackedData<Boolean> IS_BEING_CONTROLLED = TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).build();
	public static final TrackedData<Integer> FORGET_GOLEM_TIME = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).build();
	public static final TrackedData<Optional<BlockPos>> TUNING_FORK_POS = TrackedData.Builder.create(OPTIONAL_BLOCK_POS, () -> Optional.empty()).build();
	public static final TrackedData<Optional<UUID>> TUNING_FORK_TARGET_UUID = TrackedData.Builder.create(OPTIONAL_UUID, () -> Optional.empty()).build();
	public static final TrackedData<ResourceLocation> REWIND_DIMENSION = TrackedData.Builder.create(ResourceLocation.STREAM_CODEC, () -> Level.OVERWORLD.location()).enableSaving(ResourceLocation.CODEC.fieldOf("ResourceLocation")).build();
	//TODO: Convert into one tracked Vec3?
	public static final TrackedData<Double> REWIND_X = TrackedData.Builder.create(ByteBufCodecs.DOUBLE, () -> 0.0D).enableSaving(Codec.DOUBLE.fieldOf("Double")).build();
	public static final TrackedData<Double> REWIND_Y = TrackedData.Builder.create(ByteBufCodecs.DOUBLE, () -> 0.0D).enableSaving(Codec.DOUBLE.fieldOf("Double")).build();
	public static final TrackedData<Double> REWIND_Z = TrackedData.Builder.create(ByteBufCodecs.DOUBLE, () -> 0.0D).enableSaving(Codec.DOUBLE.fieldOf("Double")).build();
	public static final TrackedData<Boolean> SHOULD_DEFLECT = TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).enableSaving(Codec.BOOL.fieldOf("Boolean")).build();
	public static final TrackedData<Boolean> BONUS_DEFLECT = TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).enableSaving(Codec.BOOL.fieldOf("Boolean")).build();
	public static final TrackedData<Integer> RICOCHETS = TrackedData.Builder.create(ByteBufCodecs.INT, () -> 0).enableSaving(Codec.INT.fieldOf("Integer")).build();
	//TODO: Convert into one tracked Vec3?
	public static final TrackedData<Double> DEFLECT_X = TrackedData.Builder.create(ByteBufCodecs.DOUBLE, () -> 0.0D).enableSaving(Codec.DOUBLE.fieldOf("Double")).build();
	public static final TrackedData<Double> DEFLECT_Y = TrackedData.Builder.create(ByteBufCodecs.DOUBLE, () -> 0.0D).enableSaving(Codec.DOUBLE.fieldOf("Double")).build();
	public static final TrackedData<Double> DEFLECT_Z = TrackedData.Builder.create(ByteBufCodecs.DOUBLE, () -> 0.0D).enableSaving(Codec.DOUBLE.fieldOf("Double")).build();
	public static final TrackedData<ItemStack> UNICORN_HORN = TrackedData.Builder.create(ItemStack.OPTIONAL_STREAM_CODEC, () -> ItemStack.EMPTY).enableSaving(ItemStack.OPTIONAL_CODEC.fieldOf("id").fieldOf("count").fieldOf("components")).build();
	public static final TrackedData<Boolean> GLOW_UNICORN_HORN = TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).enableSaving(Codec.BOOL.fieldOf("Boolean")).build();
	public static final TrackedData<Boolean> OBSCURITY_INVISIBILITY = TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).enableSaving(Codec.BOOL.fieldOf("Boolean")).build();

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
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("bonus_deflect"), BONUS_DEFLECT);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("ricochets"), RICOCHETS);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("deflect_x"), DEFLECT_X);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("deflect_y"), DEFLECT_Y);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("deflect_z"), DEFLECT_Z);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("unicorn_horn"), UNICORN_HORN);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("glow_unicorn_horn"), GLOW_UNICORN_HORN);
		TrackedDataManager.INSTANCE.registerData(CavernsAndChasms.location("obscurity_invisibility"), OBSCURITY_INVISIBILITY);
	}
}