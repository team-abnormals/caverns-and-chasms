package com.teamabnormals.caverns_and_chasms.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SparkParticle extends SimpleAnimatedParticle {
	protected SparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
		super(level, x, y, z, spriteSet, 0.1F);
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
		this.rCol = 1.0F;
		this.gCol = 1.0F;
		this.bCol = 1.0F;
		this.gravity = 0.75F;
		this.setSize(0.02F, 0.02F);
		this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
	}

	public void tick() {
		super.tick();
		float f = (float) this.age / this.lifetime;
		this.gCol = 1.0F - f * 0.5F;
		this.bCol = 1.0F - f;
		this.alpha = 1.0F - f * 0.5F;
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

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
		Vec3 vec3 = camera.getPosition();
		float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
		float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
		float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());

		Vec3 velocity = new Vec3(this.x - this.xo, this.y - this.yo, this.z - this.zo);
		if (velocity.lengthSqr() > 0.0001) {
			velocity = velocity.normalize();
		} else {
			velocity = new Vec3(0, 1, 0);
		}

		Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F);
		Vector3f forward = new Vector3f((float) velocity.x, (float) velocity.y, (float) velocity.z);

		Quaternionf quaternionf = new Quaternionf();
		if (!forward.equals(up)) {
			quaternionf.rotationTo(up, forward);
		}

		Vector3f[] avector3f = new Vector3f[]{
				new Vector3f(-1.0F, -1.0F, 0.0F),
				new Vector3f(-1.0F, 1.0F, 0.0F),
				new Vector3f(1.0F, 1.0F, 0.0F),
				new Vector3f(1.0F, -1.0F, 0.0F)
		};

		float f3 = this.getQuadSize(partialTicks);
		for (int i = 0; i < 4; ++i) {
			Vector3f vertex = avector3f[i];
			vertex.rotate(quaternionf);
			vertex.mul(f3);
			vertex.add(f, f1, f2);
		}

		float f6 = this.getU0();
		float f7 = this.getU1();
		float f4 = this.getV0();
		float f5 = this.getV1();
		int light = this.getLightColor(partialTicks);

		vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
		vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
		vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
		vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
	}
}