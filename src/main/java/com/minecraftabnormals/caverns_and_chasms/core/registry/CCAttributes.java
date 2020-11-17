package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CCAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, CavernsAndChasms.MODID);

    public static final RegistryObject<Attribute> OFFENSIVE_AFFLICTION_CHANCE = ATTRIBUTES.register("offensive_affliction_chance", () -> new RangedAttribute("attribute.name.generic.offensive_affliction_chance", 0.0D, 0.0D, 1.0D));
    public static final RegistryObject<Attribute> DEFENSIVE_AFFLICTION_CHANCE = ATTRIBUTES.register("defensive_affliction_chance", () -> new RangedAttribute("attribute.name.generic.defensive_affliction_chance", 0.0D, 0.0D, 1.0D));
    public static final RegistryObject<Attribute> EXPERIENCE_BOOST = ATTRIBUTES.register("experience_boost", () -> new RangedAttribute("attribute.name.generic.experience_boost", 0.0D, 0.0D, 1.0D));
}
