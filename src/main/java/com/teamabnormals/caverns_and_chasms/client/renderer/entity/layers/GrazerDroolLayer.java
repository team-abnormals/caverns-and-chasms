package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.GrazerModel;
import com.teamabnormals.caverns_and_chasms.client.resources.GrazerSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.AbstractGrazer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrazerDroolLayer extends RenderLayer<AbstractGrazer, GrazerModel> {

	public GrazerDroolLayer(RenderLayerParent<AbstractGrazer, GrazerModel> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, AbstractGrazer grazer, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!grazer.isBaby())
			this.getParentModel().renderOverlay(GrazerSpriteUploader.getDroolSprite(), stack, packedLight, LivingEntityRenderer.getOverlayCoords(grazer, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
	}
}