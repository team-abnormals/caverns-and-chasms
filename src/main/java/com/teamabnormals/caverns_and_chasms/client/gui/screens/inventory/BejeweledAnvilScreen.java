package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.common.inventory.BejeweledAnvilMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BejeweledAnvilScreen extends ItemCombinerScreen<BejeweledAnvilMenu> {
	private static final ResourceLocation ANVIL_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/gui/container/bejeweled_anvil.png");
	private EditBox name;

	public BejeweledAnvilScreen(BejeweledAnvilMenu p_97874_, Inventory p_97875_, Component p_97876_) {
		super(p_97874_, p_97875_, p_97876_, ANVIL_LOCATION);
		this.titleLabelX = 60;
	}

	public void containerTick() {
		super.containerTick();
		this.name.tick();
	}

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

	public void resize(Minecraft p_97886_, int p_97887_, int p_97888_) {
		String s = this.name.getValue();
		this.init(p_97886_, p_97887_, p_97888_);
		this.name.setValue(s);
	}

	public boolean keyPressed(int p_97878_, int p_97879_, int p_97880_) {
		if (p_97878_ == 256) {
			this.minecraft.player.closeContainer();
		}

		return !this.name.keyPressed(p_97878_, p_97879_, p_97880_) && !this.name.canConsumeInput() ? super.keyPressed(p_97878_, p_97879_, p_97880_) : true;
	}

	private void onNameChanged(String p_97899_) {
		Slot slot = this.menu.getSlot(0);
		if (slot.hasItem()) {
			String s = p_97899_;
			if (!slot.getItem().hasCustomHoverName() && p_97899_.equals(slot.getItem().getHoverName().getString())) {
				s = "";
			}

			if (this.menu.setItemName(s)) {
				this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
			}

		}
	}

	protected void renderBg(GuiGraphics p_283345_, float p_283412_, int p_282871_, int p_281306_) {
		super.renderBg(p_283345_, p_283412_, p_282871_, p_281306_);
		p_283345_.blit(ANVIL_LOCATION, this.leftPos + 59, this.topPos + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
	}

	public void renderFg(GuiGraphics p_283449_, int p_283263_, int p_281526_, float p_282957_) {
		this.name.render(p_283449_, p_283263_, p_281526_, p_282957_);
	}

	protected void renderErrorIcon(GuiGraphics p_282905_, int p_283237_, int p_282237_) {
		if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
			p_282905_.blit(ANVIL_LOCATION, p_283237_ + 99, p_282237_ + 45, this.imageWidth, 0, 28, 21);
		}

	}

	public void slotChanged(AbstractContainerMenu p_97882_, int p_97883_, ItemStack p_97884_) {
		if (p_97883_ == 0) {
			this.name.setValue(p_97884_.isEmpty() ? "" : p_97884_.getHoverName().getString());
			this.name.setEditable(!p_97884_.isEmpty());
			this.setFocused(this.name);
		}

	}
}