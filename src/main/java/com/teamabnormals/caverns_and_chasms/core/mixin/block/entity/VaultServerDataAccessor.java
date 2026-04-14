package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VaultServerData.class)
public interface VaultServerDataAccessor {

	@Invoker("hasRewardedPlayer")
	boolean invokeHasRewardedPlayer(Player player);
}