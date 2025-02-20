package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isScoping()Z"), method = "turnPlayer", remap = false)
	private boolean isScoping(LocalPlayer player) {
		return player.isScoping() && player.getUseItem().is(Items.SPYGLASS);
	}
}