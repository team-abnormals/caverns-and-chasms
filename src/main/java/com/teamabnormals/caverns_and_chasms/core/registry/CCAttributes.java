package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<Attribute, Attribute> STEALTH = register("stealth", 0.0D, 0.0D, 1.0D);
	public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = register("magic_damage", 0.0D, 0.0D, 30.0D);
	public static final DeferredHolder<Attribute, Attribute> MAGIC_PROTECTION = register("magic_protection", 0.0D, 0.0D, 30.0D);
	public static final DeferredHolder<Attribute, Attribute> EXPERIENCE_BOOST = register("experience_boost", 0.0D, 0.0D, 30.0D);
	public static final DeferredHolder<Attribute, Attribute> SLOWNESS_INFLICTION = register("slowness_infliction", 0.0D, 0.0D, 30.0D);
	public static final DeferredHolder<Attribute, Attribute> LIFESTEAL = register("lifesteal", 0.0D, 0.0D, 5.0D);

	private static DeferredHolder<Attribute, Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue) {
		return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + CavernsAndChasms.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue));
	}
}
