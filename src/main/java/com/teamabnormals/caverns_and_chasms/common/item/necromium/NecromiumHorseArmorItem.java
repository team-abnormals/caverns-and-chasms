package com.teamabnormals.caverns_and_chasms.common.item.necromium;

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

public class NecromiumHorseArmorItem extends AnimalArmorItem {

	public NecromiumHorseArmorItem(Holder<ArmorMaterial> armorMaterial, Item.Properties builder) {
		super(armorMaterial, BodyType.EQUESTRIAN, false, builder);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getAttributeModifiers(slot, stack));
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(ArmorItem.Type.CHESTPLATE);
		builder.put(CCAttributes.SLOWNESS_INFLICTION.get(), new AttributeModifier(uuid, "Slowness infliction", 4.0F, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.CHEST ? builder.build() : super.getAttributeModifiers(slot, stack);
	}
}