package com.teamabnormals.caverns_and_chasms.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.caverns_and_chasms.client.resources.DeeperSpriteUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DeeperModel<T extends Entity> extends ListModel<T> {
	private final ModelPart head;
	private final ModelPart body;

	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;

	public DeeperModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.leg1 = root.getChild("leg1");
		this.leg2 = root.getChild("leg2");
		this.leg3 = root.getChild("leg3");
		this.leg4 = root.getChild("leg4");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation).texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.3F, 0.3F, 0.3F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("hat_1", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -8.3F, 0.0F, 0.0F, Mth.PI / 4F, 0.0F));
		head.addOrReplaceChild("hat_2", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -8.0F, 0.0F, 12.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -8.3F, 0.0F, 0.0F, -Mth.PI / 4F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation).texOffs(40, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.3F, 0.3F, 0.3F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(-2.0F, 18.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(2.0F, 18.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(-2.0F, 18.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(2.0F, 18.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void renderOverlay(DeeperSprite sprite, boolean emissive, PoseStack matrixStack, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		TextureAtlasSprite textureatlassprite = DeeperSpriteUploader.getSprite(sprite);
		RenderType render = emissive ? BlueprintRenderTypes.getUnshadedTranslucentEntity(DeeperSpriteUploader.ATLAS_LOCATION, false) : RenderType.entityTranslucent(DeeperSpriteUploader.ATLAS_LOCATION);
		super.renderToBuffer(matrixStack, textureatlassprite.wrap(Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(render)), emissive ? 15728880 : packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.head, this.body, this.leg1, this.leg2, this.leg3, this.leg4);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}

	@OnlyIn(Dist.CLIENT)
	public static enum DeeperSprite {
		PRIMED,
		EMISSIVE,
		CHARGED,
		CHARGED_EMISSIVE
	}
}