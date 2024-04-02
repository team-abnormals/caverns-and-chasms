package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.LargeArrow;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class LargeArrowRenderer extends EntityRenderer<LargeArrow> {
	private static final ResourceLocation LARGE_ARROW = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/projectiles/large_arrow.png");

	public LargeArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(LargeArrow entity) {
		return LARGE_ARROW;
	}

	@Override
	public void render(LargeArrow entity, float p_113840_, float p_113841_, PoseStack poseStack, MultiBufferSource buffer, int p_113844_) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_113841_, entity.yRotO, entity.getYRot()) - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_113841_, entity.xRotO, entity.getXRot())));

		float f9 = (float) entity.shakeTime - p_113841_;
		if (f9 > 0.0F) {
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			poseStack.mulPose(Axis.ZP.rotationDegrees(f10));
		}

		poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
		poseStack.scale(0.05625F, 0.05625F, 0.05625F);
		poseStack.translate(-8.0D, 0.0D, 0.0D);
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
		PoseStack.Pose posestack$pose = poseStack.last();
		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();

		float half = 6.0F / 16.0F;
		float whole = half * 2;
		float arrow = 0.6875F;

		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, -6, -6, 0.0F, half, -3, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, -6, 6, half, half, -3, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, 6, 6, half, whole, -3, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, 6, -6, 0.0F, whole, -3, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, 6, -6, 0.0F, half, 1, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, 6, 6, half, half, 1, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, -6, 6, half, whole, 1, 0, 0, p_113844_);
		this.vertex(matrix4f, matrix3f, vertexconsumer, -10, -6, -6, 0.0F, whole, 1, 0, 0, p_113844_);

		for (int j = 0; j < 4; ++j) {
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			this.vertex(matrix4f, matrix3f, vertexconsumer, -11, -6, 0, 0.0F, 0.0F, 0, 1, 0, p_113844_);
			this.vertex(matrix4f, matrix3f, vertexconsumer, 11, -6, 0, arrow, 0.0F, 0, 1, 0, p_113844_);
			this.vertex(matrix4f, matrix3f, vertexconsumer, 11, 6, 0, arrow, half, 0, 1, 0, p_113844_);
			this.vertex(matrix4f, matrix3f, vertexconsumer, -11, 6, 0, 0.0F, half, 0, 1, 0, p_113844_);
		}

		poseStack.popPose();
		super.render(entity, p_113840_, p_113841_, poseStack, buffer, p_113844_);
	}

	public void vertex(Matrix4f p_113826_, Matrix3f p_113827_, VertexConsumer p_113828_, int p_113829_, int p_113830_, int p_113831_, float p_113832_, float p_113833_, int p_113834_, int p_113835_, int p_113836_, int p_113837_) {
		p_113828_.vertex(p_113826_, (float) p_113829_, (float) p_113830_, (float) p_113831_).color(255, 255, 255, 255).uv(p_113832_, p_113833_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_113837_).normal(p_113827_, (float) p_113834_, (float) p_113836_, (float) p_113835_).endVertex();
	}
}