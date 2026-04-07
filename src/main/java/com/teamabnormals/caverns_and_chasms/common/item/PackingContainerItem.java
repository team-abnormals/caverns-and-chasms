package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PackingContainerItem extends Item implements DyeableLeatherItem {
	private static final String TAG_ITEM = "Item";
	public static final int MAX_WEIGHT = 512;
	private static final int CONTAINER_IN_CONTAINER_WEIGHT = 4;
	private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

	public PackingContainerItem(Item.Properties properties) {
		super(properties);
	}

	public static float getFullnessDisplay(ItemStack stack) {
		return (float) getContentWeight(stack) / (float) MAX_WEIGHT;
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("display");
		return tag != null && tag.contains("color", 99) ? tag.getInt("color") : -1;
	}

	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(TAG_ITEM)) {
			ItemStack item = ofLargeCount(tag.getCompound(TAG_ITEM));
			MutableComponent hoverName = Component.empty().append(item.getHoverName());
			if (item.hasCustomHoverName()) {
				hoverName.withStyle(ChatFormatting.ITALIC);
			}
			return Component.translatable("item.caverns_and_chasms.packing_container.full", hoverName);
		}
		return super.getName(stack);
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
			return false;
		} else {
			ItemStack otherStack = slot.getItem();
			CompoundTag tag = stack.getOrCreateTag();
			if (otherStack.isEmpty()) {
				this.playRemoveOneSound(player);
				removeOne(stack).ifPresent((p_150740_) -> {
					add(stack, slot.safeInsert(p_150740_));
				});
				return true;
			} else if (otherStack.getItem().canFitInsideContainerItems() && (!tag.contains(TAG_ITEM) || ItemStack.isSameItemSameTags(ofLargeCount(tag.getCompound(TAG_ITEM)), otherStack))) {
				int weight = getWeight(otherStack);
				if (weight > 0) {
					int i = (MAX_WEIGHT - getContentWeight(stack)) / weight;
					int j = add(stack, slot.safeTake(otherStack.getCount(), i, player));
					if (j > 0) {
						this.playInsertSound(player);
						return true;
					}
				}
			}
			this.playInsertFailSound(player);
			return true;
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction action, Player player, SlotAccess p_150747_) {
		if (stack.getCount() != 1) return false;
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			if (otherStack.isEmpty()) {
				removeOne(stack).ifPresent((p_186347_) -> {
					this.playRemoveOneSound(player);
					p_150747_.set(p_186347_);
				});
			} else {
				int i = add(stack, otherStack);
				if (i > 0) {
					this.playInsertSound(player);
					otherStack.shrink(i);
				} else {
					this.playInsertFailSound(player);
				}
			}

			player.containerMenu.slotsChanged(player.getInventory());
			return true;
		} else {
			return false;
		}
	}

	public InteractionResultHolder<ItemStack> use(Level p_150760_, Player p_150761_, InteractionHand p_150762_) {
		ItemStack itemstack = p_150761_.getItemInHand(p_150762_);
		if (dropContents(itemstack, p_150761_)) {
			this.playDropContentsSound(p_150761_);
			p_150761_.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(itemstack, p_150760_.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	public boolean isBarVisible(ItemStack stack) {
		return getContentWeight(stack) > 0;
	}

	public int getBarWidth(ItemStack stack) {
		return Math.min(1 + 12 * getContentWeight(stack) / MAX_WEIGHT, 13);
	}

	public int getBarColor(ItemStack stack) {
		return BAR_COLOR;
	}

	private static int add(ItemStack stack, ItemStack otherStack) {
		if (!otherStack.isEmpty() && otherStack.getItem().canFitInsideContainerItems()) {
			CompoundTag tag = stack.getOrCreateTag();
			int i = getContentWeight(stack);
			int j = getWeight(otherStack);
			if (j == 0) {
				return 0;
			}
			int k = Math.min(otherStack.getCount(), (MAX_WEIGHT - i) / j);
			if (k != 0) {
				if (tag.contains(TAG_ITEM)) {
					CompoundTag itemTag = tag.getCompound(TAG_ITEM);
					ItemStack itemstack = ofLargeCount(itemTag);
					if (ItemStack.isSameItemSameTags(itemstack, otherStack)) {
						itemstack.grow(k);
						saveLargeCount(itemstack, itemTag);
						tag.put(TAG_ITEM, itemTag);
						return k;
					}
				} else if (i == 0) {
					ItemStack itemstack1 = otherStack.copyWithCount(k);
					CompoundTag tag2 = new CompoundTag();
					saveLargeCount(itemstack1, tag2);
					tag.put(TAG_ITEM, tag2);
					return k;
				}

			}
			return 0;
		} else {
			return 0;
		}
	}

	private static int getWeight(ItemStack stack) {
		if (stack.is(CCItems.PACKING_CONTAINER.get())) {
			return CONTAINER_IN_CONTAINER_WEIGHT + getContentWeight(stack);
		} else {
			if ((stack.is(Items.BEEHIVE) || stack.is(Items.BEE_NEST)) && stack.hasTag()) {
				CompoundTag tag = BlockItem.getBlockEntityData(stack);
				if (tag != null && !tag.getList("Bees", 10).isEmpty()) {
					return MAX_STACK_SIZE;
				}
			}

			return MAX_STACK_SIZE / stack.getMaxStackSize();
		}
	}

	private static int getContentWeight(ItemStack stack) {
		ItemStack contents = getContents(stack);
		return getWeight(contents) * contents.getCount();
	}

	private static Optional<ItemStack> removeOne(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains(TAG_ITEM)) {
			return Optional.empty();
		} else {
			CompoundTag itemTag = tag.getCompound(TAG_ITEM);
			if (itemTag.isEmpty()) {
				return Optional.empty();
			} else {
				ItemStack firstStack = ofLargeCount(itemTag);
				ItemStack newStack = firstStack.copy();

				int count = firstStack.getMaxStackSize();
				if (firstStack.getCount() <= count) {
					count = firstStack.getCount();
				}

				newStack.setCount(count);
				firstStack.shrink(count);
				saveLargeCount(firstStack, itemTag);

				if (!firstStack.isEmpty()) {
					tag.put(TAG_ITEM, itemTag);
				} else {
					stack.removeTagKey(TAG_ITEM);
				}

				return Optional.of(newStack);

			}
		}
	}

	public static CompoundTag saveLargeCount(ItemStack stack, CompoundTag tag) {
		stack.save(tag);
		tag.putInt("ActualCount", stack.getCount());
		return tag;
	}

	public static ItemStack ofLargeCount(CompoundTag tag) {
		ItemStack stack = ItemStack.of(tag);
		stack.setCount(tag.getInt("ActualCount"));
		return stack;
	}

	private static boolean dropContents(ItemStack stack, Player player) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains(TAG_ITEM)) {
			return false;
		} else {
			Optional<ItemStack> removeStack = removeOne(stack);
			if (player instanceof ServerPlayer) {
				removeStack.ifPresent(itemStack -> player.drop(itemStack, true));
			}
			return true;
		}
	}

	private static ItemStack getContents(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null) {
			return ItemStack.EMPTY;
		} else {
			CompoundTag listtag = tag.getCompound(TAG_ITEM);
			return ofLargeCount(listtag);
		}
	}

	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return Optional.of(new PackingContainerTooltip(getContents(stack)));
	}

	public void appendHoverText(ItemStack stack, Level p_150750_, List<Component> p_150751_, TooltipFlag p_150752_) {
		p_150751_.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(stack), MAX_WEIGHT).withStyle(ChatFormatting.GRAY));
	}

	public void onDestroyed(ItemEntity p_150728_) {
		ItemUtils.onContainerDestroyed(p_150728_, Stream.of(getContents(p_150728_.getItem())));
	}

	public SoundEvent getInsertSound() {
		return CCSoundEvents.PACKING_CONTAINER_INSERT.get();
	}

	private void playRemoveOneSound(Entity p_186343_) {
		p_186343_.playSound(CCSoundEvents.PACKING_CONTAINER_REMOVE_ONE.get(), 0.8F, 0.8F + p_186343_.level().getRandom().nextFloat() * 0.4F);
	}

	private void playDropContentsSound(Entity p_186354_) {
		p_186354_.playSound(CCSoundEvents.PACKING_CONTAINER_DROP_CONTENTS.get(), 0.8F, 0.8F + p_186354_.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(Entity p_186352_) {
		p_186352_.playSound(this.getInsertSound(), 0.8F, 0.8F + p_186352_.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertFailSound(Entity p_186352_) {
		p_186352_.playSound(CCSoundEvents.PACKING_CONTAINER_INSERT_FAIL.get(), 0.8F, 0.8F + p_186352_.level().getRandom().nextFloat() * 0.4F);
	}

	public static class PackingContainerTooltip implements TooltipComponent {
		private final ItemStack item;

		public PackingContainerTooltip(ItemStack p_150677_) {
			this.item = p_150677_;
		}

		public ItemStack getItems() {
			return this.item;
		}
	}

	public static boolean addToContainer(Inventory inventory, ItemStack otherStack) {
		for (NonNullList<ItemStack> list : inventory.compartments) {
			for (ItemStack stack : list) {
				if (stack.getItem() instanceof PackingContainerItem item) {
					CompoundTag tag = stack.getOrCreateTag();
					if (tag.contains(TAG_ITEM)) {
						int i = add(stack, otherStack);
						if (i > 0) {
							ServerPlayer player = (ServerPlayer) inventory.player;
							ServerLevel level = (ServerLevel) player.level();
							level.playSound(null, player.getX(), player.getY(), player.getZ(), item.getInsertSound(), SoundSource.PLAYERS, 0.8F, 0.8F + level.getRandom().nextFloat() * 0.4F);
							otherStack.shrink(i);
							stack.setPopTime(5);
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}
