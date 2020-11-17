package com.minecraftabnormals.caverns_and_chasms.common.item.silver;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.potion.EffectInstance;

public class SilverShovelItem extends ShovelItem {

	public SilverShovelItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
		super(tier, attackDamageIn, attackSpeedIn, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(target.isEntityUndead()) target.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
		return super.hitEntity(stack, target, attacker);
	}
}
