package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

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
			int i;
			if (stack.is(ItemTags.DYEABLE)) {
				i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, -6265536));
			} else {
				i = -1;
			}
			this.model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(HORN_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, i);
			if (emissive) {
				this.model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucentEmissive(HORN_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, i);
			}
		}
	}
}