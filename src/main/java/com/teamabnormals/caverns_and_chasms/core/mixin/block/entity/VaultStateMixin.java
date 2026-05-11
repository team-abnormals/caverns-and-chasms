package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.entity.CCVaultSharedData;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.block.entity.vault.VaultState$3")
public abstract class VaultStateMixin {

	@Inject(method = "onEnter", at = @At("HEAD"), cancellable = true)
	private void onEnterInject(ServerLevel level, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous, CallbackInfo ci) {
		if (sharedData instanceof CCVaultSharedData data) {
			TrialToken token = data.getInsertStack().getItemHolder().getData(CCDataMaps.TRIAL_TOKENS);
			if (token != null && token.tokenSound() != null) {
				level.playSound(null, pos, token.tokenSound().value(), SoundSource.BLOCKS);
				ci.cancel();
			}
		}
	}
}