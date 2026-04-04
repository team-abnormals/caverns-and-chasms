package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class SparklerSeedParticle extends NoRenderParticle {
	private int life;
	private final int lifeTime = 6;
	protected final Supplier<ParticleOptions> particle;

	SparklerSeedParticle(Supplier<ParticleOptions> particle, ClientLevel p_106947_, double p_106948_, double p_106949_, double p_106950_) {
		super(p_106947_, p_106948_, p_106949_, p_106950_, 0.0D, 0.0D, 0.0D);
		this.particle = particle;
	}

	public void tick() {

		for (int i = 0; i < 5; i++) {
			double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 0.3D;
			double d1 = this.y - 0.1D + (this.random.nextDouble() - this.random.nextDouble()) * 0.3D;
			double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 0.3D;
			this.level.addParticle(this.particle.get(), d0, d1, d2, (float) this.life / (float) this.lifeTime, 0.0D, 0.0D);
		}

		++this.life;
		if (this.life == 8) {
			this.remove();
		}

	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		protected final Supplier<ParticleOptions> particle;

		public Provider(Supplier<ParticleOptions> particle) {
			this.particle = particle;
		}

		public Particle createParticle(SimpleParticleType p_106969_, ClientLevel p_106970_, double p_106971_, double p_106972_, double p_106973_, double p_106974_, double p_106975_, double p_106976_) {
			return new SparklerSeedParticle(this.particle, p_106970_, p_106971_, p_106972_, p_106973_);
		}
	}
}