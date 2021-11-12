package com.minecraftabnormals.caverns_and_chasms.client.render.layer;

import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UndeadParrotLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
	private final ParrotModel parrotModel;

	public UndeadParrotLayer(RenderLayerParent<T, PlayerModel<T>> rendererIn, EntityModelSet modelSet) {
		super(rendererIn);
		this.parrotModel = new ParrotModel(modelSet.bakeLayer(ModelLayers.PARROT));
	}

	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.renderParrot(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
		this.renderParrot(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
	}

	private void renderParrot(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, boolean leftShoulderIn) {
		CompoundTag nbt = leftShoulderIn ? entity.getShoulderEntityLeft() : entity.getShoulderEntityRight();
		EntityType.byString(nbt.getString("id")).filter((type) -> type == CCEntityTypes.ZOMBIE_PARROT.get() || type == CCEntityTypes.SKELETON_PARROT.get()).ifPresent((type) -> {
			matrixStackIn.pushPose();
			matrixStackIn.translate(leftShoulderIn ? (double) 0.4F : (double) -0.4F, entity.isCrouching() ? (double) -1.3F : -1.5D, 0.0D);
			VertexConsumer vertexConsumer = bufferIn.getBuffer(this.parrotModel.renderType(type == CCEntityTypes.ZOMBIE_PARROT.get() ? ZombieParrotRenderer.TEXTURE : SkeletonParrotRenderer.TEXTURE));
			this.parrotModel.renderOnShoulder(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, limbSwing, limbSwingAmount, netHeadYaw, headPitch, entity.tickCount);
			matrixStackIn.popPose();
		});
	}
}
