package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.item.component.PackingContainerContents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;

public class PackingContainerItem extends Item {
	private static final String TAG_ITEM = "Item";
	private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

	public PackingContainerItem(Item.Properties properties) {
		super(properties);
	}

	public static float getFullnessDisplay(ItemStack stack) {
		PackingContainerContents contents = stack.getOrDefault(CCDataComponents.PACKING_CONTAINER_CONTENTS, PackingContainerContents.EMPTY);
		return contents.weight().floatValue();
	}

	@Override
	public Component getName(ItemStack stack) {
		if (stack.has(CCDataComponents.PACKING_CONTAINER_CONTENTS)) {
			ItemStack item = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS).items();
			MutableComponent hoverName = Component.empty().append(item.getHoverName());
			if (item.has(DataComponents.CUSTOM_NAME)) {
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
			PackingContainerContents contents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
			if (contents == null) {
				return false;
			} else {
				ItemStack itemstack = slot.getItem();
				PackingContainerContents.Mutable mutable = new PackingContainerContents.Mutable(contents);
				if (itemstack.isEmpty()) {
					this.playRemoveOneSound(player);
					ItemStack removeStack = mutable.removeOne();
					if (removeStack != null) {
						ItemStack insertStack = slot.safeInsert(removeStack);
						mutable.tryInsert(insertStack);
					}
				} else if (itemstack.canFitInsideContainerItems()) {
					int i = mutable.tryTransfer(slot, player);
					if (i > 0) {
						this.playInsertSound(player);
					} else {
						this.playInsertFailSound(player);
					}
				} else {
					this.playInsertFailSound(player);
				}

				stack.set(CCDataComponents.PACKING_CONTAINER_CONTENTS, mutable.toImmutable());
				return true;
			}
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (stack.getCount() != 1) return false;
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			PackingContainerContents contents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
			if (contents == null) {
				return false;
			} else {
				PackingContainerContents.Mutable mutable = new PackingContainerContents.Mutable(contents);
				if (otherStack.isEmpty()) {
					ItemStack removeStack = mutable.removeOne();
					if (removeStack != null) {
						this.playRemoveOneSound(player);
						access.set(removeStack);
					}
				} else {
					int i = mutable.tryInsert(otherStack);
					if (i > 0) {
						this.playInsertSound(player);
					} else {
						this.playInsertFailSound(player);
					}
				}

				player.containerMenu.slotsChanged(player.getInventory());
				stack.set(CCDataComponents.PACKING_CONTAINER_CONTENTS, mutable.toImmutable());
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (dropContents(stack, player)) {
			this.playDropContentsSound(player);
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(stack);
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		PackingContainerContents contents = stack.getOrDefault(CCDataComponents.PACKING_CONTAINER_CONTENTS, PackingContainerContents.EMPTY);
		return contents.weight().compareTo(Fraction.ZERO) > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		PackingContainerContents contents = stack.getOrDefault(CCDataComponents.PACKING_CONTAINER_CONTENTS, PackingContainerContents.EMPTY);
		return Math.min(1 + Mth.mulAndTruncate(contents.weight(), 12), 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return BAR_COLOR;
	}

	private static boolean dropContents(ItemStack stack, Player player) {
		PackingContainerContents contents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
		if (contents != null && !contents.isEmpty()) {
			stack.set(CCDataComponents.PACKING_CONTAINER_CONTENTS, PackingContainerContents.EMPTY);
			if (player instanceof ServerPlayer) {
				player.drop(contents.itemsCopy(), true);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
				? Optional.ofNullable(stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS)).map(PackingContainerTooltip::new)
				: Optional.empty();
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		PackingContainerContents contents = stack.get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
		if (contents != null) {
			int i = Mth.mulAndTruncate(contents.weight(), 512);
			tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", i, 512).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void onDestroyed(ItemEntity itemEntity) {
		PackingContainerContents contents = itemEntity.getItem().get(CCDataComponents.PACKING_CONTAINER_CONTENTS);
		if (contents != null) {
			itemEntity.getItem().set(CCDataComponents.PACKING_CONTAINER_CONTENTS, PackingContainerContents.EMPTY);
			ItemUtils.onContainerDestroyed(itemEntity, List.of(contents.itemsCopy()));
		}
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

	public record PackingContainerTooltip(PackingContainerContents contents) implements TooltipComponent {
	}
}
