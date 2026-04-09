package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.teamabnormals.caverns_and_chasms.client.model.CopperArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class CopperArmorItem extends ArmorItem {

	public CopperArmorItem(Holder<ArmorMaterial> material, Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
		EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(type.getSlot());
		ResourceLocation name = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		modifiers = modifiers.withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(name, -0.1F, Operation.ADD_MULTIPLIED_TOTAL), slot);
		return modifiers;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return slot == EquipmentSlot.HEAD ? CopperArmorModel.INSTANCE : properties;
			}
		});
	}
}
