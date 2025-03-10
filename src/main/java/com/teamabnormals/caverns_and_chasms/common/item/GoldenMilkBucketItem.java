package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;

public class GoldenMilkBucketItem extends MilkBucketItem {

	public GoldenMilkBucketItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!level.isClientSide()) {
			entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
		}

		if (entity instanceof ServerPlayer player) {
			CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
			player.awardStat(Stats.ITEM_USED.get(this));
		}

		return GoldenBucketItem.getEmptySuccessItem(stack, entity instanceof Player player ? player : null);
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		return GoldenBucketItem.decreaseFluidLevel(stack);
	}
}