package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BabyGrazerDroolParticle extends UpFacingParticle {

	private BabyGrazerDroolParticle(ClientLevel level, double x, double y, double z, float rotation) {
		super(level, x, y, z, rotation);
		this.lifetime = 320;
	}

	@Override
	public void tick() {
		this.yd = -0.05D;
		super.tick();
		if (!this.onGround) {
			this.remove();
		} else {
			float f = (float) this.age / this.lifetime;
			this.quadSize = (f < 0.5F ? -4.0F * f * f + 4.0F * f : 1.0F) * 0.6F;
			this.alpha = Math.min(-4.0F * f + 4.0F, 1.0F);
			this.yOffset = 0.01F - 0.005F * f;
			this.yOffsetO = this.yOffset;
			this.stoppedByCollision = false;
		}
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

		@Override
		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double rotation, double ySpeed, double zSpeed) {
			BabyGrazerDroolParticle particle = new BabyGrazerDroolParticle(level, x, y, z, (float) rotation);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}
}