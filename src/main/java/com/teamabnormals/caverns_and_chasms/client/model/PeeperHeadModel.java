package com.teamabnormals.caverns_and_chasms.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PeeperHeadModel extends SkullModel {
	protected final ModelPart pupil;

	public PeeperHeadModel(ModelPart part) {
		super(part);
		this.pupil = this.head.getChild("pupil");
	}

	public static LayerDefinition createHeadLayer() {
		MeshDefinition meshdefinition = SkullModel.createHeadModel();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -4.99F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(40, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
		PartDefinition pupil = head.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -5.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(ageInTicks, netHeadYaw, headPitch);
		if (ageInTicks < 0) {
			this.head.yRot = ageInTicks;
		}
	}
}