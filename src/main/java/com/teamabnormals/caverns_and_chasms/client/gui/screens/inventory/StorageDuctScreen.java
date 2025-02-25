package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StorageDuctScreen extends AbstractContainerScreen<StorageDuctMenu> {
	private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/gui/container/storage_duct.png");

	public StorageDuctScreen(StorageDuctMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(GuiGraphics p_283282_, int p_282467_, int p_282129_, float p_281965_) {
		this.renderBackground(p_283282_);
		super.render(p_283282_, p_282467_, p_282129_, p_281965_);
		this.renderTooltip(p_283282_, p_282467_, p_282129_);
	}

	@Override
	protected void renderBg(GuiGraphics p_283137_, float p_282476_, int p_281600_, int p_283194_) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		p_283137_.blit(CONTAINER_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}