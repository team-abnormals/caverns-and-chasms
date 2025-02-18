package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class CCDamageTypeTags {
	public static final TagKey<DamageType> BYPASSES_TETHER_POTIONS = damageTypeTag("bypasses_tether_potions");

	private static TagKey<DamageType> damageTypeTag(String tagName) {
		return TagUtil.damageTypeTag(CavernsAndChasms.MOD_ID, tagName);
	}
}