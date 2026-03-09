package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.caverns_and_chasms.client.model.PeeperModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.PeeperRenderer;
import com.teamabnormals.caverns_and_chasms.client.resources.PeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Peeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PeeperPrimedLayer extends RenderLayer<Peeper, PeeperModel<Peeper>> {

	public PeeperPrimedLayer(RenderLayerParent<Peeper, PeeperModel<Peeper>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Peeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float alpha = getExplosionEmissionProgress(entity, partialTicks);
		if (entity.isPowered()) {
			this.getParentModel().renderOverlay(PeeperSpriteUploader.getChargedPeeperGlowSprite(), true, poseStack, 15728880, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
		} else {
			this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(BlueprintRenderTypes.getUnshadedTranslucentEntity(PeeperRenderer.PEEPER_GLOW, false)), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
		}

	}

	private float getExplosionEmissionProgress(Peeper livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getSwelling(partialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? Mth.clamp(f - 0.25F, 0.0F, 1.0F) : Mth.clamp(f, 0.0F, 1.0F);
	}
}