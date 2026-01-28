package com.teamabnormals.caverns_and_chasms.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolsModifier;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
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
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CCLootModifierProvider extends LootModifierProvider {

	public CCLootModifierProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(CavernsAndChasms.MOD_ID, output, provider);
	}

	@Override
	protected void registerEntries(Provider provider) {
		this.entry("simple_dungeon").selects(BuiltInLootTables.SIMPLE_DUNGEON)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 15))))
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 10))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 4))));
		this.entry("abandoned_mineshaft").selects(BuiltInLootTables.ABANDONED_MINESHAFT)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(Items.BUNDLE, 5), lootPool(CCItems.DEPTH_GAUGE.get(), 5))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SPINEL.get(), 5, 6, 11))))
				.addModifier(new LootPoolEntriesModifier(false, 2, List.of(lootPool(CCBlocks.SPIKED_RAIL.get(), 5, 1, 4))));
		this.entry("stronghold_corridor").selects(BuiltInLootTables.STRONGHOLD_CORRIDOR).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1), lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 1))));
		this.entry("stronghold_crossing").selects(BuiltInLootTables.STRONGHOLD_CROSSING).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3))));

		this.entry("shipwreck_map").selects(BuiltInLootTables.SHIPWRECK_MAP)
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.BAROMETER.get(), 1), lootPool(CCItems.DEPTH_GAUGE.get(), 1))));
		this.entry("shipwreck_treasure").selects(BuiltInLootTables.SHIPWRECK_TREASURE)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(lootPool(CCItems.SILVER_INGOT.get(), 10, 1, 5))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_NUGGET.get(), 10, 1, 10), lootPool(CCItems.SPINEL.get(), 20, 1, 8))));
		this.entry("buried_treasure").selects(BuiltInLootTables.BURIED_TREASURE)
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.SILVER_INGOT.get(), 10, 1, 4))));

		this.entry("desert_pyramid").selects(BuiltInLootTables.DESERT_PYRAMID).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 1, 5), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 10), lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 15))));
		this.entry("jungle_temple").selects(BuiltInLootTables.JUNGLE_TEMPLE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 2, 7), lootPool(CCItems.SPINEL.get(), 15, 2, 5), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1), lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 1))));
		this.entry("woodland_mansion").selects(BuiltInLootTables.WOODLAND_MANSION).addModifier(new LootPoolEntriesModifier(false, 1,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 4))));

		this.entry("village_fisher").selects(BuiltInLootTables.VILLAGE_FISHER)
				.addModifier(new LootPoolsModifier(List.of(LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":barometer").setRolls(UniformGenerator.between(0.0F, 1.0F)).add(LootItem.lootTableItem(CCItems.BAROMETER.get())).build()), false));
		this.entry("village_weaponsmith").selects(BuiltInLootTables.VILLAGE_WEAPONSMITH).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 3), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1), lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 1))));
		this.entry("village_toolsmith").selects(BuiltInLootTables.VILLAGE_TOOLSMITH).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 1, 1, 3))));
		this.entry("village_temple").selects(BuiltInLootTables.VILLAGE_TEMPLE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SPINEL.get(), 1, 1, 4), lootPool(CCItems.SILVER_INGOT.get(), 1, 1, 4))));

		this.entry("ruined_portal").selects(BuiltInLootTables.RUINED_PORTAL)
				.addModifier(new LootPoolEntriesModifier(false, 0,
						List.of(lootPool(CCBlocks.GOLDEN_BARS.get(), 5, 8, 16), lootPool(CCBlocks.LAVA_LAMP.get(), 5), lootPool(CCItems.GOLDEN_BUCKET.get(), 1))))
				.addModifier(new LootPoolsModifier(List.of(LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":lodestone").add(EmptyLootItem.emptyItem()).add(LootItem.lootTableItem(Blocks.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).build()), false));
		this.entry("nether_bridge").selects(BuiltInLootTables.NETHER_BRIDGE)
				.addModifier(new LootPoolEntriesModifier(false, 0, List.of(
						lootPool(CCItems.SILVER_INGOT.get(), 5, 1, 5),
						lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 6),
						lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 5))))
				.addModifier(new LootPoolEntriesModifier(false, 1, List.of(lootPool(CCItems.EXILE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), 1))));
		this.entry("bastion_bridge").selects(BuiltInLootTables.BASTION_BRIDGE)
				.addModifier(new LootPoolsModifier(List.of(LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":golden_bucket").add(LootItem.lootTableItem(CCItems.GOLDEN_BUCKET.get())).build()), false));

		this.entry("end_city_treasure").selects(BuiltInLootTables.END_CITY_TREASURE).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 2, 7), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1), lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 1))));

		this.entry("kousa_sanctum").selects(new ResourceLocation("atmospheric", "chests/kousa_sanctum")).addModifier(new LootPoolEntriesModifier(false, 0,
				List.of(lootPool(CCItems.SILVER_INGOT.get(), 15, 2, 7), lootPool(CCItems.SILVER_HORSE_ARMOR.get(), 1), lootPool(CCItems.COPPER_HORSE_ARMOR.get(), 1))));

		this.entry("copper_ore").selects(new ResourceLocation("blocks/copper_ore")).addModifier(new LootPoolsModifier(
				List.of(LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":turqouise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(CCItems.TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))).invert())
								.when(LootItemRandomChanceCondition.randomChance(0.02F))
						).build()), false
		));

		this.entry("deepslate_copper_ore").selects(new ResourceLocation("blocks/deepslate_copper_ore")).addModifier(new LootPoolsModifier(
				List.of(LootPool.lootPool().name(CavernsAndChasms.MOD_ID + ":turqouise").setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(CCItems.TURQUOISE.get())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))).invert())
								.when(LootItemRandomChanceCondition.randomChance(0.04F))
						).build()), false
		));
	}


	private static LootPoolEntryContainer lootPool(ItemLike item, int weight) {
		return LootItem.lootTableItem(item).setWeight(weight).build();
	}

	private static LootPoolEntryContainer lootPool(ItemLike item, int weight, int min, int max) {
		return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
	}
}