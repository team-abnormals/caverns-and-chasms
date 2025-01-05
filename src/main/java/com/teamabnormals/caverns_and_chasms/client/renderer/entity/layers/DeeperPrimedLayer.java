package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel.DeeperSprite;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Deeper;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

@OnlyIn(Dist.CLIENT)
public class DeeperPrimedLayer extends RenderLayer<Deeper, DeeperModel<Deeper>> {
	private final DeeperModel<Deeper> model;

	public DeeperPrimedLayer(RenderLayerParent<Deeper, DeeperModel<Deeper>> renderer, EntityRendererProvider.Context context) {
		super(renderer);
		this.model = new DeeperModel<>(context.bakeLayer(CCModelLayers.DEEPER));
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, Deeper deeper, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean charged = deeper.isPowered();
		float emission = this.getExplosionEmissionProgress(deeper, partialTick);
		float alpha = charged ? 1.0F : emission;
		this.model.setupAnim(deeper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.model.renderOverlay(charged ? DeeperSprite.CHARGED : DeeperSprite.PRIMED, false, stack, packedLightIn, LivingEntityRenderer.getOverlayCoords(deeper, charged ? emission : 0.0F), 1.0F, 1.0F, 1.0F, alpha);
		this.model.renderOverlay(charged ? DeeperSprite.CHARGED_EMISSIVE : DeeperSprite.EMISSIVE, true, stack, packedLightIn, LivingEntityRenderer.getOverlayCoords(deeper, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
	}

	private float getExplosionEmissionProgress(Deeper deeper, float partialTick) {
		float f = deeper.getSwelling(partialTick);
		return (int) (f * 10.0F) % 2 == 0 ? Mth.clamp(f - 0.25F, 0.0F, 1.0F) : Mth.clamp(f, 0.0F, 1.0F);
	}
}