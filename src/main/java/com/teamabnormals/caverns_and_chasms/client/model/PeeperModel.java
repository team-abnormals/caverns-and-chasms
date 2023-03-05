package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PeeperModel<T extends Peeper> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart pupil;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;

	public PeeperModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.pupil = root.getChild("head").getChild("pupil");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		CubeDeformation extrudes = new CubeDeformation(0.25F);
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -7.99F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(40, 0).addBox(-5.0F, -12.0F, -7.99F, 10.0F, 10.0F, 10.0F, extrudes), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition pupil = head.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -8.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 17.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 20).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 17.0F, 4.0F, extrudes), PartPose.offset(0.0F, 22.0F, 0.0F));
		PartDefinition right_hind_leg = root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(48, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(112, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, extrudes), PartPose.offset(-2.0F, 16.0F, 4.0F));
		PartDefinition left_hind_leg = root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(32, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 16.0F, 4.0F));
		PartDefinition right_front_leg = root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 16.0F, -4.0F));
		PartDefinition left_front_leg = root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(16, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(80, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, extrudes), PartPose.offset(2.0F, 16.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T peeper, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		Entity target = Minecraft.getInstance().getCameraEntity();
		if (peeper.getTarget() != null) {
			target = peeper.getTarget();
		}

		if (target != null) {
//			Vec3 vec3 = target.getEyePosition(0.0F);
//			Vec3 vec31 = peeper.getEyePosition(0.0F);
//			double d0 = vec3.y - vec31.y;
//			if (d0 > 0.0D) {
//				this.pupil.y = 0.0F;
//			} else {
//				this.pupil.y = 1.0F;
//			}
//
//			Vec3 vec32 = peeper.getViewVector(0.0F);
//			vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
//			Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
//			double d1 = vec32.dot(vec33);
//			this.pupil.x = Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1);
		}

		this.pupil.visible = true;
	}
}