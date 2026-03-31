package com.teamabnormals.caverns_and_chasms.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class S2CPushPlayerMessage {
	private final float x;
	private final float y;
	private final float z;

	public S2CPushPlayerMessage(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static S2CPushPlayerMessage deserialize(FriendlyByteBuf buf) {
		return new S2CPushPlayerMessage(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeFloat(this.x);
		buf.writeFloat(this.y);
		buf.writeFloat(this.z);
	}

	public static void handle(S2CPushPlayerMessage message, Supplier<Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				LocalPlayer player = Minecraft.getInstance().player;
				player.move(MoverType.PLAYER, new Vec3(message.x, message.y, message.z));
			});
		}
		context.setPacketHandled(true);
	}
}