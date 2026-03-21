package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class C2SBoneFluteRecallMessage extends C2SAbstractBoneFluteCommandMessage {

	public void serialize(FriendlyByteBuf buf) {
	}

	public static C2SBoneFluteRecallMessage deserialize(FriendlyByteBuf buf) {
		return new C2SBoneFluteRecallMessage();
	}

	public static void handle(C2SBoneFluteRecallMessage message, Supplier<Context> ctx) {
		message.handle(ctx.get());
	}

	protected void executeCommand(Level level, Player player) {
		for (Rat rat : getNearbyPets(level, player)) {
			rat.setOrderedToSit(false);
			rat.detachFromEntity();
			rat.setTarget(null);
			rat.setCommandedTarget(null);
			rat.setCommandedPos(null);
		}
	}

	protected SoundEvent getSound() {
		return CCSoundEvents.BONE_FLUTE_RECALL.get();
	}
}