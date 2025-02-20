package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isScoping()Z"), method = "renderArmWithItem", remap = false)
	private boolean isScoping(AbstractClientPlayer player) {
		return player.isScoping() && !player.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.MONOCLE.get());
	}
}