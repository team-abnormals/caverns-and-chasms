package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenStorageDuctPayload(int windowId, int containerSize, BlockPos blockPos) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<OpenStorageDuctPayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("open_storage_duct"));

	public static final StreamCodec<ByteBuf, OpenStorageDuctPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, OpenStorageDuctPayload::windowId,
			ByteBufCodecs.VAR_INT, OpenStorageDuctPayload::containerSize,
			BlockPos.STREAM_CODEC, OpenStorageDuctPayload::blockPos,
			OpenStorageDuctPayload::new
	);

	public static void handle(OpenStorageDuctPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> ClientNetworkHandler.handleOpenStorageDuct(payload));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}