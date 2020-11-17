package com.minecraftabnormals.caverns_and_chasms.common.item.silver;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;

public class SilverHoeItem extends HoeItem {

	public SilverHoeItem(IItemTier tier, int maxDamage, float attackSpeedIn, Properties builder) {
		super(tier, maxDamage, attackSpeedIn, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(target.isEntityUndead()) target.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
		return super.hitEntity(stack, target, attacker);
	}
}
