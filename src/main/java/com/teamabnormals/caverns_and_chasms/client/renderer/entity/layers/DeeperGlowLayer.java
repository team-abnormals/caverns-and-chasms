package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel.DeeperSprite;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.DeeperRenderer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.Deeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeeperGlowLayer extends RenderLayer<Deeper, DeeperModel<Deeper>> {

	public DeeperGlowLayer(RenderLayerParent<Deeper, DeeperModel<Deeper>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, Deeper deeper, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean charged = deeper.isPowered();
		float emission = DeeperRenderer.getExplosionEmissionProgress(deeper, partialTick, charged);
		float alpha = charged ? 1.0F : emission;
		this.getParentModel().renderOverlay(charged ? DeeperSprite.CHARGED : DeeperSprite.PRIMED, false, stack, packedLightIn, LivingEntityRenderer.getOverlayCoords(deeper, charged ? emission : 0.0F), 1.0F, 1.0F, 1.0F, alpha);
		this.getParentModel().renderOverlay(charged ? DeeperSprite.CHARGED_EMISSIVE : DeeperSprite.EMISSIVE, true, stack, packedLightIn, LivingEntityRenderer.getOverlayCoords(deeper, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
	}
}