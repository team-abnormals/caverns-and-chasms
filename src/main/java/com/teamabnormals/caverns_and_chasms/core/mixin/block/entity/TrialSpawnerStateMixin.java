package com.teamabnormals.caverns_and_chasms.core.mixin.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.core.other.CCLootTables;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(TrialSpawnerState.class)
public abstract class TrialSpawnerStateMixin {

	@WrapOperation(method = "tickAndGetNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/random/SimpleWeightedRandomList;getRandomValue(Lnet/minecraft/util/RandomSource;)Ljava/util/Optional;"))
	private Optional<ResourceKey<LootTable>> onPlace(SimpleWeightedRandomList<ResourceKey<LootTable>> instance, RandomSource random, Operation<Optional<ResourceKey<LootTable>>> original) {
		Optional<ResourceKey<LootTable>> lootTable = original.call(instance, random);
		if (lootTable.isPresent()) {
			ResourceKey<LootTable> key = lootTable.get();
			if (key.equals(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES)) {
				return Optional.of(CCLootTables.SPAWNER_TRIAL_CHAMBER_TOKEN);
			}
			if (key.equals(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES)) {
				return Optional.of(CCLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_TOKEN);
			}
		}
		return lootTable;
	}
}