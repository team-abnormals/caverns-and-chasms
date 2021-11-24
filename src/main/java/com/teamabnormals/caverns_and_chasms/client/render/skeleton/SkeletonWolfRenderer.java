package com.teamabnormals.caverns_and_chasms.client.render.skeleton;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class SkeletonWolfRenderer extends WolfRenderer {
	private static final ResourceLocation SKELETON_WOLF_TEXTURES = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/wolf/skeleton_wolf.png");

	public SkeletonWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Wolf entity) {
		return SKELETON_WOLF_TEXTURES;
	}
}