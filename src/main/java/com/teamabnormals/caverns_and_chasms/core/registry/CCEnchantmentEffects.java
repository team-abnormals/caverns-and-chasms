package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class CCEnchantmentEffects {
	public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> INVISIBLE_WHEN_CROUCHING = register("turn_invisible_when_crouching", b -> b.persistent(Unit.CODEC));

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<Builder<T>> operator) {
		return COMPONENTS.register(name, () -> operator.apply(DataComponentType.builder()).build());
	}
}
