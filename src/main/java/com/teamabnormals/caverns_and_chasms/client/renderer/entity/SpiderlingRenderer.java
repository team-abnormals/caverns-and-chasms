package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Spiderling;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpiderlingRenderer extends SpiderRenderer<Spiderling> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/spider/spider.png");

	public SpiderlingRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius *= 0.35F;
	}

	@Override
	protected void scale(Spiderling entity, PoseStack stack, float partialTicks) {
		stack.scale(0.35F, 0.35F, 0.35F);
	}

	@Override
	public ResourceLocation getTextureLocation(Spiderling entity) {
		return TEXTURE;
	}
}