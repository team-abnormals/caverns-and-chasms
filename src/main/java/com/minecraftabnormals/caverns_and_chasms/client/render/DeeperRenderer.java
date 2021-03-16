package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class DeeperRenderer extends MobRenderer<DeeperEntity, DeeperModel<DeeperEntity>> {

	public DeeperRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new DeeperModel<>(false, 0.0F), 0.5F);
		this.addLayer(new DeeperRenderLayer(this));
	}

	@Override
	protected void preRenderCallback(DeeperEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
	}

	@Override
	protected float getOverlayProgress(DeeperEntity livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
		return (int) (f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
	}

	@Override
	public ResourceLocation getEntityTexture(DeeperEntity entity) {
		return MissingTextureSprite.getLocation();
	}
}