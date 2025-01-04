package com.teamabnormals.caverns_and_chasms.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition.SlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CCItemCombinerMenu extends AbstractContainerMenu {
	protected final ContainerLevelAccess access;
	protected final Player player;
	protected final Container inputSlots;
	private final List<Integer> inputSlotIndexes;
	protected final Container resultSlots;
	private final List<Integer> resultSlotIndexes;

	protected abstract boolean mayPickup(Player p_39798_, boolean p_39799_);

	protected abstract void onTake(Player p_150601_, ItemStack p_150602_);

	protected abstract boolean isValidBlock(BlockState p_39788_);

	public CCItemCombinerMenu(@Nullable MenuType<?> menu, int p_39774_, Inventory inventory, ContainerLevelAccess level) {
		super(menu, p_39774_);
		this.access = level;
		this.player = inventory.player;
		CCItemCombinerMenuSlotDefinition inputSlotDefinitions = this.createSlotDefinitions();
		this.inputSlots = this.createContainer(inputSlotDefinitions.getNumOfInputSlots());
		this.inputSlotIndexes = inputSlotDefinitions.getInputSlotIndexes();

		this.resultSlots = this.createContainer(inputSlotDefinitions.getNumOfResultSlots());
		this.resultSlotIndexes = inputSlotDefinitions.getResultSlotIndexes();

		this.createInputSlots(inputSlotDefinitions);
		this.createResultSlot(inputSlotDefinitions);
		this.createInventorySlots(inventory);
	}

	private void createInputSlots(CCItemCombinerMenuSlotDefinition definition) {
		for (final SlotDefinition slot : definition.getSlots()) {
			this.addSlot(new Slot(this.inputSlots, slot.slotIndex(), slot.x(), slot.y()) {
				public boolean mayPlace(ItemStack p_267156_) {
					return slot.mayPlace().test(p_267156_);
				}
			});
		}

	}

	private void createResultSlot(CCItemCombinerMenuSlotDefinition definition) {
		for (final SlotDefinition slot : definition.getResultSlots()) {
			this.addSlot(new Slot(this.resultSlots, slot.slotIndex(), slot.x(), slot.y()) {
				public boolean mayPlace(ItemStack p_39818_) {
					return false;
				}

				public boolean mayPickup(Player p_39813_) {
					return CCItemCombinerMenu.this.mayPickup(p_39813_, this.hasItem());
				}

				public void onTake(Player p_150604_, ItemStack p_150605_) {
					CCItemCombinerMenu.this.onTake(p_150604_, p_150605_);
				}
			});
		}
	}

	private void createInventorySlots(Inventory p_267325_) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(p_267325_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(p_267325_, k, 8 + k * 18, 142));
		}

	}

	public abstract void createResult();

	protected abstract CCItemCombinerMenuSlotDefinition createSlotDefinitions();

	private SimpleContainer createContainer(int p_267204_) {
		return new SimpleContainer(p_267204_) {
			public void setChanged() {
				super.setChanged();
				CCItemCombinerMenu.this.slotsChanged(this);
			}
		};
	}

	public void slotsChanged(Container p_39778_) {
		super.slotsChanged(p_39778_);
		if (p_39778_ == this.inputSlots) {
			this.createResult();
		}
	}

	public void removed(Player p_39790_) {
		super.removed(p_39790_);
		this.access.execute((p_39796_, p_39797_) -> {
			this.clearContainer(p_39790_, this.inputSlots);
			if (!this.areResultsFull() && !this.areResultsEmpty()) {
				this.clearContainer(p_39790_, this.resultSlots);
			}
		});
	}

	public boolean areResultsEmpty() {
		return this.resultSlots.getItem(0).isEmpty() && this.resultSlots.getItem(1).isEmpty() && this.resultSlots.getItem(2).isEmpty();
	}

	public boolean areResultsFull() {
		return !this.resultSlots.getItem(0).isEmpty() && !this.resultSlots.getItem(1).isEmpty() && !this.resultSlots.getItem(2).isEmpty();
	}

	public boolean stillValid(Player p_39780_) {
		return this.access.evaluate((p_39785_, p_39786_) -> {
			return !this.isValidBlock(p_39785_.getBlockState(p_39786_)) ? false : p_39780_.distanceToSqr((double) p_39786_.getX() + 0.5D, (double) p_39786_.getY() + 0.5D, (double) p_39786_.getZ() + 0.5D) <= 64.0D;
		}, true);
	}

	public ItemStack quickMoveStack(Player p_39792_, int p_39793_) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(p_39793_);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			int i = this.getInventorySlotStart();
			int j = this.getUseRowEnd();
			if (this.resultSlotIndexes.contains(p_39793_)) {
				if (!this.moveItemStackTo(itemstack1, i, j, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (this.inputSlotIndexes.contains(p_39793_)) {
				if (!this.moveItemStackTo(itemstack1, i, j, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.canMoveIntoInputSlots(itemstack1) && p_39793_ >= this.getInventorySlotStart() && p_39793_ < this.getUseRowEnd()) {
				int k = this.getSlotToQuickMoveTo(itemstack);
				if (!this.moveItemStackTo(itemstack1, k, this.getLastResultSlot(), false)) {
					return ItemStack.EMPTY;
				}
			} else if (p_39793_ >= this.getInventorySlotStart() && p_39793_ < this.getInventorySlotEnd()) {
				if (!this.moveItemStackTo(itemstack1, this.getUseRowStart(), this.getUseRowEnd(), false)) {
					return ItemStack.EMPTY;
				}
			} else if (p_39793_ >= this.getUseRowStart() && p_39793_ < this.getUseRowEnd() && !this.moveItemStackTo(itemstack1, this.getInventorySlotStart(), this.getInventorySlotEnd(), false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(p_39792_, itemstack1);
		}

		return itemstack;
	}

	protected boolean canMoveIntoInputSlots(ItemStack p_39787_) {
		return true;
	}

	public int getSlotToQuickMoveTo(ItemStack p_267159_) {
		return this.inputSlots.isEmpty() ? 0 : this.inputSlotIndexes.get(0);
	}

	public int getLastResultSlot() {
		return this.inputSlotIndexes.size() + this.resultSlotIndexes.size() - 1;
	}

	private int getInventorySlotStart() {
		return this.getLastResultSlot() + 1;
	}

	private int getInventorySlotEnd() {
		return this.getInventorySlotStart() + 27;
	}

	private int getUseRowStart() {
		return this.getInventorySlotEnd();
	}

	private int getUseRowEnd() {
		return this.getUseRowStart() + 9;
	}
}