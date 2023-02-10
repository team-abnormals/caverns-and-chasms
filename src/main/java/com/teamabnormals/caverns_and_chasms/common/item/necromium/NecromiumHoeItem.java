package com.teamabnormals.caverns_and_chasms.common.item.necromium;

import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class NecromiumHoeItem extends HoeItem implements NecromiumItem  {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(CCItems.NECROMIUM_AXE);

	public NecromiumHoeItem(Tier tier, int maxDamage, float attackSpeedIn, Properties builder) {
		super(tier, maxDamage, attackSpeedIn, builder);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return this.addSlownessInflictionAttribute(1.0F, slot, super.getAttributeModifiers(slot, stack));
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		this.inflictSlowness(stack, target);
		return super.hurtEnemy(stack, target, attacker);
	}


	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}