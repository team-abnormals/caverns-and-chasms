package com.minecraftabnormals.caverns_and_chasms.client.render.zombie;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;

public class ZombieParrotRenderer extends ParrotRenderer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/parrot/zombie_parrot.png");

	public ZombieParrotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Parrot entity) {
		return TEXTURE;
	}
}