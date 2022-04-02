package com.teamabnormals.caverns_and_chasms.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TmtRenderer extends EntityRenderer<PrimedTmt> {
	public TmtRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	public void render(PrimedTmt primedTmt, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		matrixStack.translate(0.0D, 0.5D, 0.0D);
		int i = primedTmt.getFuse();
		if ((float)i - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f *= f;
			f *= f;
			float f1 = 1.0F + f * 0.3F;
			matrixStack.scale(f1, f1, f1);
		}

		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
		matrixStack.translate(-0.5D, -0.5D, 0.5D);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		TntMinecartRenderer.renderWhiteSolidBlock(CCBlocks.TMT.get().defaultBlockState(), matrixStack, buffer, packedLight, i / 5 % 2 == 0);
		matrixStack.popPose();
		super.render(primedTmt, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(PrimedTmt primedTmt) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}