package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.animal.horse.Horse;

public class CopperHorseArmorLayer extends HorseArmorLayer {

	public CopperHorseArmorLayer(RenderLayerParent<Horse, HorseModel<Horse>> parent, EntityModelSet modelSet) {
		super(parent, modelSet);
		this.model = new CopperHorseArmorModel<>(modelSet.bakeLayer(CCModelLayers.COPPER_HORSE_ARMOR));
	}
}