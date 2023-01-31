package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports

public class CopperGolemModel<T extends CopperGolem> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "copper_golem"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart nose;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private float headSpinTicks;
	private float buttonPressTicks;

	public CopperGolemModel(ModelPart rootIn) {
		this.root = rootIn;
		this.head = rootIn.getChild("head");
		this.nose = this.head.getChild("nose");
		this.body = rootIn.getChild("body");
		this.rightArm = this.body.getChild("right_arm");
		this.leftArm = this.body.getChild("left_arm");
		this.rightLeg = rootIn.getChild("right_leg");
		this.leftLeg = rootIn.getChild("left_leg");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.4F))
				.texOffs(0, 33).addBox(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.6F))
				.texOffs(22, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(22, 18).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(12, 22).addBox(-1.0F, 0.0F, -1.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -3.5F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 15.0F, 0.0F));
		body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-3.0F, -1.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, 0.0F));
		body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -1.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(24, 7).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 21.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 7).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 21.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void prepareMobModel(T copperGolem, float limbSwing, float limbSwingTicks, float partialTick) {
		super.prepareMobModel(copperGolem, limbSwing, limbSwingTicks, partialTick);
		this.headSpinTicks = copperGolem.getHeadSpinTicks(partialTick);
		this.buttonPressTicks = copperGolem.getPressButtonTicks(partialTick);
	}

	@Override
	public void setupAnim(T copperGolem, float limbSwing, float limbSwingTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float headspinanim = this.headSpinTicks > 10 ? Mth.TWO_PI * (this.headSpinTicks - 10F) / 16F : this.headSpinTicks > 4 ? -Mth.sin(Mth.PI * (this.headSpinTicks - 4.0F) / 6.0F) * (this.headSpinTicks - 4.0F) / 10.0F : 0.0F;
		float headtilt = this.headSpinTicks < 10 ? Mth.sin(Mth.PI * this.headSpinTicks * 0.3F) * this.headSpinTicks * 0.012F : 0.0F;

		this.head.yRot = netHeadYaw * (Mth.PI / 180F) + headspinanim;
		this.head.xRot = headPitch * (Mth.PI / 180F);
		this.head.zRot = headtilt;

		if (this.buttonPressTicks > 0.0F) {
			this.rightArm.xRot = -1.0F + Mth.triangleWave(this.buttonPressTicks, 12F);
			this.leftArm.xRot = -1.0F + Mth.triangleWave(this.buttonPressTicks, 12F);
		} else {
			this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * 1.6F * limbSwingTicks;
			this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * 1.6F * limbSwingTicks;
		}

		this.rightLeg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13F) * limbSwingTicks;
		this.leftLeg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13F) * limbSwingTicks;
	}
}