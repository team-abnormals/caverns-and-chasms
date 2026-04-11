package com.teamabnormals.caverns_and_chasms.common.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.common.item.PackingContainerItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;

public record PackingContainerContents(ItemStack items, int count, Fraction weight) implements TooltipComponent {
	public static final PackingContainerContents EMPTY = new PackingContainerContents(ItemStack.EMPTY, 0);
	public static final Codec<PackingContainerContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					ItemStack.CODEC.fieldOf("items").forGetter(PackingContainerContents::items),
					Codec.INT.fieldOf("count").forGetter(PackingContainerContents::count))
			.apply(instance, PackingContainerContents::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, PackingContainerContents> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC, PackingContainerContents::items,
			ByteBufCodecs.INT, PackingContainerContents::count,
			PackingContainerContents::new);

	private static final Fraction CONTAINER_IN_CONTAINER_WEIGHT = Fraction.getFraction(1, 16);
	private static final int NO_STACK_INDEX = -1;

	public PackingContainerContents(ItemStack items, int count) {
		this(items, count, items.isEmpty() ? Fraction.ZERO : computeContentWeight(items, count));
	}

	private static Fraction computeContentWeight(ItemStack item, int count) {
		Fraction fraction = Fraction.ZERO;
		fraction = fraction.add(getWeight(item).multiplyBy(Fraction.getFraction(count, 1)));
		return fraction;
	}

	public static Fraction getWeight(ItemStack stack) {
		PackingContainerContents contents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
		if (contents != null) {
			return CONTAINER_IN_CONTAINER_WEIGHT.add(contents.weight());
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
		return this.count();
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
			return other instanceof PackingContainerContents contents && this.weight.equals(contents.weight) && ItemStack.matches(this.items, contents.items);
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
		private int count;
		private Fraction weight;

		public Mutable(PackingContainerContents contents) {
			this.items = contents.items;
			this.count = contents.count;
			this.weight = contents.weight;
		}

		public PackingContainerContents.Mutable clearItems() {
			this.items = ItemStack.EMPTY;
			this.count = 0;
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
				} else if (this.hasStack(stack) || this.items.isEmpty()){
					this.weight = this.weight.add(PackingContainerContents.getWeight(stack).multiplyBy(Fraction.getFraction(i, 1)));
					if (this.hasStack(stack)) {
						this.count += i;
						stack.shrink(i);
						return i;
					} else {
						int j = Math.min(i, stack.getCount());
						this.items = stack.copyWithCount(1);
						this.count = j;
						stack.shrink(j);
						return j;
					}
				}
			}

			return 0;
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
				int amount = Math.min(this.count, this.items.getMaxStackSize());
				ItemStack stack = this.items.copyWithCount(amount);
				this.count -= amount;
				this.weight = this.weight.subtract(PackingContainerContents.getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
				if (this.count <= 0) {
					this.clearItems();
				}
				return stack;
			}
		}

		public Fraction weight() {
			return this.weight;
		}

		public int count() {
			return this.count;
		}

		public PackingContainerContents toImmutable() {
			return new PackingContainerContents(this.items.copy(), this.count, this.weight);
		}
	}

	public static boolean addToContainer(Inventory inventory, ItemStack otherStack) {
		for (NonNullList<ItemStack> list : inventory.compartments) {
			for (ItemStack stack : list) {
				PackingContainerContents contents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
				if (stack.getItem() instanceof PackingContainerItem item && contents != null && !contents.items.isEmpty()) {
					PackingContainerContents.Mutable mutable = new PackingContainerContents.Mutable(contents);
					int i = mutable.tryInsert(otherStack);
					stack.set(CCDataComponents.PACKING_CONTAINER_CONTENTS, mutable.toImmutable());
					if (i > 0) {
						ServerPlayer player = (ServerPlayer) inventory.player;
						ServerLevel level = (ServerLevel) player.level();
						level.playSound(null, player.getX(), player.getY(), player.getZ(), item.getInsertSound(), SoundSource.PLAYERS, 0.8F, 0.8F + level.getRandom().nextFloat() * 0.4F);
						stack.setPopTime(5);
						return true;
					}
				}
			}
		}

		return false;
	}
}