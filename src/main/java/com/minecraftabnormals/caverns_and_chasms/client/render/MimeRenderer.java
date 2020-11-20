package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.minecraftabnormals.caverns_and_chasms.client.model.MimeArmorModel;
import com.minecraftabnormals.caverns_and_chasms.client.model.MimeModel;
import com.minecraftabnormals.caverns_and_chasms.common.entity.MimeEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

public class MimeRenderer extends BipedRenderer<MimeEntity, MimeModel<MimeEntity>> {
	private static final ResourceLocation MIME_LOCATION = new ResourceLocation(CavernsAndChasms.MODID, "textures/entity/mime.png");

	public MimeRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new MimeModel<MimeEntity>(0.0F), 0.5F);
		this.addLayer(new BipedArmorLayer<>(this, new MimeArmorModel<MimeEntity>(0.5F), new MimeArmorModel<MimeEntity>(1.0F)));
	}

	@Override
	public ResourceLocation getEntityTexture(MimeEntity entity) {
		return MIME_LOCATION;
	}
}