package com.minecraftabnormals.cave_upgrade.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.cave_upgrade.client.DeeperSpriteUploader;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ACRenderTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class DeeperModel<T extends Entity> extends SegmentedModel<T> {
	private final boolean emissive;

	private final ModelRenderer head;
	private final ModelRenderer body;

	private final ModelRenderer leg1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg3;
	private final ModelRenderer leg4;

	public DeeperModel(boolean emissive, float modelSize) {
		this.textureWidth = 64;
		this.textureHeight = 64;

		this.emissive = emissive;

		this.head = new ModelRenderer(this);
		this.head.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		this.head.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F + 0.3F, false);

		this.body = new ModelRenderer(this);
		this.body.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.body.setTextureOffset(16, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		this.body.setTextureOffset(40, 16).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.3F, false);

		this.leg1 = new ModelRenderer(this);
		this.leg1.setRotationPoint(-2.0F, 18.0F, 4.0F);
		this.leg1.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		this.leg2 = new ModelRenderer(this);
		this.leg2.setRotationPoint(2.0F, 18.0F, 4.0F);
		this.leg2.setTextureOffset(0, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		this.leg3 = new ModelRenderer(this);
		this.leg3.setRotationPoint(-2.0F, 18.0F, -4.0F);
		this.leg3.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		this.leg4 = new ModelRenderer(this);
		this.leg4.setRotationPoint(2.0F, 18.0F, -4.0F);
		this.leg4.setTextureOffset(0, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		TextureAtlasSprite sprite = this.emissive ? DeeperSpriteUploader.getEmissiveSprite() : DeeperSpriteUploader.getSprite();
		RenderType render = this.emissive ? ACRenderTypes.getEmissiveTransluscentEntity(DeeperSpriteUploader.ATLAS_LOCATION, false) : RenderType.getEntityCutout(DeeperSpriteUploader.ATLAS_LOCATION);
		super.render(matrixStack, sprite.wrapBuffer(Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().getBuffer(render)), this.emissive ? 15728880 : packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.head, this.body, this.leg1, this.leg2, this.leg3, this.leg4);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
		this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
		this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}
}