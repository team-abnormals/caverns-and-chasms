package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSpinelBoomMessage {
	public float posX;
	public float posY;
	public float posZ;
	public float strength;

	public S2CSpinelBoomMessage(float x, float y, float z, float strength) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.strength = strength;
	}

	public static S2CSpinelBoomMessage deserialize(FriendlyByteBuf buf) {
		float posX = buf.readFloat();
		float posY = buf.readFloat();
		float posZ = buf.readFloat();
		float strength = buf.readFloat();
		return new S2CSpinelBoomMessage(posX, posY, posZ, strength);
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeFloat(this.posX);
		buf.writeFloat(this.posY);
		buf.writeFloat(this.posZ);
		buf.writeFloat(this.strength);
	}

	public static void handle(S2CSpinelBoomMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		LocalPlayer player = Minecraft.getInstance().player;
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			SpinelBoom boom = new SpinelBoom(player.getCommandSenderWorld(), null, message.posX, message.posY, message.posZ, message.strength);
			boom.finalizeExplosion(true);
			context.setPacketHandled(true);
		}
	}
}