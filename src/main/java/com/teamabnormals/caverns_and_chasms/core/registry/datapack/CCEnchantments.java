package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEnchantmentTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantmentEffects;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class CCEnchantments {
	public static final ResourceKey<Enchantment> CONCEAL = create("conceal");
	public static final ResourceKey<Enchantment> OBSCURITY = create("obscurity");
	public static final ResourceKey<Enchantment> EXTENDING = create("extending");

	public static void bootstrap(BootstrapContext<Enchantment> context) {
		HolderGetter<Item> items = context.lookup(Registries.ITEM);
		HolderGetter<Enchantment> enchants = context.lookup(Registries.ENCHANTMENT);

		register(context, EXTENDING, Enchantment.enchantment(Enchantment.definition(
				items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 2, 2, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(60, 10), 4, EquipmentSlotGroup.LEGS
		)).withEffect(EnchantmentEffectComponents.ATTRIBUTES,
				new EnchantmentAttributeEffect(CavernsAndChasms.location("enchantment.extending"), Attributes.BLOCK_INTERACTION_RANGE, LevelBasedValue.perLevel(1.0F), AttributeModifier.Operation.ADD_VALUE)
		));

		register(context, CONCEAL, Enchantment.enchantment(Enchantment.definition(
				items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 2, 3, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(60, 10), 4, EquipmentSlotGroup.HEAD
		)).withEffect(EnchantmentEffectComponents.ATTRIBUTES,
				new EnchantmentAttributeEffect(CavernsAndChasms.location("enchantment.conceal"), CCAttributes.STEALTH, LevelBasedValue.perLevel(0.1F), AttributeModifier.Operation.ADD_VALUE)
		).exclusiveWith(enchants.getOrThrow(CCEnchantmentTags.COWL_EXCLUSIVE)));

		register(context, OBSCURITY, Enchantment.enchantment(Enchantment.definition(
				items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE), 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, EquipmentSlotGroup.HEAD
		)).withEffect(CCEnchantmentEffects.INVISIBLE_WHEN_CROUCHING.get()).exclusiveWith(enchants.getOrThrow(CCEnchantmentTags.COWL_EXCLUSIVE)));
	}

	private static ResourceKey<Enchantment> create(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, CavernsAndChasms.location(name));
	}

	private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
		context.register(key, builder.build(key.location()));
	}
}