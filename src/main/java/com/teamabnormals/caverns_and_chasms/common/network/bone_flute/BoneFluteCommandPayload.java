package com.teamabnormals.caverns_and_chasms.common.network.bone_flute;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.item.BoneFluteItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public interface BoneFluteCommandPayload {

	default void handleCommand(IPayloadContext context) {
		if (context.connection().getDirection().isServerbound()) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player != null) {
					Level level = player.level();
					level.playSound(null, player, this.getSound(), SoundSource.RECORDS, 3.0F, 1.0F);
					this.executeCommand(level, player);
				}
			});
		}
	}

	void executeCommand(Level level, Player player);

	SoundEvent getSound();

	static List<Rat> getNearbyPets(Level level, Player player) {
		return level.getEntitiesOfClass(Rat.class, player.getBoundingBox().inflate(BoneFluteItem.COMMAND_RANGE), (entity) -> entity.getOwner() == player && entity.distanceToSqr(player) <= BoneFluteItem.COMMAND_RANGE * BoneFluteItem.COMMAND_RANGE);
	}
}