package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BoneFluteAttackPayload(int targetId) implements CustomPacketPayload, BoneFluteCommandPayload {
	public static final CustomPacketPayload.Type<BoneFluteAttackPayload> TYPE = new CustomPacketPayload.Type<>(CavernsAndChasms.location("bone_flute_attack"));

	public static final StreamCodec<ByteBuf, BoneFluteAttackPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, BoneFluteAttackPayload::targetId,
			BoneFluteAttackPayload::new
	);

	public static void handle(BoneFluteAttackPayload payload, IPayloadContext context) {
		payload.handleCommand(context);
	}

	@Override
	public void executeCommand(Level level, Player player) {
		Entity entity = level.getEntity(this.targetId);
		if (entity instanceof LivingEntity living) {
			for (Rat rat : BoneFluteCommandPayload.getNearbyPets(level, player)) {
				rat.setOrderedToSit(false);
				rat.setCommandedTarget(living);
				rat.setCommandedPos(null);
			}
		}
	}

	@Override
	public SoundEvent getSound() {
		return CCSoundEvents.BONE_FLUTE_ATTACK.get();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}