package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CopperHorseArmorItem extends HorseArmorItem {

	public CopperHorseArmorItem(int armorValue, String tierArmor, Properties builder) {
		super(armorValue, CavernsAndChasms.location("textures/entity/horse/armor/horse_armor_" + tierArmor + ".png"), builder);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(ArmorItem.Type.CHESTPLATE);
		builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Copper armor slowness", -0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		return slot == EquipmentSlot.CHEST ? builder.build() : super.getAttributeModifiers(slot, stack);
	}
}