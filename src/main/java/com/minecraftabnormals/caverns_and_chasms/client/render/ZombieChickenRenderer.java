package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.common.entity.ZombieChickenEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZombieChickenRenderer extends MobRenderer<ZombieChickenEntity, ChickenModel<ZombieChickenEntity>> {
	public ZombieChickenRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ChickenModel<>(), 0.3F);
	}

	public ResourceLocation getEntityTexture(ZombieChickenEntity entity) {
		return new ResourceLocation(CavernsAndChasms.MODID, "textures/entity/zombie_chicken.png");
	}

	protected float handleRotationFloat(ZombieChickenEntity livingBase, float partialTicks) {
		float f = MathHelper.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
		float f1 = MathHelper.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}
}