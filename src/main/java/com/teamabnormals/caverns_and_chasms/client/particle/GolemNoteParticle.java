package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GolemNoteParticle extends TextureSheetParticle {
	private float rot;

	GolemNoteParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.friction = 0.66F;
		this.yd = 0.04D;
		this.lifetime = 16;
		this.rot = this.random.nextFloat() * 2F * Mth.PI;
		float color = this.random.nextFloat();
		this.rCol = Math.max(0.0F, Mth.sin((color + 0.0F) * (Mth.PI * 2F)) * 0.65F + 0.35F);
		this.gCol = Math.max(0.0F, Mth.sin((color + 0.33333334F) * (Mth.PI * 2F)) * 0.65F + 0.35F);
		this.bCol = Math.max(0.0F, Mth.sin((color + 0.6666667F) * (Mth.PI * 2F)) * 0.65F + 0.35F);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.move(this.xd, this.yd, this.zd);
			this.xd = Mth.sin(this.age * 0.5F) * Mth.sin(this.rot) * 0.03D;
			this.zd = Mth.sin(this.age * 0.5F) * Mth.cos(this.rot) * 0.03D;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ) {
			GolemNoteParticle particle = new GolemNoteParticle(level, x, y, z);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}
}