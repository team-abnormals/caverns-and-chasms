package com.teamabnormals.caverns_and_chasms.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class S2CMovingDoorSoundMessage {
	private BlockPos pos;
	private float volume;

	public S2CMovingDoorSoundMessage(BlockPos pos, float volume) {
		this.pos = pos;
		this.volume = volume;
	}

	public static S2CMovingDoorSoundMessage deserialize(FriendlyByteBuf buf) {
		return new S2CMovingDoorSoundMessage(buf.readBlockPos(), buf.readFloat());
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos);
		buf.writeFloat(this.volume);
	}

	public static void handle(S2CMovingDoorSoundMessage message, Supplier<Context> supplier) {
		supplier.get().enqueueWork(() -> ClientNetworkHandler.handleRollerDoorSound(message));
		supplier.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public BlockPos getPos() {
		return this.pos;
	}

	@OnlyIn(Dist.CLIENT)
	public float getVolume() {
		return this.volume;
	}
}