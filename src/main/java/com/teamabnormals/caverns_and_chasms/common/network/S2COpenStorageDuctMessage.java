package com.teamabnormals.caverns_and_chasms.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class S2COpenStorageDuctMessage {
	private final int windowId;
	private final int containerSize;
	private final BlockPos blockPos;

	public S2COpenStorageDuctMessage(int windowId, int containerSize, BlockPos blockPos) {
		this.windowId = windowId;
		this.containerSize = containerSize;
		this.blockPos = blockPos;
	}

	public static void serialize(S2COpenStorageDuctMessage message, FriendlyByteBuf buffer) {
		buffer.writeVarInt(message.windowId);
		buffer.writeVarInt(message.containerSize);
		buffer.writeBlockPos(message.blockPos);
	}

	public static S2COpenStorageDuctMessage deserialize(FriendlyByteBuf buffer) {
		return new S2COpenStorageDuctMessage(buffer.readVarInt(), buffer.readVarInt(), buffer.readBlockPos());
	}

	public static void handle(S2COpenStorageDuctMessage message, Supplier<Context> supplier) {
		supplier.get().enqueueWork(() -> ClientNetworkHandler.handleOpenStorageDuct(message));
		supplier.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public int getWindowId() {
		return this.windowId;
	}

	@OnlyIn(Dist.CLIENT)
	public int getContainerSize() {
		return this.containerSize;
	}

	@OnlyIn(Dist.CLIENT)
	public BlockPos getBlockPos() {
		return this.blockPos;
	}
}