package com.teamabnormals.caverns_and_chasms.common.effect;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public class SubtleMobEffect extends MobEffect {

	public SubtleMobEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x8ED2E5);
	}

	@Override
	public ParticleOptions createParticleOptions(MobEffectInstance effect) {
		return CCParticleTypes.SUBTLE_EFFECT.get();
	}
}