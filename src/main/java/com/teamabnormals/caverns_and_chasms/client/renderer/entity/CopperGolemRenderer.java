package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.caverns_and_chasms.client.model.CopperGolemModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemModel<CopperGolem>> {

	public CopperGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new CopperGolemModel<>(CopperGolemModel.createLayerDefinition().bakeRoot()), 0.4F);
	}

	@Override
	public ResourceLocation getTextureLocation(CopperGolem copperGolem) {
		return copperGolem.getOxidation().getTextureLocation();
	}

	@Override
	protected void setupRotations(CopperGolem copperGolem, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupRotations(copperGolem, matrixStack, ageInTicks, rotationYaw, partialTicks);
		float f = (float) (copperGolem.level.getGameTime() - copperGolem.lastHit) + partialTicks;
		if (f < 5.0F) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
		}
	}
}