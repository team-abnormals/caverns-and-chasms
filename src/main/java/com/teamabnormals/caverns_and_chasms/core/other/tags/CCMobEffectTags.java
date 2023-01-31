package com.teamabnormals.caverns_and_chasms.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public class CCMobEffectTags {
	public static final TagKey<MobEffect> BEJEWELED_APPLE_CANNOT_INFLICT = mobEffectTag("bejeweled_apple_cannot_inflict");

	private static TagKey<MobEffect> mobEffectTag(String name) {
		return TagUtil.mobEffectTag(CavernsAndChasms.MOD_ID, name);
	}
}
