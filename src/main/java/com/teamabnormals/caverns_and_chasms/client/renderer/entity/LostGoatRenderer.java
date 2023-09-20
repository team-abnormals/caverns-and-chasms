package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.client.model.LostGoatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.LostGoat;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LostGoatRenderer extends MobRenderer<LostGoat, LostGoatModel<LostGoat>> {
	private static final ResourceLocation LOST_GOAT_LOCATION = new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/lost_goat.png");

	public LostGoatRenderer(EntityRendererProvider.Context context) {
		super(context, new LostGoatModel<>(context.bakeLayer(CCModelLayers.LOST_GOAT)), 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(LostGoat entity) {
		return LOST_GOAT_LOCATION;
	}
}