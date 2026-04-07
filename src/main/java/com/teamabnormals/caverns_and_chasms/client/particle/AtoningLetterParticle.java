package com.teamabnormals.caverns_and_chasms.client.particle;

import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.AtoningTableEnchantmentNames;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AtoningLetterParticle extends UpFacingParticle {

	private AtoningLetterParticle(ClientLevel level, double x, double y, double z, float rotation) {
		super(level, x, y, z, rotation);
		this.rCol = 0.85F;
		this.gCol = 0.55F;
		this.bCol = 0.72F;
		this.lifetime = 150;
		this.quadSize = 0.17F;
	}

	@Override
	public void tick() {
		super.tick();
		float f = Math.max(1.0F - this.age / 4.0F, 0.0F);
		this.rCol = 0.85F + f * 0.08F;
		this.gCol = 0.55F + f * 0.15F;
		this.bCol = 0.72F + f * 0.08F;
		this.alpha = Math.min(1.0F - 1.5F * this.age / this.lifetime + 0.5F, 1.0F);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public int getLightColor(float partialTick) {
		return 15728880;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double rotation, double letter, double zSpeed) {
			AtoningLetterParticle particle = new AtoningLetterParticle(level, x, y, z, (float) rotation);
			particle.setSprite(this.sprites.get((int) letter, AtoningTableEnchantmentNames.LETTERS.length - 1));
			return particle;
		}
	}
}