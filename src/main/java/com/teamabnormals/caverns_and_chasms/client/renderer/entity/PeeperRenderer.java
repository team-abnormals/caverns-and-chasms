package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.PeeperModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.PeeperPowerLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.PeeperPrimedLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PeeperRenderer extends MobRenderer<Peeper, PeeperModel<Peeper>> {
	public static final ResourceLocation PEEPER = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/peeper/peeper.png");
	public static final ResourceLocation PEEPER_GLOW = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/peeper/peeper_glow.png");

	public PeeperRenderer(EntityRendererProvider.Context context) {
		super(context, new PeeperModel<>(context.bakeLayer(CCModelLayers.PEEPER)), 0.5F);
		this.addLayer(new PeeperPrimedLayer(this, context));
		this.addLayer(new PeeperPowerLayer(this, context.getModelSet()));
	}

	@Override
	protected void scale(Peeper entity, PoseStack matrixStackIn, float partialTickTime) {
		float f = entity.getSwelling(partialTickTime);
		float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
		f = Mth.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
	}

	@Override
	public ResourceLocation getTextureLocation(Peeper entity) {
		return PEEPER;
	}
}