package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

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
		this.pupil = this.head.getChild("pupil");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		CubeDeformation extrudes = new CubeDeformation(0.25F);
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -7.99F, 10.0F, 10.0F, 10.0F, deformation).texOffs(40, 0).addBox(-5.0F, -12.0F, -7.99F, 10.0F, 10.0F, 10.0F, extrudes), PartPose.offset(0.0F, 1.0F, 0.0F));
		head.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, deformation), PartPose.offset(0.0F, -7.0F, -8.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 17.0F, 4.0F, deformation).texOffs(40, 20).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 17.0F, 4.0F, extrudes), PartPose.offset(0.0F, 22.0F, 0.0F));
		root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(48, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation).texOffs(112, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, extrudes), PartPose.offset(-2.0F, 16.0F, 4.0F));
		root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(32, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation), PartPose.offset(2.0F, 16.0F, 4.0F));
		root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation), PartPose.offset(-2.0F, 16.0F, -4.0F));
		root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(16, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation).texOffs(80, 41).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 9.0F, 4.0F, extrudes), PartPose.offset(2.0F, 16.0F, -4.0F));

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
			this.pupil.xScale = 1.0F;
			this.pupil.yScale = 1.0F;

			float scale = Math.min(1.5F, 2.0F / peeper.distanceTo(target));
			this.pupil.xScale += scale;
			this.pupil.yScale += scale;

			RandomSource random = peeper.getRandom();
			this.pupil.x = 0.0F;
			this.pupil.y = -7.0F;
			if (peeper.distanceTo(target) <= 3.0F) {
				this.pupil.x += (float) MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random);
				this.pupil.y += (float) MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random);
			}
		}

		this.pupil.visible = true;
	}
}