package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.inventory.BejeweledAnvilMenu;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

	@Shadow
	public ServerPlayer player;

	@Shadow
	@Final
	static Logger LOGGER;

	@Inject(method = "handleRenameItem", at = @At("TAIL"))
	public void handleRenameItem(ServerboundRenameItemPacket packet, CallbackInfo ci) {
		AbstractContainerMenu abstractcontainermenu = this.player.containerMenu;
		if (abstractcontainermenu instanceof BejeweledAnvilMenu anvilmenu) {
			if (!anvilmenu.stillValid(this.player)) {
				LOGGER.debug("Player {} interacted with invalid menu {}", this.player, anvilmenu);
				return;
			}

			anvilmenu.setItemName(packet.getName());
		}
	}
}
