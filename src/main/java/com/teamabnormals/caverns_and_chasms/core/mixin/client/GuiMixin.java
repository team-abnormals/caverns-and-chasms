package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.teamabnormals.caverns_and_chasms.client.gui.MonocleGui;
import com.teamabnormals.caverns_and_chasms.common.item.MonocleItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class GuiMixin implements MonocleGui {
	private static final ResourceLocation MONOCLE_SCOPE_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/misc/monocle_scope.png");

	@Shadow
	protected int screenWidth;

	@Shadow
	protected int screenHeight;

	@Shadow
	@Final
	protected Minecraft minecraft;

	@Override
	public void renderMonocleOverlay(GuiGraphics gui, float scopeScale) {
		float f = (float) Math.min(this.screenWidth, this.screenHeight);
		float f1 = Math.min((float) this.screenWidth / f, (float) this.screenHeight / f) * scopeScale;
		int i = Mth.floor(f * f1);
		int j = Mth.floor(f * f1);
		int k = (this.screenWidth - i) / 2;
		int l = (this.screenHeight - j) / 2;
		int i1 = k + i;
		int j1 = l + j;
		gui.blit(MONOCLE_SCOPE_LOCATION, k, l, -90, 0.0F, 0.0F, i, j, i, j);
		gui.fill(RenderType.guiOverlay(), 0, j1, this.screenWidth, this.screenHeight, -90, -16777216);
		gui.fill(RenderType.guiOverlay(), 0, 0, this.screenWidth, l, -90, -16777216);
		gui.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
		gui.fill(RenderType.guiOverlay(), i1, l, this.screenWidth, j1, -90, -16777216);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSpyglassOverlay(Lnet/minecraft/client/gui/GuiGraphics;F)V"), method = "render")
	private void renderSpyglassOverlay(Gui gui, GuiGraphics graphics, float scopeSize) {
		Player player = this.minecraft.player;
		if (player != null && MonocleItem.isUsingMonocle(player)) {
			((MonocleGui) gui).renderMonocleOverlay(graphics, scopeSize);
		} else {
			gui.renderSpyglassOverlay(graphics, scopeSize);
		}
	}
}