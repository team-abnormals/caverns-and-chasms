package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.common.inventory.CCItemCombinerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class CCItemCombinerScreen<T extends CCItemCombinerMenu> extends AbstractContainerScreen<T> implements ContainerListener {
	private final ResourceLocation menuResource;

	public CCItemCombinerScreen(T menu, Inventory playerInventory, Component title, ResourceLocation menuResource) {
		super(menu, playerInventory, title);
		this.menuResource = menuResource;
	}

	protected void subInit() {
	}

	@Override
	protected void init() {
		super.init();
		this.subInit();
		this.menu.addSlotListener(this);
	}

	@Override
	public void removed() {
		super.removed();
		this.menu.removeSlotListener(this);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderFg(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	protected void renderFg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		guiGraphics.blit(this.menuResource, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		this.renderErrorIcon(guiGraphics, this.leftPos, this.topPos);
	}

	protected abstract void renderErrorIcon(GuiGraphics guiGraphics, int x, int y);

	@Override
	public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
	}

	@Override
	public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
	}
}