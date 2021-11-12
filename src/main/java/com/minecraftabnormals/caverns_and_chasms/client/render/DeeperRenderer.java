package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.minecraftabnormals.caverns_and_chasms.client.render.layer.DeeperEmissiveLayer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DeeperRenderer extends MobRenderer<DeeperEntity, DeeperModel<DeeperEntity>> {

	public DeeperRenderer(EntityRendererProvider.Context context) {
		super(context, new DeeperModel<>(false, DeeperModel.createLayerDefinition().bakeRoot()), 0.5F);
		this.addLayer(new DeeperEmissiveLayer(this));
	}

	@Override
	protected void scale(DeeperEntity entity, PoseStack matrixStackIn, float partialTickTime) {
		float f = entity.getSwelling(partialTickTime);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
	}

	@Override
	protected float getWhiteOverlayProgress(DeeperEntity livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getSwelling(partialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(DeeperEntity entity) {
		return MissingTextureAtlasSprite.getLocation();
	}
}