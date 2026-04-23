package com.teamabnormals.caverns_and_chasms.core.other;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover.Default;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCDataMaps {

	public static final AdvancedDataMapType<Block, TinDeflection, Default<TinDeflection, Block>> TIN_DEFLECTIONS = AdvancedDataMapType.builder(CavernsAndChasms.location("tin_deflections"), Registries.BLOCK, TinDeflection.CODEC).synced(TinDeflection.CODEC, false).merger(new DeflectionMerger()).build();
	public static final DataMapType<Item, TrialToken> TRIAL_TOKENS = DataMapType.builder(CavernsAndChasms.location("trial_tokens"), Registries.ITEM, TrialToken.CODEC).synced(TrialToken.CODEC, false).build();

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event) {
		event.register(TIN_DEFLECTIONS);
		event.register(TRIAL_TOKENS);
	}

	public record TinDeflection(double horizontalFactor, double verticalFactor, boolean hasBonusDeflect) {

		public TinDeflection(double horizontalFactor, double verticalFactor) {
			this(horizontalFactor, verticalFactor, false);
		}

		public static final Codec<TinDeflection> CODEC = RecordCodecBuilder.create(in -> in.group(
				Codec.DOUBLE.fieldOf("horizontal_factor").forGetter(TinDeflection::horizontalFactor),
				Codec.DOUBLE.fieldOf("vertical_factor").forGetter(TinDeflection::verticalFactor),
				Codec.BOOL.optionalFieldOf("has_bonus_deflect", false).forGetter(TinDeflection::hasBonusDeflect)
		).apply(in, TinDeflection::new));
	}

	public record TrialToken(ItemStack keyItem, ResourceKey<LootTable> tokenLootTable, ResourceKey<LootTable> replaceLootTable, ResourceKey<LootTable> vaultLootTable) {
		public static final Codec<TrialToken> CODEC = RecordCodecBuilder.create(in -> in.group(
				ItemStack.CODEC.fieldOf("key_item").forGetter(TrialToken::keyItem),
				ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("token_loot_table").forGetter(TrialToken::tokenLootTable),
				ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("replace_loot_table").forGetter(TrialToken::replaceLootTable),
				ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("vault_loot_table").forGetter(TrialToken::vaultLootTable)
		).apply(in, TrialToken::new));
	}

	public static class DeflectionMerger implements DataMapValueMerger<Block, TinDeflection> {

		@Override
		public TinDeflection merge(Registry<Block> registry, Either<TagKey<Block>, ResourceKey<Block>> first, TinDeflection firstValue, Either<TagKey<Block>, ResourceKey<Block>> second, TinDeflection secondValue) {
			if (first.left().isPresent() && first.left().get().equals(CCBlockTags.DEFLECTS_PROJECTILES)) {
				return secondValue;
			} else if (second.left().isPresent() && second.left().get().equals(CCBlockTags.DEFLECTS_PROJECTILES)) {
				return firstValue;
			}

			return new TinDeflection((firstValue.horizontalFactor + secondValue.horizontalFactor) * 0.5D, (firstValue.verticalFactor + secondValue.verticalFactor) * 0.5D, firstValue.hasBonusDeflect || secondValue.hasBonusDeflect);
		}
	}
}
