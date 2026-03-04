package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.client.model.UnicornHornModel;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.other.CCModelLayers;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class UnicornHornLayer<T extends AbstractHorse, M extends HorseModel<T>> extends RenderLayer<T, M> {
	public static final ResourceLocation HORN_TEXTURE = CavernsAndChasms.location("textures/entity/horse/unicorn_horn.png");
	private final UnicornHornModel<T> model;

	public UnicornHornLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
		super(parent);
		this.model = new UnicornHornModel<>(modelSet.bakeLayer(CCModelLayers.UNICORN_HORN));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!((IDataManager) horse).getValue(CCDataProcessors.UNICORN_HORN).isEmpty()) {
			ItemStack stack = ((IDataManager) horse).getValue(CCDataProcessors.UNICORN_HORN);
			boolean emissive = ((IDataManager) horse).getValue(CCDataProcessors.GLOW_UNICORN_HORN);
			this.getParentModel().copyPropertiesTo(this.model);
			this.model.prepareMobModel(horse, limbSwing, limbSwingAmount, partialTicks);
			this.model.setupAnim(horse, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			float f;
			float f1;
			float f2;
			if (stack.getItem() instanceof DyeableLeatherItem dyeable) {
				int i = dyeable.getColor(stack);
				f = (float) (i >> 16 & 255) / 255.0F;
				f1 = (float) (i >> 8 & 255) / 255.0F;
				f2 = (float) (i & 255) / 255.0F;
			} else {
				f = 1.0F;
				f1 = 1.0F;
				f2 = 1.0F;
			}
			VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HORN_TEXTURE));
			this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, f, f1, f2, 1.0F);
			if (emissive) {
				VertexConsumer emissiveConsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(HORN_TEXTURE));
				this.model.renderToBuffer(poseStack, emissiveConsumer, packedLight, OverlayTexture.NO_OVERLAY, f, f1, f2, 1.0F);
			}
		}
	}
}