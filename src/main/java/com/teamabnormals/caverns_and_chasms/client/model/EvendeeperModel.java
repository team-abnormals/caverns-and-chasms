package com.teamabnormals.caverns_and_chasms.client.model;

import com.teamabnormals.caverns_and_chasms.client.resources.EvendeeperSpriteUploader;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Evendeeper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EvendeeperModel extends DeeperModel<Evendeeper> {

	public EvendeeperModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation, boolean includeHat) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -6.0F, 8.0F, 8.0F, 8.0F, deformation).texOffs(32, 0).addBox(-4.0F, -8.0F, -6.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)).texOffs(32, 38).addBox(-4.0F, -10.0F, -6.0F, 4.0F, 2.0F, 4.0F, deformation.extend(0.19F)), PartPose.offset(0.0F, 6.0F, 0.0F));
		if (includeHat) {
			head.addOrReplaceChild("hat_1", CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, -7.0F, 0.0F, 6.0F, 7.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -10.0F, -4.0F, 0.0F, Mth.PI / 4F, 0.0F));
			head.addOrReplaceChild("hat_2", CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, -7.0F, 0.0F, 6.0F, 7.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -10.0F, -4.0F, 0.0F, -Mth.PI / 4F, 0.0F));
		}
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 14.0F, 4.0F, deformation).texOffs(32, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 14.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, deformation), PartPose.offset(-2.0F, 20.0F, 4.0F));
		root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offset(2.0F, 18.0F, 4.0F));
		root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offset(-2.0F, 18.0F, -4.0F));
		root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, deformation), PartPose.offset(2.0F, 20.0F, -4.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected TextureAtlasSprite getTextureAtlasSprite(DeeperSprite deeperSprite) {
		return EvendeeperSpriteUploader.getSprite(deeperSprite);
	}

	@Override
	protected ResourceLocation getTextureAtlasLocation() {
		return EvendeeperSpriteUploader.ATLAS_LOCATION;
	}
}