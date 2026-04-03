package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeeperHeadModel extends SkullModel {
	public DeeperHeadModel(ModelPart part) {
		super(part);
	}

	public static LayerDefinition createHeadLayer() {
		MeshDefinition meshdefinition = SkullModel.createHeadModel();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
		head.addOrReplaceChild("hat_1", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -8.3F, 0.0F, 0.0F, Mth.PI / 4F, 0.0F));
		head.addOrReplaceChild("hat_2", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -8.3F, 0.0F, 0.0F, -Mth.PI / 4F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}