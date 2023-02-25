package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.FlyModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Fly;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FlyRenderer extends MobRenderer<Fly, FlyModel<Fly>> {
	private static final ResourceLocation FLY_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/fly.png");

	public FlyRenderer(EntityRendererProvider.Context context) {
		super(context, new FlyModel<>(context.bakeLayer(CCModelLayers.FLY)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(Fly bee) {
		return FLY_TEXTURE;
	}

	@Override
	protected void scale(Fly fly, PoseStack matrixStack, float partialTickTime) {
		matrixStack.scale(1.0F, 1.0F, 1.0F);
	}
}
