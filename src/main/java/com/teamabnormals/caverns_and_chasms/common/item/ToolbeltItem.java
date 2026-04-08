package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ToolbeltItem extends ArmorItem {

	public ToolbeltItem(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
		EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation name = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		modifiers = modifiers.withModifierAdded(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(name, 2.0F, Operation.ADD_VALUE), slot);
		return modifiers;
	}
}