package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.caverns_and_chasms.client.model.MimeModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.MimeArmorLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.MimeItemInHandLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MimeRenderer extends HumanoidMobRenderer<Mime, MimeModel> {
	public static final ResourceLocation MIME_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/mime.png");

	public MimeRenderer(EntityRendererProvider.Context context) {
		super(context, new MimeModel(context.bakeLayer(CCModelLayers.MIME)), 0.5F);
		this.layers.removeIf(entry -> entry instanceof ItemInHandLayer);
		this.addLayer(new MimeArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		this.addLayer(new MimeItemInHandLayer(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(Mime mime) {
		return MIME_TEXTURE;
	}

	@Override
	protected void setupRotations(Mime mime, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
		float f = mime.getSwimAmount(partialTicks);
		super.setupRotations(mime, stack, ageInTicks, rotationYaw, partialTicks);
		if (f > 0.0F) {
			float f3 = mime.isInWater() ? -90F - mime.getXRot() : -90F;
			float f4 = Mth.lerp(f, 0.0F, f3);
			stack.mulPose(Vector3f.XP.rotationDegrees(f4));
			if (mime.isVisuallySwimming())
				stack.translate(0.0D, -1D, 0.3D);
		}
	}
}