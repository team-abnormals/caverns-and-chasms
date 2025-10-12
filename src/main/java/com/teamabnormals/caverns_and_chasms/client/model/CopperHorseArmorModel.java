package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class CopperHorseArmorModel<T extends AbstractHorse> extends HorseModel<T> {

	public CopperHorseArmorModel(ModelPart modelPart) {
		super(modelPart);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingTicks, ageInTicks, netHeadYaw, headPitch);
		for (ModelPart part : this.saddleParts) {
			part.visible = false;
		}
	}
}