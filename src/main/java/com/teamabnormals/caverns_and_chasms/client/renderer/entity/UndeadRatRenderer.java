package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class UndeadRatRenderer extends RatRenderer {
	private static final ResourceLocation ZOMBIE_RAT_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/zombie_rat.png");
	private static final ResourceLocation SKELETON_RAT_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/skeleton_rat.png");

	public UndeadRatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Rat entity) {
		return true ? ZOMBIE_RAT_TEXTURE : SKELETON_RAT_TEXTURE;
	}
}