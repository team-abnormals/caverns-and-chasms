package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.FlyModel;
import com.minecraftabnormals.caverns_and_chasms.common.entity.FlyEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FlyRenderer extends MobRenderer<FlyEntity, FlyModel<FlyEntity>> {
	public FlyRenderer(EntityRendererManager renderManager) {
		super(renderManager, new FlyModel<>(), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(FlyEntity bee) {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/fly.png");
	}

	@Override
	protected void scale(FlyEntity fly, MatrixStack matrixStack, float partialTickTime) {
		matrixStack.scale(1.0F, 1.0F, 1.0F);
	}
}
