package com.teamabnormals.caverns_and_chasms.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public abstract class UpFacingParticle extends TextureSheetParticle {
	protected double yOffset = 0.01D;
	protected double yOffsetO = 0.01D;

	protected UpFacingParticle(ClientLevel level, double x, double y, double z, float rotation) {
		super(level, x, y, z);
		this.roll = rotation;
		this.oRoll = this.roll;
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
		Vec3 vec3 = camera.getPosition();
		float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
		float f1 = (float) (Mth.lerp(partialTick, this.yo + this.yOffsetO, this.y + this.yOffset) - vec3.y());
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
			vertex.rotateZ(Mth.lerp(partialTick, this.oRoll, this.roll));
			vertex.rotateX(Mth.HALF_PI);
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