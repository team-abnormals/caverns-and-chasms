package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.common.entity.BabySpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;

public class BabySpiderRenderer extends SpiderRenderer<BabySpiderEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/spider/spider.png");

	public BabySpiderRenderer(EntityRendererManager renderer) {
		super(renderer);
		this.shadowSize *= 0.35F;
	}

	protected void preRenderCallback(BabySpiderEntity entity, MatrixStack stack, float partialTicks) {
		stack.scale(0.35F, 0.35F, 0.35F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(BabySpiderEntity entity) {
		return TEXTURE;
	}
}