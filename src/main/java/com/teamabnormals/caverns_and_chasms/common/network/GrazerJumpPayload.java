package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.SaddledGrazer;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GrazerJumpPayload(double jumpForce, float angle) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<GrazerJumpPayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("grazer_jump"));

	public static final StreamCodec<ByteBuf, GrazerJumpPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, GrazerJumpPayload::jumpForce,
			ByteBufCodecs.FLOAT, GrazerJumpPayload::angle,
			GrazerJumpPayload::new
	);

	// TODO: Maybe this shouldn't directly make the grazer jump?
	public static void handle(GrazerJumpPayload payload, IPayloadContext context) {
		if (context.connection().getDirection().isServerbound()) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player.getVehicle() instanceof SaddledGrazer grazer && grazer.canExecuteJump()) {
					float angle = Math.max(-payload.angle(), 0.0F);
					double xMotion = Mth.cos(angle * Mth.DEG_TO_RAD);
					double yMotion = Mth.sin(angle * Mth.DEG_TO_RAD);

					grazer.setBounceHeight(payload.jumpForce() * (0.7D + yMotion * 0.9D));
					grazer.setBouncingBackwards(false);
					grazer.setState(GrazerState.BOUNCING);
					grazer.setDeltaMovement(new Vec3(0.0D, 0.0D, payload.jumpForce() * xMotion * 0.6D).yRot(-grazer.getYRot() * Mth.DEG_TO_RAD));
				}
			});
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}