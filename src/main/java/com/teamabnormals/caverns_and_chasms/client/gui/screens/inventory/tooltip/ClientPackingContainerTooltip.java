package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.tooltip;

import com.teamabnormals.caverns_and_chasms.common.item.PackingContainerItem.PackingContainerTooltip;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ClientPackingContainerTooltip implements ClientTooltipComponent {
	public static final ResourceLocation TEXTURE_LOCATION = CavernsAndChasms.location("textures/gui/container/packing_container.png");
	private final ItemStack items;

	public ClientPackingContainerTooltip(PackingContainerTooltip p_169873_) {
		this.items = p_169873_.getItems();
	}

	public int getHeight() {
		return 26;
	}

	public int getWidth(Font p_169901_) {
		return 20;
	}

	public void renderImage(Font p_194042_, int p_194043_, int p_194044_, GuiGraphics p_282522_) {
		int j1 = p_194043_;
		int k1 = p_194044_;
		this.renderSlot(j1, k1, p_282522_, p_194042_);
	}

	private void renderSlot(int p_283180_, int p_282972_, GuiGraphics p_283625_, Font p_281863_) {
		this.blit(p_283625_, p_283180_, p_282972_);
		if (!this.items.isEmpty()) {
			ItemStack itemstack = this.items;
			this.blit(p_283625_, p_283180_, p_282972_);
			p_283625_.renderItem(itemstack, p_283180_ + 2, p_282972_ + 2, 0);
			p_283625_.renderItemDecorations(p_281863_, itemstack, p_283180_ + 2, p_282972_ + 2);
		}
	}

	private void blit(GuiGraphics p_281273_, int p_282428_, int p_281897_) {
		p_281273_.blit(TEXTURE_LOCATION, p_282428_, p_281897_, 0, 0, 0, 20, 21, 128, 128);
	}
}