package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.common.inventory.BejeweledAnvilMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BejeweledAnvilScreen extends ItemCombinerScreen<BejeweledAnvilMenu> {
	private static final ResourceLocation ANVIL_LOCATION = CavernsAndChasms.location("textures/gui/container/bejeweled_anvil.png");
	private static final ResourceLocation TEXT_FIELD_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/text_field");
	private static final ResourceLocation TEXT_FIELD_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/text_field_disabled");
	private static final ResourceLocation ERROR_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/error");
	private EditBox name;

	public BejeweledAnvilScreen(BejeweledAnvilMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, ANVIL_LOCATION);
		this.titleLabelX = 60;
	}

	@Override
	protected void subInit() {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, Component.translatable("container.repair"));
		this.name.setCanLoseFocus(false);
		this.name.setTextColor(-1);
		this.name.setTextColorUneditable(-1);
		this.name.setBordered(false);
		this.name.setMaxLength(50);
		this.name.setResponder(this::onNameChanged);
		this.name.setValue("");
		this.addWidget(this.name);
		this.setInitialFocus(this.name);
		this.name.setEditable(false);
	}

	@Override
	protected void setInitialFocus() {
		this.setInitialFocus(this.name);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String s = this.name.getValue();
		this.init(minecraft, width, height);
		this.name.setValue(s);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			this.minecraft.player.closeContainer();
		}

		return !this.name.keyPressed(keyCode, scanCode, modifiers) && !this.name.canConsumeInput() ? super.keyPressed(keyCode, scanCode, modifiers) : true;
	}

	private void onNameChanged(String p_97899_) {
		Slot slot = this.menu.getSlot(0);
		if (slot.hasItem()) {
			String s = p_97899_;
			if (!slot.getItem().has(DataComponents.CUSTOM_NAME) && p_97899_.equals(slot.getItem().getHoverName().getString())) {
				s = "";
			}

			if (this.menu.setItemName(s)) {
				this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
			}

		}
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
		guiGraphics.blitSprite(this.menu.getSlot(0).hasItem() ? TEXT_FIELD_SPRITE : TEXT_FIELD_DISABLED_SPRITE, this.leftPos + 59, this.topPos + 20, 110, 16);
	}

	@Override
	public void renderFg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.name.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {
		if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
			guiGraphics.blitSprite(ERROR_SPRITE, x + 99, y + 45, 28, 21);
		}
	}

	@Override
	public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
		if (slotInd == 0) {
			this.name.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
			this.name.setEditable(!stack.isEmpty());
			this.setFocused(this.name);
		}
	}
}