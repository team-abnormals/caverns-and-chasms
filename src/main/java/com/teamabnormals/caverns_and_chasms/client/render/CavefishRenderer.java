package com.teamabnormals.caverns_and_chasms.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.caverns_and_chasms.client.model.CavefishModel;
import com.teamabnormals.caverns_and_chasms.common.entity.Cavefish;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CavefishRenderer extends MobRenderer<Cavefish, CavefishModel<Cavefish>> {
	private static final ResourceLocation CAVEFISH_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/cavefish.png");

	public CavefishRenderer(EntityRendererProvider.Context context) {
		super(context, new CavefishModel<>(CavefishModel.createLayerDefinition().bakeRoot()), 0.25F);
	}

	@Override
	public ResourceLocation getTextureLocation(Cavefish entity) {
		return CAVEFISH_LOCATION;
	}

	@Override
	protected void setupRotations(Cavefish entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
		float f = 4.3F * Mth.sin(0.6F * ageInTicks);
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));
		if (!entityLiving.isInWater()) {
			matrixStackIn.translate(0.1F, 0.1F, -0.1F);
			matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
		}
	}
}