package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.DeeperRenderer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.DeeperHat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class DeeperHatLayer extends RenderLayer<Deeper, DeeperModel<Deeper>> {

	public DeeperHatLayer(RenderLayerParent<Deeper, DeeperModel<Deeper>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, Deeper deeper, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		DeeperHat hat = deeper.getHat();
		if (hat == DeeperHat.NONE)
			return;
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(hat.getTexture()));
		this.getParentModel().renderToBuffer(stack, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(deeper, deeper.isPowered() ? DeeperRenderer.getExplosionEmissionProgress(deeper, partialTick, true) : 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
	}
}