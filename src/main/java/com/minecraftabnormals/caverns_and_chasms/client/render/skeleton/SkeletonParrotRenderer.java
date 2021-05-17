package com.minecraftabnormals.caverns_and_chasms.client.render.skeleton;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.ResourceLocation;

public class SkeletonParrotRenderer extends ParrotRenderer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/parrot/skeleton_parrot.png");

	public SkeletonParrotRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getEntityTexture(ParrotEntity entity) {
		return TEXTURE;
	}
}