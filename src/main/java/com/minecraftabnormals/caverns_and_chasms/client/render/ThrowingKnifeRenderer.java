package com.minecraftabnormals.caverns_and_chasms.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrowingKnifeRenderer<T extends Entity & IRendersAsItem> extends EntityRenderer<T> {
	private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	private final float scale;
	private final boolean field_229126_f_;

	public ThrowingKnifeRenderer(EntityRendererManager p_i226035_1_, float p_i226035_3_, boolean p_i226035_4_) {
		super(p_i226035_1_);
		this.scale = p_i226035_3_;
		this.field_229126_f_ = p_i226035_4_;
	}

	public ThrowingKnifeRenderer(EntityRendererManager renderManagerIn) {
		this(renderManagerIn, 1.0F, false);
	}

	@Override
	protected int getBlockLight(T entityIn, BlockPos partialTicks) {
		return this.field_229126_f_ ? 15 : super.getBlockLight(entityIn, partialTicks);
	}

	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		float testScale = 1.5F;
		matrixStackIn.scale(this.scale, this.scale, this.scale);
		matrixStackIn.scale(testScale, testScale, testScale);

		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-135.0F));

		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		matrixStackIn.translate(0.0F, -0.175F, 0.0F);

		this.itemRenderer.renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	public ResourceLocation getEntityTexture(Entity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
