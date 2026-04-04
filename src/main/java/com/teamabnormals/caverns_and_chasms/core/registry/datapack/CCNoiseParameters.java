package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class CCNoiseParameters {
	public static final ResourceKey<NoiseParameters> CAVE_GROWTHS = createKey("cave_growths");
	public static final ResourceKey<NoiseParameters> CAVE_GROWTHS_MOSCHATEL = createKey("cave_growths_moschatel");
	public static final ResourceKey<NoiseParameters> CAVE_GROWTH_GRADIENT = createKey("cave_growth_gradient");

	public static void bootstrap(BootstapContext<NoiseParameters> context) {
		context.register(CAVE_GROWTHS, new NoiseParameters(-8, 1.0D));
		context.register(CAVE_GROWTHS_MOSCHATEL, new NoiseParameters(-8, 1.0D));
		context.register(CAVE_GROWTH_GRADIENT, new NoiseParameters(-2, 1.0D));
	}

	public static ResourceKey<NoiseParameters> createKey(String name) {
		return ResourceKey.create(Registries.NOISE, CavernsAndChasms.location(name));
	}
}