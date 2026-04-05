package com.teamabnormals.caverns_and_chasms.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.AegisModel;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AegisRenderer extends BlockEntityWithoutLevelRenderer {
	private static final ResourceLocation AEGIS_LOCATION = CavernsAndChasms.location("textures/entity/aegis/aegis_base.png");
	private static final ResourceLocation OVERLAY_LOCATION = CavernsAndChasms.location("textures/entity/aegis/aegis_overlay.png");

	public static final AegisRenderer INSTANCE = new AegisRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

	private final EntityModelSet modelSet;
	private AegisModel aegisModel;

	public AegisRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
		super(dispatcher, modelSet);
		this.modelSet = modelSet;
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		this.aegisModel = new AegisModel(this.modelSet.bakeLayer(CCModelLayers.AEGIS));
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {
		if (stack.is(CCItems.AEGIS.get())) {
			poseStack.pushPose();
			poseStack.scale(1.0F, -1.0F, -1.0F);

			VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, this.aegisModel.renderType(AEGIS_LOCATION), false, stack.hasFoil());
			this.aegisModel.handle().render(poseStack, consumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
			this.aegisModel.plate().render(poseStack, consumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

			if (stack.getItem() instanceof DyeableLeatherItem dyeable) {
				int i = dyeable.getColor(stack);
				float r = (float) (i >> 16 & 255) / 255.0F;
				float g = (float) (i >> 8 & 255) / 255.0F;
				float b = (float) (i & 255) / 255.0F;

				VertexConsumer overlayConsumer = ItemRenderer.getFoilBufferDirect(buffer, this.aegisModel.renderType(OVERLAY_LOCATION), false, stack.hasFoil());
				this.aegisModel.handle().render(poseStack, overlayConsumer, packedLight, overlay, r, g, b, 1.0F);
				this.aegisModel.plate().render(poseStack, overlayConsumer, packedLight, overlay, r, g, b, 1.0F);
			}

			poseStack.popPose();
		}
	}
}