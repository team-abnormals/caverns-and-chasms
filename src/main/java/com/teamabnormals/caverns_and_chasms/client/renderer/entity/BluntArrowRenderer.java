package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.BluntArrow;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BluntArrowRenderer extends ArrowRenderer<BluntArrow> {
	private static final ResourceLocation BLUNT_ARROW = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/projectiles/blunt_arrow.png");

	public BluntArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(BluntArrow entity) {
		return BLUNT_ARROW;
	}
}