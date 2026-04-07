package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class CopperHorseArmorModel<T extends AbstractHorse> extends HorseModel<T> {

	public CopperHorseArmorModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static MeshDefinition createBodyMesh(CubeDeformation deformation) {
		MeshDefinition mesh = HorseModel.createBodyMesh(deformation);
		PartDefinition root = mesh.getRoot();
		PartDefinition headParts = root.getChild("head_parts");
		PartDefinition head = headParts.getChild("head");
		head.addOrReplaceChild("rod_base", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -17.0F, -1.0F, 4.0F, 4.0F, 4.0F), PartPose.ZERO);
		head.addOrReplaceChild("rod", CubeListBuilder.create().texOffs(56, 8).addBox(-1.0F, -13.0F, 0.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
		return mesh;
	}
}