package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.client.model.EvendeeperModel;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Evendeeper;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvendeeperRenderer extends DeeperRenderer<Evendeeper> {
	public static final ResourceLocation EVENDEEPER_TEXTURE = CavernsAndChasms.location("textures/entity/evendeeper/evendeeper.png");

	public EvendeeperRenderer(EntityRendererProvider.Context context) {
		super(context, new EvendeeperModel(context.getModelSet().bakeLayer(CCModelLayers.EVENDEEPER)), new EvendeeperModel(context.getModelSet().bakeLayer(CCModelLayers.EVENDEEPER_ARMOR)));
	}

	@Override
	public ResourceLocation getTextureLocation(Evendeeper evendeeper) {
		return EVENDEEPER_TEXTURE;
	}
}