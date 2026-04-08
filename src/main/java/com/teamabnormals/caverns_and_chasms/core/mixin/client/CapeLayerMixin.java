package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	public CapeLayerMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_117346_) {
		super(p_117346_);
	}

	@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/PlayerModel;renderCloak(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
	private void getCloakTextureLocation(PlayerModel model, PoseStack poseStack, VertexConsumer vertexConsumer, int p_103414_, int p_103415_, Operation<Void> original, PoseStack p_116615_, MultiBufferSource bufferSource, int p_116617_, AbstractClientPlayer player) {
		ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.is(CCItems.COWL.get())) {
			this.getParentModel().renderCloak(p_116615_, vertexConsumer, p_116617_, OverlayTexture.NO_OVERLAY);
			int i;
			if (stack.is(ItemTags.DYEABLE)) {
				i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, DyedItemColor.LEATHER_COLOR));
			} else {
				i = -1;
			}
			model.cloak.render(p_116615_, vertexConsumer, p_116617_, OverlayTexture.NO_OVERLAY, i);
		} else {
			original.call(model, poseStack, vertexConsumer, p_103414_, p_103415_);
		}
	}
}