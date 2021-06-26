package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.RatModel;
import com.minecraftabnormals.caverns_and_chasms.client.render.layer.RatHeldItemLayer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.RatEntity.RatType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RatRenderer extends MobRenderer<RatEntity, RatModel<RatEntity>> {

	public RatRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new RatModel<>(), 0.2F);
		this.addLayer(new RatHeldItemLayer(this));
	}

	@Override
	public ResourceLocation getTextureLocation(RatEntity entity) {
		RatType type = RatType.byId(entity.getRatType());
		return type.getTextureLocation();
	}
}