package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.client.model.CopperGolemModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemModel<CopperGolem>> {

	public CopperGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new CopperGolemModel<>(CopperGolemModel.createLayerDefinition().bakeRoot()), 0.4F);
	}

	@Override
	public ResourceLocation getTextureLocation(CopperGolem copperGolem) {
		return copperGolem.getOxidation().getTextureLocation();
	}
}