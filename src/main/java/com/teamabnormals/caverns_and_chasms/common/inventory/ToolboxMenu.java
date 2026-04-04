package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ToolboxMenu extends AbstractContainerMenu {
	private final Container container;

	public ToolboxMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(14));
	}

	public ToolboxMenu(int id, Inventory inventory, Container container) {
		super(CCMenuTypes.TOOLBOX.get(), id);
		checkContainerSize(container, 14);
		this.container = container;
		container.startOpen(inventory.player);

		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 7; ++j) {
				this.addSlot(new ToolboxSlot(container, j + i * 7, 26 + j * 18, 32 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
		}
	}

	@Override
	public boolean stillValid(Player p_40195_) {
		return this.container.stillValid(p_40195_);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < this.container.getContainerSize()) {
				if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemstack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}
}