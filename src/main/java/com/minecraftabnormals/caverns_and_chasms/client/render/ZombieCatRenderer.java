package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.ResourceLocation;

public class ZombieCatRenderer extends CatRenderer {
	private static final ResourceLocation ZOMBIE_CAT_TEXTURES = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/cat/zombie_cat.png");

	public ZombieCatRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getEntityTexture(CatEntity entity) {
		return ZOMBIE_CAT_TEXTURES;
	}
}