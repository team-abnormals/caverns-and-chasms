package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SanguineArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {
	public static final SanguineArmorModel<?> INSTANCE = new SanguineArmorModel<>(createLayerDefinition(LayerDefinitions.OUTER_ARMOR_DEFORMATION).bakeRoot());

	public SanguineArmorModel(ModelPart modelPart) {
		super(modelPart);
	}

	public static MeshDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshDefinition = HumanoidArmorModel.createBodyLayer(deformation);
		PartDefinition partDefinition = meshDefinition.getRoot();

		CubeDeformation halved = new CubeDeformation(deformation.growX / 2.0F, deformation.growY / 2.0F, deformation.growZ / 2.0F);

		float hornX = deformation.growX * 1.5F;
		float hornY = deformation.growY * -1.0F;
		float shoulderX = deformation.growX * 0.5F;
		float shoulderY = deformation.growY * -1.5F;

		partDefinition.getChild("head").addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F - hornX, -11.0F + hornY, -1.0F, 2.0F, 5.0F, 2.0F, halved), PartPose.ZERO);
		partDefinition.getChild("head").addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(24, 0).addBox(4.0F + hornX, -11.0F + hornY, -1.0F, 2.0F, 5.0F, 2.0F, halved), PartPose.ZERO);
		partDefinition.getChild("right_arm").addOrReplaceChild("right_shoulder_horn", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F - shoulderX, -5.0F + shoulderY, -1.0F, 2.0F, 3.0F, 2.0F, halved), PartPose.ZERO);
		partDefinition.getChild("left_arm").addOrReplaceChild("left_shoulder_horn", CubeListBuilder.create().texOffs(56, 0).addBox(1.0F + shoulderX, -5.0F + shoulderY, -1.0F, 2.0F, 3.0F, 2.0F, halved), PartPose.ZERO);

		return meshDefinition;
	}

	public static LayerDefinition createLayerDefinition(CubeDeformation deformation) {
		return LayerDefinition.create(createBodyLayer(deformation), 64, 32);
	}
}