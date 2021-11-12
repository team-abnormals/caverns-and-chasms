package com.minecraftabnormals.caverns_and_chasms.client.render.zombie;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.resources.ResourceLocation;

public class ZombieWolfRenderer extends WolfRenderer {
	private static final ResourceLocation ZOMBIE_WOLF_TEXTURES = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/wolf/zombie_wolf.png");

	public ZombieWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Wolf entity) {
		return ZOMBIE_WOLF_TEXTURES;
	}
}