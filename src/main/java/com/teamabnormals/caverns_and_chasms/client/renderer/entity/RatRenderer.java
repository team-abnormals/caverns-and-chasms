package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatAngryEyesLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatCollarLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatEarsLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatHeldItemLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RatRenderer extends MobRenderer<Rat, RatModel> {
	private static final Minecraft MC = Minecraft.getInstance();

	public RatRenderer(EntityRendererProvider.Context context) {
		super(context, new RatModel(context.bakeLayer(CCModelLayers.RAT)), 0.3F);
		this.addLayer(new RatEarsLayer(this));
		this.addLayer(new RatAngryEyesLayer(this));
		this.addLayer(new RatCollarLayer(this));
		this.addLayer(new RatHeldItemLayer(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(Rat rat) {
		return rat.getVariant().getTexture(rat.isVisuallyWounded(), rat.isDirty());
	}

	@Override
	protected void setupRotations(Rat rat, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupRotations(rat, stack, ageInTicks, rotationYaw, partialTicks);
		if (rat.isAttachedToEntity()) {
			stack.rotateAround(Axis.XP.rotationDegrees(90.0F), 0.0F, 0.25F, 0.0F);
			stack.mulPose(Axis.YP.rotationDegrees(Mth.sin((ageInTicks + rat.getAnimTimeOffset()) * 0.75F) * -10.0F));
		}
	}

	@Override
	public boolean shouldRender(Rat rat, Frustum frustum, double camX, double camY, double camZ) {
		return (rat.getAttachedEntity() != MC.cameraEntity || !MC.options.getCameraType().isFirstPerson()) && super.shouldRender(rat, frustum, camX, camY, camZ);
	}
}