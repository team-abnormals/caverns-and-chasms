package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel.RatPose;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatOnShoulderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private final RatModel ratModel;
	private final ItemInHandRenderer itemInHandRenderer;

	public RatOnShoulderLayer(PlayerRenderer renderer, EntityModelSet entityModelSet) {
		super(renderer);
		this.ratModel = new RatModel(entityModelSet.bakeLayer(CCModelLayers.RAT));
		this.ratModel.pose = RatPose.ON_SHOULDER;
		this.itemInHandRenderer = renderer.entityRenderDispatcher.getItemInHandRenderer();
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.render(poseStack, buffer, packedLight, player, partialTicks, netHeadYaw, headPitch, true);
		this.render(poseStack, buffer, packedLight, player, partialTicks, netHeadYaw, headPitch, false);
	}

	private void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float partialTick, float netHeadYaw, float headPitch, boolean isLeftShoulder) {
		CompoundTag compound = isLeftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
		EntityType.byString(compound.getString("id")).filter((entitytype) -> entitytype == CCEntityTypes.RAT.get()).ifPresent((entitytype) -> {
			poseStack.pushPose();
			poseStack.translate(isLeftShoulder ? (double) 0.4F : (double) -0.4F, player.isCrouching() ? (double) -1.3F : -1.5D, -0.0625D);
			this.ratModel.renderFromTag(compound, player.level(), player, this.itemInHandRenderer, poseStack, buffer, packedLight, 0.0F, 0.0F, player.tickCount + partialTick, netHeadYaw, headPitch);
			poseStack.popPose();
		});
	}
}