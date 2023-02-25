package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.teamabnormals.caverns_and_chasms.client.model.CopperGolemModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemModel<CopperGolem>> {

	public CopperGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new CopperGolemModel<>(context.bakeLayer(CCModelLayers.COPPER_GOLEM)), 0.4F);
	}

	@Override
	public ResourceLocation getTextureLocation(CopperGolem copperGolem) {
		CopperGolem.Oxidation oxidation = copperGolem.getOxidation();
		String damaged = copperGolem.isDamaged() ? "_damaged" : "";
		return new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/copper_golem/copper_golem_" + oxidation.name().toLowerCase(Locale.ROOT) + damaged + ".png");
	}
}