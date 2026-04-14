package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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
		if (config.keyItem().is(Items.TRIAL_KEY) && ItemStack.isSameItemSameComponents(stack, new ItemStack(CCItems.TRIAL_TOKEN.get())) && stack.getCount() >= config.keyItem().getCount()) {
			cir.setReturnValue(true);
		}

		if (config.keyItem().is(Items.OMINOUS_TRIAL_KEY) && ItemStack.isSameItemSameComponents(stack, new ItemStack(CCItems.OMINOUS_TRIAL_TOKEN.get())) && stack.getCount() >= config.keyItem().getCount()) {
			cir.setReturnValue(true);
		}
	}

	@WrapOperation(method = "tryInsertKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/vault/VaultBlockEntity$Server;resolveItemsToEject(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/block/entity/vault/VaultConfig;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Ljava/util/List;"))
	private static List<ItemStack> tryInsertKey(ServerLevel level, VaultConfig config, BlockPos pos, Player player, Operation<List<ItemStack>> original, ServerLevel level1, BlockPos pos1, BlockState state, VaultConfig config1, VaultServerData serverData, VaultSharedData sharedData, Player player1, ItemStack stack) {
		if (stack.is(CCItems.TRIAL_TOKEN.get())) {
			return resolveTokenItemsToEject(level, BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES, pos, player);
		}

		if (stack.is(CCItems.OMINOUS_TRIAL_TOKEN.get())) {

			return resolveTokenItemsToEject(level, BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, pos, player);
		}

		return original.call(level, config, pos, player);
	}

	@Unique
	private static List<ItemStack> resolveTokenItemsToEject(ServerLevel level, ResourceKey<LootTable> newLootTable, BlockPos pos, Player player) {
		LootTable loottable = level.getServer().reloadableRegistries().getLootTable(newLootTable);
		LootParams lootparams = new LootParams.Builder(level)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withLuck(player.getLuck())
				.withParameter(LootContextParams.THIS_ENTITY, player)
				.create(LootContextParamSets.VAULT);
		return loottable.getRandomItems(lootparams);
	}
}