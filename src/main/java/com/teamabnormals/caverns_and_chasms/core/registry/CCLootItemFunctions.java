package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.loot.FortuneEnchantFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCLootItemFunctions {
	public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<?>> FORTUNE_ENCHANT = LOOT_FUNCTION_TYPES.register("fortune_enchant", () -> new LootItemFunctionType<>(new FortuneEnchantFunction.Serializer()));
}