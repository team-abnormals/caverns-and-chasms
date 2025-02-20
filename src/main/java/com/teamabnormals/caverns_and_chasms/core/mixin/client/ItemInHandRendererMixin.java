package com.teamabnormals.caverns_and_chasms.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.caverns_and_chasms.common.item.MonocleItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
	private void isScoping(AbstractClientPlayer player, float p_109373_, float p_109374_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_, CallbackInfo ci) {
		if (MonocleItem.isScopingMonocle(player)) {
			ci.cancel();
		}
	}
}