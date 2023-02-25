package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatCollarLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatHeldItemLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat.RatType;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatRenderer extends MobRenderer<Rat, RatModel<Rat>> {

	public RatRenderer(EntityRendererProvider.Context context) {
		super(context, new RatModel<>(context.bakeLayer(CCModelLayers.RAT)), 0.3F);
		this.addLayer(new RatCollarLayer(this));
		this.addLayer(new RatHeldItemLayer(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(Rat entity) {
		RatType type = RatType.byId(entity.getRatType());
		return type.getTextureLocation();
	}
}