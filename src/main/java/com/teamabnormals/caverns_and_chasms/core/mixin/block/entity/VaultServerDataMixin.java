package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;


import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultServerData.class)
public abstract class VaultServerDataMixin {

	@Inject(method = "hasRewardedPlayer", at = @At("RETURN"), cancellable = true)
	private void hasRewardedPlayer(Player player, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && (player.getMainHandItem().is(CCItems.TRIAL_TOKEN) || player.getMainHandItem().is(CCItems.OMINOUS_TRIAL_TOKEN))) {
			cir.setReturnValue(false);
		}
	}
}
