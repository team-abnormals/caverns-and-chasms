package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BoneFluteSitPayload() implements CustomPacketPayload, BoneFluteCommandPayload {
	public static final CustomPacketPayload.Type<BoneFluteSitPayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("bone_flute_sit"));

	public static final StreamCodec<ByteBuf, BoneFluteSitPayload> STREAM_CODEC = StreamCodec.unit(new BoneFluteSitPayload());

	public static void handle(BoneFluteSitPayload payload, IPayloadContext context) {
		payload.handleCommand(context);
	}

	@Override
	public void executeCommand(Level level, Player player) {
		for (Rat rat : BoneFluteCommandPayload.getNearbyPets(level, player)) {
			rat.setOrderedToSit(true);
			rat.detachFromEntity();
			rat.setTarget(null);
			rat.setCommandedTarget(null);
			rat.setCommandedPos(null);
		}
	}

	@Override
	public SoundEvent getSound() {
		return CCSoundEvents.BONE_FLUTE_SIT.get();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}