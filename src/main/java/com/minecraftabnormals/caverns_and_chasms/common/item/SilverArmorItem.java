package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.util.LazyValue;

import java.util.UUID;

public class SilverArmorItem extends ArmorItem {
	private static final UUID MODIFIER = UUID.fromString("b220f09e-6a72-482e-9e34-6bc9e6b7e749");
	private final LazyValue<Multimap<Attribute, AttributeModifier>> attributes;

	public SilverArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
		this.attributes = new LazyValue<>(() -> {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getAttributeModifiers(slot));
			builder.put(CCAttributes.DEFENSIVE_AFFLICTION_CHANCE.get(), new AttributeModifier(MODIFIER, "Affliction chance", 0.25F, AttributeModifier.Operation.ADDITION));
			return builder.build();
		});
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == this.slot ? this.attributes.getValue() : super.getAttributeModifiers(equipmentSlot);
	}
}
