package com.minecraftabnormals.caverns_and_chasms.client.model;// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class SanguineArmorModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "sanguine_armor"), "main");
	private static final Map<Integer, SanguineArmorModel<? extends LivingEntity>> CACHE = new HashMap<>();

	public ModelPart helmet;
	public ModelPart chestplate;
	public ModelPart leggingsLeft;
	public ModelPart leggingsRight;
	public ModelPart bootsLeft;
	public ModelPart bootsRight;
	public ModelPart shoulderPadRight;
	public ModelPart shoulderPadLeft;

	private final EquipmentSlot slot;
	private final byte entityFlag;

	public SanguineArmorModel(int entityFlag, ModelPart root) {
		super(root);

		this.slot = EquipmentSlot.values()[entityFlag & 15];
		this.entityFlag = (byte) (entityFlag >> 4);

		this.helmet = root.getChild("helmet");
		this.chestplate = root.getChild("chestplate");
		this.leggingsLeft = root.getChild("leggingsLeft");
		this.leggingsRight = root.getChild("leggingsRight");
		this.bootsLeft = root.getChild("bootsLeft");
		this.bootsRight = root.getChild("bootsRight");
		this.shoulderPadRight = root.getChild("shoulderPadRight");
		this.shoulderPadLeft = root.getChild("shoulderPadLeft");
	}
	//Layer Definition
	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition helmet = root.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(47, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, false).addBox(-6.9F, -11.55F, -1.0F, 2.0F, 5.0F, 2.0F, false).addBox(4.9F, -11.55F, -1.0F, 2.0F, 5.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition chestplate = root.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(37, 13).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 14.0F, 5.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leggingsLeft = root.addOrReplaceChild("leggingsLeft", CubeListBuilder.create().texOffs(56, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, false), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leggingsRight = root.addOrReplaceChild("leggingsRight", CubeListBuilder.create().texOffs(39, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, false), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition bootsLeft = root.addOrReplaceChild("bootsLeft", CubeListBuilder.create().texOffs(81, 16).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 6.0F, 4.0F, false), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition bootsRight = root.addOrReplaceChild("bootsRight", CubeListBuilder.create().texOffs(64, 16).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 6.0F, 4.0F, false), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition shoulderPadRight = root.addOrReplaceChild("shoulderPadRight", CubeListBuilder.create().texOffs(58, 3).addBox(-4.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, false).addBox(-4.2F, -5.4F, -1.0F, 2.0F, 3.0F, 2.0F, false), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition shoulderPadLeft = root.addOrReplaceChild("shoulderPadLeft", CubeListBuilder.create().texOffs(67, 3).addBox(-1.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, false).addBox(2.2F, -5.4F, -1.0F, 1.9999998F, 3.0F, 2.0F, false), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	public SanguineArmorModel(int entityFlag) {
		this(entityFlag, createLayerDefinition().bakeRoot());
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		boolean child = (this.entityFlag & 4) > 0;

		if (this.slot == EquipmentSlot.HEAD) {
			matrixStack.pushPose();
			this.helmet.copyFrom(this.head);
			if (child) {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
				this.helmet.setPos(0.0F, 15.0F, 0.0F);
			}
			this.helmet.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();

		}

		if (this.slot == EquipmentSlot.CHEST) {
			matrixStack.pushPose();

			this.chestplate.copyFrom(this.body);
			this.shoulderPadLeft.copyFrom(this.leftArm);
			this.shoulderPadRight.copyFrom(this.rightArm);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.chestplate.setPos(0.0F, 24.0F, 0.0F);
				this.shoulderPadLeft.setPos(5.0F, 24.0F, 0.0F);
				this.shoulderPadRight.setPos(-5.0F, 24.0F, 0.0F);
			}
			this.shoulderPadLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.shoulderPadRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);

			this.chestplate.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();
		}

		if (this.slot == EquipmentSlot.LEGS) {
			matrixStack.pushPose();
			matrixStack.scale(1.01F, 1.0F, 1.01F);
			this.leggingsLeft.copyFrom(this.leftLeg);
			this.leggingsRight.copyFrom(this.rightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.leggingsLeft.setPos(2.0F, 36.0F, 0.0F);
				this.leggingsRight.setPos(-2.0F, 36.0F, 0.0F);
			}
			this.leggingsLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.leggingsRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();
		}

		if (this.slot == EquipmentSlot.FEET) {
			matrixStack.pushPose();
			matrixStack.scale(1.05F, 1.0F, 1.05F);

			this.bootsLeft.copyFrom(this.leftLeg);
			this.bootsRight.copyFrom(this.rightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.bootsLeft.setPos(2.0F, 37.0F, 0.0F);
				this.bootsRight.setPos(-2.0F, 37.0F, 0.0F);
			}
			this.bootsLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.bootsRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();
		}
	}

	@SuppressWarnings("unchecked")
	public static <A extends HumanoidModel<?>> A getModel(EquipmentSlot slot, LivingEntity entity) {
		int entityFlag = (slot.ordinal() & 15) | (0) << 4 | (0) << 5 | (entity.isBaby() ? 1 : 0) << 6;
		return (A) CACHE.computeIfAbsent(entityFlag, SanguineArmorModel::new);
	}
}