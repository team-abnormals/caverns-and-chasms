package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SilverHorseArmorItem extends AnimalArmorItem {

	public SilverHorseArmorItem(Holder<ArmorMaterial> armorMaterial, Item.Properties builder) {
		super(armorMaterial, BodyType.EQUESTRIAN, false, builder);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
		EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation name = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		modifiers = modifiers.withModifierAdded(CCAttributes.MAGIC_PROTECTION, new AttributeModifier(name, 0.4D, Operation.ADD_VALUE), slot);
		return modifiers;
	}
}