package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.client.particle.AfflictionParticle.DamageProvider;
import com.teamabnormals.caverns_and_chasms.client.particle.AfflictionParticle.SparkProvider;
import com.teamabnormals.caverns_and_chasms.client.particle.ChipParticle;
import com.teamabnormals.caverns_and_chasms.client.particle.StoneDustParticle;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.LavaParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.PlayerCloudParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<SimpleParticleType> CURSED_FLAME = createBasicParticleType(true, "cursed_flame");
	public static final RegistryObject<SimpleParticleType> CURSED_AMBIENT = createBasicParticleType(true, "cursed_ambient");
	public static final RegistryObject<SimpleParticleType> MIME_ENERGY = createBasicParticleType(true, "mime_energy");
	public static final RegistryObject<SimpleParticleType> MIME_SPARK = createBasicParticleType(true, "mime_spark");
	public static final RegistryObject<SimpleParticleType> AFFLICTION_DAMAGE = createBasicParticleType(true, "affliction_damage");
	public static final RegistryObject<SimpleParticleType> AFFLICTION_SPARK = createBasicParticleType(true, "affliction_spark");
	public static final RegistryObject<SimpleParticleType> STONE_DUST = createBasicParticleType(true, "stone_dust");
	public static final RegistryObject<SimpleParticleType> DEEPSLATE_DUST = createBasicParticleType(true, "deepslate_dust");
	public static final RegistryObject<SimpleParticleType> STONE_CHIP = createBasicParticleType(true, "stone_chip");
	public static final RegistryObject<SimpleParticleType> DEEPSLATE_CHIP = createBasicParticleType(true, "deepslate_chip");

	private static RegistryObject<SimpleParticleType> createBasicParticleType(boolean alwaysShow, String name) {
		return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
		ParticleEngine manager = Minecraft.getInstance().particleEngine;
		manager.register(CURSED_FLAME.get(), FlameParticle.Provider::new);
		manager.register(CURSED_AMBIENT.get(), LavaParticle.Provider::new);
		manager.register(MIME_ENERGY.get(), PlayerCloudParticle.Provider::new);
		manager.register(MIME_SPARK.get(), PlayerCloudParticle.Provider::new);
		manager.register(AFFLICTION_DAMAGE.get(), DamageProvider::new);
		manager.register(AFFLICTION_SPARK.get(), SparkProvider::new);
		manager.register(STONE_DUST.get(), StoneDustParticle.Provider::new);
		manager.register(DEEPSLATE_DUST.get(), StoneDustParticle.Provider::new);
		manager.register(STONE_CHIP.get(), ChipParticle.Provider::new);
		manager.register(DEEPSLATE_CHIP.get(), ChipParticle.Provider::new);
	}
}
