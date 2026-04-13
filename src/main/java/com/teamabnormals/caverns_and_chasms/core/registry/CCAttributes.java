package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = register("magic_damage", 0.0D, 0.0D, 30.0D);
	public static final DeferredHolder<Attribute, Attribute> MAGIC_PROTECTION = registerPercentage("magic_protection", 0.0D, 0.0D, 5.0D);
	public static final DeferredHolder<Attribute, Attribute> EXPERIENCE_BOOST = registerPercentage("experience_boost", 0.0D, 0.0D, 5.0D);
	public static final DeferredHolder<Attribute, Attribute> LIFESTEAL = registerPercentage("lifesteal", 0.0D, 0.0D, 5.0D);
	public static final DeferredHolder<Attribute, Attribute> STEALTH = registerPercentage("stealth", 0.0D, 0.0D, 1.0D);
	public static final DeferredHolder<Attribute, Attribute> SLOWNESS_INFLICTION = register("slowness_infliction", 0.0D, 0.0D, 30.0D);
	public static final DeferredHolder<Attribute, Attribute> SLOWNESS_RETRIBUTION = register("slowness_retribution", 0.0D, 0.0D, 30.0D);

	private static DeferredHolder<Attribute, Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue) {
		return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + CavernsAndChasms.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue));
	}

	private static DeferredHolder<Attribute, Attribute> registerPercentage(String name, double defaultValue, double minimumValue, double maximumValue) {
		return ATTRIBUTES.register(name, () -> new PercentageAttribute("attribute." + CavernsAndChasms.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue));
	}

	@SubscribeEvent
	public static void onEntityModifyAttributes(EntityAttributeModificationEvent event) {
		event.getTypes().forEach(entityType -> {
			event.add(entityType, STEALTH);
			event.add(entityType, MAGIC_PROTECTION);
			event.add(entityType, MAGIC_DAMAGE);
			event.add(entityType, EXPERIENCE_BOOST);
			event.add(entityType, SLOWNESS_INFLICTION);
			event.add(entityType, SLOWNESS_RETRIBUTION);
			event.add(entityType, LIFESTEAL);
		});
	}
}
