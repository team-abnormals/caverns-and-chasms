package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.teamabnormals.caverns_and_chasms.client.resources.DeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Deeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeeperPrimedLayer extends RenderLayer<Deeper, DeeperModel<Deeper>> {
	private final DeeperModel<Deeper> model;
	private final DeeperModel<Deeper> emissiveModel;

	public DeeperPrimedLayer(RenderLayerParent<Deeper, DeeperModel<Deeper>> renderer) {
		super(renderer);
		this.model = new DeeperModel<>(DeeperModel.DeeperSprite.PRIMED, DeeperModel.createLayerDefinition().bakeRoot());
		this.emissiveModel = new DeeperModel<>(DeeperModel.DeeperSprite.EMISSIVE, DeeperModel.createLayerDefinition().bakeRoot());
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Deeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = getExplosionEmissionProgress(entity, partialTicks);
		this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.emissiveModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(BlueprintRenderTypes.getUnshadedTranslucentEntity(DeeperSpriteUploader.ATLAS_LOCATION, false)), packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, f);
		this.emissiveModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(BlueprintRenderTypes.getUnshadedTranslucentEntity(DeeperSpriteUploader.ATLAS_LOCATION, false)), packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, f);
	}

	private float getExplosionEmissionProgress(Deeper livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getSwelling(partialTicks);
		return Mth.clamp(f * f, 0.0F, 1.0F);
	}
}