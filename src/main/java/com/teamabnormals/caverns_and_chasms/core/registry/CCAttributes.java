package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CCAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<Attribute> WEAKNESS_AURA = register("weakness_aura", 0.0D, 0.0D, 30.0D);
	public static final RegistryObject<Attribute> LIFESTEAL = register("lifesteal", 0.0D, 0.0D, 5.0D);

	private static RegistryObject<Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue) {
		return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + CavernsAndChasms.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue));
	}
}
