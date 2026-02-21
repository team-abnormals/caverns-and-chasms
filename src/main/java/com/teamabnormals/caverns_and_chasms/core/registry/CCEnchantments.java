package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.enchantment.ConcealEnchantment;
import com.teamabnormals.caverns_and_chasms.common.enchantment.ExtendingEnchantment;
import com.teamabnormals.caverns_and_chasms.common.enchantment.ObscurityEnchantment;
import com.teamabnormals.caverns_and_chasms.common.item.CowlItem;
import com.teamabnormals.caverns_and_chasms.common.item.ToolbeltItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, CavernsAndChasms.MOD_ID);

	public static EnchantmentCategory COWL = EnchantmentCategory.create("COWL", item -> item instanceof CowlItem);
	public static EnchantmentCategory TOOLBELT = EnchantmentCategory.create("TOOLBELT", item -> item instanceof ToolbeltItem);

	public static final RegistryObject<Enchantment> CONCEAL = ENCHANTMENTS.register("conceal", () -> new ConcealEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.HEAD));
	public static final RegistryObject<Enchantment> OBSCURITY = ENCHANTMENTS.register("obscurity", () -> new ObscurityEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.HEAD));

	public static final RegistryObject<Enchantment> EXTENDING = ENCHANTMENTS.register("extending", () -> new ExtendingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.LEGS));

}