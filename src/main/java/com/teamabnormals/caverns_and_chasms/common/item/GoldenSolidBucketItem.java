package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class GoldenSolidBucketItem extends SolidBucketItem {

	public GoldenSolidBucketItem(Block block, SoundEvent placeSound, Item.Properties properties) {
		super(block, placeSound, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		Level level = context.getLevel();
		ItemStack stack = context.getItemInHand();
		if (GoldenBucketItem.canBeFilled(stack) && player != null && !player.isCrouching()) {
			InteractionResultHolder<ItemStack> result = CCItems.GOLDEN_BUCKET.get().use(level, player, hand);
			if (result.getResult().consumesAction()) {
				player.setItemInHand(hand, result.getObject());
				return result.getResult();
			}
		}

		ItemStack newStack = stack.copy();
		InteractionResult result = super.useOn(context);
		if (result.consumesAction() && player != null && !player.isCreative()) {
			player.setItemInHand(hand, GoldenBucketItem.decreaseFluidLevel(newStack));
		}

		return result;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		return GoldenBucketItem.decreaseFluidLevel(stack.copy());
	}
}