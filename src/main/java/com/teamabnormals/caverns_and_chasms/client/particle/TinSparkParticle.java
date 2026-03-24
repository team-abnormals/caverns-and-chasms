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
import org.joml.Vector3f;

public class TinSparkParticle extends SimpleAnimatedParticle {
	private Vec3 prevDisplacement;
	private Vec3 displacement;

	protected TinSparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
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

		this.displacement = new Vec3(this.xd, this.yd, this.zd);
		this.prevDisplacement = this.displacement;
	}

	public void tick() {
		this.prevDisplacement = this.displacement;

		super.tick();
		float f = (float) this.age / this.lifetime;
		this.gCol = 1.0F - f * 0.5F;
		this.bCol = 1.0F - f;
		this.alpha = 1.0F - f * 0.5F;

		this.displacement = new Vec3(this.x - this.xo, this.y - this.yo, this.z - this.zo);
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
			TinSparkParticle particle = new TinSparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
			particle.pickSprite(this.sprites);
			return particle;
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
		Vec3 vec3 = camera.getPosition();
		float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
		float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
		float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());

		Vec3 displacement = this.prevDisplacement.lerp(this.displacement, partialTick);

		Vec3 forward = displacement.lengthSqr() > 0.0001D ? displacement.normalize() : new Vec3(0.0D, -1.0D, 0.0D);
		Vec3 towardsCamera = new Vec3(-x, -y, -z);
		Vec3 side = towardsCamera.subtract(forward.scale(towardsCamera.dot(forward) / forward.dot(forward))).normalize().cross(forward);

		float f3 = this.getQuadSize(partialTick);
		Vector3f[] avector3f = new Vector3f[]{
				side.reverse().subtract(forward).toVector3f().mul(f3).add(x, y, z),
				side.reverse().add(forward).toVector3f().mul(f3).add(x, y, z),
				side.add(forward).toVector3f().mul(f3).add(x, y, z),
				side.subtract(forward).toVector3f().mul(f3).add(x, y, z)
		};

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