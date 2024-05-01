package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.teamabnormals.caverns_and_chasms.common.block.TmtBlock;
import com.teamabnormals.caverns_and_chasms.common.block.ToolboxBlock;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes.*;

public class CCLootTableProvider extends LootTableProvider {

	public CCLootTableProvider(PackOutput output) {
		super(output, BuiltInLootTables.all(), ImmutableList.of(
				new LootTableProvider.SubProviderEntry(CCBlockLoot::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(CCEntityLoot::new, LootContextParamSets.ENTITY)
		));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
	}

	private static class CCBlockLoot extends BlockLootSubProvider {
		private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

		private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
		private static final LootItemCondition.Builder HAS_PICKAXE = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.PICKAXES));
		private static final LootItemCondition.Builder HAS_SHOVEL = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.SHOVELS));

		protected CCBlockLoot() {
			super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		public void generate() {
			this.add(SILVER_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_SILVER.get()));
			this.add(DEEPSLATE_SILVER_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_SILVER.get()));
			this.add(SOUL_SILVER_ORE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.SILVER_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
			this.add(SPINEL_ORE.get(), this::createSpinelOreDrops);
			this.add(DEEPSLATE_SPINEL_ORE.get(), this::createSpinelOreDrops);

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
			this.dropSelf(SPIKED_RAIL.get());
			this.dropSelf(SILVER_BARS.get());
			this.dropSelf(BRAZIER.get());
			this.dropSelf(SOUL_BRAZIER.get());
			this.dropSelf(ENDER_BRAZIER.get());
			this.dropSelf(CUPRIC_BRAZIER.get());

			this.dropSelf(SANGUINE_BLOCK.get());
			this.dropSelf(SANGUINE_TILES.get());
			this.dropSelf(SANGUINE_TILE_STAIRS.get());
			this.dropSelf(SANGUINE_TILE_WALL.get());
			this.add(SANGUINE_TILE_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(FORTIFIED_SANGUINE_TILES.get());
			this.dropSelf(FORTIFIED_SANGUINE_TILE_STAIRS.get());
			this.dropSelf(FORTIFIED_SANGUINE_TILE_WALL.get());
			this.add(FORTIFIED_SANGUINE_TILE_SLAB.get(), this::createSlabItemTable);

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

			this.add(TOOLBOX.get(), this::createToolboxDrop);
			this.add(EXPOSED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WEATHERED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(OXIDIZED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_EXPOSED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_WEATHERED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_OXIDIZED_TOOLBOX.get(), this::createToolboxDrop);

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
			this.add(COBBLESTONE_BRICK_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(COBBLESTONE_TILES.get());
			this.dropSelf(COBBLESTONE_TILE_STAIRS.get());
			this.dropSelf(COBBLESTONE_TILE_WALL.get());
			this.add(COBBLESTONE_TILE_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(MOSSY_COBBLESTONE_BRICKS.get());
			this.dropSelf(MOSSY_COBBLESTONE_BRICK_STAIRS.get());
			this.dropSelf(MOSSY_COBBLESTONE_BRICK_WALL.get());
			this.add(MOSSY_COBBLESTONE_BRICK_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(MOSSY_COBBLESTONE_TILES.get());
			this.dropSelf(MOSSY_COBBLESTONE_TILE_STAIRS.get());
			this.dropSelf(MOSSY_COBBLESTONE_TILE_WALL.get());
			this.add(MOSSY_COBBLESTONE_TILE_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(COBBLED_DEEPSLATE_BRICKS.get());
			this.dropSelf(COBBLED_DEEPSLATE_BRICK_STAIRS.get());
			this.dropSelf(COBBLED_DEEPSLATE_BRICK_WALL.get());
			this.add(COBBLED_DEEPSLATE_BRICK_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(COBBLED_DEEPSLATE_TILES.get());
			this.dropSelf(COBBLED_DEEPSLATE_TILE_STAIRS.get());
			this.dropSelf(COBBLED_DEEPSLATE_TILE_WALL.get());
			this.add(COBBLED_DEEPSLATE_TILE_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(CALCITE_STAIRS.get());
			this.dropSelf(CALCITE_WALL.get());
			this.add(CALCITE_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(POLISHED_CALCITE.get());
			this.dropSelf(POLISHED_CALCITE_STAIRS.get());
			this.add(POLISHED_CALCITE_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(TUFF_STAIRS.get());
			this.dropSelf(TUFF_WALL.get());
			this.add(TUFF_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(POLISHED_TUFF.get());
			this.dropSelf(POLISHED_TUFF_STAIRS.get());
			this.add(POLISHED_TUFF_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(SUGILITE.get());
			this.dropSelf(SUGILITE_STAIRS.get());
			this.dropSelf(SUGILITE_WALL.get());
			this.add(SUGILITE_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(POLISHED_SUGILITE.get());
			this.dropSelf(POLISHED_SUGILITE_STAIRS.get());
			this.add(POLISHED_SUGILITE_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(DRIPSTONE_SHINGLES.get());
			this.dropSelf(DRIPSTONE_SHINGLE_STAIRS.get());
			this.dropSelf(DRIPSTONE_SHINGLE_WALL.get());
			this.add(DRIPSTONE_SHINGLE_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(CHISELED_DRIPSTONE_SHINGLES.get());
			this.dropSelf(FLOODED_DRIPSTONE_SHINGLES.get());

			this.dropSelf(AMETHYST_BLOCK.get());
			this.dropSelf(CUT_AMETHYST.get());
			this.dropSelf(CUT_AMETHYST_BRICKS.get());
			this.dropSelf(CUT_AMETHYST_BRICK_STAIRS.get());
			this.dropSelf(CUT_AMETHYST_BRICK_WALL.get());
			this.add(CUT_AMETHYST_BRICK_SLAB.get(), this::createSlabItemTable);

			this.dropSelf(ECHO_BLOCK.get());

			this.dropSelf(LAPIS_LAZULI_BRICKS.get());
			this.dropSelf(LAPIS_LAZULI_BRICK_STAIRS.get());
			this.dropSelf(LAPIS_LAZULI_BRICK_WALL.get());
			this.add(LAPIS_LAZULI_BRICK_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(LAPIS_LAZULI_PILLAR.get());
			this.dropSelf(LAPIS_LAZULI_LAMP.get());

			this.dropSelf(SPINEL_BLOCK.get());
			this.dropSelf(SPINEL_BRICKS.get());
			this.dropSelf(SPINEL_BRICK_STAIRS.get());
			this.dropSelf(SPINEL_BRICK_WALL.get());
			this.add(SPINEL_BRICK_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(SPINEL_PILLAR.get());
			this.dropSelf(SPINEL_LAMP.get());

			this.dropSelf(SANGUINE_TILES.get());
			this.dropSelf(SANGUINE_TILE_STAIRS.get());
			this.add(SANGUINE_TILE_SLAB.get(), this::createSlabItemTable);

			this.add(CUPRIC_FIRE.get(), noDrop());
			this.dropSelf(CUPRIC_LANTERN.get());
			this.dropSelf(CUPRIC_TORCH.get());
			this.dropOther(CUPRIC_WALL_TORCH.get(), CUPRIC_TORCH.get());
			this.add(CUPRIC_CAMPFIRE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(Items.COPPER_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));

			this.dropSelf(AZALEA_PLANKS.get());
			this.dropSelf(AZALEA_LOG.get());
			this.dropSelf(AZALEA_WOOD.get());
			this.dropSelf(STRIPPED_AZALEA_LOG.get());
			this.dropSelf(STRIPPED_AZALEA_WOOD.get());
			this.dropSelf(AZALEA_SIGNS.getFirst().get());
			this.dropSelf(AZALEA_HANGING_SIGNS.getFirst().get());
			this.dropSelf(AZALEA_PRESSURE_PLATE.get());
			this.dropSelf(AZALEA_TRAPDOOR.get());
			this.dropSelf(AZALEA_BUTTON.get());
			this.dropSelf(AZALEA_STAIRS.get());
			this.dropSelf(AZALEA_FENCE.get());
			this.dropSelf(AZALEA_FENCE_GATE.get());
			this.dropSelf(AZALEA_BOARDS.get());
			this.dropSelf(AZALEA_LADDER.get());
			this.add(AZALEA_SLAB.get(), this::createSlabItemTable);
			this.add(AZALEA_DOOR.get(), this::createDoorTable);
			this.add(AZALEA_BEEHIVE.get(), VanillaBlockLoot::createBeeHiveDrop);
			this.add(AZALEA_CHEST.get(), this::createNameableBlockEntityTable);
			this.add(TRAPPED_AZALEA_CHEST.get(), this::createNameableBlockEntityTable);
			this.add(AZALEA_BOOKSHELF.get(), (block) -> createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F)));
			this.dropWhenSilkTouch(CHISELED_AZALEA_BOOKSHELF.get());
		}

		protected LootTable.Builder createToolboxDrop(Block p_124295_) {
			return LootTable.lootTable().withPool(applyExplosionCondition(p_124295_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124295_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Lock", "BlockEntityTag.Lock").copy("LootTable", "BlockEntityTag.LootTable").copy("LootTableSeed", "BlockEntityTag.LootTableSeed")).apply(SetContainerContents.setContents(CCBlockEntityTypes.TOOLBOX.get()).withEntry(DynamicLoot.dynamicEntry(ToolboxBlock.CONTENTS))))));
		}

		protected LootTable.Builder createSpinelOreDrops(Block block) {
			return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.SPINEL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		}

		@Override
		public Iterable<Block> getKnownBlocks() {
			return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(CavernsAndChasms.MOD_ID)).collect(Collectors.toSet());
		}
	}


	private static class CCEntityLoot extends EntityLootSubProvider {
		private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = ImmutableSet.of(COPPER_GOLEM.get(), OXIDIZED_COPPER_GOLEM.get());

		protected CCEntityLoot() {
			super(FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		public void generate() {
			this.add(COPPER_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
			this.add(OXIDIZED_COPPER_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
			this.add(DEEPER.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			this.add(PEEPER.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			this.add(MIME.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CCItems.SPINEL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//			this.add(FLY.get(), LootTable.lootTable());
			// this.add(RAT.get(), LootTable.lootTable());
			this.add(GLARE.get(), LootTable.lootTable());
			this.add(LOST_GOAT.get(), LootTable.lootTable());

			this.add(EntityType.SILVERFISH, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CCItems.SILVER_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		}

		@Override
		public Stream<EntityType<?>> getKnownEntityTypes() {
			return ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(entity -> entity == EntityType.SILVERFISH || ForgeRegistries.ENTITY_TYPES.getKey(entity).getNamespace().equals(CavernsAndChasms.MOD_ID));
		}

		@Override
		protected boolean canHaveLootTable(EntityType<?> entityType) {
			return SPECIAL_LOOT_TABLE_TYPES.contains(entityType) || entityType.getCategory() != MobCategory.MISC;
		}
	}
}