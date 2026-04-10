package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.teamabnormals.blueprint.common.remolder.data.RemolderProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.MinMaxBounds.Doubles;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.blueprint.common.remolder.RemolderTypes.sequence;
import static com.teamabnormals.blueprint.common.remolder.util.LootRemolders.addEntry;
import static com.teamabnormals.blueprint.common.remolder.util.LootRemolders.addPool;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;

public class CCDataRemolderProvider extends RemolderProvider {

	public CCDataRemolderProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, Target.DATA_PACK, output, provider);
	}

	@Override
	protected void registerEntries(Provider provider) {

//		HolderGetter<Structure> structures = provider.lookupOrThrow(Registries.STRUCTURE);
//		this.entry("worldgen/structure_set/mineshafts")
//				.path("worldgen/structure_set/mineshafts")
//				.remolder(add(target("structures[]"), value(
//						StructureSet.entry(structures.getOrThrow(CCStructures.MINESHAFT_LUSH), 1), StructureSelectionEntry.CODEC)
//				));

		LootItemCondition.Builder hasSilkTouch = hasSilkTouch(provider);
		this.lootRemolder(BuiltInLootTables.SIMPLE_DUNGEON).remolder(sequence(
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 15)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 10)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 10)),
				addEntry(1, entry(SILVER_INGOT.get(), 5, 1, 4)),
				addEntry(1, entry(ZIRCONIA.get(), 10, 1, 2))));

		this.lootRemolder(BuiltInLootTables.ABANDONED_MINESHAFT).remolder(sequence(
				addEntry(0, entry(Items.BUNDLE, 5)),
				addEntry(0, entry(DEPTH_GAUGE.get(), 5)),
				addEntry(0, entry(TOOLBELT.get(), 5)),
				addEntry(1, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(1, entry(SPINEL.get(), 5, 6, 11)),
				addEntry(1, entry(TIN_INGOT.get(), 2, 1, 3)),
				addEntry(2, entry(CCBlocks.SPARKLER.getFirst().get(), 15, 4, 12)),
				addEntry(2, entry(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4))));

		this.lootRemolder(BuiltInLootTables.STRONGHOLD_CORRIDOR).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(0, entry(TIN_INGOT.get(), 8, 1, 3)),
				addEntry(0, entry(ZIRCONIA.get(), 5, 1, 2)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 10, 1, 3))));

		this.lootRemolder(BuiltInLootTables.STRONGHOLD_CROSSING).remolder(sequence(
				addEntry(0, LootItem.lootTableItem(COWL.get()).setWeight(2).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)).build()),
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 10, 1, 3))));

		this.lootRemolder(BuiltInLootTables.STRONGHOLD_LIBRARY).remolder(sequence(
				addEntry(0, entry(ZIRCONIA.get(), 1, 1, 3))));

		this.lootRemolder(BuiltInLootTables.SHIPWRECK_MAP).remolder(sequence(
				addEntry(1, entry(BAROMETER.get(), 1)),
				addEntry(1, entry(DEPTH_GAUGE.get(), 1))));

		this.lootRemolder(BuiltInLootTables.SHIPWRECK_TREASURE).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 10, 1, 5)),
				addEntry(1, entry(SILVER_NUGGET.get(), 10, 1, 10)),
				addEntry(1, entry(SPINEL.get(), 20, 1, 8))));

		this.lootRemolder(BuiltInLootTables.BURIED_TREASURE).remolder(sequence(
				addEntry(1, entry(SILVER_INGOT.get(), 10, 1, 4)),
				addEntry(2, entry(ZIRCONIA.get(), 5, 1, 2)),
				addEntry(2, entry(TURQUOISE.get(), 1))));

		this.lootRemolder(BuiltInLootTables.DESERT_PYRAMID).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 15, 1, 5)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 10)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 15))));

		this.lootRemolder(BuiltInLootTables.JUNGLE_TEMPLE).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 15, 2, 7)),
				addEntry(0, entry(SPINEL.get(), 15, 2, 5)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1))));

		this.lootRemolder(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER).remolder(sequence(
				addEntry(0, entry(RICOCHET_ARROW.get(), 10, 1, 4))));

		this.lootRemolder(BuiltInLootTables.WOODLAND_MANSION).remolder(sequence(
				addPool(pool("turquoise").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(TURQUOISE.get())).build()),
				addEntry(0, entry(COWL.get(), 10)),
				addEntry(1, entry(SILVER_INGOT.get(), 5, 1, 4)),
				addEntry(1, entry(TIN_INGOT.get(), 8, 1, 4)),
				addEntry(1, entry(ZIRCONIA.get(), 5, 1, 2))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_FISHER).remolder(sequence(
				addPool(pool("barometer").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(BAROMETER.get())).build()),
				addEntry(0, entry(CAVEFISH.get(), 1, 1, 3))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_WEAPONSMITH).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 3)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_TOOLSMITH).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 1, 1, 3))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_TEMPLE).remolder(sequence(
				addEntry(0, entry(SPINEL.get(), 1, 1, 4)),
				addEntry(0, entry(SILVER_INGOT.get(), 1, 1, 4)),
				addEntry(0, entry(ZIRCONIA.get(), 1))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_FLETCHER).remolder(sequence(
				addEntry(0, entry(CCBlocks.HOOP.get(), 1)),
				addEntry(0, entry(BLUNT_ARROW.get(), 1, 1, 8))));

		this.lootRemolder(BuiltInLootTables.VILLAGE_SNOWY_HOUSE).remolder(sequence(
				addEntry(0, entry(SILVER_NUGGET.get(), 1, 1, 4))));

		this.lootRemolder(BuiltInLootTables.FISHING_FISH).remolder(sequence(
				addEntry(0, LootItem.lootTableItem(CAVEFISH.get()).setWeight(70).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setY(Doubles.atMost(30.0D)))).build())));

		this.lootRemolder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE).remolder(sequence(
				addEntry(0, entry(TOOLBELT.get(), 1))));

		this.lootRemolder(BuiltInLootTables.IGLOO_CHEST).remolder(sequence(
				addEntry(0, entry(TOOLBELT.get(), 2))));

		this.lootRemolder(BuiltInLootTables.PILLAGER_OUTPOST).remolder(sequence(
				addEntry(1, entry(COWL.get(), 1)),
				addEntry(3, entry(RICOCHET_ARROW.get(), 2, 1, 4)),
				addEntry(3, entry(LARGE_ARROW.get(), 4, 1, 2))));

		this.lootRemolder(BuiltInLootTables.ANCIENT_CITY).remolder(sequence(
				addEntry(0, entry(TUNING_FORK.get(), 2)),
				addEntry(0, entry(CCBlocks.SPARKLER.getFirst().get(), 5, 1, 15)),
				addEntry(0, entry(BEJEWELED_APPLE.get(), 3, 1, 3)),
				addEntry(0, entry(ZIRCONIA.get(), 3, 1, 2)),
				addEntry(0, LootItem.lootTableItem(COWL.get()).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, UniformGenerator.between(20.0F, 39.0F))).build())));

		this.lootRemolder(BuiltInLootTables.RUINED_PORTAL).remolder(sequence(
				addPool(pool("lodestone").add(EmptyLootItem.emptyItem()).add(LootItem.lootTableItem(Blocks.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).build()),
				addEntry(0, entry(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16)),
				addEntry(0, entry(CCBlocks.LAVA_LAMP.get(), 5)),
				addEntry(0, entry(GOLDEN_BUCKET.get(), 1))));

		this.lootRemolder(BuiltInLootTables.NETHER_BRIDGE).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 5, 1, 5)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 6)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 5)),
				addEntry(0, entry(TURQUOISE.get(), 1)),
				addEntry(1, entry(EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), 1))));

		this.lootRemolder(BuiltInLootTables.BASTION_BRIDGE).remolder(sequence(
				addPool(pool("golden_bucket").add(LootItem.lootTableItem(GOLDEN_BUCKET.get())).build()),
				addEntry(1, LootItem.lootTableItem(TOOLBELT.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)).build()),
				addEntry(1, entry(SILVER_INGOT.get(), 1, 4, 9)), addEntry(1, entry(LARGE_ARROW.get(), 1, 4, 12)),
				addEntry(2, entry(SILVER_NUGGET.get(), 1, 2, 6))));

		this.lootRemolder(BuiltInLootTables.BASTION_HOGLIN_STABLE).remolder(sequence(
				addEntry(0, entry(TURQUOISE.get(), 6))));

		this.lootRemolder(BuiltInLootTables.BASTION_TREASURE).remolder(sequence(
				addEntry(0, entry(TURQUOISE.get(), 6)),
				addEntry(1, entry(SILVER_INGOT.get(), 1, 3, 9)),
				addEntry(1, entry(CCBlocks.SILVER_BLOCK.get(), 1, 2, 5)),
				addEntry(1, entry(LARGE_ARROW.get(), 1, 6, 10))));

		this.lootRemolder(BuiltInLootTables.BASTION_OTHER).remolder(sequence(
				addEntry(0, entry(LARGE_ARROW.get(), 1, 4, 8)),
				addEntry(0, entry(TURQUOISE.get(), 3)),
				addEntry(1, entry(SILVER_INGOT.get(), 2, 1, 6)),
				addEntry(1, entry(CCBlocks.SILVER_BLOCK.get(), 2)),
				addEntry(2, entry(SILVER_NUGGET.get(), 1, 2, 8))));

		this.lootRemolder(BuiltInLootTables.END_CITY_TREASURE).remolder(sequence(
				addEntry(0, entry(BEJEWELED_APPLE.get(), 5, 3, 9)),
				addEntry(0, entry(SILVER_INGOT.get(), 15, 2, 7)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1)),
				addEntry(0, entry(TURQUOISE.get(), 1))));

		this.lootRemolder(ResourceLocation.fromNamespaceAndPath("atmospheric", "chests/kousa_sanctum")).remolder(sequence(
				addEntry(0, entry(SILVER_INGOT.get(), 15, 2, 7)),
				addEntry(0, entry(SILVER_HORSE_ARMOR.get(), 1)), addEntry(0, entry(COPPER_HORSE_ARMOR.get(), 1))));

		this.lootRemolder(ResourceLocation.withDefaultNamespace("entities/elder_guardian")).remolder(sequence(addPool(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get()).setWeight(1)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(LootItemRandomChanceCondition.randomChance(0.1F))).build())));

		this.lootRemolder(ResourceLocation.withDefaultNamespace("blocks/copper_ore")).remolder(sequence(addPool(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(hasSilkTouch.invert())
								.when(LootItemRandomChanceCondition.randomChance(0.005F))).build())));

		this.lootRemolder(ResourceLocation.withDefaultNamespace("blocks/deepslate_copper_ore")).remolder(sequence(addPool(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(hasSilkTouch.invert())
								.when(LootItemRandomChanceCondition.randomChance(0.01F))).build())));
	}

	protected LootItemCondition.Builder hasSilkTouch(Provider provider) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		return MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(
				List.of(new EnchantmentPredicate(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))));
	}

	public Entry lootRemolder(ResourceKey<LootTable> key) {
		return this.lootRemolder(key.location());
	}

	public Entry lootRemolder(ResourceLocation location) {
		String name = location.getPath();
		return this.entry(name).path(name);
	}

	private static LootPool.Builder pool(String name) {
		return LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":" + name);
	}

	private static LootPoolEntryContainer entry(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer entry(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}