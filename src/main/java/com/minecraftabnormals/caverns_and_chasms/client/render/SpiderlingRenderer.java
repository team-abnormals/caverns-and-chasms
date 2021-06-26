package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.common.entity.SpiderlingEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;

public class SpiderlingRenderer extends SpiderRenderer<SpiderlingEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/spider/spider.png");

	public SpiderlingRenderer(EntityRendererManager renderer) {
		super(renderer);
		this.shadowRadius *= 0.35F;
	}

	protected void scale(SpiderlingEntity entity, MatrixStack stack, float partialTicks) {
		stack.scale(0.35F, 0.35F, 0.35F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(SpiderlingEntity entity) {
		return TEXTURE;
	}
}