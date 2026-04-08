package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;

import java.util.UUID;

public class SilverHorseArmorItem extends AnimalArmorItem {

	public SilverHorseArmorItem(Holder<ArmorMaterial> armorMaterial, Item.Properties builder) {
		super(armorMaterial, BodyType.EQUESTRIAN, false, builder);
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