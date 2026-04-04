package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.DeeperModel;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.DeeperGlowLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.DeeperHatLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.DeeperPowerLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Deeper;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeeperRenderer<T extends Deeper> extends MobRenderer<T, DeeperModel<T>> {
	public static final ResourceLocation DEEPER_TEXTURE = CavernsAndChasms.location("textures/entity/deeper/deeper.png");

	public DeeperRenderer(EntityRendererProvider.Context context) {
		this(context, new DeeperModel<>(context.bakeLayer(CCModelLayers.DEEPER)), new DeeperModel<>(context.bakeLayer(CCModelLayers.DEEPER_ARMOR)));
	}

	public DeeperRenderer(EntityRendererProvider.Context context, DeeperModel<T> model, DeeperModel<T> powerModel) {
		super(context, model, 0.5F);
		this.addLayer(new DeeperGlowLayer<>(this));
		this.addLayer(new DeeperHatLayer<>(this));
		this.addLayer(new DeeperPowerLayer<>(this, powerModel));
	}

	@Override
	protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
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
	public ResourceLocation getTextureLocation(T deeper) {
		return DEEPER_TEXTURE;
	}

	@Override
	protected float getWhiteOverlayProgress(T deeper, float partialTick) {
		if (!deeper.isPowered())
			return 0.0F;
		return getExplosionEmissionProgress(deeper, partialTick, true);
	}

	public static float getExplosionEmissionProgress(Deeper deeper, float partialTick, boolean charged) {
		float f = deeper.getSwelling(partialTick);
		return (int) (f * 10.0F) % 2 == 0 ? (charged ? 0.0F : Mth.clamp(f - 0.25F, 0.0F, 1.0F)) : Mth.clamp(f, 0.0F, 1.0F);
	}
}