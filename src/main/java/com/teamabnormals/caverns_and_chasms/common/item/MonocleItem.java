package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;

public class MonocleItem extends SpyglassItem implements Equipable {

	public MonocleItem(Properties properties) {
		super(properties);
	}

	public static boolean isWearingMonocle(LivingEntity entity) {
		return entity.getItemBySlot(EquipmentSlot.HEAD).is(CCItems.MONOCLE.get());
	}

	public static boolean isScopingMonocle(LivingEntity entity) {
		return entity.isUsingItem() && entity.getUseItem().is(CCItems.MONOCLE.get());
	}

	public static boolean isUsingMonocle(LivingEntity entity) {
		return isScopingMonocle(entity) || isWearingMonocle(entity);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.playSound(CCSoundEvents.MONOCLE_USE.get(), 1.0F, 1.0F);
		player.awardStat(Stats.ITEM_USED.get(this));
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		this.stopUsing(entity);
		return stack;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int i) {
		this.stopUsing(entity);
	}

	private void stopUsing(LivingEntity entity) {
		entity.playSound(CCSoundEvents.MONOCLE_STOP_USING.get(), 1.0F, 1.0F);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return this.getEquipmentSlot(new ItemStack(this));
	}

	@Override
	public SoundEvent getEquipSound() {
		return CCSoundEvents.MONOCLE_EQUIP.get();
	}
}