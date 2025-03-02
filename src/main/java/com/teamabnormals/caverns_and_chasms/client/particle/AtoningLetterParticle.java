package com.teamabnormals.caverns_and_chasms.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.common.block.entity.AtoningTableSentences;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

public class AtoningLetterParticle extends TextureSheetParticle {

	private float rot;

	protected AtoningLetterParticle(ClientLevel level, double x, double y, double z, float rotation) {
		super(level, x, y, z);
		this.rCol = 0.85F;
		this.gCol = 0.55F;
		this.bCol = 0.72F;
		this.rot = rotation;
		this.lifetime = 150;
		this.quadSize = 0.15F;
	}

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

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double rotation, double letter, double zSpeed) {
			AtoningLetterParticle particle = new AtoningLetterParticle(level, x, y, z, (float) rotation);
			particle.setSprite(this.sprites.get((int) letter, AtoningTableSentences.LETTERS.length - 1));
			return particle;
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
		Vec3 vec3 = camera.getPosition();
		float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
		float f1 = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
		float f2 = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());

		Vector3f[] avector3f = new Vector3f[]{
				new Vector3f(-1.0F, -1.0F, 0.0F),
				new Vector3f(-1.0F, 1.0F, 0.0F),
				new Vector3f(1.0F, 1.0F, 0.0F),
				new Vector3f(1.0F, -1.0F, 0.0F)
		};

		float f3 = this.getQuadSize(partialTick);
		for (int i = 0; i < 4; ++i) {
			Vector3f vertex = avector3f[i];
			vertex.rotateZ(this.rot);
			vertex.rotateX(Mth.PI / 2.0F);
			vertex.mul(f3);
			vertex.add(f, f1, f2);
		}

		float f6 = this.getU0();
		float f7 = this.getU1();
		float f4 = this.getV0();
		float f5 = this.getV1();
		int light = this.getLightColor(partialTick);

		vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
		vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
		vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
		vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
	}
}