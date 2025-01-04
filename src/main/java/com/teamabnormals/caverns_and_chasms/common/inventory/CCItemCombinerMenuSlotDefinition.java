package com.teamabnormals.caverns_and_chasms.common.inventory;

import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition.SlotDefinition;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CCItemCombinerMenuSlotDefinition {
	private final List<SlotDefinition> slots;
	private final List<SlotDefinition> resultSlots;

	CCItemCombinerMenuSlotDefinition(List<SlotDefinition> slots, List<SlotDefinition> resultSlots) {
		if (!slots.isEmpty() && !resultSlots.isEmpty()) {
			this.slots = slots;
			this.resultSlots = resultSlots;
		} else {
			throw new IllegalArgumentException("Need to define both inputSlots and resultSlots");
		}
	}

	public static CCItemCombinerMenuSlotDefinition.Builder create() {
		return new CCItemCombinerMenuSlotDefinition.Builder();
	}

	public boolean hasSlot(int index) {
		return this.slots.size() >= index;
	}

	public SlotDefinition getSlot(int p_266907_) {
		return this.slots.get(p_266907_);
	}

	public List<SlotDefinition> getResultSlots() {
		return this.resultSlots;
	}

	public List<SlotDefinition> getSlots() {
		return this.slots;
	}

	public int getNumOfInputSlots() {
		return this.slots.size();
	}

	public int getResultSlotIndex() {
		return this.getNumOfInputSlots();
	}

	public int getNumOfResultSlots() {
		return this.resultSlots.size();
	}

	public List<Integer> getInputSlotIndexes() {
		return this.slots.stream().map(SlotDefinition::slotIndex).collect(Collectors.toList());
	}

	public List<Integer> getResultSlotIndexes() {
		return this.resultSlots.stream().map(slotDefinition -> slotDefinition.slotIndex() + this.getNumOfInputSlots()).collect(Collectors.toList());
	}

	public static class Builder {
		private final List<SlotDefinition> slots = new ArrayList<>();
		private final List<SlotDefinition> resultSlots = new ArrayList<>();

		public CCItemCombinerMenuSlotDefinition.Builder withSlot(int p_267315_, int p_267028_, int p_266815_, Predicate<ItemStack> p_267120_) {
			this.slots.add(new SlotDefinition(p_267315_, p_267028_, p_266815_, p_267120_));
			return this;
		}

		public CCItemCombinerMenuSlotDefinition.Builder withResultSlot(int index, int x, int y) {
			this.resultSlots.add(new SlotDefinition(index, x, y, (resultSlot) -> false));
			return this;
		}

		public CCItemCombinerMenuSlotDefinition build() {
			return new CCItemCombinerMenuSlotDefinition(this.slots, this.resultSlots);
		}
	}
}