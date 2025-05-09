package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.Grazer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.GrazerRunPhase;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// TODO: Maybe the legs should be barely able to move because they're so close to each other. Would fix z-fighting too.
// TODO: Hurt animation where it leans back, opens its jaw and its head shakes.
// TODO: Charge animation where it leans back and opens its jaw like stupid.
public class GrazerModel extends HierarchicalModel<Grazer> {
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart jaw;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	private final ModelPart leftHindLeg;
	private final ModelPart rightHindLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightFrontLeg;

	public GrazerModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.jaw = this.body.getChild("jaw");
		this.leftWing = this.body.getChild("left_wing");
		this.rightWing = this.body.getChild("right_wing");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -25.0F, -19.0F, 10.0F, 12.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-5.0F, -25.0F, -19.0F, 10.0F, 24.0F, 24.0F, new CubeDeformation(0.5F))
		.texOffs(68, 12).addBox(-5.0F, -13.0F, -7.0F, 10.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(12, 84).addBox(-5.0F, -13.0F, -19.0F, 10.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
		body.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(15, 105).addBox(-5.0F, 0.0F, -9.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(33, 116).addBox(5.0F, 3.0F, -9.0F, 0.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -7.0F));
		body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(68, 89).addBox(0.0F, -20.0F, 0.0F, 1.0F, 22.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -12.0F, 5.0F));
		body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(68, 89).addBox(-1.0F, -20.0F, 0.0F, 1.0F, 22.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -12.0F, 5.0F));
		root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(84, 71).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 15.0F, 3.0F));
		root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(84, 71).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 15.0F, 3.0F));
		root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(68, 71).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.05F, 15.0F, -2.0F));
		root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(68, 71).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.05F, 15.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(Grazer grazer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
		this.rightFrontLeg.xRot = this.leftHindLeg.xRot;
		this.leftFrontLeg.xRot = this.rightHindLeg.xRot;

		GrazerRunPhase runphase = grazer.getRunPhase();

		if (runphase == GrazerRunPhase.RUNNING) {
			this.body.xRot = (-0.15F - Mth.cos(limbSwing) * 0.15F) * limbSwingAmount;
			this.jaw.xRot = (0.15F - Mth.cos(limbSwing - 0.5F) * 0.15F) * limbSwingAmount;
			this.rightWing.yRot = (-0.6F - Mth.cos(limbSwing * 0.6662F) * 0.6F) * limbSwingAmount;
			this.leftWing.yRot = -this.rightWing.yRot;
		} else {
			this.body.xRot = 0.0F;
			this.jaw.xRot = 0.0F;
			this.rightWing.yRot = 0.0F;
			this.leftWing.yRot = 0.0F;
		}
	}
}