package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class SaddledGrazerRenderer extends GrazerRenderer {
	public static final ResourceLocation SADDLED_LOCATION = CavernsAndChasms.location("textures/entity/grazer/saddled_grazer.png");
	public static final ResourceLocation BABY_LOCATION = CavernsAndChasms.location("textures/entity/grazer/baby_grazer.png");

	public SaddledGrazerRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractGrazer grazer) {
		if (grazer.isBaby())
			return BABY_LOCATION;
		else
			return SADDLED_LOCATION;
	}
}