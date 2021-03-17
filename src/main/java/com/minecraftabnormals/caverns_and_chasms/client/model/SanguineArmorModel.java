package com.minecraftabnormals.caverns_and_chasms.client.model;// Made with Blockbench 3.8.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.HashMap;
import java.util.Map;

public class SanguineArmorModel<T extends LivingEntity> extends BipedModel<T> {
	private static final Map<Integer, SanguineArmorModel<? extends LivingEntity>> CACHE = new HashMap<>();

	public ModelRenderer helmet;
	public ModelRenderer chestplate;
	public ModelRenderer leggingsLeft;
	public ModelRenderer leggingsRight;
	public ModelRenderer bootsLeft;
	public ModelRenderer bootsRight;
	public ModelRenderer shoulderPadRight;
	public ModelRenderer shoulderPadLeft;

	private final EquipmentSlotType slot;
	private final byte entityFlag;

	private SanguineArmorModel(int entityFlag) {
		super(1.0F, 0.0F, 64, 64);
		this.slot = EquipmentSlotType.values()[entityFlag & 15];
		this.entityFlag = (byte) (entityFlag >> 4);

		textureWidth = 128;
		textureHeight = 64;

		helmet = new ModelRenderer(this);
		helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmet.setTextureOffset(1, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.6F, false);
		helmet.setTextureOffset(38, 1).addBox(-6.9F, -11.55F, -1.0F, 2.0F, 5.0F, 2.0F, 0.25F, false);
		helmet.setTextureOffset(47, 1).addBox(4.9F, -11.55F, -1.0F, 2.0F, 5.0F, 2.0F, 0.25F, false);

		chestplate = new ModelRenderer(this);
		chestplate.setRotationPoint(0.0F, 0.0F, 0.0F);
		chestplate.setTextureOffset(37, 13).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 14.0F, 5.0F, 0.3F, false);

		leggingsLeft = new ModelRenderer(this);
		leggingsLeft.setRotationPoint(2.0F, 12.0F, 0.0F);
		leggingsLeft.setTextureOffset(56, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.25F, true);

		leggingsRight = new ModelRenderer(this);
		leggingsRight.setRotationPoint(-2.0F, 12.0F, 0.0F);
		leggingsRight.setTextureOffset(39, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.25F, false);

		bootsLeft = new ModelRenderer(this);
		bootsLeft.setRotationPoint(2.0F, 12.0F, 0.0F);
		bootsLeft.setTextureOffset(81, 16).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.5F, true);

		bootsRight = new ModelRenderer(this);
		bootsRight.setRotationPoint(-2.0F, 12.0F, 0.0F);
		bootsRight.setTextureOffset(64, 16).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.5F, false);

		shoulderPadRight = new ModelRenderer(this);
		shoulderPadRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
		shoulderPadRight.setTextureOffset(6, 19).addBox(-4.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, 0.3F, false);
		shoulderPadRight.setTextureOffset(58, 3).addBox(-4.2F, -5.4F, -1.0F, 2.0F, 3.0F, 2.0F, 0.1F, false);

		shoulderPadLeft = new ModelRenderer(this);
		shoulderPadLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
		shoulderPadLeft.setTextureOffset(6, 32).addBox(-1.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, 0.3F, true);
		shoulderPadLeft.setTextureOffset(67, 3).addBox(2.2F, -5.4F, -1.0F, 2.0F, 3.0F, 2.0F, 0.1F, true);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		boolean child = (this.entityFlag & 4) > 0;

		if (this.slot == EquipmentSlotType.HEAD) {
			matrixStack.push();
			this.helmet.copyModelAngles(this.bipedHead);
			if (child) {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
				this.helmet.setRotationPoint(0.0F, 15.0F, 0.0F);
			}
			this.helmet.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();

		}

		if (this.slot == EquipmentSlotType.CHEST) {
			matrixStack.push();

			this.chestplate.copyModelAngles(this.bipedBody);
			this.shoulderPadLeft.copyModelAngles(this.bipedLeftArm);
			this.shoulderPadRight.copyModelAngles(this.bipedRightArm);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.chestplate.setRotationPoint(0.0F, 24.0F, 0.0F);
				this.shoulderPadLeft.setRotationPoint(5.0F, 24.0F, 0.0F);
				this.shoulderPadRight.setRotationPoint(-5.0F, 24.0F, 0.0F);
			}
			this.shoulderPadLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.shoulderPadRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);

			this.chestplate.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();
		}

		if (this.slot == EquipmentSlotType.LEGS) {
			matrixStack.push();
			matrixStack.scale(1.01F, 1.0F, 1.01F);
			this.leggingsLeft.copyModelAngles(this.bipedLeftLeg);
			this.leggingsRight.copyModelAngles(this.bipedRightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.leggingsLeft.setRotationPoint(2.0F, 36.0F, 0.0F);
				this.leggingsRight.setRotationPoint(-2.0F, 36.0F, 0.0F);
			}
			this.leggingsLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.leggingsRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();
		}

		if (this.slot == EquipmentSlotType.FEET) {
			matrixStack.push();
			matrixStack.scale(1.05F, 1.0F, 1.05F);

			this.bootsLeft.copyModelAngles(this.bipedLeftLeg);
			this.bootsRight.copyModelAngles(this.bipedRightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.bootsLeft.setRotationPoint(2.0F, 37.0F, 0.0F);
				this.bootsRight.setRotationPoint(-2.0F, 37.0F, 0.0F);
			}
			this.bootsLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.bootsRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();
		}
	}

	@SuppressWarnings("unchecked")
	public static <A extends BipedModel<?>> A getModel(EquipmentSlotType slot, LivingEntity entity) {
		int entityFlag = (slot.ordinal() & 15) | (0) << 4 | (0) << 5 | (entity.isChild() ? 1 : 0) << 6;
		return (A) CACHE.computeIfAbsent(entityFlag, SanguineArmorModel::new);
	}
}