package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCDecoratedPotPatterns {
	public static final DeferredRegister<String> DECORATED_POT_PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERNS, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<String> BOOM = register("boom_pottery_pattern");
	public static final RegistryObject<String> CAST = register("cast_pottery_pattern");
	public static final RegistryObject<String> RIDE = register("ride_pottery_pattern");
	public static final RegistryObject<String> STALKER = register("stalker_pottery_pattern");

	public static RegistryObject<String> register(String name) {
		return DECORATED_POT_PATTERNS.register(name, () -> name);
	}

	public static void registerDecoratedPotPatterns() {
		DataUtil.registerDecoratedPotPattern(Pair.of(CCItems.BOOM_POTTERY_SHERD.get(), BOOM));
		DataUtil.registerDecoratedPotPattern(Pair.of(CCItems.CAST_POTTERY_SHERD.get(), CAST));
		DataUtil.registerDecoratedPotPattern(Pair.of(CCItems.RIDE_POTTERY_SHERD.get(), RIDE));
		DataUtil.registerDecoratedPotPattern(Pair.of(CCItems.STALKER_POTTERY_SHERD.get(), STALKER));
	}
}