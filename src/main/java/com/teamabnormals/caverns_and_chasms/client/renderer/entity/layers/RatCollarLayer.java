package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatCollarLayer extends RenderLayer<Rat, RatModel> {
	private static final ResourceLocation RAT_COLLAR_LOCATION = CavernsAndChasms.location("textures/entity/rat/rat_collar.png");

	public RatCollarLayer(RenderLayerParent<Rat, RatModel> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Rat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (rat.isTame() && !rat.isInvisible()) {
			renderCollar(this.getParentModel(), poseStack, buffer, packedLight, rat.getCollarColor(), rat.hurtTime, rat.deathTime);
		}
	}

	public static void renderCollar(RatModel model, PoseStack poseStack, MultiBufferSource buffer, int packedLight, DyeColor collarColor, int hurtTime, int deathTime) {
		float[] afloat = collarColor.getTextureDiffuseColors();
		int overlaycoords = OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(hurtTime > 0 || deathTime > 0));
		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(RAT_COLLAR_LOCATION));
		model.renderToBuffer(poseStack, ivertexbuilder, packedLight, overlaycoords, afloat[0], afloat[1], afloat[2], 1.0F);
	}
}