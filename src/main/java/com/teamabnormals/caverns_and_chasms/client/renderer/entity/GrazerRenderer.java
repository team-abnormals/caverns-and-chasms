package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.GrazerModel;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GrazerRenderer extends MobRenderer<Grazer, GrazerModel> {
	public static final ResourceLocation GRAZER = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/grazer/grazer.png");

	public GrazerRenderer(EntityRendererProvider.Context context) {
		super(context, new GrazerModel(context.bakeLayer(CCModelLayers.GRAZER)), 0.5F);
	}

	@Override
	protected void setupRotations(Grazer grazer, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
		super.setupRotations(grazer, poseStack, ageInTicks, rotationYaw, partialTick);
		poseStack.translate(0.0D, grazer.shellCenterY() - 21D / 16D, -grazer.shellCenterZ() + 7D / 16D);
		poseStack.rotateAround(Axis.XP.rotationDegrees(-Mth.rotLerp(partialTick, grazer.xRotO, grazer.getXRot())), 0.0F, 21F / 16F, -7F / 16F);
	}

	@Override
	public ResourceLocation getTextureLocation(Grazer grazer) {
		return GRAZER;
	}
}