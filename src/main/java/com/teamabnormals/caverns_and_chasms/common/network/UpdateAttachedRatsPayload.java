package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record UpdateAttachedRatsPayload(int entityId, List<Integer> ratIds) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<UpdateAttachedRatsPayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("update_attached_rats"));

	public static final StreamCodec<ByteBuf, UpdateAttachedRatsPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, UpdateAttachedRatsPayload::entityId,
			ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), UpdateAttachedRatsPayload::ratIds,
			UpdateAttachedRatsPayload::new
	);

	public UpdateAttachedRatsPayload(RatHolder entity) {
		this(((LivingEntity) entity).getId(), entity.getAttachedRats().stream().map(Rat::getId).toList());
	}

	public static void handle(UpdateAttachedRatsPayload payload, IPayloadContext context) {
		if (context.connection().getDirection().isClientbound()) {
			context.enqueueWork(() -> {
				Level level = ClientInfo.getClientPlayerLevel();
				Entity entity = level.getEntity(payload.entityId());
				if (entity instanceof LivingEntity living) {
					for (Rat rat : ((RatHolder) living).getAttachedRats()) {
						rat.setDetachedFromEntity();
					}

					for (int id : payload.ratIds()) {
						if (level.getEntity(id) instanceof Rat rat) {
							rat.setAttachedToEntity(living);
						}
					}
				}
			});
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}