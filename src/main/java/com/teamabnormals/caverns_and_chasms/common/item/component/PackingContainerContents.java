package com.teamabnormals.caverns_and_chasms.common.item.component;

import com.mojang.serialization.Codec;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;

public record PackingContainerContents(ItemStack items, Fraction weight) implements TooltipComponent {
	public static final PackingContainerContents EMPTY = new PackingContainerContents(ItemStack.EMPTY);
	public static final Codec<PackingContainerContents> CODEC = ItemStack.CODEC.xmap(PackingContainerContents::new, p_331551_ -> p_331551_.items);
	public static final StreamCodec<RegistryFriendlyByteBuf, PackingContainerContents> STREAM_CODEC = ItemStack.STREAM_CODEC.map(PackingContainerContents::new, contents -> contents.items);
	private static final Fraction CONTAINER_IN_CONTAINER_WEIGHT = Fraction.getFraction(1, 16);
	private static final int NO_STACK_INDEX = -1;

	public PackingContainerContents(ItemStack items) {
		this(items, computeContentWeight(items));
	}

	private static Fraction computeContentWeight(ItemStack content) {
		Fraction fraction = Fraction.ZERO;
		fraction = fraction.add(getWeight(content).multiplyBy(Fraction.getFraction(content.getCount(), 1)));
		return fraction;
	}

	static Fraction getWeight(ItemStack stack) {
		PackingContainerContents bundlecontents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS.get());
		if (bundlecontents != null) {
			return CONTAINER_IN_CONTAINER_WEIGHT.add(bundlecontents.weight());
		} else {
			List<BeehiveBlockEntity.Occupant> list = stack.getOrDefault(DataComponents.BEES, List.of());
			return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, stack.getMaxStackSize() * 8);
		}
	}

	public ItemStack items() {
		return this.items;
	}

	public ItemStack itemsCopy() {
		return this.items.copy();
	}

	public int size() {
		return this.items.getCount();
	}

	public Fraction weight() {
		return this.weight;
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof PackingContainerContents(ItemStack items1, Fraction weight1) && this.weight.equals(weight1) && ItemStack.matches(this.items, items1);
		}
	}

	@Override
	public int hashCode() {
		return ItemStack.hashItemAndComponents(this.items);
	}

	@Override
	public String toString() {
		return "PackingContainerContents" + this.items;
	}

	public static class Mutable {
		private ItemStack items;
		private Fraction weight;

		public Mutable(PackingContainerContents contents) {
			this.items = contents.items;
			this.weight = contents.weight;
		}

		public PackingContainerContents.Mutable clearItems() {
			this.items = ItemStack.EMPTY;
			this.weight = Fraction.ZERO;
			return this;
		}

		private boolean hasStack(ItemStack stack) {
			return ItemStack.isSameItemSameComponents(this.items, stack);
		}

		private int getMaxAmountToAdd(ItemStack stack) {
			Fraction fraction = Fraction.ONE.subtract(this.weight);
			return Math.max(fraction.divideBy(PackingContainerContents.getWeight(stack)).intValue(), 0);
		}

		public int tryInsert(ItemStack stack) {
			if (!stack.isEmpty() && stack.canFitInsideContainerItems()) {
				int i = Math.min(stack.getCount(), this.getMaxAmountToAdd(stack));
				if (i == 0) {
					return 0;
				} else {
					this.weight = this.weight.add(PackingContainerContents.getWeight(stack).multiplyBy(Fraction.getFraction(i, 1)));
					if (this.hasStack(stack)) {
						ItemStack itemstack = this.items.copyAndClear();
						ItemStack itemstack1 = itemstack.copyWithCount(itemstack.getCount() + i);
						stack.shrink(i);
						this.items = itemstack1;
					} else {
						this.items = stack.split(i);
					}

					return i;
				}
			} else {
				return 0;
			}
		}

		public int tryTransfer(Slot slot, Player player) {
			ItemStack itemstack = slot.getItem();
			int i = this.getMaxAmountToAdd(itemstack);
			return this.tryInsert(slot.safeTake(itemstack.getCount(), i, player));
		}

		@Nullable
		public ItemStack removeOne() {
			if (this.items.isEmpty()) {
				return null;
			} else {
				ItemStack stack = this.items.copyAndClear();
				this.weight = this.weight.subtract(PackingContainerContents.getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
				return stack;
			}
		}

		public Fraction weight() {
			return this.weight;
		}

		public PackingContainerContents toImmutable() {
			return new PackingContainerContents(this.items.copy(), this.weight);
		}
	}
}