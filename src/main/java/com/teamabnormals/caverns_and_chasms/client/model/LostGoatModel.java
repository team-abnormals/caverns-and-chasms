package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class LostGoatModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart head;
	private final ModelPart body;

	public LostGoatModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(18, 65).addBox(-6.0F, -6.0F, -3.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(18, 65).mirror().addBox(2.0F, -6.0F, -3.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 10.0F, -8.5F, -0.1745F, 0.0F, 0.0F));

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(34, 51).addBox(-3.0F, -4.0F, -8.0F, 5.0F, 6.0F, 10.0F, new CubeDeformation(0.25F))
				.texOffs(54, 61).addBox(-3.0F, 1.0F, -8.0F, 5.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 0.9599F, 0.0F, 0.0F));

		PartDefinition mouth = nose.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(34, 67).addBox(-3.0F, 0.0F, -8.0F, 5.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition left_horn = head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(26, 59).addBox(-0.01F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 7.0F));

		PartDefinition right_horn = head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(26, 59).addBox(-2.99F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 7.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -17.0F, -7.0F, 9.0F, 14.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(59, 29).addBox(-6.0F, -18.0F, 4.0F, 13.0F, 15.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 76).addBox(-3.0F, -7.0F, -6.0F, 7.0F, 6.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 31).addBox(-5.0F, -18.0F, -8.0F, 11.0F, 16.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(59, 9).addBox(-7.0F, -19.0F, -5.0F, 15.0F, 17.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(26, 90).addBox(-4.0F, -7.0F, -7.0F, 9.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 25.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}