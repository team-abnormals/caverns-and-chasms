package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.teamabnormals.caverns_and_chasms.common.block.entity.CCVaultSharedData;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(VaultSharedData.class)
public abstract class VaultSharedDataMixin implements CCVaultSharedData {

	@Unique
	private ItemStack caverns_and_chasms$insertStack = ItemStack.EMPTY;

	@Shadow
	private Set<UUID> connectedPlayers;

	@Shadow
	protected abstract void markDirty();

	@Inject(method = "updateConnectedPlayersWithinRange", at = @At("HEAD"), cancellable = true)
	private void updateConnectedPlayersWithinRange(ServerLevel level, BlockPos pos, VaultServerData serverData, VaultConfig config, double deactivationRange, CallbackInfo ci) {
		Set<UUID> set = config.playerDetector()
				.detect(level, config.entitySelector(), pos, deactivationRange, false)
				.stream()
				.filter(uuid -> !serverData.getRewardedPlayers().contains(uuid) || canUseTokenOnVault(level, config, uuid))
				.collect(Collectors.toSet());
		if (!this.connectedPlayers.equals(set)) {
			this.connectedPlayers = set;
			this.markDirty();
		}
		ci.cancel();
	}

	@Unique
	private static boolean canUseTokenOnVault(ServerLevel level, VaultConfig config, UUID uuid) {
		Player player = level.getPlayerByUUID(uuid);
		ItemStack stack = player.getMainHandItem();
		TrialToken token = stack.getItemHolder().getData(CCDataMaps.TRIAL_TOKENS);
		return token != null && VaultBlockEntity.Server.isValidToInsert(config, stack);
	}

	@Override
	public ItemStack getInsertStack() {
		return this.caverns_and_chasms$insertStack;
	}

	@Override
	public void setInsertStack(ItemStack lastUsedItem) {
		this.caverns_and_chasms$insertStack = lastUsedItem;
	}
}