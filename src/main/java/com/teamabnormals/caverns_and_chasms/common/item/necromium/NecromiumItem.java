package com.teamabnormals.caverns_and_chasms.common.item.necromium;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.UUID;

public interface NecromiumItem {
	UUID SLOWNESS_INFLICTION_UUID = UUID.fromString("47b62d26-2010-4a6a-9f87-ebe11c50f467");

	default Multimap<Attribute, AttributeModifier> addSlownessInflictionAttribute(float damage, EquipmentSlot slot, Multimap<Attribute, AttributeModifier> map) {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(map);
		builder.put(CCAttributes.SLOWNESS_INFLICTION.get(), new AttributeModifier(SLOWNESS_INFLICTION_UUID, "Slowness infliction", damage, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? builder.build() : map;
	}

	default void inflictSlowness(ItemStack stack, LivingEntity target) {
		Collection<AttributeModifier> slownessModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.SLOWNESS_INFLICTION.get());
		if (!slownessModifiers.isEmpty()) {
			int slownessLevel = (int) slownessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, slownessLevel));
		}
	}
}