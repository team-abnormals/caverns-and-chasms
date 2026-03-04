package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.CavefishModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Cavefish;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CavefishRenderer extends MobRenderer<Cavefish, CavefishModel<Cavefish>> {
	private static final ResourceLocation CAVEFISH_LOCATION = CavernsAndChasms.location("textures/entity/cavefish.png");

	public CavefishRenderer(EntityRendererProvider.Context context) {
		super(context, new CavefishModel<>(context.bakeLayer(CCModelLayers.CAVEFISH)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(Cavefish cavefish) {
		return CAVEFISH_LOCATION;
	}

	@Override
	protected void setupRotations(Cavefish cavefish, PoseStack stack, float p_116228_, float p_116229_, float p_116230_) {
		super.setupRotations(cavefish, stack, p_116228_, p_116229_, p_116230_);
		float f = 4.3F * Mth.sin(0.6F * p_116228_);
		stack.mulPose(Axis.YP.rotationDegrees(f));
		if (!cavefish.isInWater()) {
			stack.translate(0.2F, 0.1F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees(90.0F));
		}
	}
}