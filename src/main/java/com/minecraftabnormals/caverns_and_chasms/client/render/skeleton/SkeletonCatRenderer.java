package com.minecraftabnormals.caverns_and_chasms.client.render.skeleton;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.ResourceLocation;

public class SkeletonCatRenderer extends CatRenderer {
	private static final ResourceLocation SKELETON_CAT_TEXTURES = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/cat/skeleton_cat.png");

	public SkeletonCatRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getTextureLocation(CatEntity entity) {
		return SKELETON_CAT_TEXTURES;
	}
}