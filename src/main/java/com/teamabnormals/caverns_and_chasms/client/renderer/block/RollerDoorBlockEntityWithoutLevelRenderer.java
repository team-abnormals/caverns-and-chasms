package com.teamabnormals.caverns_and_chasms.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.renderer.block.TypedBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.client.model.RollerDoorRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RollerDoorBlockEntityWithoutLevelRenderer<C extends BlockEntity> extends TypedBlockEntityWithoutLevelRenderer<C> {

	public RollerDoorBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet, C be) {
		super(dispatcher, modelSet, be);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		RollerDoorRenderer.renderAsItem = true;
		super.renderByItem(stack, displayContext, poseStack, buffer, combinedLight, combinedOverlay);
		RollerDoorRenderer.renderAsItem = false;
	}
}