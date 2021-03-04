package com.minecraftabnormals.caverns_and_chasms.common.item.silver;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.LazyValue;
import net.minecraft.util.NonNullList;

import java.util.UUID;

public class SilverArmorItem extends ArmorItem {
	private final LazyValue<Multimap<Attribute, AttributeModifier>> attributes;
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.GOLDEN_BOOTS);

	public SilverArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
		this.attributes = new LazyValue<>(() -> {
			UUID uuid = ArmorItem.ARMOR_MODIFIERS[slot.getIndex()];
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getAttributeModifiers(slot));
			builder.put(CCAttributes.AFFLICTION_CHANCE.get(), new AttributeModifier(uuid, "Affliction chance", 0.25F, AttributeModifier.Operation.ADDITION));
			return builder.build();
		});
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == this.slot ? this.attributes.getValue() : super.getAttributeModifiers(equipmentSlot);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
