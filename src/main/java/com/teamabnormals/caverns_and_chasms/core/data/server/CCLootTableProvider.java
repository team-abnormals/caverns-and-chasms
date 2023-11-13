package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.block.VerticalSlabBlock;
import com.teamabnormals.blueprint.common.block.VerticalSlabBlock.VerticalSlabType;
import com.teamabnormals.caverns_and_chasms.common.block.TmtBlock;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes.*;

public class CCLootTableProvider extends LootTableProvider {
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> tables = ImmutableList.of(Pair.of(CCBlockLoot::new, LootContextParamSets.BLOCK), Pair.of(CCEntityLoot::new, LootContextParamSets.ENTITY));

	public CCLootTableProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
		return tables;
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
	}

	private static class CCBlockLoot extends BlockLoot {
		private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
		private static final LootItemCondition.Builder HAS_PICKAXE = MatchTool.toolMatches(ItemPredicate.Builder.item().of(CCItemTags.TOOLS_PICKAXES));
		private static final LootItemCondition.Builder HAS_SHOVEL = MatchTool.toolMatches(ItemPredicate.Builder.item().of(CCItemTags.TOOLS_SHOVELS));

		@Override
		public void addTables() {
			this.add(SILVER_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_SILVER.get()));
			this.add(DEEPSLATE_SILVER_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_SILVER.get()));
			this.add(SOUL_SILVER_ORE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.SILVER_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
			this.add(SPINEL_ORE.get(), CCBlockLoot::createSpinelOreDrops);
			this.add(DEEPSLATE_SPINEL_ORE.get(), CCBlockLoot::createSpinelOreDrops);

			this.dropWhenSilkTouch(FRAGILE_STONE.get());
			this.dropWhenSilkTouch(FRAGILE_DEEPSLATE.get());
			this.add(ROCKY_DIRT.get(), (block) -> LootTable.lootTable().withPool(LootPool.lootPool().add(AlternativesEntry.alternatives(LootItem.lootTableItem(ROCKY_DIRT.get()).when(HAS_SILK_TOUCH), LootItem.lootTableItem(Items.COBBLESTONE).when(HAS_PICKAXE), LootItem.lootTableItem(Items.DIRT).when(HAS_SHOVEL), applyExplosionCondition(ROCKY_DIRT.get(), LootItem.lootTableItem(ROCKY_DIRT.get()))))));
			this.dropSelf(ROTTEN_FLESH_BLOCK.get());
			this.dropSelf(NECROMIUM_BLOCK.get());
			this.dropSelf(DEEPER_HEAD.get());
			this.dropSelf(PEEPER_HEAD.get());
			this.dropSelf(MIME_HEAD.get());

			this.dropSelf(SILVER_BLOCK.get());
			this.dropSelf(RAW_SILVER_BLOCK.get());
			this.dropSelf(MEDIUM_WEIGHTED_PRESSURE_PLATE.get());
			this.dropSelf(SILVER_BUTTON.get());
			this.dropSelf(SPIKED_RAIL.get());
			this.dropSelf(SILVER_BARS.get());
			this.dropSelf(BRAZIER.get());
			this.dropSelf(SOUL_BRAZIER.get());
			this.dropSelf(ENDER_BRAZIER.get());
			this.dropSelf(CUPRIC_BRAZIER.get());

			this.add(CCBlocks.TMT.get(), LootTable.lootTable().withPool(applyExplosionCondition(CCBlocks.TMT.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CCBlocks.TMT.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(CCBlocks.TMT.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TmtBlock.UNSTABLE, false)))))));

			this.dropSelf(LAVA_LAMP.get());
			this.dropSelf(GOLDEN_BARS.get());

			this.dropSelf(FLOODLIGHT.get());
			this.dropSelf(EXPOSED_FLOODLIGHT.get());
			this.dropSelf(WEATHERED_FLOODLIGHT.get());
			this.dropSelf(OXIDIZED_FLOODLIGHT.get());
			this.dropSelf(WAXED_FLOODLIGHT.get());
			this.dropSelf(WAXED_EXPOSED_FLOODLIGHT.get());
			this.dropSelf(WAXED_WEATHERED_FLOODLIGHT.get());
			this.dropSelf(WAXED_OXIDIZED_FLOODLIGHT.get());

//			this.dropSelf(INDUCTOR.get());

			this.add(TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(EXPOSED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(WEATHERED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(OXIDIZED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(WAXED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(WAXED_EXPOSED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(WAXED_WEATHERED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);
			this.add(WAXED_OXIDIZED_TOOLBOX.get(), CCBlockLoot::createToolboxDrop);

			this.dropSelf(COPPER_BARS.get());
			this.dropSelf(EXPOSED_COPPER_BARS.get());
			this.dropSelf(WEATHERED_COPPER_BARS.get());
			this.dropSelf(OXIDIZED_COPPER_BARS.get());
			this.dropSelf(WAXED_COPPER_BARS.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_BARS.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_BARS.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_BARS.get());

			this.dropSelf(COPPER_BUTTON.get());
			this.dropSelf(EXPOSED_COPPER_BUTTON.get());
			this.dropSelf(WEATHERED_COPPER_BUTTON.get());
			this.dropSelf(OXIDIZED_COPPER_BUTTON.get());
			this.dropSelf(WAXED_COPPER_BUTTON.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_BUTTON.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_BUTTON.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_BUTTON.get());

			this.dropSelf(EXPOSED_LIGHTNING_ROD.get());
			this.dropSelf(WEATHERED_LIGHTNING_ROD.get());
			this.dropSelf(OXIDIZED_LIGHTNING_ROD.get());
			this.dropSelf(WAXED_LIGHTNING_ROD.get());
			this.dropSelf(WAXED_EXPOSED_LIGHTNING_ROD.get());
			this.dropSelf(WAXED_WEATHERED_LIGHTNING_ROD.get());
			this.dropSelf(WAXED_OXIDIZED_LIGHTNING_ROD.get());

			this.dropSelf(COBBLESTONE_BRICKS.get());
			this.dropSelf(COBBLESTONE_BRICK_STAIRS.get());
			this.dropSelf(COBBLESTONE_BRICK_WALL.get());
			this.add(COBBLESTONE_BRICK_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(COBBLESTONE_TILES.get());
			this.dropSelf(COBBLESTONE_TILE_STAIRS.get());
			this.dropSelf(COBBLESTONE_TILE_WALL.get());
			this.add(COBBLESTONE_TILE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.dropSelf(MOSSY_COBBLESTONE_BRICKS.get());
			this.dropSelf(MOSSY_COBBLESTONE_BRICK_STAIRS.get());
			this.dropSelf(MOSSY_COBBLESTONE_BRICK_WALL.get());
			this.add(MOSSY_COBBLESTONE_BRICK_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(MOSSY_COBBLESTONE_TILES.get());
			this.dropSelf(MOSSY_COBBLESTONE_TILE_STAIRS.get());
			this.dropSelf(MOSSY_COBBLESTONE_TILE_WALL.get());
			this.add(MOSSY_COBBLESTONE_TILE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.dropSelf(CALCITE_STAIRS.get());
			this.dropSelf(CALCITE_WALL.get());
			this.add(CALCITE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(CALCITE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(POLISHED_CALCITE.get());
			this.dropSelf(POLISHED_CALCITE_STAIRS.get());
			this.add(POLISHED_CALCITE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(POLISHED_CALCITE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.dropSelf(TUFF_STAIRS.get());
			this.dropSelf(TUFF_WALL.get());
			this.add(TUFF_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(TUFF_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(POLISHED_TUFF.get());
			this.dropSelf(POLISHED_TUFF_STAIRS.get());
			this.add(POLISHED_TUFF_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(POLISHED_TUFF_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.dropSelf(SUGILITE.get());
			this.dropSelf(SUGILITE_STAIRS.get());
			this.dropSelf(SUGILITE_WALL.get());
			this.add(SUGILITE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(SUGILITE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(POLISHED_SUGILITE.get());
			this.dropSelf(POLISHED_SUGILITE_STAIRS.get());
			this.add(POLISHED_SUGILITE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(POLISHED_SUGILITE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.dropSelf(DRIPSTONE_SHINGLES.get());
			this.dropSelf(DRIPSTONE_SHINGLE_STAIRS.get());
			this.dropSelf(DRIPSTONE_SHINGLE_WALL.get());
			this.add(DRIPSTONE_SHINGLE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(DRIPSTONE_SHINGLE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(CHISELED_DRIPSTONE_SHINGLES.get());
			this.dropSelf(FLOODED_DRIPSTONE_SHINGLES.get());

			this.dropSelf(AMETHYST_BLOCK.get());
			this.dropSelf(CUT_AMETHYST.get());
			this.dropSelf(CUT_AMETHYST_BRICKS.get());
			this.dropSelf(CUT_AMETHYST_BRICK_STAIRS.get());
			this.dropSelf(CUT_AMETHYST_BRICK_WALL.get());
			this.add(CUT_AMETHYST_BRICK_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(CUT_AMETHYST_BRICK_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.dropSelf(ECHO_BLOCK.get());

			this.dropSelf(LAPIS_LAZULI_BRICKS.get());
			this.dropSelf(LAPIS_LAZULI_BRICK_STAIRS.get());
			this.dropSelf(LAPIS_LAZULI_BRICK_WALL.get());
			this.add(LAPIS_LAZULI_BRICK_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(LAPIS_LAZULI_BRICK_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(LAPIS_LAZULI_PILLAR.get());
			this.dropSelf(LAPIS_LAZULI_LAMP.get());

			this.dropSelf(SPINEL_BLOCK.get());
			this.dropSelf(SPINEL_BRICKS.get());
			this.dropSelf(SPINEL_BRICK_STAIRS.get());
			this.dropSelf(SPINEL_BRICK_WALL.get());
			this.add(SPINEL_BRICK_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(SPINEL_BRICK_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.dropSelf(SPINEL_PILLAR.get());
			this.dropSelf(SPINEL_LAMP.get());

			this.dropSelf(SANGUINE_PLATES.get());
			this.dropSelf(SANGUINE_STAIRS.get());
			this.add(SANGUINE_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(SANGUINE_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);

			this.add(CUPRIC_FIRE.get(), noDrop());
			this.dropSelf(CUPRIC_LANTERN.get());
			this.dropSelf(CUPRIC_TORCH.get());
			this.dropOther(CUPRIC_WALL_TORCH.get(), CUPRIC_TORCH.get());
			this.add(CUPRIC_CAMPFIRE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(Items.COPPER_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
			this.add(CCBlocks.CUPRIC_CANDLE.get(), BlockLoot::createCandleDrops);
			this.add(CCBlocks.CUPRIC_CANDLE_CAKE.get(), createCandleCakeDrops(CCBlocks.CUPRIC_CANDLE.get()));

			this.dropSelf(AZALEA_PLANKS.get());
			this.dropSelf(AZALEA_LOG.get());
			this.dropSelf(AZALEA_WOOD.get());
			this.dropSelf(STRIPPED_AZALEA_LOG.get());
			this.dropSelf(STRIPPED_AZALEA_WOOD.get());
			this.dropSelf(AZALEA_SIGN.getFirst().get());
			this.dropSelf(AZALEA_PRESSURE_PLATE.get());
			this.dropSelf(AZALEA_TRAPDOOR.get());
			this.dropSelf(AZALEA_BUTTON.get());
			this.dropSelf(AZALEA_STAIRS.get());
			this.dropSelf(AZALEA_FENCE.get());
			this.dropSelf(AZALEA_FENCE_GATE.get());
			this.dropSelf(AZALEA_BOARDS.get());
			this.dropSelf(AZALEA_POST.get());
			this.dropSelf(STRIPPED_AZALEA_POST.get());
			this.dropSelf(AZALEA_HEDGE.get());
			this.dropSelf(FLOWERING_AZALEA_HEDGE.get());
			this.dropSelf(AZALEA_LADDER.get());
			this.dropSelf(VERTICAL_AZALEA_PLANKS.get());
			this.add(AZALEA_SLAB.get(), BlockLoot::createSlabItemTable);
			this.add(AZALEA_VERTICAL_SLAB.get(), CCBlockLoot::createVerticalSlabItemTable);
			this.add(AZALEA_DOOR.get(), BlockLoot::createDoorTable);
			this.add(AZALEA_BEEHIVE.get(), BlockLoot::createBeeHiveDrop);
			this.add(AZALEA_CHEST.getFirst().get(), BlockLoot::createNameableBlockEntityTable);
			this.add(AZALEA_CHEST.getSecond().get(), BlockLoot::createNameableBlockEntityTable);
			this.add(AZALEA_BOOKSHELF.get(), (block) -> createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F)));
		}

		protected static LootTable.Builder createToolboxDrop(Block p_124295_) {
			return LootTable.lootTable().withPool(applyExplosionCondition(p_124295_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124295_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Lock", "BlockEntityTag.Lock").copy("LootTable", "BlockEntityTag.LootTable").copy("LootTableSeed", "BlockEntityTag.LootTableSeed")).apply(SetContainerContents.setContents(CCBlockEntityTypes.TOOLBOX.get()).withEntry(DynamicLoot.dynamicEntry(ToolboxBlock.CONTENTS))))));
		}

		protected static LootTable.Builder createSpinelOreDrops(Block block) {
			return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.SPINEL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		}

		protected static LootTable.Builder createVerticalSlabItemTable(Block block) {
			return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(applyExplosionDecay(block, LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(VerticalSlabBlock.TYPE, VerticalSlabType.DOUBLE)))))));
		}

		@Override
		public Iterable<Block> getKnownBlocks() {
			return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(CavernsAndChasms.MOD_ID)).collect(Collectors.toSet());
		}
	}


	private static class CCEntityLoot extends EntityLoot {
		private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = ImmutableSet.of(COPPER_GOLEM.get(), OXIDIZED_COPPER_GOLEM.get());

		@Override
		public void addTables() {
			this.add(COPPER_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
			this.add(OXIDIZED_COPPER_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
			this.add(DEEPER.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			this.add(PEEPER.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			this.add(MIME.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CCItems.SPINEL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//			this.add(FLY.get(), LootTable.lootTable());
			// this.add(RAT.get(), LootTable.lootTable());
			this.add(GLARE.get(), LootTable.lootTable());
			this.add(LOST_GOAT.get(), LootTable.lootTable());
		}

		@Override
		public Iterable<EntityType<?>> getKnownEntities() {
			return ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(entity -> ForgeRegistries.ENTITY_TYPES.getKey(entity).getNamespace().equals(CavernsAndChasms.MOD_ID)).collect(Collectors.toSet());
		}

		@Override
		protected boolean isNonLiving(EntityType<?> entityType) {
			return !SPECIAL_LOOT_TABLE_TYPES.contains(entityType) && entityType.getCategory() == MobCategory.MISC;
		}
	}
}