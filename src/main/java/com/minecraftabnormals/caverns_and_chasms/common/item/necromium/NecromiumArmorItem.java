package com.minecraftabnormals.caverns_and_chasms.common.item.necromium;

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

public class NecromiumArmorItem extends ArmorItem {
	private static final UUID MODIFIER = UUID.fromString("e4013bfe-9fb9-4503-8df2-d5a027de2d14");
	private final LazyValue<Multimap<Attribute, AttributeModifier>> attributes;

	public NecromiumArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
		this.attributes = new LazyValue<>(() -> {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getAttributeModifiers(slot));
			builder.put(CCAttributes.WEAKNESS_AURA.get(), new AttributeModifier(MODIFIER, "Weakness aura", 1.0D, AttributeModifier.Operation.ADDITION));
			return builder.build();
		});
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == this.slot ? this.attributes.getValue() : super.getAttributeModifiers(equipmentSlot);
	}
}
