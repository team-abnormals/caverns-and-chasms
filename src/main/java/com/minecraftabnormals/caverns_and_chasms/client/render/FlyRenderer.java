package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.FlyModel;
import com.minecraftabnormals.caverns_and_chasms.common.entity.FlyEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FlyRenderer extends MobRenderer<FlyEntity, FlyModel<FlyEntity>> {

	public FlyRenderer(EntityRendererProvider.Context context) {
		super(context, new FlyModel<>(FlyModel.createLayerDefinition().bakeRoot()), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(FlyEntity bee) {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/fly.png");
	}

	@Override
	protected void scale(FlyEntity fly, PoseStack matrixStack, float partialTickTime) {
		matrixStack.scale(1.0F, 1.0F, 1.0F);
	}
}
