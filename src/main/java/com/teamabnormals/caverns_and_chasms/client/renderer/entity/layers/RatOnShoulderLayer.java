package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.RatModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat.RatType;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RatOnShoulderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private final RatModel<Rat> model;
	private final ItemInHandRenderer itemInHandRenderer;

	public RatOnShoulderLayer(PlayerRenderer renderer, EntityModelSet entityModelSet) {
		super(renderer);
		this.model = new RatModel<>(entityModelSet.bakeLayer(CCModelLayers.RAT));
		this.itemInHandRenderer = renderer.entityRenderDispatcher.getItemInHandRenderer();
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.render(matrixStackIn, bufferIn, packedLightIn, player, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch, true);
		this.render(matrixStackIn, bufferIn, packedLightIn, player, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch, false);
	}

	private void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch, boolean leftShoulder) {
		CompoundTag compound = leftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
//		EntityType.byString(compound.getString("id")).filter((entitytype) -> entitytype == CCEntityTypes.RAT.get()).ifPresent((entitytype) -> {
//			matrixStackIn.pushPose();
//
//			matrixStackIn.translate(leftShoulder ? (double) 0.4F : (double) -0.4F, player.isCrouching() ? (double) -1.3F : -1.5D, -0.0625D);
//
//			boolean isbaby = compound.getInt("Age") < 0;
//			float ageinticks = player.tickCount + partialTicks;
//			RatType type = RatType.byId(compound.getInt("Type"));
//			DyeColor collarcolor = compound.contains("CollarColor", 99) ? DyeColor.byId(compound.getInt("CollarColor")) : DyeColor.RED;
//			ItemStack stack = ItemStack.of(compound.getList("HandItems", 10).getCompound(0));
//			VertexConsumer vertexconsumer = bufferIn.getBuffer(this.model.renderType(type.getTextureLocation()));
//
//			this.model.young = isbaby;
//			this.model.renderOnShoulder(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, limbSwing, limbSwingAmount, ageinticks, netHeadYaw, headPitch);
//			RatCollarLayer.renderCollar(this.model, matrixStackIn, bufferIn, packedLightIn, collarcolor, 0, 0, limbSwing, limbSwingAmount, ageinticks, netHeadYaw, headPitch);
//			RatHeldItemLayer.renderItem(this.model, this.itemInHandRenderer, matrixStackIn, bufferIn, packedLightIn, player, isbaby, stack, limbSwing, limbSwingAmount, partialTicks, ageinticks, netHeadYaw, headPitch);
//
//			matrixStackIn.popPose();
//		});
	}
}