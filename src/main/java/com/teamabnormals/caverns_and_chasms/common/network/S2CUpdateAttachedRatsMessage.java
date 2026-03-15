package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.List;
import java.util.function.Supplier;

public final class S2CUpdateAttachedRatsMessage {
	private int entityId;
	private int[] ratIds;

	private S2CUpdateAttachedRatsMessage(int entityId, int[] ratIds) {
		this.entityId = entityId;
		this.ratIds = ratIds;
	}

	public S2CUpdateAttachedRatsMessage(RatHolder entity) {
		this.entityId = ((LivingEntity) entity).getId();
		List<Rat> rats = entity.getAttachedRats();
		this.ratIds = new int[rats.size()];
		for (int i = 0; i < rats.size(); i++) {
			this.ratIds[i] = rats.get(i).getId();
		}
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeVarInt(this.entityId);
		buf.writeVarIntArray(this.ratIds);
	}

	public static S2CUpdateAttachedRatsMessage deserialize(FriendlyByteBuf buf) {
		return new S2CUpdateAttachedRatsMessage(buf.readVarInt(), buf.readVarIntArray());
	}

	public static void handle(S2CUpdateAttachedRatsMessage message, Supplier<Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Level level = ClientInfo.getClientPlayerLevel();
				Entity entity = level.getEntity(message.entityId);
				if (entity instanceof LivingEntity livingentity) {
					for (Rat rat : ((RatHolder) livingentity).getAttachedRats()) {
						rat.setDetachedFromEntity();
					}
					for (int id : message.ratIds) {
						if (level.getEntity(id) instanceof Rat rat) {
							rat.setAttachedToEntity(livingentity);
						}
					}
				}
			});
		}
		context.setPacketHandled(true);
	}
}