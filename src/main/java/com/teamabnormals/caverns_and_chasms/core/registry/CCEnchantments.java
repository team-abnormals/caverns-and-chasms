package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.enchantment.ThievingEnchantment;
import com.teamabnormals.caverns_and_chasms.common.item.CowlItem;
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

	public static final RegistryObject<Enchantment> THIEVING = ENCHANTMENTS.register("thieving", () -> new ThievingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.HEAD));
}