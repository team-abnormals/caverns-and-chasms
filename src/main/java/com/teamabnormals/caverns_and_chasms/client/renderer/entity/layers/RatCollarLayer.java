package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatCollarLayer extends RenderLayer<Rat, RatModel<Rat>> {
	private static final ResourceLocation RAT_COLLAR_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/rat_collar.png");

	public RatCollarLayer(RenderLayerParent<Rat, RatModel<Rat>> renderer) {
		super(renderer);
	}

	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Rat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (rat.isTame()) {
			float[] afloat = rat.getCollarColor().getTextureDiffuseColors();
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(RAT_COLLAR_LOCATION));
			this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(rat, 0.0F), afloat[0], afloat[1], afloat[2], 1.0F);
		}
	}
}