package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class NetheriteHorseArmorItem extends AnimalArmorItem {

	public NetheriteHorseArmorItem(Holder<ArmorMaterial> armorMaterial, Properties builder) {
		super(armorMaterial, BodyType.EQUESTRIAN, false, builder);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
		EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation name = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		modifiers = modifiers.withModifierAdded(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(name, 0.4F, Operation.ADD_VALUE), slot);
		return modifiers;
	}
}