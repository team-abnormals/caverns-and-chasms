package com.minecraftabnormals.caverns_and_chasms.client.render.zombie;

import com.minecraftabnormals.caverns_and_chasms.client.render.RatRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ZombieRatRenderer extends RatRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/rat/zombie_rat.png");

	public ZombieRatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(RatEntity entity) {
		return TEXTURE;
	}
}