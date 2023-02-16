package com.teamabnormals.caverns_and_chasms.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.client.model.GlareModel;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Glare;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlareEyesLayer<T extends Glare, M extends GlareModel<T>> extends EyesLayer<T, M> {
	private static final RenderType GLARE_EYES = RenderType.eyes(new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/glare/glare_eyes.png"));
	private static final RenderType GLARE_EYES_GRUMPY = RenderType.eyes(new ResourceLocation(CavernsAndChasms.MOD_ID, "textures/entity/glare/glare_eyes_grumpy.png"));

	public GlareEyesLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		VertexConsumer consumer = buffer.getBuffer(this.renderType(entity.isGrumpy()));
		this.getParentModel().renderToBuffer(poseStack, consumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	public RenderType renderType(boolean grumpy) {
		return grumpy ? GLARE_EYES_GRUMPY : GLARE_EYES;
	}

	@Override
	public RenderType renderType() {
		return renderType(false);
	}
}