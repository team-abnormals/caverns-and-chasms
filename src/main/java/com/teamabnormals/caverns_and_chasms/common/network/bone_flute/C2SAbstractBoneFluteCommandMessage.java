package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.item.BoneFluteItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;

public abstract class C2SAbstractBoneFluteCommandMessage {

	protected void handle(NetworkEvent.Context context) {
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null) {
					Level level = player.level();
					level.playSound(null, player, this.getSound(), SoundSource.RECORDS, 3.0F, 1.0F);
					this.executeCommand(level, player);
				}
			});
			context.setPacketHandled(true);
		}
	}

	protected abstract void executeCommand(Level level, Player player);

	protected abstract SoundEvent getSound();

	protected static List<Rat> getNearbyPets(Level level, Player player) {
		return level.getEntitiesOfClass(Rat.class, player.getBoundingBox().inflate(BoneFluteItem.COMMAND_RANGE), (entity) -> entity.getOwner() == player && entity.distanceToSqr(player) <= BoneFluteItem.COMMAND_RANGE * BoneFluteItem.COMMAND_RANGE);
	}
}