package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Glare;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class GlareModel<T extends Glare> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(CavernsAndChasms.MOD_ID, "glare"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart bottom;

	public GlareModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.bottom = root.getChild("bottom");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -13.0F, -6.0F, 12.0F, 13.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 0.01F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-6.0F, -11.0F, -6.0F, 12.0F, 0.01F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(24, 42).addBox(-5.0F, -12.0F, -1.0F, 10.0F, 10.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 25).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(17, 1).addBox(-5.0F, 3.9F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 42).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.bottom.xRot = Math.min(limbSwingAmount / 0.3F, 0.3F) * 0.6981317F;

		//TODO: Make tail move back and forth a bit when idle
		this.head.y += Math.sin(ageInTicks / 5) * 0.5F;
		this.bottom.y += Math.sin(ageInTicks / 5) * 0.5F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}