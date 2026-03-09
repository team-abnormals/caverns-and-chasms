package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvendeeperHeadModel extends SkullModel {
	public EvendeeperHeadModel(ModelPart part) {
		super(part);
	}

	public static LayerDefinition createHeadLayer() {
		MeshDefinition meshdefinition = SkullModel.createHeadModel();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F).texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)).texOffs(32, 38).addBox(-4.0F, -10.0F, -4.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.19F)), PartPose.ZERO);
		head.addOrReplaceChild("hat_1", CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, -7.0F, 0.0F, 6.0F, 7.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -10.0F, -2.0F, 0.0F, Mth.PI / 4F, 0.0F));
		head.addOrReplaceChild("hat_2", CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, -7.0F, 0.0F, 6.0F, 7.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -10.0F, -2.0F, 0.0F, -Mth.PI / 4F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}