package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerState;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.SaddledGrazer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class C2SGrazerJumpMessage {
	private double bounceHeight;

	public C2SGrazerJumpMessage(double bounceHeight) {
		this.bounceHeight = bounceHeight;
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeDouble(this.bounceHeight);
	}

	public static C2SGrazerJumpMessage deserialize(FriendlyByteBuf buf) {
		return new C2SGrazerJumpMessage(buf.readDouble());
	}

	public static void handle(C2SGrazerJumpMessage message, Supplier<Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null) {
					if (player.getVehicle() instanceof SaddledGrazer grazer) {
						grazer.setBounceHeight(message.bounceHeight);
						grazer.setState(GrazerState.BOUNCING);
					}
				}
			});
			context.setPacketHandled(true);
		}
	}
}