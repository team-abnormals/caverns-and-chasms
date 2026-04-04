package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TurquoiseParticle extends TextureSheetParticle {
	private final boolean floor;

	public TurquoiseParticle(ClientLevel level, boolean floor, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.setSize(0.02F, 0.02F);
		this.quadSize *= 1.5F;
		this.gravity = floor ? 0.75F : 1.0F;
		this.lifetime = this.random.nextInt(floor ? 60 : 20) + (floor ? 60 : 20);
		this.floor = floor;
		if (this.floor)
			this.stoppedByCollision = true;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}


	@Override
	public void tick() {
		super.tick();
		if (!this.floor)
			this.stoppedByCollision = false;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			TurquoiseParticle particle = new TurquoiseParticle(level, false, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}

	public static class StepProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public StepProvider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			TurquoiseParticle particle = new TurquoiseParticle(level, true, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}
}
