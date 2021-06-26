package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.caverns_and_chasms.common.enchantment.ProspectingEnchantment;
import com.minecraftabnormals.caverns_and_chasms.common.enchantment.TreasuringEnchantment;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Array;

public class CCEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Enchantment> TREASURING = ENCHANTMENTS.register("treasuring", TreasuringEnchantment::new);
	public static final RegistryObject<Enchantment> PROSPECTING = ENCHANTMENTS.register("prospecting", ProspectingEnchantment::new);

	public static final EnchantmentType ORE_DETECTOR = EnchantmentType.create("ORE_DETECTOR", (item) -> item == CCItems.ORE_DETECTOR.get());

	public static void registerEnchantmentTypes() {
		ItemGroup.TAB_TOOLS.setEnchantmentCategories(DataUtil.add(ItemGroup.TAB_TOOLS.getEnchantmentCategories(), ORE_DETECTOR));
	}
}
