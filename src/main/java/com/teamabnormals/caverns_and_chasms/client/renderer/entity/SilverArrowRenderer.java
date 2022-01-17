package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.SilverArrow;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SilverArrowRenderer extends ArrowRenderer<SilverArrow> {

	public SilverArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public ResourceLocation getTextureLocation(SilverArrow entity) {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/projectiles/silver_arrow.png");
	}
}