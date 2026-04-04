package com.teamabnormals.caverns_and_chasms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class AegisModel extends Model {
	private final ModelPart root;
	private final ModelPart plate;
	private final ModelPart handle;

	public AegisModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.root = root;
		this.plate = root.getChild("plate");
		this.handle = root.getChild("handle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
		root.addOrReplaceChild("plate", CubeListBuilder.create()
				.texOffs(0, 32).addBox(-7.0F, -11.0F, -3.0F, 14.0F, 17.0F, 2.0F)
				.texOffs(0, 51).addBox(-5.0F, 6.0F, -3.0F, 10.0F, 11.0F, 2.0F)
				.texOffs(0, 0).addBox(-7.0F, -11.0F, -3.0F, 14.0F, 28.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 64);
	}

	public ModelPart plate() {
		return this.plate;
	}

	public ModelPart handle() {
		return this.handle;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}