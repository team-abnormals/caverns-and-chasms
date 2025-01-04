package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SilverHorseArmorItem extends HorseArmorItem {

	public SilverHorseArmorItem(Item.Properties builder) {
		super(7, new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/horse/armor/horse_armor_silver.png"), builder);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(ArmorItem.Type.CHESTPLATE);
		builder.put(CCAttributes.MAGIC_PROTECTION.get(), new AttributeModifier(uuid, "Magic protection", 0.4D, AttributeModifier.Operation.MULTIPLY_BASE));
		return slot == EquipmentSlot.CHEST ? builder.build() : super.getAttributeModifiers(slot, stack);
	}
}