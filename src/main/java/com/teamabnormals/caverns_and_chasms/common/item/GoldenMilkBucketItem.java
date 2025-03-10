package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.core.other.tags.BlueprintEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
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
		return GoldenBucketItem.decreaseFluidLevel(stack.copy());
	}

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event) {
		Player player = event.getEntity();
		ItemStack stack = event.getItemStack();
		Level level = event.getLevel();
		InteractionHand hand = event.getHand();

		if (event.getTarget() instanceof LivingEntity entity && entity.getType().is(BlueprintEntityTypeTags.MILKABLE) && !entity.isBaby()) {
			if (GoldenBucketItem.isEmpty(stack) || (stack.is(CCItems.GOLDEN_MILK_BUCKET.get()) && GoldenBucketItem.canBeFilled(stack))){
				ItemStack milkBucket = new ItemStack(CCItems.GOLDEN_MILK_BUCKET.get());
				if (!GoldenBucketItem.isEmpty(stack)) {
					GoldenBucketItem.setFluidLevel(milkBucket, GoldenBucketItem.getFluidLevel(stack) + 1);
				}
				milkBucket = GoldenBucketItem.createFilledResult(stack, player, milkBucket);

				player.playSound(entity instanceof Goat goat ? goat.getMilkingSound() : SoundEvents.COW_MILK, 1.0F, 1.0F);
				entity.gameEvent(GameEvent.ENTITY_INTERACT);
				player.setItemInHand(hand, milkBucket);

				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
				event.setCanceled(true);
			}
		}
	}
}