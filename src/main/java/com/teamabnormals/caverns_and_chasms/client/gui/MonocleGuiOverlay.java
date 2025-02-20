package com.teamabnormals.caverns_and_chasms.client.gui;

import com.teamabnormals.caverns_and_chasms.common.item.MonocleItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class MonocleGuiOverlay implements IGuiOverlay {
	private static final ResourceLocation MONOCLE_SCOPE_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/misc/monocle_scope.png");

	public float overlayScopeScale;

	public boolean shouldRender(Player player) {
		return MonocleItem.isScopingMonocle(player);
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		gui.setupOverlayRenderState(true, false);
		float deltaFrame = gui.getMinecraft().getDeltaFrameTime();
		this.overlayScopeScale = Mth.lerp(0.5F * deltaFrame, this.overlayScopeScale, 1.125F);
		if (gui.getMinecraft().options.getCameraType().isFirstPerson()) {
			if (shouldRender(gui.getMinecraft().player)) {
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
