package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.*;

public class CCParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<SimpleParticleType> CURSED_FLAME = createBasicParticleType(true, "cursed_flame");
	public static final RegistryObject<SimpleParticleType> CURSED_AMBIENT = createBasicParticleType(true, "cursed_ambient");
	public static final RegistryObject<SimpleParticleType> MIME_ENERGY = createBasicParticleType(true, "mime_energy");
	public static final RegistryObject<SimpleParticleType> MIME_SPARK = createBasicParticleType(true, "mime_spark");

	private static RegistryObject<SimpleParticleType> createBasicParticleType(boolean alwaysShow, String name) {
		return PARTICLES.register(name, () -> new SimpleParticleType(alwaysShow));
	}

	@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegisterParticleFactories {

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
			Minecraft.getInstance().particleEngine.register(CURSED_FLAME.get(), FlameParticle.Provider::new);
			Minecraft.getInstance().particleEngine.register(CURSED_AMBIENT.get(), LavaParticle.Provider::new);
			Minecraft.getInstance().particleEngine.register(MIME_ENERGY.get(), PlayerCloudParticle.Provider::new);
			Minecraft.getInstance().particleEngine.register(MIME_SPARK.get(), PlayerCloudParticle.Provider::new);
		}
	}
}
