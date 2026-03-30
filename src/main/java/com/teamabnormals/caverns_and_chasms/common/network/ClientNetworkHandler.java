package com.teamabnormals.caverns_and_chasms.common.network;

import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.StorageDuctScreen;
import com.teamabnormals.caverns_and_chasms.client.resources.sounds.MovingDoorMoveSoundInstance;
import com.teamabnormals.caverns_and_chasms.common.block.entity.StorageDuctBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClientNetworkHandler {
	public static void handleOpenStorageDuct(S2COpenStorageDuctMessage packet) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			BlockEntity blockEntity = player.level().getBlockEntity(packet.getBlockPos());
			if (blockEntity instanceof StorageDuctBlockEntity storageDuct) {
				StorageDuctMenu container = new StorageDuctMenu(packet.getWindowId(), player.getInventory(), new SimpleContainer(packet.getContainerSize()), null);
				player.containerMenu = container;
				Minecraft.getInstance().setScreen(new StorageDuctScreen(container, player.getInventory(), storageDuct.getDisplayName(), Math.min(packet.getContainerSize(), 54)));
			}
		}
	}

	public static void handleRollerDoorSound(S2CMovingDoorSoundMessage packet) {
		Minecraft mc = Minecraft.getInstance();
		BlockPos blockPos = packet.getPos();
		if (mc.level.getBlockEntity(blockPos) instanceof MovingDoorHeaderBlockEntity headerEntity) {
			MovingDoorMoveSoundInstance soundInstance = headerEntity.getSoundInstance();
			if (soundInstance != null) {
				soundInstance.setVolume(packet.getVolume());
			}
		}
	}
}