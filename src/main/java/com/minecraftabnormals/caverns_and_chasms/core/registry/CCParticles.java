package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CCParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<BasicParticleType> CURSED_FLAME = createBasicParticleType(true, "cursed_flame");
	public static final RegistryObject<BasicParticleType> CURSED_AMBIENT = createBasicParticleType(true, "cursed_ambient");
	public static final RegistryObject<BasicParticleType> MIME_ENERGY = createBasicParticleType(true, "mime_energy");
	public static final RegistryObject<BasicParticleType> MIME_SPARK = createBasicParticleType(true, "mime_spark");

	private static RegistryObject<BasicParticleType> createBasicParticleType(boolean alwaysShow, String name) {
		return PARTICLES.register(name, () -> new BasicParticleType(alwaysShow));
	}

	@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegisterParticleFactories {

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
			Minecraft.getInstance().particles.registerFactory(CURSED_FLAME.get(), FlameParticle.Factory::new);
			Minecraft.getInstance().particles.registerFactory(CURSED_AMBIENT.get(), LavaParticle.Factory::new);
			Minecraft.getInstance().particles.registerFactory(MIME_ENERGY.get(), CloudParticle.Factory::new);
			Minecraft.getInstance().particles.registerFactory(MIME_SPARK.get(), CloudParticle.Factory::new);
		}
	}
}
