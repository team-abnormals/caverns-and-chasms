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

public class LargeArrowRenderer extends EntityRenderer<LargeArrow> {
	private static final ResourceLocation LARGE_ARROW = CavernsAndChasms.location("textures/entity/projectiles/large_arrow.png");

	public LargeArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(LargeArrow entity) {
		return LARGE_ARROW;
	}

	@Override
	public void render(LargeArrow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

		float f9 = (float) entity.shakeTime - partialTicks;
		if (f9 > 0.0F) {
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			poseStack.mulPose(Axis.ZP.rotationDegrees(f10));
		}

		poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
		poseStack.scale(0.05625F, 0.05625F, 0.05625F);
		poseStack.translate(-8.0D, 0.0D, 0.0D);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
		PoseStack.Pose pose = poseStack.last();

		float half = 6.0F / 16.0F;
		float whole = half * 2;
		float arrow = 0.6875F;

		this.vertex(pose, consumer, -10, -6, -6, 0.0F, half, -3, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, -6, 6, half, half, -3, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, 6, 6, half, whole, -3, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, 6, -6, 0.0F, whole, -3, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, 6, -6, 0.0F, half, 1, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, 6, 6, half, half, 1, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, -6, 6, half, whole, 1, 0, 0, packedLight);
		this.vertex(pose, consumer, -10, -6, -6, 0.0F, whole, 1, 0, 0, packedLight);

		for (int j = 0; j < 4; ++j) {
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			this.vertex(pose, consumer, -11, -6, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
			this.vertex(pose, consumer, 11, -6, 0, arrow, 0.0F, 0, 1, 0, packedLight);
			this.vertex(pose, consumer, 11, 6, 0, arrow, half, 0, 1, 0, packedLight);
			this.vertex(pose, consumer, -11, 6, 0, 0.0F, half, 0, 1, 0, packedLight);
		}

		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	public void vertex(PoseStack.Pose pose, VertexConsumer consumer, int x, int y, int z, float u, float v, int normalX, int normalY, int normalZ, int packedLight) {
		consumer.addVertex(pose, (float) x, (float) y, (float) z).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float) normalX, (float) normalZ, (float) normalY);
	}
}