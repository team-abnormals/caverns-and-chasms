package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlintParticle extends TextureSheetParticle {
	protected FlintParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
		this.gravity = 0.8F;
		this.quadSize *= 2.5F;
		this.lifetime = 40;
	}

	@Override
	public void tick() {
		this.hasPhysics = this.age >= 10;
		super.tick();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
			FlintParticle particle = new FlintParticle(clientLevel, v, v1, v2, v3, v4, v5);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}
}
