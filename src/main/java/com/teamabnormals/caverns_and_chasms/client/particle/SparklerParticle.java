package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SparklerParticle extends RisingParticle {

	public SparklerParticle(ClientLevel level, double x, double y, double z, double velX, double velY, double velZ) {
		super(level, x, y, z, velX, velY, velZ);

		double dx = this.random.nextDouble() - 0.5D;
		double dy = this.random.nextDouble() * 0.5D;
		double dz = this.random.nextDouble() - 0.5D;

		double speed = 0.015D / Math.sqrt(dx * dx + dy * dy + dz * dz);

		this.xd = dx * speed;
		this.yd = dy * speed;
		this.zd = dz * speed;

		this.lifetime = (int) (4.0D / (Math.random() * 0.8D + 0.2D)) + 2;
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public void move(double p_106817_, double p_106818_, double p_106819_) {
		this.setBoundingBox(this.getBoundingBox().move(p_106817_, p_106818_, p_106819_));
		this.setLocationFromBoundingbox();
	}

	public float getQuadSize(float p_106824_) {
		float f = ((float) this.age + p_106824_) / (float) this.lifetime;
		return this.quadSize * (1.0F - f * f * 0.5F);
	}

	public int getLightColor(float p_106821_) {
		float f = ((float) this.age + p_106821_) / (float) this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(p_106821_);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet p_106827_) {
			this.sprite = p_106827_;
		}

		public Particle createParticle(SimpleParticleType p_106838_, ClientLevel p_106839_, double p_106840_, double p_106841_, double p_106842_, double p_106843_, double p_106844_, double p_106845_) {
			SparklerParticle sparklerParticle = new SparklerParticle(p_106839_, p_106840_, p_106841_, p_106842_, p_106843_, p_106844_, p_106845_);
			sparklerParticle.pickSprite(this.sprite);
			return sparklerParticle;
		}
	}
}