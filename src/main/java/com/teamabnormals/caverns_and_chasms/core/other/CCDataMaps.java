package com.teamabnormals.caverns_and_chasms.core.other;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TinDeflection.DeflectionMerger;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.TrialToken.TrialTokenMerger;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCDataMaps {
	public static final DataMapType<Item, OxidizableItem> OXIDIZABLES = DataMapType.builder(CavernsAndChasms.location("oxidizables"), Registries.ITEM, OxidizableItem.CODEC).synced(OxidizableItem.OXIDIZABLE_CODEC, false).build();
	public static final DataMapType<Item, WaxableItem> WAXABLES = DataMapType.builder(CavernsAndChasms.location("waxables"), Registries.ITEM, WaxableItem.CODEC).synced(WaxableItem.WAXABLE_CODEC, false).build();

	public static final DataMapType<Block, TinDeflection> TIN_DEFLECTIONS = AdvancedDataMapType.builder(CavernsAndChasms.location("tin_deflections"), Registries.BLOCK, TinDeflection.CODEC).synced(TinDeflection.CODEC, false).merger(new DeflectionMerger()).build();
	public static final DataMapType<Item, TrialToken> TRIAL_TOKENS = AdvancedDataMapType.builder(CavernsAndChasms.location("trial_tokens"), Registries.ITEM, TrialToken.CODEC).synced(TrialToken.CODEC, false).merger(new TrialTokenMerger()).build();
	public static final DataMapType<Item, PlaceableItem> PLACEABLE_ITEMS = DataMapType.builder(CavernsAndChasms.location("placeable_items"), Registries.ITEM, PlaceableItem.CODEC).synced(PlaceableItem.CODEC, false).build();

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event) {
		event.register(TIN_DEFLECTIONS);
		event.register(TRIAL_TOKENS);
		event.register(OXIDIZABLES);
		event.register(WAXABLES);
		event.register(PLACEABLE_ITEMS);
	}

	public record PlaceableItem(Holder<Item> block) {
		public static final Codec<PlaceableItem> CODEC = RecordCodecBuilder.create(in -> in.group(
				RegistryFixedCodec.create(Registries.ITEM).fieldOf("placed_item").forGetter(PlaceableItem::block)
		).apply(in, PlaceableItem::new));

		public PlaceableItem(Holder<Block> block, HolderLookup.Provider provider) {
			this(provider.lookupOrThrow(Registries.ITEM).getOrThrow(ResourceKey.create(Registries.ITEM, block.unwrapKey().get().location().withSuffix("_placed"))));
		}
	}

	public record TinDeflection(double horizontalFactor, double verticalFactor, boolean hasBonusDeflect, Holder<SoundEvent> deflectSound) {

		public TinDeflection() {
			this(0.75D, 0.65D, false, CCSoundEvents.TIN_DEFLECT);
		}

		public TinDeflection(Holder<SoundEvent> sound) {
			this(0.75D, 0.65D, false, sound);
		}

		public TinDeflection(double horizontalFactor, double verticalFactor, Holder<SoundEvent> deflectSound) {
			this(horizontalFactor, verticalFactor, false, deflectSound);
		}

		public static final Codec<TinDeflection> CODEC = RecordCodecBuilder.create(in -> in.group(
				Codec.DOUBLE.fieldOf("horizontal_factor").forGetter(TinDeflection::horizontalFactor),
				Codec.DOUBLE.fieldOf("vertical_factor").forGetter(TinDeflection::verticalFactor),
				Codec.BOOL.optionalFieldOf("has_bonus_deflect", false).forGetter(TinDeflection::hasBonusDeflect),
				SoundEvent.CODEC.fieldOf("deflect_sound").forGetter(TinDeflection::deflectSound)
		).apply(in, TinDeflection::new));

		public static class DeflectionMerger implements DataMapValueMerger<Block, TinDeflection> {

			@Override
			public TinDeflection merge(Registry<Block> registry, Either<TagKey<Block>, ResourceKey<Block>> first, TinDeflection firstValue, Either<TagKey<Block>, ResourceKey<Block>> second, TinDeflection secondValue) {
				return new TinDeflection((firstValue.horizontalFactor + secondValue.horizontalFactor) * 0.5D, (firstValue.verticalFactor + secondValue.verticalFactor) * 0.5D, firstValue.hasBonusDeflect || secondValue.hasBonusDeflect, secondValue.deflectSound);
			}
		}
	}

	public record TrialToken(ItemStack keyItem, Holder<SoundEvent> tokenSound, Map<ResourceKey<LootTable>, ResourceKey<LootTable>> trialSpawnerLootTables, Map<ResourceKey<LootTable>, ResourceKey<LootTable>> vaultLootTables) {
		public static final Codec<TrialToken> CODEC = RecordCodecBuilder.create(in -> in.group(
				ItemStack.CODEC.fieldOf("key_item").forGetter(TrialToken::keyItem),
				SoundEvent.CODEC.fieldOf("token_sound").forGetter(TrialToken::tokenSound),
				Codec.unboundedMap(ResourceKey.codec(Registries.LOOT_TABLE), ResourceKey.codec(Registries.LOOT_TABLE)).fieldOf("trial_spawner_loot_tables").forGetter(TrialToken::trialSpawnerLootTables),
				Codec.unboundedMap(ResourceKey.codec(Registries.LOOT_TABLE), ResourceKey.codec(Registries.LOOT_TABLE)).fieldOf("vault_loot_tables").forGetter(TrialToken::vaultLootTables)
		).apply(in, TrialToken::new));

		public static class TrialTokenMerger implements DataMapValueMerger<Item, TrialToken> {

			@Override
			public TrialToken merge(Registry<Item> registry, Either<TagKey<Item>, ResourceKey<Item>> first, TrialToken firstValue, Either<TagKey<Item>, ResourceKey<Item>> second, TrialToken secondValue) {
				Map<ResourceKey<LootTable>, ResourceKey<LootTable>> mergedTrialSpawner = new HashMap<>(firstValue.trialSpawnerLootTables());
				mergedTrialSpawner.putAll(secondValue.trialSpawnerLootTables());
				Map<ResourceKey<LootTable>, ResourceKey<LootTable>> mergedVault = new HashMap<>(firstValue.vaultLootTables());
				mergedVault.putAll(secondValue.vaultLootTables());
				return new TrialToken(firstValue.keyItem(), firstValue.tokenSound(), mergedTrialSpawner, mergedVault);
			}
		}
	}

	public record OxidizableItem(Item nextOxidationStage) {
		public static final Codec<OxidizableItem> OXIDIZABLE_CODEC = BuiltInRegistries.ITEM.byNameCodec().xmap(OxidizableItem::new, OxidizableItem::nextOxidationStage);
		public static final Codec<OxidizableItem> CODEC = Codec.withAlternative(RecordCodecBuilder.create(in -> in.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("next_oxidation_stage").forGetter(OxidizableItem::nextOxidationStage)).apply(in, OxidizableItem::new)), OXIDIZABLE_CODEC);
	}

	public record WaxableItem(Item waxed) {
		public static final Codec<WaxableItem> WAXABLE_CODEC = BuiltInRegistries.ITEM.byNameCodec().xmap(WaxableItem::new, WaxableItem::waxed);
		public static final Codec<WaxableItem> CODEC = Codec.withAlternative(RecordCodecBuilder.create(in -> in.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("waxed").forGetter(WaxableItem::waxed)).apply(in, WaxableItem::new)), WAXABLE_CODEC);
	}

	public static final Map<Item, Item> INVERSE_OXIDIZABLES_DATAMAP_INTERNAL = new HashMap<>();
	public static final Map<Item, Item> INVERSE_WAXABLES_DATAMAP_INTERNAL = new HashMap<>();

	public static final Map<Item, Item> INVERSE_OXIDIZABLES_DATAMAP = Collections.unmodifiableMap(INVERSE_OXIDIZABLES_DATAMAP_INTERNAL);
	public static final Map<Item, Item> INVERSE_WAXABLES_DATAMAP = Collections.unmodifiableMap(INVERSE_WAXABLES_DATAMAP_INTERNAL);

	@SubscribeEvent
	public static void onDataMapsUpdated(DataMapsUpdatedEvent event) {
		event.ifRegistry(Registries.ITEM, registry -> {
			INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.clear();
			INVERSE_WAXABLES_DATAMAP_INTERNAL.clear();

			registry.getDataMap(OXIDIZABLES).forEach((resourceKey, oxidizable) -> INVERSE_OXIDIZABLES_DATAMAP_INTERNAL.put(oxidizable.nextOxidationStage(), BuiltInRegistries.ITEM.get(resourceKey)));
			registry.getDataMap(WAXABLES).forEach((resourceKey, waxable) -> INVERSE_WAXABLES_DATAMAP_INTERNAL.put(waxable.waxed(), BuiltInRegistries.ITEM.get(resourceKey)));
		});
	}
}
