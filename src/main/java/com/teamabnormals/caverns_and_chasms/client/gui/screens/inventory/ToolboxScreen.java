package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.inventory.ToolboxMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ToolboxScreen extends AbstractContainerScreen<ToolboxMenu> {
	private static final ResourceLocation CONTAINER_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/gui/container/toolbox.png");

	public ToolboxScreen(ToolboxMenu p_99240_, Inventory p_99241_, Component p_99242_) {
		super(p_99240_, p_99241_, p_99242_);
		++this.imageHeight;
	}

	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	public void render(PoseStack p_99249_, int p_99250_, int p_99251_, float p_99252_) {
		this.renderBackground(p_99249_);
		super.render(p_99249_, p_99250_, p_99251_, p_99252_);
		this.renderTooltip(p_99249_, p_99250_, p_99251_);
	}

	protected void renderBg(PoseStack p_99244_, float p_99245_, int p_99246_, int p_99247_) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CONTAINER_TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(p_99244_, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}