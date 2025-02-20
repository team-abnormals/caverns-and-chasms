package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.client.gui.MonocleGui;
import com.teamabnormals.caverns_and_chasms.common.item.MonocleItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;renderSpyglassOverlay(Lnet/minecraft/client/gui/GuiGraphics;F)V"), method = "renderSpyglassOverlay", remap = false)
	private void renderSpyglassOverlay(ForgeGui gui, GuiGraphics graphics, float scopeSize) {
		Player player = gui.getMinecraft().player;
		if (player != null && MonocleItem.isUsingMonocle(player)) {
			((MonocleGui) gui).renderMonocleOverlay(graphics, scopeSize);
		} else {
			gui.renderSpyglassOverlay(graphics, scopeSize);
		}
	}
}