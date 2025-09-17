package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.resources.GrazerSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.AbstractGrazer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrazerModel extends AgeableListModel<AbstractGrazer> {
	private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(GrazerSpriteUploader.ATLAS_LOCATION);

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart drool;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	private final ModelPart leftHindLeg;
	private final ModelPart rightHindLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightFrontLeg;

	public GrazerModel(ModelPart root) {
		super(true, 10.875F / 0.875F, 3.0F, 1.5F / 0.875F, 2.0F, 24.0F);
		this.head = root.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.drool = this.jaw.getChild("drool");
		this.body = root.getChild("body");
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

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 84).addBox(-5.0F, -13.0F, -19.0F, 10.0F, 9.0F, 12.0F), PartPose.offset(0.0F, 16.0F, 0.0F));
		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(15, 105).addBox(-5.0F, 0.0F, -9.0F, 10.0F, 3.0F, 9.0F), PartPose.offset(0.0F, -4.0F, -7.0F));
		jaw.addOrReplaceChild("drool", CubeListBuilder.create().texOffs(33, 116).addBox(5.0F, 3.0F, -9.0F, 0.0F, 9.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -25.0F, -19.0F, 10.0F, 12.0F, 24.0F)
				.texOffs(0, 36).addBox(-5.0F, -25.0F, -19.0F, 10.0F, 24.0F, 24.0F, new CubeDeformation(0.5F))
				.texOffs(68, 12).addBox(-5.0F, -13.0F, -7.0F, 10.0F, 12.0F, 12.0F), PartPose.offset(0.0F, 16.0F, 0.0F));
		body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(68, 89).addBox(0.0F, -20.0F, 0.0F, 1.0F, 22.0F, 14.0F), PartPose.offset(5.0F, -12.0F, 5.0F));
		body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(68, 89).addBox(-1.0F, -20.0F, 0.0F, 1.0F, 22.0F, 14.0F), PartPose.offset(-5.0F, -12.0F, 5.0F));
		root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(84, 71).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F), PartPose.offset(3.0F, 15.0F, 3.0F));
		root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(84, 71).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F).mirror(false), PartPose.offset(-3.0F, 15.0F, 3.0F));
		root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(68, 71).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F), PartPose.offset(3.05F, 15.0F, -2.0F));
		root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(68, 71).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F).mirror(false), PartPose.offset(-3.05F, 15.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(AbstractGrazer grazer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float partialtick = ageInTicks - (float) grazer.tickCount;

		if (grazer.isBaby()) {
			this.body.xRot = 0.0F;
			this.jaw.xRot = 0.0F;
			this.rightWing.yRot = -0.6F - Mth.cos(ageInTicks) * 0.6F;
			this.leftWing.yRot = 0.6F + Mth.cos(ageInTicks) * 0.6F;
			this.rightHindLeg.xRot = Mth.cos(ageInTicks * 0.7F) * 0.5F;
			this.leftHindLeg.xRot = Mth.cos(ageInTicks * 0.7F + Mth.PI) * 0.5F;
			this.rightFrontLeg.xRot = Mth.cos(ageInTicks * 0.7F + Mth.PI) * 0.5F;
			this.leftFrontLeg.xRot = Mth.cos(ageInTicks * 0.7F) * 0.5F;
		} else {
			float runamount = grazer.getRunAmount(partialtick);
			float bounceamount = grazer.getBounceAmount(partialtick);
			float wiggleamount = grazer.getWiggleAmount(partialtick);
			float onbackamount = grazer.getOnBackAmount(partialtick);
			float bestupidamount = grazer.getBeStupidAmount(partialtick);
			float walkamount = 1.0F - Math.max(bounceamount, wiggleamount);
			float idleanimamount = 1.0F - Math.max(Math.max(runamount, bounceamount), wiggleamount);

			float wingflapanim = grazer.getWingFlapAnim(partialtick);

			this.body.xRot = 0.0F;
			this.jaw.xRot = 0.0F;
			this.rightWing.yRot = 0.0F;
			this.leftWing.yRot = 0.0F;
			this.rightHindLeg.y = 15.0F;
			this.leftHindLeg.y = 15.0F;
			this.rightFrontLeg.y = 15.0F;
			this.leftFrontLeg.y = 15.0F;
			this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * walkamount;
			this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount * walkamount;
			this.rightFrontLeg.xRot = this.leftHindLeg.xRot;
			this.leftFrontLeg.xRot = this.rightHindLeg.xRot;

			// Wing flapping idle animation
			this.rightWing.yRot += (-0.5F + Mth.cos(wingflapanim * Mth.PI * 0.2F) * 0.5F) * idleanimamount;
			this.leftWing.yRot += (0.5F - Mth.cos(wingflapanim * Mth.PI * 0.2F) * 0.5F) * idleanimamount;

			// Being stupid animation
			float f = -bestupidamount * bestupidamount + 2.0F * bestupidamount;
			this.body.xRot += -0.2F * f;
			this.jaw.xRot += 0.6F * f;

			// Running animation
			this.body.xRot += (-0.15F - Mth.cos(limbSwing) * 0.15F) * limbSwingAmount * runamount;
			this.jaw.xRot += (0.15F - Mth.cos(limbSwing - 0.5F) * 0.15F) * limbSwingAmount * runamount;
			this.rightWing.yRot += (-0.6F - Mth.cos(limbSwing * 0.6662F) * 0.6F) * limbSwingAmount * runamount;
			this.leftWing.yRot += (0.6F + Mth.cos(limbSwing * 0.6662F) * 0.6F) * limbSwingAmount * runamount;

			// Bouncing animation
			this.rightWing.yRot += (-0.8F - Mth.cos(ageInTicks * 0.4F) * 0.6F) * bounceamount;
			this.leftWing.yRot += (0.8F + Mth.cos(ageInTicks * 0.4F) * 0.6F) * bounceamount;
			this.rightHindLeg.y += -6.0F * bounceamount;
			this.leftHindLeg.y += -6.0F * bounceamount;
			this.rightFrontLeg.y += -6.0F * bounceamount;
			this.leftFrontLeg.y += -6.0F * bounceamount;

			// Wiggling animation
			this.jaw.xRot += (0.15F - Mth.sin(ageInTicks * 0.6F - 0.5F) * 0.15F) * wiggleamount;
			this.rightWing.yRot += (-0.6F - Mth.cos(ageInTicks) * 0.6F) * wiggleamount * (1.0F - onbackamount * 0.5F);
			this.leftWing.yRot += (0.6F + Mth.cos(ageInTicks) * 0.6F) * wiggleamount * (1.0F - onbackamount * 0.5F);
			this.rightHindLeg.xRot += Mth.cos(ageInTicks * 0.7F) * 0.5F * wiggleamount;
			this.leftHindLeg.xRot += Mth.cos(ageInTicks * 0.7F + Mth.PI) * 0.5F * wiggleamount;
			this.rightFrontLeg.xRot += Mth.cos(ageInTicks * 0.7F + Mth.PI) * 0.5F * wiggleamount;
			this.leftFrontLeg.xRot += Mth.cos(ageInTicks * 0.7F) * 0.5F * wiggleamount;

			// Adjust wings when wiggling and stuck on back
			this.rightWing.yRot += -Mth.HALF_PI * onbackamount;
			this.leftWing.yRot += Mth.HALF_PI * onbackamount;
		}

		this.head.xRot = this.body.xRot;
	}

	public void renderOverlay(TextureAtlasSprite sprite, PoseStack matrixStack, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStack, sprite.wrap(Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RENDER_TYPE)), packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg);
	}
}