package com.teamabnormals.caverns_and_chasms.common.network;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SpinelBoomPayload(float posX, float posY, float posZ, float strength, List<BlockPos> affectedBlockPositions) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SpinelBoomPayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("spinel_boom"));

	public static final StreamCodec<ByteBuf, SpinelBoomPayload> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public SpinelBoomPayload decode(ByteBuf buf) {
			float posX = buf.readFloat();
			float posY = buf.readFloat();
			float posZ = buf.readFloat();
			float strength = buf.readFloat();

			int size = buf.readInt();
			List<BlockPos> positions = Lists.newArrayListWithCapacity(size);

			for (int i = 0; i < size; i++) {
				int x = buf.readByte() + Mth.floor(posX);
				int y = buf.readByte() + Mth.floor(posY);
				int z = buf.readByte() + Mth.floor(posZ);
				positions.add(new BlockPos(x, y, z));
			}

			return new SpinelBoomPayload(posX, posY, posZ, strength, positions);
		}

		@Override
		public void encode(ByteBuf buf, SpinelBoomPayload payload) {
			buf.writeFloat(payload.posX());
			buf.writeFloat(payload.posY());
			buf.writeFloat(payload.posZ());
			buf.writeFloat(payload.strength());

			buf.writeInt(payload.affectedBlockPositions().size());

			for (BlockPos pos : payload.affectedBlockPositions()) {
				int x = pos.getX() - Mth.floor(payload.posX());
				int y = pos.getY() - Mth.floor(payload.posY());
				int z = pos.getZ() - Mth.floor(payload.posZ());

				buf.writeByte(x);
				buf.writeByte(y);
				buf.writeByte(z);
			}
		}
	};

	public static void handle(SpinelBoomPayload payload, IPayloadContext context) {
		if (context.connection().getDirection().isClientbound()) {
			context.enqueueWork(() -> {
				LocalPlayer player = Minecraft.getInstance().player;
				if (player == null) return;
				SpinelBoom boom = new SpinelBoom(player.level(), null, payload.posX(), payload.posY(), payload.posZ(), payload.strength());
				boom.finalizeExplosion(true);
			});
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}