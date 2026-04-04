package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class C2SBoneFluteAttackMessage extends C2SAbstractBoneFluteCommandMessage {
	private final int targetId;

	public C2SBoneFluteAttackMessage(LivingEntity target) {
		this(target.getId());
	}

	public C2SBoneFluteAttackMessage(int targetId) {
		this.targetId = targetId;
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeInt(this.targetId);
	}

	public static C2SBoneFluteAttackMessage deserialize(FriendlyByteBuf buf) {
		return new C2SBoneFluteAttackMessage(buf.readInt());
	}

	public static void handle(C2SBoneFluteAttackMessage message, Supplier<Context> ctx) {
		message.handle(ctx.get());
	}

	protected void executeCommand(Level level, Player player) {
		Entity entity = level.getEntity(this.targetId);
		if (entity instanceof LivingEntity living) {
			for (Rat rat : getNearbyPets(level, player)) {
				rat.setOrderedToSit(false);
				rat.setCommandedTarget(living);
				rat.setCommandedPos(null);
			}
		}
	}

	protected SoundEvent getSound() {
		return CCSoundEvents.BONE_FLUTE_ATTACK.get();
	}
}