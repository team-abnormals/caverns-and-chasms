package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.PeeperModel;
import com.teamabnormals.caverns_and_chasms.client.resources.PeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PeeperChargedLayer extends RenderLayer<Peeper, PeeperModel<Peeper>> {

	public PeeperChargedLayer(RenderLayerParent<Peeper, PeeperModel<Peeper>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Peeper peeper, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (peeper.isPowered()) {
			this.getParentModel().renderOverlay(PeeperSpriteUploader.getChargedPeeperSprite(), true, poseStack, 15728880, LivingEntityRenderer.getOverlayCoords(peeper, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}