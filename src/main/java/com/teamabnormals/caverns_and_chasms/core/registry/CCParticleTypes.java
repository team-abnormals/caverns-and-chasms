package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.client.particle.AfflictionParticle.DamageProvider;
import com.teamabnormals.caverns_and_chasms.client.particle.AfflictionParticle.SparkProvider;
import com.teamabnormals.caverns_and_chasms.client.particle.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.PlayerCloudParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CCParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<SimpleParticleType> CUPRIC_FIRE_FLAME = registerSimpleParticleType(false, "cupric_fire_flame");
	public static final RegistryObject<SimpleParticleType> SMALL_CUPRIC_FIRE_FLAME = registerSimpleParticleType(false, "small_cupric_fire_flame");
	public static final RegistryObject<SimpleParticleType> MIME_ENERGY = registerSimpleParticleType(true, "mime_energy");
	public static final RegistryObject<SimpleParticleType> MIME_SPARK = registerSimpleParticleType(true, "mime_spark");
	public static final RegistryObject<SimpleParticleType> AFFLICTION_DAMAGE = registerSimpleParticleType(true, "affliction_damage");
	public static final RegistryObject<SimpleParticleType> AFFLICTION_SPARK = registerSimpleParticleType(false, "affliction_spark");
	public static final RegistryObject<SimpleParticleType> STONE_DUST = registerSimpleParticleType(true, "stone_dust");
	public static final RegistryObject<SimpleParticleType> DEEPSLATE_DUST = registerSimpleParticleType(true, "deepslate_dust");
	public static final RegistryObject<SimpleParticleType> STONE_CHIP = registerSimpleParticleType(false, "stone_chip");
	public static final RegistryObject<SimpleParticleType> DEEPSLATE_CHIP = registerSimpleParticleType(false, "deepslate_chip");
	public static final RegistryObject<SimpleParticleType> LAVA_LAMP_SMOKE = registerSimpleParticleType(true, "lava_lamp_smoke");
	public static final RegistryObject<SimpleParticleType> FLOODLIGHT_DUST = registerSimpleParticleType(false, "floodlight_dust");
	public static final RegistryObject<SimpleParticleType> SPINEL_BOOM_CIRCLE = registerSimpleParticleType(true, "spinel_boom_circle");
	public static final RegistryObject<SimpleParticleType> SPINEL_BOOM_STAR = registerSimpleParticleType(true, "spinel_boom_star");
	public static final RegistryObject<SimpleParticleType> SPINEL_BOOM_EMITTER = registerSimpleParticleType(true, "spinel_boom_emitter");
	public static final RegistryObject<SimpleParticleType> GOLEM_NOTE = registerSimpleParticleType(true, "golem_note");

	private static RegistryObject<SimpleParticleType> registerSimpleParticleType(boolean alwaysShow, String name) {
		return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
	}

	@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class RegisterParticles {
		@SubscribeEvent
		public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
			event.register(CUPRIC_FIRE_FLAME.get(), FlameParticle.Provider::new);
			event.register(SMALL_CUPRIC_FIRE_FLAME.get(), FlameParticle.SmallFlameProvider::new);
			event.register(MIME_ENERGY.get(), MimeEnergyParticle.Provider::new);
			event.register(MIME_SPARK.get(), PlayerCloudParticle.Provider::new);
			event.register(AFFLICTION_DAMAGE.get(), DamageProvider::new);
			event.register(AFFLICTION_SPARK.get(), SparkProvider::new);
			event.register(STONE_DUST.get(), StoneDustParticle.Provider::new);
			event.register(DEEPSLATE_DUST.get(), StoneDustParticle.Provider::new);
			event.register(STONE_CHIP.get(), ChipParticle.Provider::new);
			event.register(DEEPSLATE_CHIP.get(), ChipParticle.Provider::new);
			event.register(LAVA_LAMP_SMOKE.get(), LavaLampSmokeParticle.Provider::new);
			event.register(FLOODLIGHT_DUST.get(), FloodlightDustParticle.Provider::new);
			event.register(SPINEL_BOOM_CIRCLE.get(), HugeExplosionParticle.Provider::new);
			event.register(SPINEL_BOOM_STAR.get(), HugeExplosionParticle.Provider::new);
			event.register(SPINEL_BOOM_EMITTER.get(), new SpinelBoomParticle.Provider());
			event.register(GOLEM_NOTE.get(), GolemNoteParticle.Provider::new);
		}
	}
}
