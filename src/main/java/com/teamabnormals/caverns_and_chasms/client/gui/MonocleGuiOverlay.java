package com.teamabnormals.caverns_and_chasms.client.gui;

import com.teamabnormals.caverns_and_chasms.common.item.MonocleItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class MonocleGuiOverlay implements LayeredDraw.Layer {
	private static final ResourceLocation MONOCLE_SCOPE_LOCATION = CavernsAndChasms.location("textures/misc/monocle_scope.png");

	public float overlayScopeScale;

	public boolean shouldRender(Player player) {
		return MonocleItem.isScopingMonocle(player);
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		float deltaFrame = deltaTracker.getGameTimeDeltaTicks();
		this.overlayScopeScale = Mth.lerp(0.5F * deltaFrame, this.overlayScopeScale, 1.125F);
		if (mc.options.getCameraType().isFirstPerson()) {
			if (shouldRender(mc.player)) {
				renderMonocleOverlay(guiGraphics, this.overlayScopeScale);
			} else {
				this.overlayScopeScale = 0.5F;
			}
		}
	}

	public static void renderMonocleOverlay(GuiGraphics graphics, float scopeScale) {
		int screenWidth = graphics.guiWidth();
		int screenHeight = graphics.guiHeight();

		float f = (float) Math.min(screenWidth, screenHeight);
		float f1 = Math.min((float) screenWidth / f, (float) screenHeight / f) * scopeScale;
		int i = Mth.floor(f * f1);
		int j = Mth.floor(f * f1);
		int k = (screenWidth - i) / 2;
		int l = (screenHeight - j) / 2;
		int i1 = k + i;
		int j1 = l + j;
		graphics.blit(MONOCLE_SCOPE_LOCATION, k, l, -90, 0.0F, 0.0F, i, j, i, j);
		graphics.fill(RenderType.guiOverlay(), 0, j1, screenWidth, screenHeight, -90, -16777216);
		graphics.fill(RenderType.guiOverlay(), 0, 0, screenWidth, l, -90, -16777216);
		graphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
		graphics.fill(RenderType.guiOverlay(), i1, l, screenWidth, j1, -90, -16777216);
	}

	public static class MonocleHeadGuiOverlay extends MonocleGuiOverlay {
		public boolean shouldRender(Player player) {
			return MonocleItem.isWearingMonocle(player);
		}
	}
}
