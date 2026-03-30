package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatCollarLayer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers.RatHeldItemLayer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.RatVariant;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class RatModel extends AgeableListModel<Rat> {
	private static final RandomSource RANDOM = RandomSource.create();

	public final ModelPart root;
	public final ModelPart head;
	public final ModelPart leftEar;
	public final ModelPart rightEar;
	public final ModelPart body;
	public final ModelPart tail;
	public final ModelPart leftFrontLeg;
	public final ModelPart leftHindLeg;
	public final ModelPart rightFrontLeg;
	public final ModelPart rightHindLeg;

	public RatPose pose;
	private float tailWagAmount;
	private boolean eating;
	private boolean wounded;
	private float shakeAnim;

	public RatModel(ModelPart root) {
		super(false, 5.0F, 2.0F);
		this.root = root;
		this.head = root.getChild("head");
		this.leftEar = root.getChild("left_ear");
		this.rightEar = root.getChild("right_ear");
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
		root.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offset(-1.0F, -2.0F, -1.5F));
		root.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 6).mirror().addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offset(1.0F, -2.0F, -1.5F));
		head.addOrReplaceChild("right_whisker", CubeListBuilder.create().texOffs(12, 14).addBox(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F), PartPose.offset(-1.5F, -0.5F, -4.0F));
		head.addOrReplaceChild("left_whisker", CubeListBuilder.create().texOffs(17, 14).addBox(0.0F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F), PartPose.offset(1.5F, -0.5F, -4.0F));
		head.addOrReplaceChild("tooth", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 1.0F, -5.0F, 1.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 5.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		body.addOrReplaceChild("fur1", CubeListBuilder.create().texOffs(0, 22).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F), PartPose.offset(0.0F, -1.0F, 2.0F));
		body.addOrReplaceChild("fur2", CubeListBuilder.create().texOffs(14, 22).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F), PartPose.offset(0.0F, -1.0F, 6.0F));
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

	public void renderEars(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (this.head.visible) {
			if (this.young) {
				poseStack.pushPose();
				poseStack.translate(0.0F, this.babyYHeadOffset / 16.0F, this.babyZHeadOffset / 16.0F);
				this.renderEarsUnscaled(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
				poseStack.popPose();
			} else {
				this.renderEarsUnscaled(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
			}
		}
	}

	private void renderEarsUnscaled(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.pushPose();
		this.head.translateAndRotate(poseStack);
		this.leftEar.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.rightEar.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.popPose();
	}

	public void renderFromTag(CompoundTag compound, Level level, LivingEntity entity, ItemInHandRenderer itemInHandRenderer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean hasOwner = compound.hasUUID("Owner");
		boolean isBaby = compound.getInt("Age") < 0;
		RatVariant variant = level.registryAccess().registryOrThrow(CCRegistries.RAT_VARIANT).get(new ResourceLocation(compound.getString("Variant")));
		ItemStack heldStack = ItemStack.of(compound.getList("HandItems", 10).getCompound(0));
		float health = compound.getFloat("Health");
		boolean dirty = compound.getBoolean("Dirty");

		this.young = isBaby;
		this.tailWagAmount = Rat.calculateTailWagAmount(compound.getFloat("Health"), (float) getAttributeValue(compound, Attributes.MAX_HEALTH));
		this.eating = false;
		this.wounded = health <= Rat.WOUNDED_THRESHOLD;
		this.shakeAnim = 0.0F;

		if (compound.hasUUID("UUID")) {
			RANDOM.setSeed(compound.getUUID("UUID").hashCode());
		} else {
			RANDOM.setSeed(0L);
		}

		this.setupAnim(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		ResourceLocation textureLocation = variant.getTexture(this.wounded, dirty);

		VertexConsumer vertexConsumer = buffer.getBuffer(this.renderType(textureLocation));
		this.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		VertexConsumer vertexConsumerEars = buffer.getBuffer(RenderType.entityCutout(textureLocation));
		this.renderEars(poseStack, vertexConsumerEars, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		if (hasOwner) {
			DyeColor collarColor = compound.contains("CollarColor", 99) ? DyeColor.byId(compound.getInt("CollarColor")) : DyeColor.RED;
			RatCollarLayer.renderCollar(this, poseStack, buffer, packedLight, collarColor, 0, 0);
		}
		RatHeldItemLayer.renderItem(null, this, itemInHandRenderer, poseStack, buffer, packedLight, entity, isBaby, heldStack, netHeadYaw, headPitch);
	}

	@Override
	public void setupAnim(Rat rat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.setupAnim(limbSwing, limbSwingAmount, ageInTicks + rat.getAnimTimeOffset(), netHeadYaw, headPitch);
	}

	public void setupAnim(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.tail.yRot = -this.tailWagAmount * 0.45F * Mth.sin(0.6F * ageInTicks);

		if (this.pose == RatPose.SITTING) {
			this.head.setPos(0.0F, !this.young ? 15.0F : 14.0F, !this.young ? 0.5F : -1.5F);
			this.body.setPos(0.0F, 16.0F, -0.5F);
			this.body.xRot = -Mth.PI / 2.0F;
			this.tail.setPos(0.0F, 23.0F, 2.5F);
		} else {
			this.head.setPos(0.0F, !this.young ? 21.0F : 17.5F, -3.0F);
		}

		if (this.pose == RatPose.ON_SHOULDER) {
			this.tail.xRot = -1.0F;
		} else if (this.pose == RatPose.STANDING && this.wounded) {
			this.tail.xRot = -0.25F;
		}

		if (this.pose == RatPose.ATTACHED) {
			this.head.xRot = 0.6F;
			this.head.yRot = Mth.sin(ageInTicks * 0.75F) * 0.25F;
			this.body.zRot = Mth.sin(ageInTicks) * 0.3F;
		} else {
			this.head.xRot = headPitch * Mth.DEG_TO_RAD;
			this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;

			if (this.eating) {
				this.head.xRot += 0.4F + 0.05F * Mth.cos(ageInTicks * 1.5F);
				this.head.yRot += 0.4F * Mth.cos(ageInTicks * 0.3F);
			}

			if (this.wounded) {
				this.head.xRot += 0.25F + 0.1F * Mth.sin(ageInTicks * 0.1F);
				this.head.zRot = RANDOM.nextBoolean() ? -0.3F : 0.3F;
			}

			if (this.shakeAnim > 0.0F) {
				this.head.zRot += getShake(this.shakeAnim, 0.0F);
				if (this.pose == RatPose.SITTING) {
					this.body.yRot += getShake(this.shakeAnim, 1.0F);
				} else {
					this.body.zRot += getShake(this.shakeAnim, 1.0F);
				}
				this.tail.zRot += getShake(this.shakeAnim, 2.0F);
			}
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

	private static float getShake(float animTime, float timeOffset) {
		float f = 2.0F;
		float f1 = Rat.SHAKE_TIME - f;
		if (animTime > f - timeOffset && animTime < f1 - timeOffset) {
			float amount = (Mth.cos((animTime - f + timeOffset) * Mth.PI * 2.0F / f1 + Mth.PI) + 1.0F) * 0.5F;
			return Mth.sin((animTime + timeOffset) * Mth.PI * 0.5F) * amount * 0.7F;
		} else {
			return 0.0F;
		}
	}

	@Override
	public void prepareMobModel(Rat rat, float limbSwing, float limbSwingAmount, float partialTick) {
		this.pose = rat.isAttachedToEntity() ? RatPose.ATTACHED : rat.isSitting() ? RatPose.SITTING : RatPose.STANDING;
		this.tailWagAmount = rat.getTailWagAmount();
		this.eating = rat.isEating();
		this.wounded = rat.isVisuallyWounded();
		this.shakeAnim = rat.getShakeAnim(partialTick);
		RANDOM.setSeed(rat.getUUID().hashCode());
		super.prepareMobModel(rat, limbSwing, limbSwingAmount, partialTick);
	}

	public ModelPart root() {
		return this.root;
	}

	@OnlyIn(Dist.CLIENT)
	public enum RatPose {
		STANDING,
		SITTING,
		ON_SHOULDER,
		ATTACHED;
	}

	private static double getAttributeValue(CompoundTag entityData, Attribute attribute) {
		if (entityData.contains("Attributes", 9)) {
			ListTag attributes = entityData.getList("Attributes", 10);

			for (int i = 0; i < attributes.size(); ++i) {
				CompoundTag attributetag = attributes.getCompound(i);
				if (attributetag.getString("Name").equals(ForgeRegistries.ATTRIBUTES.getKey(attribute).toString())) {
					double basevalue = attributetag.getDouble("Base");
					double addition = 0.0D;
					double multiplybase = 0.0D;
					double multiplytotal = 1.0D;

					if (attributetag.contains("Modifiers", 9)) {
						ListTag modifiers = attributetag.getList("Modifiers", 10);

						for (int j = 0; j < modifiers.size(); ++j) {
							CompoundTag modifier = modifiers.getCompound(j);
							AttributeModifier.Operation operation = AttributeModifier.Operation.fromValue(modifier.getInt("Operation"));
							switch (operation) {
								case ADDITION -> addition += modifier.getDouble("Amount");
								case MULTIPLY_BASE -> multiplybase += modifier.getDouble("Amount");
								case MULTIPLY_TOTAL -> multiplytotal *= (1.0D + modifier.getDouble("Amount"));
							}
						}
					}

					double value = basevalue + addition;
					value += value * multiplybase;
					value *= multiplytotal;

					if (attribute instanceof RangedAttribute rangedattribute)
						return (Double.isNaN(value) ? rangedattribute.getMinValue() : Mth.clamp(value, rangedattribute.getMinValue(), rangedattribute.getMaxValue()));
					else
						return value;
				}
			}
		}

		return DefaultAttributes.getSupplier(CCEntityTypes.RAT.get()).getBaseValue(attribute);
	}
}