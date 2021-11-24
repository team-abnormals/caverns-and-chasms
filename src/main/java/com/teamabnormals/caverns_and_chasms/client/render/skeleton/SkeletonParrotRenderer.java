package com.teamabnormals.caverns_and_chasms.client.render.skeleton;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;

public class SkeletonParrotRenderer extends ParrotRenderer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/parrot/skeleton_parrot.png");

	public SkeletonParrotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Parrot entity) {
		return TEXTURE;
	}
}