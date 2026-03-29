package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class TinCanItem extends BundleItem {

	public TinCanItem(Properties properties) {
		super(properties);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		NonNullList<ItemStack> nonnulllist = NonNullList.create();
		getContents(stack).forEach(nonnulllist::add);
		return Optional.of(new TinCanTooltip(nonnulllist, getContentWeight(stack)));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Items")) {
			ListTag items = tag.getList("Items", 10);
			if (!items.isEmpty()) {
				int i = 0;
				CompoundTag itemTag = items.getCompound(0);
				ItemStack itemStack = ItemStack.of(itemTag);
				UseOnContext newContext = new UseOnContext(context.getLevel(), context.getPlayer(), context.getHand(), itemStack, context.getHitResult());
				return itemStack.useOn(newContext);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Items")) {
			ListTag items = tag.getList("Items", 10);
			if (!items.isEmpty()) {
				int i = 0;
				CompoundTag itemTag = items.getCompound(0);
				ItemStack itemStack = ItemStack.of(itemTag);
				return itemStack.use(level, player, hand);
			}
		}
		return InteractionResultHolder.fail(stack);
	}

	@Override
	public void playRemoveOneSound(Entity entity) {
		entity.playSound(CCSoundEvents.PACKING_CONTAINER_REMOVE_ONE.get(), 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	@Override
	public void playInsertSound(Entity entity) {
		entity.playSound(CCSoundEvents.PACKING_CONTAINER_INSERT.get(), 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	public record TinCanTooltip(NonNullList<ItemStack> items, int weight) implements TooltipComponent {
	}
}
