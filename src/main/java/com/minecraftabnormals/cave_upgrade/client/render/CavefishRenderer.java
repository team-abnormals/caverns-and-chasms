package com.minecraftabnormals.cave_upgrade.client.render;

import com.minecraftabnormals.cave_upgrade.client.model.CavefishModel;
import com.minecraftabnormals.cave_upgrade.common.entity.CavefishEntity;
import com.minecraftabnormals.cave_upgrade.core.CaveUpgrade;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CavefishRenderer extends MobRenderer<CavefishEntity, CavefishModel<CavefishEntity>> {
	private static final ResourceLocation CAVEFISH_LOCATION = new ResourceLocation(CaveUpgrade.MODID, "textures/entity/cavefish.png");

	public CavefishRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new CavefishModel<>(), 0.25F);
	}

	@Override
	public ResourceLocation getEntityTexture(CavefishEntity entity) {
		return CAVEFISH_LOCATION;
	}

	@Override
	protected void applyRotations(CavefishEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
		float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
		if (!entityLiving.isInWater()) {
			matrixStackIn.translate((double) 0.1F, (double) 0.1F, (double) -0.1F);
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
		}
	}
}