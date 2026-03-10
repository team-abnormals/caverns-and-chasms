package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.RicochetArrow;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RicochetArrowRenderer extends ArrowRenderer<RicochetArrow> {
	private static final ResourceLocation RICOCHET_ARROW = CavernsAndChasms.location("textures/entity/projectiles/ricochet_arrow.png");

	public RicochetArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(RicochetArrow entity) {
		return RICOCHET_ARROW;
	}
}