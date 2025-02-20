package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;

public class MonocleItem extends SpyglassItem {

	public MonocleItem(Properties properties) {
		super(properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
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
}