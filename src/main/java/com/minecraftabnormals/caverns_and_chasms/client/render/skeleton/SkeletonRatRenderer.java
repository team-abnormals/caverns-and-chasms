package com.minecraftabnormals.caverns_and_chasms.client.render.skeleton;

import com.minecraftabnormals.caverns_and_chasms.client.render.RatRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SkeletonRatRenderer extends RatRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/skeleton_rat.png");

	public SkeletonRatRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getEntityTexture(RatEntity entity) {
		return TEXTURE;
	}
}