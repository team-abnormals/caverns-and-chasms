package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.SaddledGrazer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class C2SGrazerJumpMessage {
	private double jumpForce;
	private float angle;

	public C2SGrazerJumpMessage(double jumpForce, float angle) {
		this.jumpForce = jumpForce;
		this.angle = angle;
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeDouble(this.jumpForce);
		buf.writeFloat(this.angle);
	}

	public static C2SGrazerJumpMessage deserialize(FriendlyByteBuf buf) {
		return new C2SGrazerJumpMessage(buf.readDouble(), buf.readFloat());
	}

	// TODO: Maybe this shouldn't directly make the grazer jump?
	public static void handle(C2SGrazerJumpMessage message, Supplier<Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null) {
					if (player.getVehicle() instanceof SaddledGrazer grazer && grazer.canExecuteJump()) {
						float angle = Math.max(-message.angle, 0.0F);
						double xMotion = Mth.cos(angle * Mth.DEG_TO_RAD);
						double yMotion = Mth.sin(angle * Mth.DEG_TO_RAD);
						grazer.setBounceHeight(message.jumpForce * (0.7D + yMotion * 0.9D));
						grazer.setBouncingBackwards(false);
						grazer.setState(GrazerState.BOUNCING);
						grazer.setDeltaMovement(new Vec3(0.0D, 0.0D, message.jumpForce * xMotion * 0.6D).yRot(-grazer.getYRot() * Mth.DEG_TO_RAD));
					}
				}
			});
			context.setPacketHandled(true);
		}
	}
}