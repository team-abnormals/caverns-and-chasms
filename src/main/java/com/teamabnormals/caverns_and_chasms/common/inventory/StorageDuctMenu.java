package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
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
	private final int containerRows;
	public int scrollRow;

	public StorageDuctMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(54));
	}

	public StorageDuctMenu(int id, Inventory inventory, Container container) {
		super(CCMenuTypes.STORAGE_DUCT.get(), id);
		this.container = container;
		this.containerRows = container.getContainerSize() / 9;
		container.startOpen(inventory.player);

		for (int i = 0; i < this.containerRows; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new StorageDuctSlot(container, this, i * 9 + j, 8 + j * 18, 18 + i * 18));
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

	private int calculateRowCount() {
		return this.containerRows - 6;
	}

	public int getRowIndexForScroll(float scroll) {
		return Math.max((int)(scroll * this.calculateRowCount() + 0.5D), 0);
	}

	public float getScrollForRowIndex(int scroll) {
		return Mth.clamp((float)scroll / this.calculateRowCount(), 0.0F, 1.0F);
	}

	public float subtractInputFromScroll(float scroll, double p_260358_) {
		return Mth.clamp(scroll - (float)(p_260358_ / this.calculateRowCount()), 0.0F, 1.0F);
	}

	public void scrollTo(float scroll) {
		this.scrollRow = this.getRowIndexForScroll(scroll);
		for (Slot slot : this.slots)
			if (slot instanceof StorageDuctSlot storageDuctSlot)
				storageDuctSlot.y = storageDuctSlot.initialY - this.scrollRow * 18;
	}

	public boolean canScroll() {
		return this.containerRows > 6;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < this.containerRows * 9) {
				if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemstack;
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