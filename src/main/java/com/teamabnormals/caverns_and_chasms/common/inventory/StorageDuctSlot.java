package com.teamabnormals.caverns_and_chasms.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class StorageDuctSlot extends Slot {
	private final StorageDuctMenu menu;
	private final int slot;
	public final int initialY;

	public StorageDuctSlot(Container container, StorageDuctMenu menu, int slot, int x, int y) {
		super(container, slot, x, y);
		this.menu = menu;
		this.slot = slot;
		this.initialY = y;
	}

	@Override
	public boolean isActive() {
		int i = this.slot / 9 - this.menu.scrollRow;
		return i >= 0 && i < 6;
	}
}