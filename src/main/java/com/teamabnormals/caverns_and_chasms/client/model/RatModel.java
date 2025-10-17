package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatCollarLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatHeldItemLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat.RatVariant;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder.AttachedRat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCRegistries;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RatModel extends AgeableListModel<Rat> {
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart tail;
	public final ModelPart leftFrontLeg;
	public final ModelPart leftHindLeg;
	public final ModelPart rightFrontLeg;
	public final ModelPart rightHindLeg;
	public RatPose pose;
	public float tailWagAmount;

	public RatModel(ModelPart root) {
		super(false, 5.0F, 2.0F);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.tail = root.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 6).addBox(-3.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(1.0F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("whisker", CubeListBuilder.create().texOffs(12, 14).addBox(-3.5F, -2.0F, -4.0F, 7.0F, 3.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("tooth", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 1.0F, -5.0F, 1.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 5.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 23.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.0F, 23.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 23.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.0F, 23.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 5).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 21.0F, 5.0F, 0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightFrontLeg, this.leftFrontLeg, this.rightHindLeg, this.leftHindLeg, this.tail);
	}

	public void renderFromTag(CompoundTag compound, Level level, LivingEntity entity, ItemInHandRenderer itemInHandRenderer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean hasowner = compound.hasUUID("Owner");
		boolean isbaby = compound.getInt("Age") < 0;
		RatVariant type = level.registryAccess().registryOrThrow(CCRegistries.RAT_VARIANT).get(new ResourceLocation(compound.getString("Variant")));
		ItemStack heldstack = ItemStack.of(compound.getList("HandItems", 10).getCompound(0));

		this.young = isbaby;
		this.tailWagAmount = Rat.calculateTailWagAmount(compound.getFloat("Health"), (float) AttachedRat.getAttributeValue(compound, Attributes.MAX_HEALTH), hasowner);
		this.setupAnim(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		VertexConsumer vertexconsumer = buffer.getBuffer(this.renderType(type.texture().withPrefix("textures/").withSuffix(".png")));
		this.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		if (hasowner) {
			DyeColor collarcolor = compound.contains("CollarColor", 99) ? DyeColor.byId(compound.getInt("CollarColor")) : DyeColor.RED;
			RatCollarLayer.renderCollar(this, poseStack, buffer, packedLight, collarcolor, 0, 0);
		}
		RatHeldItemLayer.renderItem(this, itemInHandRenderer, poseStack, buffer, packedLight, entity, isbaby, heldstack, netHeadYaw, headPitch);
	}

	@Override
	public void setupAnim(Rat rat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.setupAnim(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	public void setupAnim(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.tail.yRot = -this.tailWagAmount * 0.45F * Mth.sin(0.6F * ageInTicks);

		if (this.pose == RatPose.SITTING) {
			this.head.setPos(0.0F, !this.young ? 15.0F : 14.0F, !this.young ? 0.5F : -1.5F);
			this.body.setPos(0.0F, 16.0F, -0.5F);
			this.body.xRot = -Mth.PI / 2.0F;
			this.tail.setPos(0.0F, 23.0F, 2.5F);
		} else {
			this.head.setPos(0.0F, !this.young ? 21.0F : 17.5F, -3.0F);
			this.body.setPos(0.0F, 21.0F, -3.0F);
			this.body.xRot = 0.0F;
			this.tail.setPos(0.0F, 21.0F, 5.0F);
		}

		if (this.pose == RatPose.ON_SHOULDER) {
			this.tail.xRot = -1.0F;
		} else {
			this.tail.xRot = 0.0F;
		}

		if (this.pose == RatPose.ATTACHED) {
			this.body.zRot = Mth.sin(ageInTicks) * 0.3F;
		} else {
			this.body.zRot = 0.0F;
		}

		switch (this.pose) {
			case STANDING:
				this.rightHindLeg.setPos(-2.0F, 23.0F, 4.0F);
				this.leftHindLeg.setPos(2.0F, 23.0F, 4.0F);
				this.rightFrontLeg.setPos(-2.0F, 23.0F, -1.0F);
				this.leftFrontLeg.setPos(2.0F, 23.0F, -1.0F);
				this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
				this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
				this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
				this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
				break;
			case SITTING:
				this.rightHindLeg.setPos(-2.0F, 24.0F, -3.0F);
				this.leftHindLeg.setPos(2.0F, 24.0F, -3.0F);
				this.rightFrontLeg.setPos(-2.0F, 19.0F, -2.5F);
				this.leftFrontLeg.setPos(2.0F, 19.0F, -2.5F);
				this.rightHindLeg.xRot = -Mth.HALF_PI;
				this.leftHindLeg.xRot = -Mth.HALF_PI;
				this.rightFrontLeg.xRot = -Mth.HALF_PI;
				this.leftFrontLeg.xRot = -Mth.HALF_PI;
				break;
			case ON_SHOULDER:
				this.rightHindLeg.setPos(-1.0F, 23.0F, 3.0F);
				this.leftHindLeg.setPos(1.0F, 23.0F, 3.0F);
				this.rightFrontLeg.setPos(-1.0F, 23.0F, 0.0F);
				this.leftFrontLeg.setPos(1.0F, 23.0F, 0.0F);
				this.rightHindLeg.xRot = 0.0F;
				this.leftHindLeg.xRot = 0.0F;
				this.rightFrontLeg.xRot = 0.0F;
				this.leftFrontLeg.xRot = 0.0F;
				break;
			case ATTACHED:
				this.rightHindLeg.setPos(-2.0F, 23.0F, 4.0F);
				this.leftHindLeg.setPos(2.0F, 23.0F, 4.0F);
				this.rightFrontLeg.setPos(-2.0F, 23.0F, -1.0F);
				this.leftFrontLeg.setPos(2.0F, 23.0F, -1.0F);
				this.rightHindLeg.xRot = 0.0F;
				this.leftHindLeg.xRot = 0.0F;
				this.rightFrontLeg.xRot = 0.0F;
				this.leftFrontLeg.xRot = 0.0F;
				break;
		}
	}

	@Override
	public void prepareMobModel(Rat rat, float limbSwing, float limbSwingAmount, float partialTick) {
		this.pose = rat.isInSittingPose() ? RatPose.SITTING : RatPose.STANDING;
		this.tailWagAmount = rat.getTailWagAmount();
		super.prepareMobModel(rat, limbSwing, limbSwingAmount, partialTick);
	}

	@OnlyIn(Dist.CLIENT)
	public enum RatPose {
		STANDING,
		SITTING,
		ON_SHOULDER,
		ATTACHED;
	}
}