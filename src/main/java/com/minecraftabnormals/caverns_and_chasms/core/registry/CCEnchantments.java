package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.enchantment.ProspectingEnchantment;
import com.minecraftabnormals.caverns_and_chasms.common.enchantment.TreasuringEnchantment;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CCEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Enchantment> TREASURING = ENCHANTMENTS.register("treasuring", TreasuringEnchantment::new);
	public static final RegistryObject<Enchantment> PROSPECTING = ENCHANTMENTS.register("prospecting", ProspectingEnchantment::new);

	public static final EnchantmentCategory ORE_DETECTOR = EnchantmentCategory.create("ORE_DETECTOR", (item) -> item == CCItems.ORE_DETECTOR.get());

	public static void registerEnchantmentTypes() {
		CreativeModeTab.TAB_TOOLS.setEnchantmentCategories(DataUtil.concatArrays(CreativeModeTab.TAB_TOOLS.getEnchantmentCategories(), ORE_DETECTOR));
	}
}
