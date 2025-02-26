package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.common.inventory.StorageDuctMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StorageDuctScreen extends AbstractContainerScreen<StorageDuctMenu> {
	private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/gui/container/storage_duct.png");
	private final int containerSize;

	private boolean scrolling;
	private float scrollOffset;

	public StorageDuctScreen(StorageDuctMenu menu, Inventory inventory, Component component, int containerSize) {
		super(menu, inventory, component);
		this.imageWidth = 194;
		this.imageHeight = 222;
		this.inventoryLabelY = this.imageHeight - 94;
		this.containerSize = containerSize;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int partialTick, int mouseX, float mouseY) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, partialTick, mouseX, mouseY);
		this.renderTooltip(guiGraphics, partialTick, mouseX);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(CONTAINER_LOCATION, i, j, 0, 0, 0, this.imageWidth, this.imageHeight, 256, 512);
		guiGraphics.blit(CONTAINER_LOCATION, i + 7, j + 17, 0, 0, 222, 162, this.containerSize / 9 * 18, 256, 512);
		guiGraphics.blit(CONTAINER_LOCATION, i + 174, (int) (j + 18 + 91 * this.scrollOffset), 0, 232 + (this.canScroll() ? 0 : 12), 0, 12, 15, 256, 512);
	}

	@Override
	public boolean mouseScrolled(double p_98527_, double p_98528_, double p_98529_) {
		if (!this.canScroll()) {
			return false;
		} else {
			this.scrollOffset = this.menu.subtractInputFromScroll(this.scrollOffset, p_98529_);
			this.menu.scrollTo(this.scrollOffset);
			return true;
		}
	}

	@Override
	public boolean mouseDragged(double p_98535_, double p_98536_, int p_98537_, double p_98538_, double p_98539_) {
		if (this.scrolling) {
			int i = this.topPos + 18;
			int j = i + 108;
			this.scrollOffset = ((float)p_98536_ - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
			this.scrollOffset = Mth.clamp(this.scrollOffset, 0.0F, 1.0F);
			this.menu.scrollTo(this.scrollOffset);
			return true;
		} else {
			return super.mouseDragged(p_98535_, p_98536_, p_98537_, p_98538_, p_98539_);
		}
	}

	@Override
	public boolean mouseClicked(double p_98531_, double p_98532_, int p_98533_) {
		if (p_98533_ == 0) {
			if (this.insideScrollbar(p_98531_, p_98532_)) {
				this.scrolling = this.canScroll();
				return true;
			}
		}

		return super.mouseClicked(p_98531_, p_98532_, p_98533_);
	}

	@Override
	public boolean mouseReleased(double p_98622_, double p_98623_, int p_98624_) {
		if (p_98624_ == 0) {
			this.scrolling = false;
		}

		return super.mouseReleased(p_98622_, p_98623_, p_98624_);
	}

	@Override
	public void resize(Minecraft p_98595_, int p_98596_, int p_98597_) {
		int i = this.menu.scrollRow;
		this.init(p_98595_, p_98596_, p_98597_);
		this.scrollOffset = this.menu.getScrollForRowIndex(i);
		this.menu.scrollTo(this.scrollOffset);
	}

	private boolean canScroll() {
		return this.menu.canScroll();
	}

	private boolean insideScrollbar(double mouseX, double mouseY) {
		int i = this.leftPos;
		int j = this.topPos;
		int k = i + 174;
		int l = j + 18;
		int i1 = k + 14;
		int j1 = l + 108;
		return mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1;
	}
}