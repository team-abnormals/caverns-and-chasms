package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolsModifier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.MinMaxBounds.Doubles;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
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

import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;

public class CCLootModifierProvider extends LootModifierProvider {

	public CCLootModifierProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, output, provider);
	}

	@Override
	protected void registerEntries(Provider provider) {
		this.entry("simple_dungeon").selects(BuiltInLootTables.SIMPLE_DUNGEON)
				.addModifier(entries(0, entry(COPPER_HORSE_ARMOR.get(), 15), entry(SILVER_HORSE_ARMOR.get(), 10), entry(BEJEWELED_APPLE.get(), 10)))
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 5, 1, 4), entry(ZIRCONIA.get(), 10, 1, 2)));
		this.entry("abandoned_mineshaft").selects(BuiltInLootTables.ABANDONED_MINESHAFT)
				.addModifier(entries(0, entry(Items.BUNDLE, 5), entry(DEPTH_GAUGE.get(), 5), entry(TOOLBELT.get(), 5)))
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 5, 1, 3), entry(SPINEL.get(), 5, 6, 11), entry(TIN_INGOT.get(), 2, 1, 3)))
				.addModifier(entries(2, entry(CCBlocks.SPARKLER.getFirst().get(), 15, 4, 12), entry(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4)));

		this.entry("stronghold_corridor").selects(BuiltInLootTables.STRONGHOLD_CORRIDOR)
				.addModifier(entries(0, entry(SILVER_INGOT.get(), 5, 1, 3), entry(TIN_INGOT.get(), 8, 1, 3), entry(ZIRCONIA.get(), 5, 1, 2), entry(SILVER_HORSE_ARMOR.get(), 1), entry(COPPER_HORSE_ARMOR.get(), 1), entry(BEJEWELED_APPLE.get(), 10, 1, 3)));
		this.entry("stronghold_crossing").selects(BuiltInLootTables.STRONGHOLD_CROSSING).addModifier(entries(0,
				LootItem.lootTableItem(COWL.get()).setWeight(2).apply(EnchantRandomlyFunction.randomApplicableEnchantment()).build(),
				entry(SILVER_INGOT.get(), 5, 1, 3), entry(BEJEWELED_APPLE.get(), 10, 1, 3)));
		this.entry("stronghold_library").selects(BuiltInLootTables.STRONGHOLD_LIBRARY)
				.addModifier(entries(0, entry(ZIRCONIA.get(), 1, 1, 3)));

		this.entry("shipwreck_map").selects(BuiltInLootTables.SHIPWRECK_MAP)
				.addModifier(entries(1, entry(BAROMETER.get(), 1), entry(DEPTH_GAUGE.get(), 1)));
		this.entry("shipwreck_treasure").selects(BuiltInLootTables.SHIPWRECK_TREASURE)
				.addModifier(entries(0, entry(SILVER_INGOT.get(), 10, 1, 5)))
				.addModifier(entries(1, entry(SILVER_NUGGET.get(), 10, 1, 10), entry(SPINEL.get(), 20, 1, 8)));
		this.entry("buried_treasure").selects(BuiltInLootTables.BURIED_TREASURE)
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 10, 1, 4)))
				.addModifier(entries(2, entry(ZIRCONIA.get(), 5, 1, 2), entry(TURQUOISE.get(), 1)));

		this.entry("desert_pyramid").selects(BuiltInLootTables.DESERT_PYRAMID).addModifier(entries(0,
				entry(SILVER_INGOT.get(), 15, 1, 5), entry(SILVER_HORSE_ARMOR.get(), 10), entry(COPPER_HORSE_ARMOR.get(), 15)));
		this.entry("jungle_temple").selects(BuiltInLootTables.JUNGLE_TEMPLE).addModifier(entries(0,
				entry(SILVER_INGOT.get(), 15, 2, 7), entry(SPINEL.get(), 15, 2, 5), entry(SILVER_HORSE_ARMOR.get(), 1), entry(COPPER_HORSE_ARMOR.get(), 1)));
		this.entry("jungle_temple_dispenser").selects(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER).addModifier(entries(0,
				entry(RICOCHET_ARROW.get(), 10, 1, 4)));
		this.entry("woodland_mansion").selects(BuiltInLootTables.WOODLAND_MANSION)
				.addModifier(entries(0, entry(COWL.get(), 10)))
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 5, 1, 4), entry(TIN_INGOT.get(), 8, 1, 4), entry(ZIRCONIA.get(), 5, 1, 2)))
				.addModifier(pools(pool("turquoise").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(TURQUOISE.get())).build()));

		this.entry("village_fisher").selects(BuiltInLootTables.VILLAGE_FISHER)
				.addModifier(entries(0, entry(CAVEFISH.get(), 1, 1, 3)))
				.addModifier(pools(pool("barometer").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(BAROMETER.get())).build()));
		this.entry("village_weaponsmith").selects(BuiltInLootTables.VILLAGE_WEAPONSMITH)
				.addModifier(entries(0, entry(SILVER_INGOT.get(), 5, 1, 3), entry(SILVER_HORSE_ARMOR.get(), 1), entry(COPPER_HORSE_ARMOR.get(), 1)));
		this.entry("village_toolsmith").selects(BuiltInLootTables.VILLAGE_TOOLSMITH)
				.addModifier(entries(0, entry(SILVER_INGOT.get(), 1, 1, 3)));
		this.entry("village_temple").selects(BuiltInLootTables.VILLAGE_TEMPLE)
				.addModifier(entries(0, entry(SPINEL.get(), 1, 1, 4), entry(SILVER_INGOT.get(), 1, 1, 4), entry(ZIRCONIA.get(), 1)));
		this.entry("village_fletcher").selects(BuiltInLootTables.VILLAGE_FLETCHER)
				.addModifier(entries(0, entry(CCBlocks.HOOP.get(), 1), entry(BLUNT_ARROW.get(), 1, 1, 8)));
		this.entry("village_snowy_house").selects(BuiltInLootTables.VILLAGE_SNOWY_HOUSE)
				.addModifier(entries(0, entry(SILVER_NUGGET.get(), 1, 1, 4)));

		LootItemCondition.Builder inCave = LocationCheck.checkLocation(LocationPredicate.Builder.location().setY(Doubles.atMost(30.0D)));
		this.entry("fishing/fish").selects(BuiltInLootTables.FISHING_FISH).addModifier(new LootPoolEntriesModifier(false, 0, LootItem.lootTableItem(CAVEFISH.get()).setWeight(70).when(inCave).build()));

		this.entry("trail_ruins_rare").selects(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)
				.addModifier(entries(0, entry(TOOLBELT.get(), 1)));
		this.entry("igloo_chest").selects(BuiltInLootTables.IGLOO_CHEST)
				.addModifier(entries(0, entry(TOOLBELT.get(), 2)));

		this.entry("pillager_outpost").selects(BuiltInLootTables.PILLAGER_OUTPOST)
				.addModifier(entries(1, entry(COWL.get(), 1)))
				.addModifier(entries(3, entry(RICOCHET_ARROW.get(), 2, 1, 4), entry(LARGE_ARROW.get(), 4, 1, 2)));

		this.entry("ancient_city").selects(BuiltInLootTables.ANCIENT_CITY).addModifier(entries(0,
				entry(TUNING_FORK.get(), 2), entry(CCBlocks.SPARKLER.getFirst().get(), 5, 1, 15), entry(BEJEWELED_APPLE.get(), 3, 1, 3), entry(ZIRCONIA.get(), 3, 1, 2),
				LootItem.lootTableItem(COWL.get()).setWeight(2).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20.0F, 39.0F)).allowTreasure()).build()
		));

		this.entry("ruined_portal").selects(BuiltInLootTables.RUINED_PORTAL)
				.addModifier(entries(0, entry(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16), entry(CCBlocks.LAVA_LAMP.get(), 5), entry(GOLDEN_BUCKET.get(), 1)))
				.addModifier(pools(pool("lodestone").add(EmptyLootItem.emptyItem()).add(LootItem.lootTableItem(Blocks.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).build()));
		this.entry("nether_bridge").selects(BuiltInLootTables.NETHER_BRIDGE)
				.addModifier(entries(0, entry(SILVER_INGOT.get(), 5, 1, 5), entry(SILVER_HORSE_ARMOR.get(), 6), entry(COPPER_HORSE_ARMOR.get(), 5), entry(TURQUOISE.get(), 1)))
				.addModifier(entries(1, entry(EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), 1)));
		this.entry("bastion_bridge").selects(BuiltInLootTables.BASTION_BRIDGE)
				.addModifier(pools(pool("golden_bucket").add(LootItem.lootTableItem(GOLDEN_BUCKET.get())).build()))
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 1, 4, 9), entry(LARGE_ARROW.get(), 1, 4, 12),
						LootItem.lootTableItem(TOOLBELT.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()).build()))
				.addModifier(entries(2, entry(SILVER_NUGGET.get(), 1, 2, 6)));
		this.entry("bastion_hoglin_stable").selects(BuiltInLootTables.BASTION_HOGLIN_STABLE)
				.addModifier(entries(0, entry(TURQUOISE.get(), 6)));
		this.entry("bastion_treasure").selects(BuiltInLootTables.BASTION_TREASURE)
				.addModifier(entries(0, entry(TURQUOISE.get(), 6)))
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 1, 3, 9), entry(CCBlocks.SILVER_BLOCK.get(), 1, 2, 5), entry(LARGE_ARROW.get(), 1, 6, 10)));
		this.entry("bastion_other").selects(BuiltInLootTables.BASTION_OTHER)
				.addModifier(entries(0, entry(LARGE_ARROW.get(), 1, 4, 8), entry(TURQUOISE.get(), 3)))
				.addModifier(entries(1, entry(SILVER_INGOT.get(), 2, 1, 6), entry(CCBlocks.SILVER_BLOCK.get(), 2)))
				.addModifier(entries(1, entry(SILVER_NUGGET.get(), 1, 2, 8)));

		this.entry("end_city_treasure").selects(BuiltInLootTables.END_CITY_TREASURE)
				.addModifier(entries(0, entry(BEJEWELED_APPLE.get(), 5, 3, 9), entry(SILVER_INGOT.get(), 15, 2, 7), entry(SILVER_HORSE_ARMOR.get(), 1), entry(COPPER_HORSE_ARMOR.get(), 1), entry(TURQUOISE.get(), 1)));

		this.entry("kousa_sanctum").selects(new ResourceLocation("atmospheric", "chests/kousa_sanctum")).addModifier(entries(0,
				entry(SILVER_INGOT.get(), 15, 2, 7), entry(SILVER_HORSE_ARMOR.get(), 1), entry(COPPER_HORSE_ARMOR.get(), 1)));

		this.entry("elder_guardian").selects(new ResourceLocation("entities/elder_guardian")).addModifier(pools(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get()).setWeight(1)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(LootItemRandomChanceCondition.randomChance(0.1F))
						).build())
		);

		this.entry("copper_ore").selects(new ResourceLocation("blocks/copper_ore")).addModifier(pools(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))).invert())
								.when(LootItemRandomChanceCondition.randomChance(0.005F))
						).build())
		);

		this.entry("deepslate_copper_ore").selects(new ResourceLocation("blocks/deepslate_copper_ore")).addModifier(pools(
				pool("turquoise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))).invert())
								.when(LootItemRandomChanceCondition.randomChance(0.01F))
						).build())
		);
	}

	private static LootPoolEntriesModifier entries(int index, LootPoolEntryContainer... entries) {
		return new LootPoolEntriesModifier(false, index, entries);
	}

	private static LootPoolsModifier pools(LootPool... pools) {
		return new LootPoolsModifier(List.of(pools), false);
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