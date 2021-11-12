package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.common.entity.SpiderlingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpiderlingRenderer extends SpiderRenderer<SpiderlingEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/spider/spider.png");

	public SpiderlingRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius *= 0.35F;
	}

	protected void scale(SpiderlingEntity entity, PoseStack stack, float partialTicks) {
		stack.scale(0.35F, 0.35F, 0.35F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(SpiderlingEntity entity) {
		return TEXTURE;
	}
}