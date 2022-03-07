package com.teamabnormals.caverns_and_chasms.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LavaLampSmokeParticle extends BaseAshSmokeParticle {
	protected LavaLampSmokeParticle(ClientLevel level, double p_107686_, double p_107687_, double p_107688_, double p_107689_, double p_107690_, double p_107691_, SpriteSet p_107693_) {
		super(level, p_107686_, p_107687_, p_107688_, 0.1F, 0.1F, 0.1F, p_107689_, p_107690_, p_107691_, 1.5F, p_107693_, 0.3F, level.random.nextInt(20) + 20, -0.1F, true);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double p_107709_, double p_107710_, double p_107711_, double p_107712_, double p_107713_, double p_107714_) {
			return new LavaLampSmokeParticle(level, p_107709_, p_107710_, p_107711_, p_107712_, p_107713_, p_107714_, this.sprites);
		}
	}
}