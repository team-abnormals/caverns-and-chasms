package com.minecraftabnormals.caverns_and_chasms.client.render.zombie;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;

public class ZombieWolfRenderer extends WolfRenderer {
	private static final ResourceLocation ZOMBIE_WOLF_TEXTURES = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/wolf/zombie_wolf.png");

	public ZombieWolfRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getEntityTexture(WolfEntity entity) {
		return ZOMBIE_WOLF_TEXTURES;
	}
}