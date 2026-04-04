package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class C2SBoneFluteMoveMessage extends C2SAbstractBoneFluteCommandMessage {
	private final BlockPos targetPos;

	public C2SBoneFluteMoveMessage(BlockPos targetPos) {
		this.targetPos = targetPos;
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.targetPos);
	}

	public static C2SBoneFluteMoveMessage deserialize(FriendlyByteBuf buf) {
		return new C2SBoneFluteMoveMessage(buf.readBlockPos());
	}

	public static void handle(C2SBoneFluteMoveMessage message, Supplier<Context> ctx) {
		message.handle(ctx.get());
	}

	protected void executeCommand(Level level, Player player) {
		for (Rat rat : getNearbyPets(level, player)) {
			rat.setOrderedToSit(false);
			rat.detachFromEntity();
			rat.setTarget(null);
			rat.setCommandedTarget(null);
			rat.setCommandedPos(this.targetPos);
		}
	}

	protected SoundEvent getSound() {
		return CCSoundEvents.BONE_FLUTE_MOVE.get();
	}
}