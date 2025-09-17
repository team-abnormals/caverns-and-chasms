package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.GrazerModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.GrazerDroolLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GrazerRenderer extends MobRenderer<AbstractGrazer, GrazerModel> {
	public static final ResourceLocation LOCATION = CavernsAndChasms.location("textures/entity/grazer/grazer.png");

	public GrazerRenderer(EntityRendererProvider.Context context) {
		super(context, new GrazerModel(context.bakeLayer(CCModelLayers.GRAZER)), 0.5F);
		this.addLayer(new GrazerDroolLayer(this));
	}

	@Override
	protected void setupRotations(AbstractGrazer grazer, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
		super.setupRotations(grazer, poseStack, ageInTicks, rotationYaw, partialTick);
		float rot = -Mth.rotLerp(partialTick, grazer.xRotO, grazer.getXRot()) + Mth.sin(ageInTicks * 0.6F) * 3.5F * grazer.getWiggleAmount(partialTick);
		double ycenter = 21D / 16D * grazer.getScale();
		double zcenter = 7D / 16D * grazer.getScale();
		poseStack.translate(0.0D, grazer.shellCenterY(partialTick) - ycenter, -grazer.shellCenterZ(partialTick) + zcenter);
		poseStack.rotateAround(Axis.XP.rotationDegrees(rot), 0.0F, (float) ycenter, (float) -zcenter);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractGrazer grazer) {
		return LOCATION;
	}
}