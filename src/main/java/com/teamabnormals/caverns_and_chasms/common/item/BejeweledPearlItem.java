package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.ThrownBejeweledPearl;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class BejeweledPearlItem extends Item {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.ENDER_PEARL);

	public BejeweledPearlItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int releaseTime) {
		this.throwPearl(stack, level, entity, this.getUseDuration(stack) - releaseTime);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		return this.throwPearl(stack, level, entity, getMaxLifetime() - getChargeStageDuration());
	}

	private ItemStack throwPearl(ItemStack stack, Level level, LivingEntity entity, int useTime) {
		if (entity instanceof Player player) {
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			player.getCooldowns().addCooldown(this, 20);
			if (!level.isClientSide) {
				ThrownBejeweledPearl pearl = new ThrownBejeweledPearl(level, player);
				pearl.setLife(getChargeStage(useTime) * getChargeStageDuration());
				pearl.setItem(stack);
				pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
				level.addFreshEntity(pearl);
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return getMaxLifetime();
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	public static int getChargeStage(int useTime) {
		return Mth.floor(useTime / getChargeStageDuration());
	}

	public static int getChargeStageDuration() {
		return 4;
	}

	public static int getChargeStages() {
		return 8;
	}

	public static int getMaxLifetime() {
		return getChargeStages() * getChargeStageDuration();
	}
}
