package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AfflictionParticle extends TextureSheetParticle {

	AfflictionParticle(ClientLevel level, double p_105920_, double p_105921_, double p_105922_, double p_105923_, double p_105924_, double p_105925_) {
		super(level, p_105920_, p_105921_, p_105922_, 0.0D, 0.0D, 0.0D);
		this.friction = 0.7F;
		this.gravity = 0.5F;
		this.xd *= 0.1F;
		this.yd *= 0.1F;
		this.zd *= 0.1F;
		this.xd += p_105923_ * 0.4D;
		this.yd += p_105924_ * 0.4D;
		this.zd += p_105925_ * 0.4D;
		float f = (float) (Math.random() * (double) 0.3F + (double) 0.6F);
		this.rCol = f;
		this.gCol = f;
		this.bCol = f;
		this.quadSize *= 0.75F;
		this.lifetime = Math.max((int) (6.0D / (Math.random() * 0.8D + 0.6D)), 1);
		this.hasPhysics = false;
		this.tick();
	}

	@Override
	public float getQuadSize(float p_105938_) {
		return this.quadSize * Mth.clamp(((float) this.age + p_105938_) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@OnlyIn(Dist.CLIENT)
	public static class DamageProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public DamageProvider(SpriteSet p_105941_) {
			this.sprite = p_105941_;
		}

		public Particle createParticle(SimpleParticleType p_105952_, ClientLevel p_105953_, double p_105954_, double p_105955_, double p_105956_, double p_105957_, double p_105958_, double p_105959_) {
			AfflictionParticle critparticle = new AfflictionParticle(p_105953_, p_105954_, p_105955_, p_105956_, p_105957_, p_105958_ + 1.0D, p_105959_);
			critparticle.setLifetime(20);
			critparticle.pickSprite(this.sprite);
			return critparticle;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class SparkProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public SparkProvider(SpriteSet sprite) {
			this.sprite = sprite;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double p_105996_, double p_105997_, double p_105998_, double p_105999_, double p_106000_, double p_106001_) {
			AfflictionParticle particle = new AfflictionParticle(level, p_105996_, p_105997_, p_105998_, p_105999_, p_106000_, p_106001_);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}