package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class CCEnchantmentTags {
	public static final TagKey<Enchantment> COWL_EXCLUSIVE = enchantmentTag("exclusive_set/cowl");

	public static TagKey<Enchantment> enchantmentTag(String name) {
		return TagUtil.enchantmentTag(CavernsAndChasms.MOD_ID, name);
	}
}