package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCDecoratedPotPatterns {
	public static final DeferredRegister<DecoratedPotPattern> DECORATED_POT_PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERN, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<DecoratedPotPattern, ?> BOOM = register("boom_pottery_pattern");
	public static final DeferredHolder<DecoratedPotPattern, ?> CAST = register("cast_pottery_pattern");
	public static final DeferredHolder<DecoratedPotPattern, ?> RIDE = register("ride_pottery_pattern");
	public static final DeferredHolder<DecoratedPotPattern, ?> STALKER = register("stalker_pottery_pattern");

	public static DeferredHolder<DecoratedPotPattern, ?> register(String name) {
		return DECORATED_POT_PATTERNS.register(name, () -> new DecoratedPotPattern(CavernsAndChasms.location(name + "_pottery_pattern")));
	}

	public static void registerDecoratedPotPatterns() {
		DataUtil.registerDecoratedPotPattern(
				Pair.of(CCItems.BOOM_POTTERY_SHERD.get(), BOOM),
				Pair.of(CCItems.CAST_POTTERY_SHERD.get(), CAST),
				Pair.of(CCItems.RIDE_POTTERY_SHERD.get(), RIDE),
				Pair.of(CCItems.STALKER_POTTERY_SHERD.get(), STALKER)
		);
	}
}