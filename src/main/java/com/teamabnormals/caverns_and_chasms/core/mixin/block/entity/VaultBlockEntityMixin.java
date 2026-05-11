package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.block.entity.CCVaultSharedData;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(VaultBlockEntity.Server.class)
public abstract class VaultBlockEntityMixin {

	@Inject(method = "isValidToInsert", at = @At("RETURN"), cancellable = true)
	private static void isValidToInsert(VaultConfig config, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		TrialToken trialToken = stack.getItemHolder().getData(CCDataMaps.TRIAL_TOKENS);
		if (trialToken != null && ItemStack.isSameItemSameComponents(config.keyItem(), trialToken.keyItem()) && stack.getCount() >= config.keyItem().getCount()) {
			cir.setReturnValue(true);
		}
	}

	@WrapOperation(method = "tryInsertKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/vault/VaultBlockEntity$Server;resolveItemsToEject(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/block/entity/vault/VaultConfig;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Ljava/util/List;"))
	private static List<ItemStack> resolveItemsToEject(ServerLevel level, VaultConfig config, BlockPos pos, Player player, Operation<List<ItemStack>> original, ServerLevel level1, BlockPos pos1, BlockState state, VaultConfig config1, VaultServerData serverData, VaultSharedData sharedData, Player player1, ItemStack stack) {
		TrialToken trialToken = stack.getItemHolder().getData(CCDataMaps.TRIAL_TOKENS);
		if (trialToken != null) {
			if (trialToken.vaultLootTables().containsKey(config.lootTable())) {
				return resolveTokenItemsToEject(level, trialToken.vaultLootTables().get(config.lootTable()), pos, player);
			}
		}
		return original.call(level, config, pos, player);
	}

	@WrapOperation(method = "tryInsertKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
	private static void resolveItemsToEject(ItemStack stack, int amount, LivingEntity entity, Operation<Void> original, ServerLevel level, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player, ItemStack stack1) {
		CCVaultSharedData data = (CCVaultSharedData) sharedData;
		data.setInsertStack(stack.copy());
		original.call(stack, amount, entity);
	}

	@WrapOperation(method = "tryInsertKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/vault/VaultServerData;addToRewardedPlayers(Lnet/minecraft/world/entity/player/Player;)V"))
	private static void addToRewardedPlayers(VaultServerData instance, Player player, Operation<Void> original, ServerLevel level, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player1, ItemStack stack) {
		CCVaultSharedData data = (CCVaultSharedData) sharedData;
		if (data.getInsertStack().getItemHolder().getData(CCDataMaps.TRIAL_TOKENS) == null) {
			original.call(instance, player);
		}
		data.setInsertStack(ItemStack.EMPTY);
	}

	@Unique
	private static List<ItemStack> resolveTokenItemsToEject(ServerLevel level, ResourceKey<LootTable> newLootTable, BlockPos pos, Player player) {
		LootTable loottable = level.getServer().reloadableRegistries().getLootTable(newLootTable);
		LootParams lootparams = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.VAULT);
		return loottable.getRandomItems(lootparams);
	}
}