package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.client.model.GlareModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.GlareEyesLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.glare.Glare;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlareRenderer extends MobRenderer<Glare, GlareModel<Glare>> {
	private static final ResourceLocation GLARE_TEXTURE = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/glare/glare.png");

	public GlareRenderer(EntityRendererProvider.Context context) {
		super(context, new GlareModel<>(context.bakeLayer(GlareModel.LOCATION)), 0.4F);
		this.addLayer(new GlareEyesLayer<>(this));
	}

	public ResourceLocation getTextureLocation(Glare glare) {
		return GLARE_TEXTURE;
	}
}