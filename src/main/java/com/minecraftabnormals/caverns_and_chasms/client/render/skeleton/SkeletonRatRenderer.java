package com.minecraftabnormals.caverns_and_chasms.client.render.skeleton;

import com.minecraftabnormals.caverns_and_chasms.client.render.RatRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SkeletonRatRenderer extends RatRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/skeleton_rat.png");

	public SkeletonRatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(RatEntity entity) {
		return TEXTURE;
	}
}