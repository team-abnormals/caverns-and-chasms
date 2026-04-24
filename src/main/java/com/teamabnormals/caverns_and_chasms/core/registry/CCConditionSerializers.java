package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition.Serializer;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CCConditionSerializers {
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<MapCodec<? extends ICondition>, Serializer> CONFIG = CONDITION_SERIALIZERS.register("config", () -> new ConfigValueCondition.Serializer(DataUtil.getConfigValues(CCConfig.COMMON, CCConfig.CLIENT)));

	public static class CCConditions {
		public static final ConfigValueCondition TRIAL_CHAMBERS_REPALETTE = config(CCConfig.COMMON.trialChambersRepalette, "trial_chambers_repalette");

		public static ConfigValueCondition config(ModConfigSpec.ConfigValue<?> value, String key, boolean inverted) {
			return new ConfigValueCondition(CONFIG.get(), value, key, Maps.newHashMap(), inverted);
		}

		public static ConfigValueCondition config(ModConfigSpec.ConfigValue<?> value, String key) {
			return config(value, key, false);
		}
	}
}