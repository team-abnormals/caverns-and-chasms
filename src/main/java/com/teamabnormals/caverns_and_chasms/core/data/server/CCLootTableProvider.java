package com.teamabnormals.caverns_and_chasms.core.data.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.teamabnormals.caverns_and_chasms.common.block.*;
import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.loot.FortuneEnchantFunction;
import net.minecraft.advancements.critereon.*;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate.Builder;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.teamabnormals.caverns_and_chasms.core.other.CCBlockFamilies.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes.*;

public class CCLootTableProvider extends LootTableProvider {

	public CCLootTableProvider(PackOutput output) {
		super(output, BuiltInLootTables.all(), ImmutableList.of(
				new LootTableProvider.SubProviderEntry(CCBlockLoot::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(CCEntityLoot::new, LootContextParamSets.ENTITY),
				new LootTableProvider.SubProviderEntry(CCChestLoot::new, LootContextParamSets.CHEST),
				new LootTableProvider.SubProviderEntry(CCArchaeologyLoot::new, LootContextParamSets.ARCHAEOLOGY)
		));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
	}

	private static class CCBlockLoot extends BlockLootSubProvider {
		private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

		public static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
		public static final LootItemCondition.Builder HAS_PICKAXE = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.PICKAXES));
		public static final LootItemCondition.Builder HAS_SHOVEL = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.SHOVELS));

		protected CCBlockLoot() {
			super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		public void generate() {
			this.add(SILVER_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_SILVER.get()));
			this.add(DEEPSLATE_SILVER_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_SILVER.get()));
			this.add(SOUL_SILVER_ORE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.SILVER_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
			this.add(TIN_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_TIN.get()));
			this.add(DEEPSLATE_TIN_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_TIN.get()));
			this.add(CASSITERITE_TIN_ORE.get(), (block) -> createOreDrop(block, CCItems.RAW_TIN.get()));
			this.add(SPINEL_ORE.get(), this::createSpinelOreDrops);
			this.add(DEEPSLATE_SPINEL_ORE.get(), this::createSpinelOreDrops);
			this.add(TURQUOISE_ORE.get(), this::createTurquoiseOreDrops);
			this.add(DEEPSLATE_TURQUOISE_ORE.get(), this::createTurquoiseOreDrops);

			this.dropWhenSilkTouch(FRAGILE_STONE.get());
			this.dropWhenSilkTouch(FRAGILE_DEEPSLATE.get());
			this.add(ROCKY_DIRT.get(), (block) -> LootTable.lootTable().withPool(LootPool.lootPool().add(AlternativesEntry.alternatives(
					LootItem.lootTableItem(ROCKY_DIRT.get()).when(HAS_SILK_TOUCH),
					LootItem.lootTableItem(Items.COBBLESTONE).when(HAS_PICKAXE),
					LootItem.lootTableItem(Items.FLINT).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).otherwise(LootItem.lootTableItem(Items.DIRT)).when(HAS_SHOVEL),
					applyExplosionCondition(ROCKY_DIRT.get(), LootItem.lootTableItem(ROCKY_DIRT.get()))))));
			this.add(FLINT_BLOCK.get(), (block -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.FLINT)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))));
			this.dropSelf(CHARCOAL_BLOCK.get());
			this.add(COAL.get(), this::createCoalDrops);
			this.add(CHARCOAL.get(), this::createCoalDrops);

			this.add(COPPER_INGOT.get(), this::createIngotDrops);
			this.add(EXPOSED_COPPER_INGOT.get(), this::createIngotDrops);
			this.add(WEATHERED_COPPER_INGOT.get(), this::createIngotDrops);
			this.add(OXIDIZED_COPPER_INGOT.get(), this::createIngotDrops);
			this.add(WAXED_COPPER_INGOT.get(), this::createIngotDrops);
			this.add(WAXED_EXPOSED_COPPER_INGOT.get(), this::createIngotDrops);
			this.add(WAXED_WEATHERED_COPPER_INGOT.get(), this::createIngotDrops);
			this.add(WAXED_OXIDIZED_COPPER_INGOT.get(), this::createIngotDrops);

			this.add(IRON_INGOT.get(), this::createIngotDrops);
			this.add(GOLD_INGOT.get(), this::createIngotDrops);
			this.add(NETHERITE_INGOT.get(), this::createIngotDrops);
			this.add(SILVER_INGOT.get(), this::createIngotDrops);
			this.add(TIN_INGOT.get(), this::createIngotDrops);
			this.add(NECROMIUM_INGOT.get(), this::createIngotDrops);

			this.add(BRICK.get(), this::createIngotDrops);
			this.add(NETHER_BRICK.get(), this::createIngotDrops);
			this.add(EUMUS_BRICK.get(), this::createIngotDrops);

			this.dropSelf(ROTTEN_FLESH_BLOCK.get());
			this.dropSelf(NECROMIUM_BLOCK.get());
			this.dropSelf(DEEPER_HEAD.get());
			this.dropSelf(PEEPER_HEAD.get());
			this.dropSelf(MIME_HEAD.get());

			this.dropSelf(SILVER_BLOCK.get());
			this.dropSelf(RAW_SILVER_BLOCK.get());
			this.dropSelf(MEDIUM_WEIGHTED_PRESSURE_PLATE.get());
			this.dropSelf(SILVER_BARS.get());
			this.dropSelf(BRAZIER.get());
			this.dropSelf(SOUL_BRAZIER.get());
			this.dropSelf(ENDER_BRAZIER.get());
			this.dropSelf(CUPRIC_BRAZIER.get());

			this.dropSelf(TIN_BLOCK.get());
			this.dropSelf(RAW_TIN_BLOCK.get());
			this.dropSelf(TIN_BARS.get());
			this.dropSelf(FLOAT_GLASS.get());
			this.dropSelf(FLOAT_GLASS_PANE.get());

			this.dropSelf(HOLD_PLATE.get());
			this.dropSelf(HOLD_BUTTON.get());
			this.dropSelf(WINCH.get());
			this.dropSelf(DIMMER.get());
			this.dropOther(WALL_DIMMER.get(), DIMMER.get());
			this.dropSelf(BOUNCER.get());
			this.dropSelf(HOOP.get());
			this.add(STORAGE_DUCT.get(), this::createNameableBlockEntityTable);
			this.dropSelf(STORAGE_DUCT_HATCH.get());

			this.dropSelf(ROLLER_DOOR.get());
			this.dropOther(ROLLER_DOOR_HEADER.get(), ROLLER_DOOR.get());

			this.dropSelf(COPPER_RAIL.get());
			this.dropSelf(EXPOSED_COPPER_RAIL.get());
			this.dropSelf(WEATHERED_COPPER_RAIL.get());
			this.dropSelf(OXIDIZED_COPPER_RAIL.get());
			this.dropSelf(WAXED_COPPER_RAIL.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_RAIL.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_RAIL.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_RAIL.get());

			this.dropSelf(HALT_RAIL.get());
			this.dropSelf(SPIKED_RAIL.get());
			this.dropSelf(SLAUGHTER_RAIL.get());

			this.dropSelf(RESISTOR.get());
			this.dropSelf(REFRACTOR.get());

			this.dropSelf(SANGUINE_BLOCK.get());
			this.blockFamily(SANGUINE_TILES_FAMILY);
			this.blockFamily(FORTIFIED_SANGUINE_TILES_FAMILY);

			this.add(CCBlocks.TMT.get(), LootTable.lootTable().withPool(applyExplosionCondition(CCBlocks.TMT.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CCBlocks.TMT.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(CCBlocks.TMT.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TmtBlock.UNSTABLE, false)))))));
			this.dropSelf(SPLURTER.get());
			this.dropSelf(SCATTERER.get());
			this.dropSelf(DISMANTLING_TABLE.get());
			this.dropSelf(BEJEWELED_ANVIL.get());
			this.add(ATONING_TABLE.get(), this::createNameableBlockEntityTable);

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

			this.add(TOOLBOX.get(), this::createToolboxDrop);
			this.add(EXPOSED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WEATHERED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(OXIDIZED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_EXPOSED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_WEATHERED_TOOLBOX.get(), this::createToolboxDrop);
			this.add(WAXED_OXIDIZED_TOOLBOX.get(), this::createToolboxDrop);

			this.dropSelf(CHISELED_COPPER.get());
			this.dropSelf(EXPOSED_CHISELED_COPPER.get());
			this.dropSelf(WEATHERED_CHISELED_COPPER.get());
			this.dropSelf(OXIDIZED_CHISELED_COPPER.get());
			this.dropSelf(WAXED_CHISELED_COPPER.get());
			this.dropSelf(WAXED_EXPOSED_CHISELED_COPPER.get());
			this.dropSelf(WAXED_WEATHERED_CHISELED_COPPER.get());
			this.dropSelf(WAXED_OXIDIZED_CHISELED_COPPER.get());

			this.dropSelf(COPPER_GRATE.get());
			this.dropSelf(EXPOSED_COPPER_GRATE.get());
			this.dropSelf(WEATHERED_COPPER_GRATE.get());
			this.dropSelf(OXIDIZED_COPPER_GRATE.get());
			this.dropSelf(WAXED_COPPER_GRATE.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_GRATE.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_GRATE.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_GRATE.get());

			this.dropSelf(COPPER_BULB.get());
			this.dropSelf(EXPOSED_COPPER_BULB.get());
			this.dropSelf(WEATHERED_COPPER_BULB.get());
			this.dropSelf(OXIDIZED_COPPER_BULB.get());
			this.dropSelf(WAXED_COPPER_BULB.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_BULB.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_BULB.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_BULB.get());

			this.add(COPPER_DOOR.get(), this::createDoorTable);
			this.add(EXPOSED_COPPER_DOOR.get(), this::createDoorTable);
			this.add(WEATHERED_COPPER_DOOR.get(), this::createDoorTable);
			this.add(OXIDIZED_COPPER_DOOR.get(), this::createDoorTable);
			this.add(WAXED_COPPER_DOOR.get(), this::createDoorTable);
			this.add(WAXED_EXPOSED_COPPER_DOOR.get(), this::createDoorTable);
			this.add(WAXED_WEATHERED_COPPER_DOOR.get(), this::createDoorTable);
			this.add(WAXED_OXIDIZED_COPPER_DOOR.get(), this::createDoorTable);

			this.dropSelf(COPPER_TRAPDOOR.get());
			this.dropSelf(EXPOSED_COPPER_TRAPDOOR.get());
			this.dropSelf(WEATHERED_COPPER_TRAPDOOR.get());
			this.dropSelf(OXIDIZED_COPPER_TRAPDOOR.get());
			this.dropSelf(WAXED_COPPER_TRAPDOOR.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_TRAPDOOR.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_TRAPDOOR.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_TRAPDOOR.get());

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

			this.dropSelf(COPPER_CHAIN.get());
			this.dropSelf(EXPOSED_COPPER_CHAIN.get());
			this.dropSelf(WEATHERED_COPPER_CHAIN.get());
			this.dropSelf(OXIDIZED_COPPER_CHAIN.get());
			this.dropSelf(WAXED_COPPER_CHAIN.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_CHAIN.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_CHAIN.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_CHAIN.get());

			this.dropSelf(COPPER_LANTERN.get());
			this.dropSelf(EXPOSED_COPPER_LANTERN.get());
			this.dropSelf(WEATHERED_COPPER_LANTERN.get());
			this.dropSelf(OXIDIZED_COPPER_LANTERN.get());
			this.dropSelf(WAXED_COPPER_LANTERN.get());
			this.dropSelf(WAXED_EXPOSED_COPPER_LANTERN.get());
			this.dropSelf(WAXED_WEATHERED_COPPER_LANTERN.get());
			this.dropSelf(WAXED_OXIDIZED_COPPER_LANTERN.get());

			this.blockFamily(COBBLESTONE_BRICKS_FAMILY);
			this.blockFamily(COBBLESTONE_TILES_FAMILY);
			this.blockFamily(MOSSY_COBBLESTONE_BRICKS_FAMILY);
			this.blockFamily(MOSSY_COBBLESTONE_TILES_FAMILY);
			this.blockFamily(COBBLED_DEEPSLATE_BRICKS_FAMILY);
			this.blockFamily(COBBLED_DEEPSLATE_TILES_FAMILY);

			this.dropSelf(STONE_WALL.get());
			this.dropSelf(POLISHED_GRANITE_WALL.get());
			this.dropSelf(POLISHED_DIORITE_WALL.get());
			this.dropSelf(POLISHED_ANDESITE_WALL.get());

			this.dropSelf(CALCITE_STAIRS.get());
			this.dropSelf(CALCITE_WALL.get());
			this.add(CALCITE_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(CALCITE_PILLAR.get());
			this.blockFamily(POLISHED_CALCITE_FAMILY);
			this.blockFamily(CALCITE_BRICKS_FAMILY);
			this.blockFamily(SMOOTH_CALCITE_FAMILY);

			this.dropSelf(TUFF_STAIRS.get());
			this.dropSelf(TUFF_WALL.get());
			this.add(TUFF_SLAB.get(), this::createSlabItemTable);
			this.blockFamily(POLISHED_TUFF_FAMILY);
			this.blockFamily(TUFF_BRICKS_FAMILY);
			this.blockFamily(SMOOTH_TUFF_FAMILY);

			this.blockFamily(SUGILITE_FAMILY);
			this.blockFamily(POLISHED_SUGILITE_FAMILY);

			this.blockFamily(CASSITERITE_FAMILY);
			this.blockFamily(CASSITERITE_BRICKS_FAMILY);
			this.dropSelf(CASSITERITE_PILLAR.get());
			this.blockFamily(POLISHED_CASSITERITE_FAMILY);
			this.blockFamily(SMOOTH_CASSITERITE_FAMILY);

			this.blockFamily(RHYOLITE_FAMILY);
			this.blockFamily(POLISHED_RHYOLITE_FAMILY);
			this.blockFamily(RHYOLITE_BRICKS_FAMILY);
			this.blockFamily(MAGMATIC_RHYOLITE_FAMILY);
			this.blockFamily(POLISHED_MAGMATIC_RHYOLITE_FAMILY);
			this.blockFamily(MAGMATIC_RHYOLITE_BRICKS_FAMILY);

			this.dropSelf(DRIPSTONE_STAIRS.get());
			this.dropSelf(DRIPSTONE_WALL.get());
			this.add(DRIPSTONE_SLAB.get(), this::createSlabItemTable);

			this.blockFamily(SMOOTH_DRIPSTONE_FAMILY);
			this.blockFamily(POLISHED_DRIPSTONE_FAMILY);
			this.blockFamily(DRIPSTONE_BRICKS_FAMILY);
			this.dropSelf(CRACKED_DRIPSTONE_BRICKS.get());
			this.blockFamily(DRIPSTONE_SHINGLES_FAMILY);
			this.dropSelf(FLOODED_DRIPSTONE_SHINGLES.get());

			this.dropSelf(AMETHYST_BLOCK.get());
			this.dropSelf(CUT_AMETHYST.get());
			this.blockFamily(CUT_AMETHYST_BRICKS_FAMILY);

			this.dropSelf(ECHO_BLOCK.get());

			this.blockFamily(LAPIS_LAZULI_BRICKS_FAMILY);
			this.dropSelf(LAPIS_LAZULI_PILLAR.get());
			this.dropSelf(LAPIS_LAZULI_LAMP.get());

			this.dropSelf(SPINEL_BLOCK.get());
			this.blockFamily(SPINEL_BRICKS_FAMILY);
			this.dropSelf(SPINEL_PILLAR.get());
			this.dropSelf(SPINEL_LAMP.get());

			this.dropSelf(TURQUOISE_BLOCK.get());
			this.blockFamily(TURQUOISE_TILES_FAMILY);
			this.dropSelf(TURQUOISE_PILLAR.get());
			this.dropSelf(TURQUOISE_LAMP.get());
			this.dropSelf(CAVIAR.get());

			this.dropSelf(ZIRCONIA_BLOCK.get());
			this.dropSelf(ZIRCONIA_LAMP.get());
			;
			this.dropSelf(ORNATE_GLASS.get());
			this.dropSelf(ORNATE_GLASS_PANE.get());

			this.dropSelf(QUARTZ_LAMP.get());
			this.dropSelf(AMETHYST_LAMP.get());
			this.dropSelf(DIAMOND_LAMP.get());
			this.dropSelf(EMERALD_LAMP.get());

			this.blockFamily(IRON_BRICKS_FAMILY);
			this.blockFamily(TIN_BRICKS_FAMILY);
			this.blockFamily(GOLD_BRICKS_FAMILY);
			this.blockFamily(SILVER_BRICKS_FAMILY);
			this.blockFamily(COPPER_BRICKS_FAMILY);
			this.blockFamily(EXPOSED_COPPER_BRICKS_FAMILY);
			this.blockFamily(WEATHERED_COPPER_BRICKS_FAMILY);
			this.blockFamily(OXIDIZED_COPPER_BRICKS_FAMILY);
			this.blockFamily(WAXED_COPPER_BRICKS_FAMILY);
			this.blockFamily(WAXED_EXPOSED_COPPER_BRICKS_FAMILY);
			this.blockFamily(WAXED_WEATHERED_COPPER_BRICKS_FAMILY);
			this.blockFamily(WAXED_OXIDIZED_COPPER_BRICKS_FAMILY);

			this.add(CUPRIC_FIRE.get(), noDrop());
			this.dropSelf(CUPRIC_LANTERN.get());
			this.dropSelf(CUPRIC_TORCH.get());
			this.dropOther(CUPRIC_WALL_TORCH.get(), CUPRIC_TORCH.get());
			this.add(CUPRIC_CAMPFIRE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(Items.RAW_COPPER).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));

			this.blockFamily(AZALEA_PLANKS_FAMILY);
			this.dropSelf(AZALEA_LOG.get());
			this.dropSelf(AZALEA_WOOD.get());
			this.dropSelf(STRIPPED_AZALEA_LOG.get());
			this.dropSelf(STRIPPED_AZALEA_WOOD.get());
			this.dropSelf(AZALEA_SIGNS.getFirst().get());
			this.dropSelf(AZALEA_HANGING_SIGNS.getFirst().get());
			this.dropSelf(AZALEA_PRESSURE_PLATE.get());
			this.dropSelf(AZALEA_TRAPDOOR.get());
			this.dropSelf(AZALEA_BUTTON.get());
			this.dropSelf(AZALEA_FENCE.get());
			this.dropSelf(AZALEA_FENCE_GATE.get());
			this.dropSelf(AZALEA_BOARDS.get());
			this.dropSelf(AZALEA_LADDER.get());
			this.add(AZALEA_DOOR.get(), this::createDoorTable);
			this.add(AZALEA_BEEHIVE.get(), VanillaBlockLoot::createBeeHiveDrop);
			this.add(AZALEA_CHEST.get(), this::createNameableBlockEntityTable);
			this.add(TRAPPED_AZALEA_CHEST.get(), this::createNameableBlockEntityTable);
			this.add(AZALEA_BOOKSHELF.get(), (block) -> createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F)));
			this.dropWhenSilkTouch(CHISELED_AZALEA_BOOKSHELF.get());

			this.dropSelf(FALSE_HOPE.get());
			this.dropSelf(MOSCHATEL.get());
			this.add(CAVE_GROWTHS.get(), BlockLootSubProvider::createShearsOnlyDrop);
			this.add(LURID_CAVE_GROWTHS.get(), BlockLootSubProvider::createShearsOnlyDrop);
			this.add(WISPY_CAVE_GROWTHS.get(), BlockLootSubProvider::createShearsOnlyDrop);
			this.add(GRAINY_CAVE_GROWTHS.get(), BlockLootSubProvider::createShearsOnlyDrop);
			this.add(WEIRD_CAVE_GROWTHS.get(), BlockLootSubProvider::createShearsOnlyDrop);
			this.add(ZESTY_CAVE_GROWTHS.get(), BlockLootSubProvider::createShearsOnlyDrop);

			this.dropPottedContents(POTTED_FALSE_HOPE.get());

			this.dropPottedContents(POTTED_MOSCHATEL.get());
			this.dropPottedContents(POTTED_CAVE_GROWTHS.get());
			this.dropPottedContents(POTTED_LURID_CAVE_GROWTHS.get());
			this.dropPottedContents(POTTED_WISPY_CAVE_GROWTHS.get());
			this.dropPottedContents(POTTED_GRAINY_CAVE_GROWTHS.get());
			this.dropPottedContents(POTTED_WEIRD_CAVE_GROWTHS.get());
			this.dropPottedContents(POTTED_ZESTY_CAVE_GROWTHS.get());

			this.dropSelf(SADDLED_EGG.get());
		}

		public void blockFamily(BlockFamily family) {
			this.dropSelf(family.getBaseBlock());

			if (family.getVariants().containsKey(Variant.STAIRS)) {
				this.dropSelf(family.get(Variant.STAIRS));
			}

			if (family.getVariants().containsKey(Variant.SLAB)) {
				this.add(family.get(Variant.SLAB), this::createSlabItemTable);
			}

			if (family.getVariants().containsKey(Variant.WALL)) {
				this.dropSelf(family.get(Variant.WALL));
			}

			if (family.getVariants().containsKey(Variant.CHISELED)) {
				this.dropSelf(family.get(Variant.CHISELED));
			}
		}

		protected LootTable.Builder createCoalDrops(Block block) {
			return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block.asItem(), LootItem.lootTableItem(block).apply(List.of(2, 3, 4), i -> {
				return SetItemCountFunction.setCount(ConstantValue.exactly((float) i.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CoalBlock.COAL, i)));
			}))));
		}

		protected LootTable.Builder createIngotDrops(Block block) {
			return createIngotDrops(block, block.asItem());
		}

		protected LootTable.Builder createIngotDrops(Block block, Item ingot) {
			return LootTable.lootTable()
					.withPool(LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1.0F))
							.add(this.applyExplosionDecay(ingot, LootItem.lootTableItem(ingot)
									.apply(List.of(1, 2, 3), i -> {
										return SetItemCountFunction.setCount(ConstantValue.exactly(i * 2))
												.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
														.setProperties(StatePropertiesPredicate.Builder.properties()
																.hasProperty(IngotBlock.LAYERS, i)
														)
												);
									})
							)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(IngotBlock.LAYERS, 0)).invert())
					)
					.withPool(LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1.0F))
							.add(this.applyExplosionDecay(ingot, LootItem.lootTableItem(ingot)
									.apply(List.of(IngotLayer.BOTH), layer -> {
										return SetItemCountFunction.setCount(ConstantValue.exactly(2))
												.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
														.setProperties(StatePropertiesPredicate.Builder.properties()
																.hasProperty(IngotBlock.TOP_INGOT, layer)
														)
												);
									})
							))
					)
					;
		}

		protected LootTable.Builder createToolboxDrop(Block p_124295_) {
			return LootTable.lootTable().withPool(applyExplosionCondition(p_124295_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124295_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Lock", "BlockEntityTag.Lock").copy("LootTable", "BlockEntityTag.LootTable").copy("LootTableSeed", "BlockEntityTag.LootTableSeed")).apply(SetContainerContents.setContents(CCBlockEntityTypes.TOOLBOX.get()).withEntry(DynamicLoot.dynamicEntry(ToolboxBlock.CONTENTS))))));
		}

		protected LootTable.Builder createSpinelOreDrops(Block block) {
			return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.SPINEL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		}

		protected LootTable.Builder createTurquoiseOreDrops(Block block) {
			return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(CCItems.TURQUOISE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F)))));
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

		public static final LootItemCondition.Builder HAS_PICKAXE = LootItemEntityPropertyCondition.hasProperties(EntityTarget.KILLER,
				EntityPredicate.Builder.entity().equipment(Builder.equipment()
						.mainhand(ItemPredicate.Builder.item().of(ItemTags.PICKAXES).build()).build()));

		public static final LootItemCondition.Builder HAS_SILK_TOUCH = LootItemEntityPropertyCondition.hasProperties(EntityTarget.KILLER,
				EntityPredicate.Builder.entity().equipment(Builder.equipment()
						.mainhand(ItemPredicate.Builder.item().of(ItemTags.PICKAXES)
								.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, Ints.ANY)).build()).build()));

		@Override
		public void generate() {
			this.add(COPPER_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
			this.add(OXIDIZED_COPPER_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
			this.add(DEEPER.get(), LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER)
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))
							.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
							.apply(FortuneEnchantFunction.fortuneMultiplier(UniformGenerator.between(0.0F, 1.0F)).when(HAS_PICKAXE))
							.when(HAS_SILK_TOUCH.invert())
					))
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.STONE)
							.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
							.when(HAS_SILK_TOUCH))
					)
					.withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS))
							.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			this.add(PEEPER.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			this.add(MIME.get(), LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(CCItems.SPINEL.get())
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))
									.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
							)
					)
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(CCItems.ZIRCONIA.get())
									.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
									.when(LootItemKilledByPlayerCondition.killedByPlayer())
									.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.15F, 0.05F))
							)
					)
			);
			this.add(GRAZER.get(), LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(CCItems.RAW_TIN.get())
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
									.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
							)
					)
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(CCBlocks.SADDLED_EGG.get())
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
									.when(LootItemKilledByPlayerCondition.killedByPlayer())
									.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.03F))
							)
					)
			);
			this.add(SADDLED_GRAZER.get(), LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(CCItems.RAW_TIN.get())
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
									.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
							)
					)
			);
//			this.add(FLY.get(), LootTable.lootTable());
			this.add(RAT.get(), LootTable.lootTable());
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

	private static class CCChestLoot implements LootTableSubProvider {

		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
			consumer.accept(CavernsAndChasms.location("chests/forge_dispenser"), LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(EmptyLootItem.emptyItem().setWeight(25))
							.add(LootItem.lootTableItem(Items.BUCKET).setWeight(10))
							.add(LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(15))
							.add(LootItem.lootTableItem(CCItems.GOLDEN_BUCKET.get()).setWeight(4))
							.add(LootItem.lootTableItem(CCItems.GOLDEN_LAVA_BUCKET.get()).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.GOLDEN_LAVA_BUCKET.get()).setWeight(2).apply(fluidLevelTag(1)))
							.add(LootItem.lootTableItem(CCItems.GOLDEN_LAVA_BUCKET.get()).setWeight(1).apply(fluidLevelTag(2)))
					));

			consumer.accept(CavernsAndChasms.location("chests/vault"), LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0F, 5.0F))
							.add(lootEntry(Items.MUSIC_DISC_MELLOHI, 5))
							.add(lootEntry(Items.MUSIC_DISC_WAIT, 5))
							.add(lootEntry(Items.GOLDEN_HORSE_ARMOR, 8))
							.add(lootEntry(CCItems.SILVER_HORSE_ARMOR.get(), 6))
							.add(lootEntry(Items.DIAMOND_HORSE_ARMOR, 4))
							.add(lootEntry(DEEPSLATE_TURQUOISE_ORE.get(), 1))
							.add(lootEntry(Items.CLOCK, 7))
							.add(lootEntry(CCItems.DEPTH_GAUGE.get(), 5))
							.add(lootEntry(Items.BELL, 8))
							.add(lootEntry(CCItems.GOLDEN_BUCKET.get(), 7))
					)

					.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0F, 3.0F))
							.add(lootEntry(Items.COOKIE, 3.0F, 5.0F, 15))
							.add(lootEntry(Items.GOLDEN_APPLE, 10))
							.add(lootEntry(Items.ENCHANTED_GOLDEN_APPLE, 1))
							.add(lootEntry(CCItems.BEJEWELED_APPLE.get(), 9))
					)

					.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(5.0F, 9.0F))
							.add(lootEntry(Items.PAINTING, 1.0F, 3.0F, 6))
							.add(lootEntry(Items.ENDER_PEARL, 1.0F, 3.0F, 4))
							.add(lootEntry(CCItems.BEJEWELED_PEARL.get(), 1.0F, 2.0F, 2))

							.add(lootEntry(Items.IRON_INGOT, 2.0F, 3.0F, 6))
							.add(lootEntry(Items.GOLD_INGOT, 3.0F, 4.0F, 5))
							.add(lootEntry(CCItems.SILVER_INGOT.get(), 2.0F, 4.0F, 4))
							.add(lootEntry(CCItems.TIN_INGOT.get(), 1.0F, 3.0F, 3))

							.add(lootEntry(Items.DIAMOND, 2.0F, 4.0F, 5))
							.add(lootEntry(Items.EMERALD, 1.0F, 3.0F, 6))
							.add(lootEntry(Items.LAPIS_LAZULI, 3.0F, 5.0F, 3))
							.add(lootEntry(CCItems.SPINEL.get(), 3.0F, 4.0F, 5))
							.add(lootEntry(CCItems.ZIRCONIA.get(), 1.0F, 3.0F, 4))
							.add(lootEntry(CCItems.TURQUOISE.get(), 2))
					)
			);
		}

		public static LootItemConditionalFunction.Builder<?> fluidLevelTag(int level) {
			CompoundTag tag = new CompoundTag();
			tag.putInt(GoldenBucketItem.NBT_TAG, level);
			return SetNbtFunction.setTag(tag);
		}

		public static LootPoolSingletonContainer.Builder<?> lootEntry(ItemLike item, float min, float max, int weight) {
			return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).setWeight(weight);
		}

		public static LootPoolSingletonContainer.Builder<?> lootEntry(ItemLike item, float count, int weight) {
			return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))).setWeight(weight);
		}

		public static LootPoolSingletonContainer.Builder<?> lootEntry(ItemLike item, int weight) {
			return LootItem.lootTableItem(item).setWeight(weight);
		}
	}


	public static class CCArchaeologyLoot implements LootTableSubProvider {
		public static final ResourceLocation FORGE_COMMON = CavernsAndChasms.location("archaeology/forge_common");
		public static final ResourceLocation FORGE_RARE = CavernsAndChasms.location("archaeology/forge_rare");

		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
			consumer.accept(FORGE_COMMON, LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(3))
							.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(3))
							.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.RAW_TIN.get()).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.RAW_SILVER.get()).setWeight(3))
							.add(LootItem.lootTableItem(Items.SCAFFOLDING).setWeight(5))
							.add(LootItem.lootTableItem(Items.BAMBOO).setWeight(4))
							.add(LootItem.lootTableItem(Items.BUCKET).setWeight(3))
							.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(2))
							.add(LootItem.lootTableItem(CCItems.ZIRCONIA.get()))
					));

			consumer.accept(FORGE_RARE, LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(CCItems.BOOM_POTTERY_SHERD.get()).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.CAST_POTTERY_SHERD.get()).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.RIDE_POTTERY_SHERD.get()).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.STALKER_POTTERY_SHERD.get()).setWeight(3))
							.add(LootItem.lootTableItem(CCItems.FORGER_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(2))
							.add(LootItem.lootTableItem(CCItems.IMMOLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(2))
							.add(LootItem.lootTableItem(CCItems.RIM_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(2))
							.add(LootItem.lootTableItem(CCItems.PLATE_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(2))
							.add(LootItem.lootTableItem(CCItems.CORE_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(2))
					));
		}
	}
}