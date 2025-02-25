package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StorageDuctMenu extends AbstractContainerMenu {
	private final Container container;
	private final int rows;

	public StorageDuctMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(54));
	}

	public StorageDuctMenu(int id, Inventory inventory, Container container) {
		super(CCMenuTypes.STORAGE_DUCT.get(), id);
		this.container = container;
		this.rows = container.getContainerSize() / 9;
		container.startOpen(inventory.player);

		for (int i = 0; i < this.rows; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(container, i * 9 + j, 8 + j * 18, 18 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 17 + i1 * 18, 84 + 56 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(inventory, l, 17 + l * 18, 150 + 48));
		}
	}

	public int getRowIndexForScroll(float scroll) {
		return Math.max((int)(scroll * this.rows + 0.5D), 0);
	}

	public float getScrollForRowIndex(int scroll) {
		return Mth.clamp((float)scroll / this.rows, 0.0F, 1.0F);
	}

	public float subtractInputFromScroll(float scroll, double p_260358_) {
		return Mth.clamp(scroll - (float)(p_260358_ / this.rows), 0.0F, 1.0F);
	}

	public void scrollTo(float scroll) {
		int i = this.getRowIndexForScroll(scroll);

		// Do stuff here.
	}

	public boolean canScroll() {
		return this.rows > 6;
	}

	// TODO: This.
	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}
}