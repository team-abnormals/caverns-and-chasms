package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(VaultSharedData.class)
public abstract class VaultSharedDataMixin {

	@Shadow
	private Set<UUID> connectedPlayers;

	@Shadow
	protected abstract void markDirty();

	@Inject(method = "updateConnectedPlayersWithinRange", at = @At("HEAD"), cancellable = true)
	private void updateConnectedPlayersWithinRange(ServerLevel level, BlockPos pos, VaultServerData serverData, VaultConfig config, double deactivationRange, CallbackInfo ci) {
		Set<UUID> set = config.playerDetector()
				.detect(level, config.entitySelector(), pos, deactivationRange, false)
				.stream()
				.filter(player -> !((VaultServerDataAccessor) serverData).invokeHasRewardedPlayer(level.getPlayerByUUID(player)))
				.collect(Collectors.toSet());
		if (!this.connectedPlayers.equals(set)) {
			this.connectedPlayers = set;
			this.markDirty();
		}
		ci.cancel();
	}
}