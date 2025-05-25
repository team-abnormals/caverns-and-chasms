package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.GrazerModel;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrazerRenderer extends MobRenderer<Grazer, GrazerModel> {
	public static final ResourceLocation GRAZER = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/grazer/grazer.png");

	public GrazerRenderer(EntityRendererProvider.Context context) {
		super(context, new GrazerModel(context.bakeLayer(CCModelLayers.GRAZER)), 0.5F);
	}

	@Override
	protected void setupRotations(Grazer grazer, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
		super.setupRotations(grazer, poseStack, ageInTicks, rotationYaw, partialTick);
		/*
		float f = grazer.getRollAnim(partialTick);
		poseStack.translate(0.0D, 1.0D - Mth.cos(f * Mth.DEG_TO_RAD) * 1.3125D, 0.5625D - Mth.sin(f * Mth.DEG_TO_RAD) * 1.0D);
		poseStack.mulPose(Axis.XP.rotationDegrees(f));
		*/
	}

	@Override
	public ResourceLocation getTextureLocation(Grazer grazer) {
		return GRAZER;
	}
}