package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.caverns_and_chasms.client.particle.*;
import com.teamabnormals.caverns_and_chasms.client.particle.SilverParticle.DamageProvider;
import com.teamabnormals.caverns_and_chasms.client.particle.SilverParticle.SparkProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.PlayerCloudParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CUPRIC_FIRE_FLAME = registerSimpleParticleType(false, "cupric_fire_flame");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_CUPRIC_FIRE_FLAME = registerSimpleParticleType(false, "small_cupric_fire_flame");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MIME_ENERGY = registerSimpleParticleType(true, "mime_energy");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MIME_SPARK = registerSimpleParticleType(true, "mime_spark");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SILVER_HIT = registerSimpleParticleType(true, "silver_hit");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SILVER_SPARK = registerSimpleParticleType(false, "silver_spark");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STONE_DUST = registerSimpleParticleType(true, "stone_dust");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEEPSLATE_DUST = registerSimpleParticleType(true, "deepslate_dust");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STONE_CHIP = registerSimpleParticleType(false, "stone_chip");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEEPSLATE_CHIP = registerSimpleParticleType(false, "deepslate_chip");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LAVA_LAMP_SMOKE = registerSimpleParticleType(true, "lava_lamp_smoke");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOODLIGHT_DUST = registerSimpleParticleType(false, "floodlight_dust");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EXPOSED_FLOODLIGHT_DUST = registerSimpleParticleType(false, "exposed_floodlight_dust");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WEATHERED_FLOODLIGHT_DUST = registerSimpleParticleType(false, "weathered_floodlight_dust");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OXIDIZED_FLOODLIGHT_DUST = registerSimpleParticleType(false, "oxidized_floodlight_dust");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPINEL_BOOM_CIRCLE = registerSimpleParticleType(true, "spinel_boom_circle");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPINEL_BOOM_STAR = registerSimpleParticleType(true, "spinel_boom_star");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPINEL_BOOM_EMITTER = registerSimpleParticleType(true, "spinel_boom_emitter");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOLEM_NOTE = registerSimpleParticleType(true, "golem_note");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TIN_SPARK = registerSimpleParticleType(false, "tin_spark");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLINT = registerSimpleParticleType(false, "flint");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TURQUOISE_BLUE = registerSimpleParticleType(false, "turquoise_blue");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TURQUOISE_GREEN = registerSimpleParticleType(false, "turquoise_green");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TURQUOISE_BLUE_STEP = registerSimpleParticleType(false, "turquoise_blue_step");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TURQUOISE_GREEN_STEP = registerSimpleParticleType(false, "turquoise_green_step");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ATONING_DAGGER = registerSimpleParticleType(true, "atoning_table_dagger");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ATONING_LETTER = registerSimpleParticleType(true, "atoning_table_letter");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DROOL_PUDDLE = registerSimpleParticleType(true, "drool_puddle");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DROOL = registerSimpleParticleType(true, "drool");
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LARGE_SMOKE_EMITTER = registerSimpleParticleType(true, "large_smoke_emitter");

	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> SPARKLER_SPARK = registerSparklerParticles("sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> WHITE_SPARKLER_SPARK = registerSparklerParticles("white_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> ORANGE_SPARKLER_SPARK = registerSparklerParticles("orange_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> MAGENTA_SPARKLER_SPARK = registerSparklerParticles("magenta_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> LIGHT_BLUE_SPARKLER_SPARK = registerSparklerParticles("light_blue_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> YELLOW_SPARKLER_SPARK = registerSparklerParticles("yellow_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> LIME_SPARKLER_SPARK = registerSparklerParticles("lime_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> PINK_SPARKLER_SPARK = registerSparklerParticles("pink_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> GRAY_SPARKLER_SPARK = registerSparklerParticles("gray_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> LIGHT_GRAY_SPARKLER_SPARK = registerSparklerParticles("light_gray_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> CYAN_SPARKLER_SPARK = registerSparklerParticles("cyan_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> PURPLE_SPARKLER_SPARK = registerSparklerParticles("purple_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> BLUE_SPARKLER_SPARK = registerSparklerParticles("blue_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> BROWN_SPARKLER_SPARK = registerSparklerParticles("brown_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> GREEN_SPARKLER_SPARK = registerSparklerParticles("green_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> RED_SPARKLER_SPARK = registerSparklerParticles("red_sparkler_spark");
	public static final Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> BLACK_SPARKLER_SPARK = registerSparklerParticles("black_sparkler_spark");

	private static DeferredHolder<ParticleType<?>, SimpleParticleType> registerSimpleParticleType(boolean alwaysShow, String name) {
		return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
	}

	private static Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> registerSparklerParticles(String name) {
		DeferredHolder<ParticleType<?>, SimpleParticleType> emitter = registerSimpleParticleType(true, name + "_emitter");
		return Pair.of(registerSimpleParticleType(true, name), emitter);
	}

	@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class RegisterParticles {
		@SubscribeEvent
		public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
			event.registerSpriteSet(CUPRIC_FIRE_FLAME.get(), FlameParticle.Provider::new);
			event.registerSpriteSet(SMALL_CUPRIC_FIRE_FLAME.get(), FlameParticle.SmallFlameProvider::new);
			event.registerSpriteSet(MIME_ENERGY.get(), MimeEnergyParticle.Provider::new);
			event.registerSpriteSet(MIME_SPARK.get(), PlayerCloudParticle.Provider::new);
			event.registerSpriteSet(SILVER_HIT.get(), DamageProvider::new);
			event.registerSpriteSet(SILVER_SPARK.get(), SparkProvider::new);
			event.registerSpriteSet(STONE_DUST.get(), StoneDustParticle.Provider::new);
			event.registerSpriteSet(DEEPSLATE_DUST.get(), StoneDustParticle.Provider::new);
			event.registerSpriteSet(STONE_CHIP.get(), ChipParticle.Provider::new);
			event.registerSpriteSet(DEEPSLATE_CHIP.get(), ChipParticle.Provider::new);
			event.registerSpriteSet(LAVA_LAMP_SMOKE.get(), LavaLampSmokeParticle.Provider::new);
			event.registerSpriteSet(FLOODLIGHT_DUST.get(), FloodlightDustParticle.Provider::new);
			event.registerSpriteSet(EXPOSED_FLOODLIGHT_DUST.get(), FloodlightDustParticle.Provider::new);
			event.registerSpriteSet(WEATHERED_FLOODLIGHT_DUST.get(), FloodlightDustParticle.Provider::new);
			event.registerSpriteSet(OXIDIZED_FLOODLIGHT_DUST.get(), FloodlightDustParticle.Provider::new);
			event.registerSpriteSet(SPINEL_BOOM_CIRCLE.get(), HugeExplosionParticle.Provider::new);
			event.registerSpriteSet(SPINEL_BOOM_STAR.get(), HugeExplosionParticle.Provider::new);
			event.registerSpecial(SPINEL_BOOM_EMITTER.get(), new SpinelBoomParticle.Provider());
			event.registerSpriteSet(GOLEM_NOTE.get(), GolemNoteParticle.Provider::new);
			event.registerSpriteSet(TIN_SPARK.get(), TinSparkParticle.Provider::new);
			event.registerSpriteSet(FLINT.get(), FlintParticle.Provider::new);
			event.registerSpriteSet(TURQUOISE_BLUE.get(), TurquoiseParticle.Provider::new);
			event.registerSpriteSet(TURQUOISE_GREEN.get(), TurquoiseParticle.Provider::new);
			event.registerSpriteSet(TURQUOISE_BLUE_STEP.get(), TurquoiseParticle.StepProvider::new);
			event.registerSpriteSet(TURQUOISE_GREEN_STEP.get(), TurquoiseParticle.StepProvider::new);
			event.registerSpriteSet(ATONING_DAGGER.get(), AtoningDaggerParticle.Provider::new);
			event.registerSpriteSet(ATONING_LETTER.get(), AtoningLetterParticle.Provider::new);
			event.registerSpriteSet(DROOL_PUDDLE.get(), DroolPuddleParticle.Provider::new);
			event.registerSpriteSet(DROOL.get(), DroolParticle.Provider::new);
			event.registerSpecial(LARGE_SMOKE_EMITTER.get(), new LargeSmokeSeedParticle.Provider());

			registerSparkler(event, SPARKLER_SPARK);
			registerSparkler(event, WHITE_SPARKLER_SPARK);
			registerSparkler(event, ORANGE_SPARKLER_SPARK);
			registerSparkler(event, MAGENTA_SPARKLER_SPARK);
			registerSparkler(event, LIGHT_BLUE_SPARKLER_SPARK);
			registerSparkler(event, YELLOW_SPARKLER_SPARK);
			registerSparkler(event, LIME_SPARKLER_SPARK);
			registerSparkler(event, PINK_SPARKLER_SPARK);
			registerSparkler(event, GRAY_SPARKLER_SPARK);
			registerSparkler(event, LIGHT_GRAY_SPARKLER_SPARK);
			registerSparkler(event, CYAN_SPARKLER_SPARK);
			registerSparkler(event, PURPLE_SPARKLER_SPARK);
			registerSparkler(event, BLUE_SPARKLER_SPARK);
			registerSparkler(event, BROWN_SPARKLER_SPARK);
			registerSparkler(event, GREEN_SPARKLER_SPARK);
			registerSparkler(event, RED_SPARKLER_SPARK);
			registerSparkler(event, BLACK_SPARKLER_SPARK);
		}

		public static void registerSparkler(RegisterParticleProvidersEvent event, Pair<DeferredHolder<ParticleType<?>, SimpleParticleType>, DeferredHolder<ParticleType<?>, SimpleParticleType>> particle) {
			event.registerSpriteSet(particle.getFirst().get(), SparklerParticle.Provider::new);
			event.registerSpecial(particle.getSecond().get(), new SparklerSeedParticle.Provider(() -> particle.getFirst().get()));
		}
	}
}
