package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(TrialSpawnerState.class)
public abstract class TrialSpawnerStateMixin {

	@WrapOperation(method = "tickAndGetNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/random/SimpleWeightedRandomList;getRandomValue(Lnet/minecraft/util/RandomSource;)Ljava/util/Optional;"))
	private Optional<ResourceKey<LootTable>> onPlace(SimpleWeightedRandomList<ResourceKey<LootTable>> instance, RandomSource random, Operation<Optional<ResourceKey<LootTable>>> original, BlockPos pos, TrialSpawner spawner, ServerLevel level) {
		Optional<ResourceKey<LootTable>> lootTable = original.call(instance, random);
		if (lootTable.isPresent()) {
			ResourceKey<LootTable> key = lootTable.get();
			for (TrialToken token : BuiltInRegistries.ITEM.getDataMap(CCDataMaps.TRIAL_TOKENS).values()) {
				if (token.trialSpawnerLootTables().containsKey(key)) {
					return Optional.of(token.trialSpawnerLootTables().get(key));
				}
			}
		}
		return lootTable;
	}
}