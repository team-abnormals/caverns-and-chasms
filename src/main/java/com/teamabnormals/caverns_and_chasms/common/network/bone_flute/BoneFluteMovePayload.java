package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BoneFluteMovePayload(BlockPos targetPos) implements CustomPacketPayload, BoneFluteCommandPayload {
	public static final CustomPacketPayload.Type<BoneFluteMovePayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("bone_flute_move"));

	public static final StreamCodec<ByteBuf, BoneFluteMovePayload> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, BoneFluteMovePayload::targetPos,
			BoneFluteMovePayload::new
	);

	public static void handle(BoneFluteMovePayload payload, IPayloadContext context) {
		payload.handleCommand(context);
	}

	@Override
	public void executeCommand(Level level, Player player) {
		for (Rat rat : BoneFluteCommandPayload.getNearbyPets(level, player)) {
			rat.setOrderedToSit(false);
			rat.detachFromEntity();
			rat.setTarget(null);
			rat.setCommandedTarget(null);
			rat.setCommandedPos(this.targetPos);
		}
	}

	@Override
	public SoundEvent getSound() {
		return CCSoundEvents.BONE_FLUTE_MOVE.get();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}