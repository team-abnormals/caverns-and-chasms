package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.MimeArmorModel;
import com.minecraftabnormals.caverns_and_chasms.client.model.MimeModel;
import com.minecraftabnormals.caverns_and_chasms.common.entity.MimeEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MimeRenderer extends HumanoidMobRenderer<MimeEntity, MimeModel<MimeEntity>> {
	private static final ResourceLocation MIME_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/mime.png");

	public MimeRenderer(EntityRendererProvider.Context context) {
		super(context, new MimeModel<>(MimeModel.createLayerDefinition().bakeRoot()), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new MimeArmorModel<>(MimeArmorModel.createLayerDefinition().bakeRoot()), new MimeArmorModel<>(MimeArmorModel.createLayerDefinition().bakeRoot())));
	}

	@Override
	public ResourceLocation getTextureLocation(MimeEntity entity) {
		return MIME_LOCATION;
	}

	@Override
	protected void setupRotations(MimeEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		float f = entityLiving.getSwimAmount(partialTicks);
		super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
		if (f > 0.0F) {
			float f3 = entityLiving.isInWater() ? -90.0F - entityLiving.getXRot() : -90.0F;
			float f4 = Mth.lerp(f, 0.0F, f3);
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f4));
			if (entityLiving.isVisuallySwimming()) {
				matrixStackIn.translate(0.0D, -1.0D, 0.3F);
			}
		}
	}
}