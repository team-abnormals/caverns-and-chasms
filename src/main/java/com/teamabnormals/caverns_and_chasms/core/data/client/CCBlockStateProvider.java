package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.common.block.*;
import com.teamabnormals.caverns_and_chasms.common.block.RefractorBlock.RefractorState;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.Plane;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

import static com.teamabnormals.caverns_and_chasms.core.other.CCBlockFamilies.*;
import static com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks.*;

public class CCBlockStateProvider extends BlueprintBlockStateProvider {

	public CCBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		this.block(RAW_SILVER_BLOCK);
		this.block(SILVER_ORE);
		this.block(DEEPSLATE_SILVER_ORE);
		this.block(SOUL_SILVER_ORE);
		this.block(SILVER_BLOCK);
		this.weightedPressurePlateBlock(MEDIUM_WEIGHTED_PRESSURE_PLATE, SILVER_BLOCK);

		this.block(RAW_TIN_BLOCK);
		this.block(TIN_ORE);
		this.block(DEEPSLATE_TIN_ORE);
		this.block(CASSITERITE_TIN_ORE);
		this.block(TIN_BLOCK);
		this.block(FLOAT_GLASS);
		this.glassPaneBlock(FLOAT_GLASS_PANE, FLOAT_GLASS);

		this.holdPlateBlock(HOLD_PLATE, TIN_BLOCK);
		this.holdButtonBlock(TIN_BLOCK, HOLD_BUTTON);
		this.dimmerBlock(DIMMER, WALL_DIMMER);
		this.block(BOUNCER);
		this.hoopBlock(HOOP);
		this.storageDuctBlock(STORAGE_DUCT);
		this.storageDuctHatchBlock(STORAGE_DUCT_HATCH);

		this.rollerDoorBlocks(ROLLER_DOOR, ROLLER_DOOR_HEADER);

		this.blockFamilyWithChiseled(IRON_BRICKS_FAMILY);
		this.blockFamilyWithChiseled(TIN_BRICKS_FAMILY);
		this.blockFamilyWithChiseled(GOLD_BRICKS_FAMILY);
		this.blockFamilyWithChiseled(SILVER_BRICKS_FAMILY);
		this.copperBlocks(COPPER_BRICKS_FAMILY, WAXED_COPPER_BRICKS_FAMILY);
		this.copperBlocks(EXPOSED_COPPER_BRICKS_FAMILY, WAXED_EXPOSED_COPPER_BRICKS_FAMILY);
		this.copperBlocks(WEATHERED_COPPER_BRICKS_FAMILY, WAXED_WEATHERED_COPPER_BRICKS_FAMILY);
		this.copperBlocks(OXIDIZED_COPPER_BRICKS_FAMILY, WAXED_OXIDIZED_COPPER_BRICKS_FAMILY);

		this.block(SPINEL_ORE);
		this.block(DEEPSLATE_SPINEL_ORE);
		this.block(SPINEL_BLOCK);
		this.cubeColumnBlock(SPINEL_LAMP);
		this.logBlock(SPINEL_PILLAR);
		this.blockFamily(SPINEL_BRICKS_FAMILY);

		this.dismantlingTableBlock(DISMANTLING_TABLE);
		this.bejeweledAnvilBlock(BEJEWELED_ANVIL);
		this.atoningTableBlock(ATONING_TABLE);

		this.block(TURQUOISE_ORE);
		this.block(DEEPSLATE_TURQUOISE_ORE);
		this.block(TURQUOISE_BLOCK);
		this.logBlock(TURQUOISE_PILLAR);
		this.blockFamily(TURQUOISE_TILES_FAMILY);
		this.caviarBlock(CAVIAR);

		this.refractorBlock(REFRACTOR);
		this.resistorBlock(RESISTOR);

		this.block(ZIRCONIA_BLOCK);

		this.cubeColumnBlock(LAPIS_LAZULI_LAMP);
		this.logBlock(LAPIS_LAZULI_PILLAR);
		this.blockFamily(LAPIS_LAZULI_BRICKS_FAMILY);

		this.block(AMETHYST_BLOCK);
		this.block(CUT_AMETHYST);
		this.blockFamily(CUT_AMETHYST_BRICKS_FAMILY);

		this.block(NECROMIUM_BLOCK);
		this.block(ROTTEN_FLESH_BLOCK);
		this.randomRotationBlock(ROCKY_DIRT);
		this.flintBlock(FLINT_BLOCK);
		this.coalBlock(COAL);
		this.coalBlock(CHARCOAL);
		this.charcoalBlock(CHARCOAL_BLOCK);

		this.ingotBlock(COPPER_INGOT);
		this.ingotBlock(EXPOSED_COPPER_INGOT);
		this.ingotBlock(WEATHERED_COPPER_INGOT);
		this.ingotBlock(OXIDIZED_COPPER_INGOT);
		this.ingotBlock(WAXED_COPPER_INGOT);
		this.ingotBlock(WAXED_EXPOSED_COPPER_INGOT);
		this.ingotBlock(WAXED_WEATHERED_COPPER_INGOT);
		this.ingotBlock(WAXED_OXIDIZED_COPPER_INGOT);

		this.ingotBlock(IRON_INGOT);
		this.ingotBlock(GOLD_INGOT);
		this.ingotBlock(NETHERITE_INGOT);
		this.ingotBlock(SILVER_INGOT);
		this.ingotBlock(TIN_INGOT);
		this.ingotBlock(NECROMIUM_INGOT);

		this.ingotBlock(BRICK);
		this.ingotBlock(NETHER_BRICK);
		this.ingotBlock(EUMUS_BRICK);

		this.blockFamily(COBBLESTONE_BRICKS_FAMILY);
		this.blockFamily(COBBLESTONE_TILES_FAMILY);
		this.blockFamily(MOSSY_COBBLESTONE_BRICKS_FAMILY);
		this.blockFamily(MOSSY_COBBLESTONE_TILES_FAMILY);
		this.blockFamily(COBBLED_DEEPSLATE_BRICKS_FAMILY);
		this.blockFamily(COBBLED_DEEPSLATE_TILES_FAMILY);

		this.wallBlock(Blocks.STONE, STONE_WALL.get());
		this.wallBlock(Blocks.POLISHED_GRANITE, POLISHED_GRANITE_WALL.get());
		this.wallBlock(Blocks.POLISHED_DIORITE, POLISHED_DIORITE_WALL.get());
		this.wallBlock(Blocks.POLISHED_ANDESITE, POLISHED_ANDESITE_WALL.get());

		this.baseBlockVariants(Blocks.CALCITE, CALCITE_STAIRS, CALCITE_SLAB, CALCITE_WALL);
		this.cubeColumnBlock(CHISELED_CALCITE_BRICKS);
		this.blockFamily(POLISHED_CALCITE_FAMILY);
		this.blockFamily(CALCITE_BRICKS_FAMILY);
		this.blockFamily(SMOOTH_CALCITE_FAMILY);
		this.logBlock(CALCITE_PILLAR);
		this.chiseledCalciteBlock(CHISELED_CALCITE);

		this.baseBlockVariants(Blocks.TUFF, TUFF_STAIRS, TUFF_SLAB, TUFF_WALL);
		this.cubeColumnBlock(CHISELED_TUFF);
		this.cubeColumnBlock(CHISELED_TUFF_BRICKS);
		this.blockFamily(TUFF_BRICKS_FAMILY);
		this.blockFamily(SMOOTH_TUFF_FAMILY);
		this.blockFamily(POLISHED_TUFF_FAMILY);

		this.blockFamily(SUGILITE_FAMILY);
		this.blockFamily(POLISHED_SUGILITE_FAMILY);

		this.blockFamily(CASSITERITE_FAMILY);
		this.blockFamily(SMOOTH_CASSITERITE_FAMILY);
		this.blockFamilyWithChiseled(CASSITERITE_BRICKS_FAMILY);
		this.blockFamily(POLISHED_CASSITERITE_FAMILY);
		this.logBlock(CASSITERITE_PILLAR);

		this.blockFamily(RHYOLITE_FAMILY);
		this.blockFamily(POLISHED_RHYOLITE_FAMILY);
		this.blockFamilyWithChiseled(RHYOLITE_BRICKS_FAMILY);
		this.blockFamily(MAGMATIC_RHYOLITE_FAMILY);
		this.blockFamily(POLISHED_MAGMATIC_RHYOLITE_FAMILY);
		this.blockFamilyWithChiseled(MAGMATIC_RHYOLITE_BRICKS_FAMILY);

		this.baseBlockVariants(Blocks.DRIPSTONE_BLOCK, DRIPSTONE_STAIRS, DRIPSTONE_SLAB, DRIPSTONE_WALL);
		this.blockFamily(DRIPSTONE_SHINGLES_FAMILY);
		this.block(CHISELED_DRIPSTONE_SHINGLES);
		this.block(FLOODED_DRIPSTONE_SHINGLES);
		this.blockFamily(SMOOTH_DRIPSTONE_FAMILY);
		this.blockFamily(POLISHED_DRIPSTONE_FAMILY);
		this.blockFamilyWithChiseled(DRIPSTONE_BRICKS_FAMILY);
		this.block(CRACKED_DRIPSTONE_BRICKS);

		this.block(SANGUINE_BLOCK);
		this.blockFamily(SANGUINE_TILES_FAMILY);
		this.blockFamily(FORTIFIED_SANGUINE_TILES_FAMILY);

		this.block(ECHO_BLOCK);

		this.cubeBottomTopBlock(TMT);

		this.splurterBlock(SPLURTER);
		this.scattererBlock(SCATTERER);

		this.poweredRailBlock(HALT_RAIL, "rail", false, "");
		this.poweredRailBlock(SPIKED_RAIL, "spiked_rail", true, "spikes");
		this.poweredRailBlock(SLAUGHTER_RAIL, "slaughter_rail", true, "axe");

		this.copperRailBlock(COPPER_RAIL, WAXED_COPPER_RAIL);
		this.copperRailBlock(EXPOSED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL);
		this.copperRailBlock(WEATHERED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL);
		this.copperRailBlock(OXIDIZED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL);

		this.block(CHISELED_COPPER.get());
		this.block(EXPOSED_CHISELED_COPPER.get());
		this.block(WEATHERED_CHISELED_COPPER.get());
		this.block(OXIDIZED_CHISELED_COPPER.get());
		this.block(WAXED_CHISELED_COPPER.get());
		this.block(WAXED_EXPOSED_CHISELED_COPPER.get());
		this.block(WAXED_WEATHERED_CHISELED_COPPER.get());
		this.block(WAXED_OXIDIZED_CHISELED_COPPER.get());

		this.block(COPPER_GRATE.get());
		this.block(EXPOSED_COPPER_GRATE.get());
		this.block(WEATHERED_COPPER_GRATE.get());
		this.block(OXIDIZED_COPPER_GRATE.get());
		this.block(WAXED_COPPER_GRATE.get());
		this.block(WAXED_EXPOSED_COPPER_GRATE.get());
		this.block(WAXED_WEATHERED_COPPER_GRATE.get());
		this.block(WAXED_OXIDIZED_COPPER_GRATE.get());

		this.copperBulbBlock(COPPER_BULB.get());
		this.copperBulbBlock(EXPOSED_COPPER_BULB.get());
		this.copperBulbBlock(WEATHERED_COPPER_BULB.get());
		this.copperBulbBlock(OXIDIZED_COPPER_BULB.get());
		this.copperBulbBlock(WAXED_COPPER_BULB.get());
		this.copperBulbBlock(WAXED_EXPOSED_COPPER_BULB.get());
		this.copperBulbBlock(WAXED_WEATHERED_COPPER_BULB.get());
		this.copperBulbBlock(WAXED_OXIDIZED_COPPER_BULB.get());

		this.doorBlocks(COPPER_DOOR.get(), COPPER_TRAPDOOR.get());
		this.doorBlocks(EXPOSED_COPPER_DOOR.get(), EXPOSED_COPPER_TRAPDOOR.get());
		this.doorBlocks(WEATHERED_COPPER_DOOR.get(), WEATHERED_COPPER_TRAPDOOR.get());
		this.doorBlocks(OXIDIZED_COPPER_DOOR.get(), OXIDIZED_COPPER_TRAPDOOR.get());
		this.doorBlocks(WAXED_COPPER_DOOR.get(), WAXED_COPPER_TRAPDOOR.get());
		this.doorBlocks(WAXED_EXPOSED_COPPER_DOOR.get(), WAXED_EXPOSED_COPPER_TRAPDOOR.get());
		this.doorBlocks(WAXED_WEATHERED_COPPER_DOOR.get(), WAXED_WEATHERED_COPPER_TRAPDOOR.get());
		this.doorBlocks(WAXED_OXIDIZED_COPPER_DOOR.get(), WAXED_OXIDIZED_COPPER_TRAPDOOR.get());

		this.ironBarsBlock(COPPER_BARS);
		this.ironBarsBlock(EXPOSED_COPPER_BARS);
		this.ironBarsBlock(WEATHERED_COPPER_BARS);
		this.ironBarsBlock(OXIDIZED_COPPER_BARS);
		this.ironBarsBlock(WAXED_COPPER_BARS);
		this.ironBarsBlock(WAXED_EXPOSED_COPPER_BARS);
		this.ironBarsBlock(WAXED_WEATHERED_COPPER_BARS);
		this.ironBarsBlock(WAXED_OXIDIZED_COPPER_BARS);
		this.ironBarsBlock(GOLDEN_BARS);
		this.ironBarsBlock(SILVER_BARS);
		this.ironBarsBlock(TIN_BARS);

		this.toolboxBlocks(TOOLBOX, WAXED_TOOLBOX, Blocks.COPPER_BLOCK);
		this.toolboxBlocks(EXPOSED_TOOLBOX, WAXED_EXPOSED_TOOLBOX, Blocks.EXPOSED_COPPER);
		this.toolboxBlocks(WEATHERED_TOOLBOX, WAXED_WEATHERED_TOOLBOX, Blocks.WEATHERED_COPPER);
		this.toolboxBlocks(OXIDIZED_TOOLBOX, WAXED_OXIDIZED_TOOLBOX, Blocks.OXIDIZED_COPPER);

		this.buttonBlock(Blocks.COPPER_BLOCK, COPPER_BUTTON.get());
		this.buttonBlock(Blocks.EXPOSED_COPPER, EXPOSED_COPPER_BUTTON.get());
		this.buttonBlock(Blocks.WEATHERED_COPPER, WEATHERED_COPPER_BUTTON.get());
		this.buttonBlock(Blocks.OXIDIZED_COPPER, OXIDIZED_COPPER_BUTTON.get());
		this.buttonBlock(Blocks.COPPER_BLOCK, WAXED_COPPER_BUTTON.get());
		this.buttonBlock(Blocks.EXPOSED_COPPER, WAXED_EXPOSED_COPPER_BUTTON.get());
		this.buttonBlock(Blocks.WEATHERED_COPPER, WAXED_WEATHERED_COPPER_BUTTON.get());
		this.buttonBlock(Blocks.OXIDIZED_COPPER, WAXED_OXIDIZED_COPPER_BUTTON.get());

		this.floodlightBlock(FLOODLIGHT.get(), FLOODLIGHT.get());
		this.floodlightBlock(EXPOSED_FLOODLIGHT.get(), EXPOSED_FLOODLIGHT.get());
		this.floodlightBlock(WEATHERED_FLOODLIGHT.get(), WEATHERED_FLOODLIGHT.get());
		this.floodlightBlock(OXIDIZED_FLOODLIGHT.get(), OXIDIZED_FLOODLIGHT.get());
		this.floodlightBlock(FLOODLIGHT.get(), WAXED_FLOODLIGHT.get());
		this.floodlightBlock(EXPOSED_FLOODLIGHT.get(), WAXED_EXPOSED_FLOODLIGHT.get());
		this.floodlightBlock(WEATHERED_FLOODLIGHT.get(), WAXED_WEATHERED_FLOODLIGHT.get());
		this.floodlightBlock(OXIDIZED_FLOODLIGHT.get(), WAXED_OXIDIZED_FLOODLIGHT.get());

		this.copperLanternBlocks(COPPER_LANTERN.get(), WAXED_COPPER_LANTERN.get(), COPPER_CHAIN.get(), WAXED_COPPER_CHAIN.get());
		this.copperLanternBlocks(EXPOSED_COPPER_LANTERN.get(), WAXED_EXPOSED_COPPER_LANTERN.get(), EXPOSED_COPPER_CHAIN.get(), WAXED_EXPOSED_COPPER_CHAIN.get());
		this.copperLanternBlocks(WEATHERED_COPPER_LANTERN.get(), WAXED_WEATHERED_COPPER_LANTERN.get(), WEATHERED_COPPER_CHAIN.get(), WAXED_WEATHERED_COPPER_CHAIN.get());
		this.copperLanternBlocks(OXIDIZED_COPPER_LANTERN.get(), WAXED_OXIDIZED_COPPER_LANTERN.get(), OXIDIZED_COPPER_CHAIN.get(), WAXED_OXIDIZED_COPPER_CHAIN.get());

		this.lightningRodBlock(EXPOSED_LIGHTNING_ROD.get(), EXPOSED_LIGHTNING_ROD.get());
		this.lightningRodBlock(WEATHERED_LIGHTNING_ROD.get(), WEATHERED_LIGHTNING_ROD.get());
		this.lightningRodBlock(OXIDIZED_LIGHTNING_ROD.get(), OXIDIZED_LIGHTNING_ROD.get());
		this.lightningRodBlock(Blocks.LIGHTNING_ROD, WAXED_LIGHTNING_ROD.get());
		this.lightningRodBlock(EXPOSED_LIGHTNING_ROD.get(), WAXED_EXPOSED_LIGHTNING_ROD.get());
		this.lightningRodBlock(WEATHERED_LIGHTNING_ROD.get(), WAXED_WEATHERED_LIGHTNING_ROD.get());
		this.lightningRodBlock(OXIDIZED_LIGHTNING_ROD.get(), WAXED_OXIDIZED_LIGHTNING_ROD.get());

		this.stoneBlock(FRAGILE_STONE.get());
		this.deepslateBlock(FRAGILE_DEEPSLATE.get());

		this.baseBlocks(AZALEA_PLANKS, AZALEA_STAIRS, AZALEA_SLAB);
		this.logBlocks(AZALEA_LOG, AZALEA_WOOD);
		this.logBlocks(STRIPPED_AZALEA_LOG, STRIPPED_AZALEA_WOOD);
		this.fenceBlock(AZALEA_PLANKS.get(), AZALEA_FENCE.get());
		this.fenceGateBlock(AZALEA_PLANKS.get(), AZALEA_FENCE_GATE.get());
		this.doorBlocks(AZALEA_DOOR.get(), AZALEA_TRAPDOOR.get());
		this.buttonBlock(AZALEA_PLANKS.get(), AZALEA_BUTTON.get());
		this.pressurePlateBlock(AZALEA_PLANKS.get(), AZALEA_PRESSURE_PLATE.get());
		this.signBlocks(AZALEA_PLANKS.get(), AZALEA_SIGNS.getFirst().get(), AZALEA_SIGNS.getSecond().get());
		this.hangingSignBlocks(STRIPPED_AZALEA_LOG, AZALEA_HANGING_SIGNS.getFirst(), AZALEA_HANGING_SIGNS.getSecond());

		this.woodworksBlocks(AZALEA_PLANKS, AZALEA_BOARDS, AZALEA_LADDER, AZALEA_BOOKSHELF, AZALEA_BEEHIVE, AZALEA_CHEST, TRAPPED_AZALEA_CHEST);
		this.chiseledBookshelfBlock(CHISELED_AZALEA_BOOKSHELF, ALTERNATE_BOOKSHELF_POSITIONS);

		this.crossBlockWithPot(FALSE_HOPE, POTTED_FALSE_HOPE);

		this.crossBlockWithPot(MOSCHATEL, POTTED_MOSCHATEL);
		this.caveGrowthsBlock(CAVE_GROWTHS, POTTED_CAVE_GROWTHS);
		this.caveGrowthsBlock(LURID_CAVE_GROWTHS, POTTED_LURID_CAVE_GROWTHS);
		this.caveGrowthsBlock(WISPY_CAVE_GROWTHS, POTTED_WISPY_CAVE_GROWTHS);
		this.caveGrowthsBlock(GRAINY_CAVE_GROWTHS, POTTED_GRAINY_CAVE_GROWTHS);
		this.caveGrowthsBlock(WEIRD_CAVE_GROWTHS, POTTED_WEIRD_CAVE_GROWTHS);
		this.caveGrowthsBlock(ZESTY_CAVE_GROWTHS, POTTED_ZESTY_CAVE_GROWTHS);

		this.saddledEggBlock(SADDLED_EGG);
	}

	public void caveGrowthsBlock(RegistryObject<Block> caveGrowths, RegistryObject<Block> flowerPot) {
		this.directionalBlock(caveGrowths.get(), this.models().cross(name(caveGrowths.get()), this.blockTexture(caveGrowths.get())));
		this.generatedItem(caveGrowths.get(), "block");
		this.simpleBlock(flowerPot.get(), this.models().singleTexture(name(flowerPot.get()), new ResourceLocation("block/flower_pot_cross"), "plant", CavernsAndChasms.location("block/potted_" + name(caveGrowths.get()))));
	}

	public void chiseledCalciteBlock(RegistryObject<Block> chiseled) {
		Block block = chiseled.get();
		ModelFile up = this.models().cubeBottomTop(name(block) + "_up", blockTexture(block).withSuffix("_up"), blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top"));
		ModelFile down = this.models().cubeBottomTop(name(block) + "_down", blockTexture(block).withSuffix("_down"), blockTexture(block).withSuffix("_top"), blockTexture(block).withSuffix("_bottom"));
		ModelFile northEast = this.cubeBottomTopHorizontal(name(block) + "_north_east", blockTexture(block).withSuffix("_up"), blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top"));
		ModelFile southWest = this.cubeBottomTopHorizontal(name(block) + "_south_west", blockTexture(block).withSuffix("_down"), blockTexture(block).withSuffix("_top"), blockTexture(block).withSuffix("_bottom"));


		this.getVariantBuilder(block)
				.partialState().with(BlockStateProperties.FACING, Direction.UP).modelForState().modelFile(up).addModel()
				.partialState().with(BlockStateProperties.FACING, Direction.DOWN).modelForState().modelFile(down).addModel()
				.partialState().with(BlockStateProperties.FACING, Direction.NORTH).modelForState().modelFile(northEast).rotationX(90).addModel()
				.partialState().with(BlockStateProperties.FACING, Direction.EAST).modelForState().modelFile(northEast).rotationX(90).rotationY(90).addModel()
				.partialState().with(BlockStateProperties.FACING, Direction.SOUTH).modelForState().modelFile(southWest).rotationX(90).addModel()
				.partialState().with(BlockStateProperties.FACING, Direction.WEST).modelForState().modelFile(southWest).rotationX(90).rotationY(90).addModel();

		this.simpleBlockItem(block, up);
	}

	public ModelFile cubeBottomTopHorizontal(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		return this.models().withExistingParent(name, CavernsAndChasms.location("block/cube_bottom_top_horizontal"))
				.texture("side", side)
				.texture("bottom", bottom)
				.texture("top", top);
	}

	@Override
	public void ironBarsBlock(Block block, ResourceLocation texture) {
		String name = name(block);
		ResourceLocation edgeTexture = suffix(texture, "_edge");

		ModelFile post = ironBarsBlock(name, "post", texture).texture("bars", edgeTexture);
		ModelFile postEnds = ironBarsBlock(name, "post_ends", texture).texture("edge", edgeTexture);
		ModelFile side = ironBarsBlock(name, "side", texture).texture("bars", texture).texture("edge", edgeTexture);
		ModelFile sideAlt = ironBarsBlock(name, "side_alt", texture).texture("bars", texture).texture("edge", edgeTexture);
		ModelFile cap = ironBarsBlock(name, "cap", texture).texture("bars", texture).texture("edge", edgeTexture);
		ModelFile capAlt = ironBarsBlock(name, "cap_alt", texture).texture("bars", texture).texture("edge", edgeTexture);

		this.paneBlock(block, post, postEnds, side, sideAlt, cap, capAlt);
	}

	public void splurterBlock(RegistryObject<Block> block) {
		String name = name(block.get());

		this.getVariantBuilder(block.get()).forAllStatesExcept(state -> {
			Direction dir = state.getValue(BlockStateProperties.FACING);
			boolean triggered = state.getValue(BlockStateProperties.TRIGGERED);
			ResourceLocation texture = blockTexture(block.get());

			Map<Direction, Integer> yRotations = Map.of(
					Direction.NORTH, 0,
					Direction.EAST, 90,
					Direction.SOUTH, 180,
					Direction.WEST, 270
			);
			ModelFile model = models().withExistingParent(name + (triggered ? "_on" : ""), "caverns_and_chasms:block/template_splurter")
					.texture("front", texture.withSuffix(triggered ? "_front_activated" : "_front"))
					.texture("side", texture.withSuffix("_side"))
					.texture("back", texture.withSuffix("_rear"));
			ModelFile modelVertical = models().withExistingParent(name + "_vertical" + (triggered ? "_on" : ""), "caverns_and_chasms:block/template_splurter_vertical")
					.texture("front", texture.withSuffix(triggered ? "_front_vertical_activated" : "_front_vertical"))
					.texture("side", texture.withSuffix("_side"))
					.texture("back", texture.withSuffix("_rear"));
			return ConfiguredModel.builder()
					.modelFile((dir == Direction.DOWN || dir == Direction.UP) ? modelVertical : model)
					.rotationX(dir == Direction.DOWN ? 180 : 0)
					.rotationY(yRotations.getOrDefault(dir, 0))
					.build();
		}, BlockStateProperties.POWER);
		this.blockItem(block.get());
	}

	public void scattererBlock(RegistryObject<Block> block) {
		String name = name(block.get());

		this.getVariantBuilder(block.get()).forAllStatesExcept(state -> {
			Direction dir = state.getValue(BlockStateProperties.FACING);
			boolean triggered = state.getValue(BlockStateProperties.TRIGGERED);
			ResourceLocation texture = blockTexture(block.get());

			Map<Direction, Integer> yRotations = Map.of(
					Direction.NORTH, 0,
					Direction.EAST, 90,
					Direction.SOUTH, 180,
					Direction.WEST, 270
			);
			ModelFile model = models().withExistingParent(name + (triggered ? "_on" : ""), "caverns_and_chasms:block/template_scatterer")
					.texture("front", texture.withSuffix(triggered ? "_front_activated" : "_front"))
					.texture("side", texture.withSuffix(triggered ? "_side_activated" : "_side"))
					.texture("top", texture.withSuffix("_top"))
					.texture("back", texture.withSuffix("_rear"));
			ModelFile modelVertical = models().withExistingParent(name + "_vertical" + (triggered ? "_on" : ""), "caverns_and_chasms:block/template_scatterer_vertical")
					.texture("front", texture.withSuffix(triggered ? "_front_vertical_activated" : "_front_vertical"))
					.texture("side", texture.withSuffix(triggered ? "_side_activated" : "_side"))
					.texture("back", texture.withSuffix("_rear"));
			return ConfiguredModel.builder()
					.modelFile((dir == Direction.DOWN || dir == Direction.UP) ? modelVertical : model)
					.rotationX(dir == Direction.DOWN ? 180 : 0)
					.rotationY(yRotations.getOrDefault(dir, 0))
					.build();
		}, BlockStateProperties.POWER);
		this.blockItem(block.get());
	}

	public void glassPaneBlock(RegistryObject<Block> pane, RegistryObject<Block> glass) {
		Block block = pane.get();
		String name = name(block);

		ResourceLocation texture = blockTexture(glass.get());
		ResourceLocation edgeTexture = texture.withSuffix("_pane_top");

		ModelFile post = glassPaneBlock(name, "post").texture("pane", texture).texture("edge", edgeTexture);
		ModelFile side = glassPaneBlock(name, "side").texture("pane", texture).texture("edge", edgeTexture);
		ModelFile sideAlt = glassPaneBlock(name, "side_alt").texture("pane", texture).texture("edge", edgeTexture);
		ModelFile noSide = glassPaneBlock(name, "noside").texture("pane", texture);
		ModelFile noSideAlt = glassPaneBlock(name, "noside_alt").texture("pane", texture);

		this.glassPaneBlock(block, post, side, sideAlt, noSide, noSideAlt);
		this.generatedItem(block, prefix("block/", BlueprintItemModelProvider.key(glass.get())));
	}

	public void glassPaneBlock(Block block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().modelFile(post).addModel().end();
		PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
			if (dir.getAxis().isHorizontal()) {
				builder.part().modelFile(dir == Direction.SOUTH || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Axis.X ? 90 : 0).addModel().condition(value, true).end();
				builder.part().modelFile(dir == Direction.SOUTH || dir == Direction.EAST ? noSideAlt : noSide).rotationY(dir == Direction.WEST ? 270 : dir == Direction.SOUTH ? 90 : 0).addModel().condition(value, false).end();
			}
		});
	}

	public BlockModelBuilder glassPaneBlock(String name, String suffix) {
		return models().getBuilder(name + "_" + suffix).parent(new UncheckedModelFile(new ResourceLocation("block/template_glass_pane_" + suffix)));
	}

	public void deepslateBlock(Block block) {
		ModelFile model = models().cubeColumn(name(block), blockTexture(block), suffix(blockTexture(block), "_top"));
		ModelFile mirroredModel = models().withExistingParent(name(block) + "_mirrored", ModelProvider.BLOCK_FOLDER + "/cube_column_mirrored")
				.texture("side", blockTexture(block))
				.texture("end", suffix(blockTexture(block), "_top"));

		this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
				.modelFile(model).rotationX(state.getValue(BlockStateProperties.AXIS) == Axis.Y ? 0 : 90).rotationY(state.getValue(BlockStateProperties.AXIS) == Axis.X ? 90 : 0)
				.nextModel().modelFile(mirroredModel).rotationX(state.getValue(BlockStateProperties.AXIS) == Axis.Y ? 0 : 90).rotationY(state.getValue(BlockStateProperties.AXIS) == Axis.X ? 90 : 0)
				.nextModel().modelFile(model).rotationX(state.getValue(BlockStateProperties.AXIS) == Axis.Y ? 0 : 90).rotationY(state.getValue(BlockStateProperties.AXIS) == Axis.X ? 90 : 180)
				.nextModel().modelFile(mirroredModel).rotationX(state.getValue(BlockStateProperties.AXIS) == Axis.Y ? 0 : 90).rotationY(state.getValue(BlockStateProperties.AXIS) == Axis.X ? 90 : 180)
				.build()
		);

		this.simpleBlockItem(block, model);
	}

	public void toolboxBlocks(RegistryObject<Block> toolbox, RegistryObject<Block> waxedToolbox, Block copperBlock) {
		ModelFile model = this.particle(toolbox.get(), blockTexture(copperBlock));
		this.simpleBlock(toolbox.get(), model);
		this.simpleBlock(waxedToolbox.get(), model);
		this.simpleBlockItem(toolbox.get(), new UncheckedModelFile(CavernsAndChasms.location("item/template_toolbox")));
		this.simpleBlockItem(waxedToolbox.get(), new UncheckedModelFile(CavernsAndChasms.location("item/template_toolbox")));
	}

	public void stoneBlock(Block block) {
		ModelFile model = models().cubeAll(name(block), blockTexture(block));
		ModelFile mirroredModel = models().singleTexture(name(block) + "_mirrored", mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_mirrored_all"), "all", blockTexture(block));
		this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).nextModel().modelFile(mirroredModel).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(mirroredModel).rotationY(180).build());
		this.simpleBlockItem(block, model);
	}

	public void copperRailBlock(RegistryObject<Block> railBlock, RegistryObject<Block> waxedRailBlock) {
		Block block = railBlock.get();

		ModelFile rail = models().withExistingParent(name(block), "block/rail_flat").texture("rail", blockTexture(block));
		ModelFile railNE = models().withExistingParent(name(block) + "_raised_ne", "block/template_rail_raised_ne").texture("rail", blockTexture(block));
		ModelFile railSW = models().withExistingParent(name(block) + "_raised_sw", "block/template_rail_raised_sw").texture("rail", blockTexture(block));

		this.getVariantBuilder(block).forAllStatesExcept(state -> {
			RailShape shape = state.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
			return ConfiguredModel.builder().modelFile(shape.isAscending() ? (shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_EAST ? railNE : railSW) : rail).rotationY(shape == RailShape.ASCENDING_WEST || shape == RailShape.ASCENDING_EAST || shape == RailShape.EAST_WEST ? 90 : 0).build();
		}, BlockStateProperties.WATERLOGGED);

		this.getVariantBuilder(waxedRailBlock.get()).forAllStatesExcept(state -> {
			RailShape shape = state.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
			return ConfiguredModel.builder().modelFile(shape.isAscending() ? (shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_EAST ? railNE : railSW) : rail).rotationY(shape == RailShape.ASCENDING_WEST || shape == RailShape.ASCENDING_EAST || shape == RailShape.EAST_WEST ? 90 : 0).build();
		}, BlockStateProperties.WATERLOGGED);

		this.generatedItem(block, "block");
		this.generatedItem(waxedRailBlock.get(), "block");
	}

	public void poweredRailBlock(RegistryObject<Block> railBlock, String parentName, boolean extrude, String extrudeName) {
		Block block = railBlock.get();
		this.getVariantBuilder(block).forAllStatesExcept(state -> {
			RailShape shape = state.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);

			boolean isRaised = shape.isAscending();
			boolean ne = shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_EAST;
			boolean y90 = shape == RailShape.ASCENDING_WEST || shape == RailShape.EAST_WEST || shape == RailShape.ASCENDING_EAST;

			String raised = isRaised ? (ne ? "_raised_ne" : "_raised_sw") : "";
			String parent = isRaised ? "template_" + parentName + raised : parentName + "_flat";

			String on;

			if (block instanceof HaltRailBlock) {
				boolean top = state.getValue(HaltRailBlock.TOP_POWERED);
				boolean bottom = state.getValue(HaltRailBlock.BOTTOM_POWERED);
				on = top && bottom ? "_on" : top ? "_top_on" : bottom ? "_bottom_on" : "";
			} else {
				on = state.getValue(BlockStateProperties.POWERED) ? "_on" : "";
			}

			BlockModelBuilder model = models().withExistingParent(name(block) + on + raised, (parentName.equals("rail") ? "" : CavernsAndChasms.MOD_ID + ":") + "block/" + parent)
					.texture("rail", blockTexture(block) + on);

			if (extrude) {
				model.texture(extrudeName, blockTexture(block) + "_" + extrudeName + on);
			}

			return ConfiguredModel.builder().modelFile(model).rotationY(y90 ? 90 : 0).build();
		}, BlockStateProperties.WATERLOGGED);

		this.generatedItem(block, "block");
	}

	public void floodlightBlock(Block parent, Block block) {
		ModelFile rod = models().withExistingParent(name(block), CavernsAndChasms.MOD_ID + ":block/template_floodlight").texture("floodlight", blockTexture(parent));

		this.getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(rod)
							.rotationX(dir == Direction.UP ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot()) % 360)
							.build();
				}, BlockStateProperties.WATERLOGGED);

		this.generatedItem(block, "item");
	}

	public void copperLanternBlocks(Block lantern, Block waxedLantern, Block chain, Block waxedChain) {
		this.copperLanternBlock(lantern, lantern, chain, chain);
		this.copperLanternBlock(lantern, waxedLantern, chain, waxedChain);
	}

	public void copperLanternBlock(Block parent, Block block, Block chainParent, Block chainBlock) {
		ModelFile lantern = models().withExistingParent(name(block), CavernsAndChasms.MOD_ID + ":block/template_copper_lantern")
				.texture("lantern", blockTexture(parent)).renderType("cutout");

		ModelFile hangingLantern = models().withExistingParent("hanging_" + name(block), CavernsAndChasms.MOD_ID + ":block/template_hanging_copper_lantern")
				.texture("lantern", blockTexture(parent))
				.texture("chain", blockTexture(chainParent)).renderType("cutout");

		ModelFile chain = models().withExistingParent(name(chainBlock), CavernsAndChasms.MOD_ID + ":block/template_copper_chain")
				.texture("all", blockTexture(chainParent)).renderType("cutout");

		this.getVariantBuilder(block).forAllStatesExcept(state -> {
			boolean hanging = state.getValue(BlockStateProperties.HANGING);
			return ConfiguredModel.builder().modelFile(hanging ? hangingLantern : lantern).build();
		}, BlockStateProperties.WATERLOGGED);

		this.getVariantBuilder(chainBlock).forAllStatesExcept(state -> {
			Axis axis = state.getValue(BlockStateProperties.AXIS);
			return ConfiguredModel.builder().modelFile(chain).rotationX(axis.isHorizontal() ? 90 : 0).rotationY(axis == Axis.X ? 90 : 0).build();
		}, BlockStateProperties.WATERLOGGED);

		this.generatedItem(block, "item");
		this.generatedItem(chainBlock, "item");
	}

	public void copperBulbBlock(Block block) {
		this.getVariantBuilder(block)
				.forAllStates(state -> {
					boolean lit = state.getValue(CopperBulbBlock.LIT);
					boolean powered = state.getValue(CopperBulbBlock.POWERED);
					String suffix = (lit ? "_lit" : "") + (powered ? "_powered" : "");
					return ConfiguredModel.builder().modelFile(this.models().cubeAll(name(block) + suffix, blockTexture(block).withSuffix(suffix))).build();
				});

		this.blockItem(block);
	}

	public void lightningRodBlock(Block parent, Block block) {
		ModelFile rod = this.models().withExistingParent(name(block), "block/lightning_rod").texture("texture", blockTexture(parent)).texture("particle", blockTexture(parent));
		ModelFile on = new ExistingModelFile(new ResourceLocation("block/lightning_rod_on"), this.models().existingFileHelper);

		this.getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(state.getValue(LightningRodBlock.POWERED) ? on : rod)
							.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.build();
				}, BlockStateProperties.WATERLOGGED);

		this.blockItem(block);
	}

	public void holdPlateBlock(RegistryObject<Block> block, RegistryObject<Block> base) {
		ModelFile pressurePlate = models().pressurePlate(name(block.get()), blockTexture(base.get()));
		ModelFile pressurePlateDown = models().pressurePlateDown(name(block.get()) + "_down", blockTexture(base.get()));
		this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(!state.getValue(HoldPlateBlock.PRESSED) ? pressurePlate : pressurePlateDown).build());
		this.blockItem(block);
	}

	public void holdButtonBlock(RegistryObject<Block> textureBlock, RegistryObject<Block> registryObject) {
		Block block = registryObject.get();

		ResourceLocation texture = blockTexture(textureBlock.get());
		ModelFile button = models().button(name(block), texture);
		ModelFile buttonPressed = models().buttonPressed(name(block) + "_pressed", texture);
		ModelFile buttonInventoryModel = models().buttonInventory(name(block) + "_inventory", texture);

		getVariantBuilder(block).forAllStatesExcept(state -> {
			Direction facing = state.getValue(HoldButtonBlock.FACING);
			AttachFace face = state.getValue(HoldButtonBlock.FACE);
			boolean pressed = state.getValue(HoldButtonBlock.PRESSED);

			return ConfiguredModel.builder()
					.modelFile(pressed ? buttonPressed : button)
					.rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180))
					.rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot())
					.uvLock(face == AttachFace.WALL)
					.build();
		}, HoldButtonBlock.POWERED);

		this.itemModels().getBuilder(name(block)).parent(buttonInventoryModel);
	}

	public void dimmerBlock(RegistryObject<Block> registryObject, RegistryObject<Block> wall) {
		Block block = registryObject.get();
		Block wallBlock = wall.get();
		this.getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					int power = state.getValue(AbstractDimmerBlock.POWER);
					String hanging = state.getValue(DimmerBlock.HANGING) ? "_hanging" : "";
					ResourceLocation location = CavernsAndChasms.location("block/dimmer_power_" + power);
					return ConfiguredModel.builder()
							.modelFile(models().withExistingParent(name(block) + hanging + "_power_" + power, CavernsAndChasms.MOD_ID + ":block/template_dimmer" + hanging).texture("dimmer", location))
							.build();
				}, BlockStateProperties.WATERLOGGED);

		this.getVariantBuilder(wallBlock)
				.forAllStatesExcept(state -> {
					int power = state.getValue(AbstractDimmerBlock.POWER);
					ResourceLocation location = CavernsAndChasms.location("block/dimmer_power_" + power);
					return ConfiguredModel.builder()
							.modelFile(models().withExistingParent(name(wallBlock) + "_power_" + power, CavernsAndChasms.MOD_ID + ":block/template_dimmer_wall").texture("dimmer", location))
							.rotationY((int) state.getValue(WallDimmerBlock.FACING).getOpposite().toYRot())
							.build();
				}, BlockStateProperties.WATERLOGGED);

		this.generatedItem(block, "item");
	}

	public void hoopBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		this.getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					ResourceLocation location = CavernsAndChasms.location("block/hoop_size_" + state.getValue(HoopBlock.SIZE) + (state.getValue(HoopBlock.OUTPUT_POWER) > 0 ? "_activated" : ""));
					Axis axis = state.getValue(HoopBlock.AXIS);
					return ConfiguredModel.builder()
							.modelFile(models().withExistingParent(location.getPath(), CavernsAndChasms.MOD_ID + ":block/template_hoop_size_" + state.getValue(HoopBlock.SIZE)).texture("hoop", location))
							.rotationX(axis.isHorizontal() ? 90 : 0)
							.rotationY(axis == Axis.X ? 90 : 0)
							.build();
				}, BlockStateProperties.WATERLOGGED);
		this.simpleBlockItem(block, new ExistingModelFile(CavernsAndChasms.location("block/hoop_size_3"), this.models().existingFileHelper));
	}

	public void storageDuctBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		this.getVariantBuilder(block)
				.forAllStates(state -> {
					Direction firstend = state.getValue(StorageDuctBlock.FIRST_END);
					Direction secondend = state.getValue(StorageDuctBlock.SECOND_END);

					if (firstend != secondend) {
						Direction end1 = firstend;
						Direction end2 = secondend;

						boolean swap = false;

						if (firstend == Direction.SOUTH && secondend == Direction.NORTH)
							swap = true;
						else if (firstend == Direction.EAST && (secondend == Direction.NORTH || secondend == Direction.SOUTH))
							swap = true;
						else if (firstend == Direction.WEST && secondend != Direction.UP && secondend != Direction.DOWN)
							swap = true;
						else if (firstend == Direction.UP && secondend != Direction.DOWN)
							swap = true;
						else if (firstend == Direction.DOWN)
							swap = true;

						if (swap) {
							end1 = secondend;
							end2 = firstend;
						}

						return ConfiguredModel.builder()
								.modelFile(models().getExistingFile(CavernsAndChasms.location("block/storage_duct_" + end1 + "_" + end2)))
								.build();
					} else {
						return ConfiguredModel.builder()
								.modelFile(models().getExistingFile(CavernsAndChasms.location("block/storage_duct_invalid")))
								.build();
					}
				});
		this.simpleBlockItem(block, models().getExistingFile(CavernsAndChasms.location("block/storage_duct_up_down")));
	}

	public void storageDuctHatchBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		ResourceLocation texture = suffix(blockTexture(block), "_");
		this.getVariantBuilder(block).forAllStatesExcept(state -> {
			Direction facing = state.getValue(StorageDuctHatchBlock.FACING);
			RelativeDirection direction = state.getValue(StorageDuctHatchBlock.HANDLE);
			boolean open = state.getValue(StorageDuctHatchBlock.OPEN);

			String directionsuffix = "_open";
			if (!open) {
				if (facing.getAxis() != Axis.Y)
					directionsuffix = "_" + direction.getSerializedName();
				else
					directionsuffix = "_down";
			}

			ModelFile model = this.models()
					.withExistingParent(name(block) + directionsuffix, CavernsAndChasms.MOD_ID + ":block/template_storage_duct_hatch")
					.texture("front", suffix(texture, "front" + directionsuffix))
					.texture("side", suffix(texture, "side"))
					.texture("back", suffix(texture, "back"));
			return ConfiguredModel.builder()
					.modelFile(model)
					.rotationX(facing == Direction.DOWN ? 90 : facing == Direction.UP ? 270 : 0)
					.rotationY(facing.getAxis() != Axis.Y ? (int) facing.getOpposite().toYRot() : ((int) direction.getCardinalDirection(Direction.DOWN).getOpposite().toYRot() + (facing == Direction.UP ? 0 : 180)) % 360)
					.build();
		}, BlockStateProperties.WATERLOGGED);
		this.simpleBlockItem(block, models().getExistingFile(CavernsAndChasms.location(name(block) + "_down")));
	}

	public void rollerDoorBlocks(RegistryObject<Block> rollerDoor, RegistryObject<Block> header) {
		ModelFile model = this.models().getBuilder(name(rollerDoor.get())).texture("particle", CavernsAndChasms.location("block/roller_door_particle"));
		this.simpleBlock(rollerDoor.get(), model);
		this.simpleBlock(header.get(), model);
	}

	public void dismantlingTableBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		ResourceLocation texture = suffix(blockTexture(block), "_");
		this.simpleBlock(registryObject.get(), models()
				.cube(name(block),
						suffix(texture, "bottom"),
						suffix(texture, "top"),
						suffix(texture, "front"),
						suffix(texture, "front"),
						suffix(texture, "side"),
						suffix(texture, "side")
				).texture("particle", suffix(texture, "front")));
		this.blockItem(block);
	}

	public void bejeweledAnvilBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		ResourceLocation texture = suffix(blockTexture(block), "_");
		this.horizontalBlock(registryObject.get(), this.models()
				.withExistingParent(name(block), CavernsAndChasms.MOD_ID + ":block/template_bejeweled_anvil")
				.texture("front", suffix(texture, "front"))
				.texture("side", suffix(texture, "side"))
				.texture("top", suffix(texture, "top"))
				.texture("bottom", suffix(texture, "bottom"))
				.texture("base_top", suffix(texture, "base_top"))
				.texture("base_bottom", suffix(texture, "base_bottom"))
		);
		this.blockItem(block);
	}

	public void atoningTableBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		ResourceLocation texture = suffix(blockTexture(block), "_");
		this.simpleBlock(registryObject.get(), this.models()
				.withExistingParent(name(block), "block/enchanting_table")
				.texture("side", suffix(texture, "side"))
				.texture("top", suffix(texture, "top"))
				.texture("bottom", suffix(texture, "bottom"))
				.texture("particle", suffix(texture, "bottom"))
		);
		this.blockItem(block);
	}

	public void caviarBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		ResourceLocation texture = suffix(blockTexture(block), "_");
		this.simpleBlock(registryObject.get(), this.models()
				.withExistingParent(name(block), CavernsAndChasms.MOD_ID + ":block/template_caviar")
				.texture("side", suffix(texture, "side"))
				.texture("top", suffix(texture, "top"))
				.texture("bottom", suffix(texture, "bottom"))
		);
		this.generatedItem(block, "item");
	}

	public void resistorBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();

		ExistingModelFile model = this.models().getExistingFile(CavernsAndChasms.location("block/resistor"));
		ExistingModelFile modelOn = this.models().getExistingFile(CavernsAndChasms.location("block/resistor_on"));

		MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block);
		for (Direction direction : Plane.HORIZONTAL) {
			int rotation = (int) (direction.toYRot() + 180) % 360;
			builder.part().modelFile(model).rotationY(rotation).addModel().condition(HorizontalDirectionalBlock.FACING, direction).condition(ResistorBlock.POWERED, false);
			builder.part().modelFile(modelOn).rotationY(rotation).addModel().condition(HorizontalDirectionalBlock.FACING, direction).condition(ResistorBlock.POWERED, true);

			for (int i = 1; i < 15; i++) {
				builder.part().modelFile(this.models().getExistingFile(CavernsAndChasms.location("block/resistor_button_" + i))).rotationY(rotation).addModel()
						.condition(HorizontalDirectionalBlock.FACING, direction).condition(ResistorBlock.RESISTANCE, i);
			}
		}

		this.generatedItem(block, "item");
	}

	public void refractorBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();

		MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block);
		for (Direction direction : Plane.HORIZONTAL) {
			int rotation = (int) (direction.toYRot() + 180) % 360;

			for (RefractorState state : RefractorState.values()) {
				ExistingModelFile model = this.models().getExistingFile(CavernsAndChasms.location("block/refractor" + getStringForState(state)));
				builder.part().modelFile(model).rotationY(rotation).addModel().condition(HorizontalDirectionalBlock.FACING, direction).condition(RefractorBlock.POWERED, state);
			}

			for (Direction torchDirection : Plane.HORIZONTAL) {
				if (torchDirection != Direction.NORTH) {
					for (int i = 0; i < 4; i++) {
						int torchRotation = (int) (rotation + torchDirection.toYRot() + 180) % 360;
						IntegerProperty intProp = getPropertyForDirection(torchDirection);
						if (i == 0) {
							ExistingModelFile lock = this.models().getExistingFile(CavernsAndChasms.location("block/refractor_lock"));
							builder.part().modelFile(lock).rotationY(torchRotation).addModel().condition(HorizontalDirectionalBlock.FACING, direction).condition(intProp, i);
						} else {
							String weight = getStringForWeight(i);
							ExistingModelFile torch = this.models().getExistingFile(CavernsAndChasms.location("block/refractor_torch" + weight));
							ExistingModelFile torchOn = this.models().getExistingFile(CavernsAndChasms.location("block/refractor_torch" + weight + "_on"));

							builder.part().modelFile(torch).rotationY(torchRotation).addModel().condition(HorizontalDirectionalBlock.FACING, direction).condition(intProp, i).condition(RefractorBlock.POWERED, getStatesForDirection(torchDirection));
							builder.part().modelFile(torchOn).rotationY(torchRotation).addModel().condition(HorizontalDirectionalBlock.FACING, direction).condition(intProp, i).condition(RefractorBlock.POWERED, getUnpoweredState(torchDirection));
						}
					}
				}
			}
		}

		this.generatedItem(block, "item");
	}

	public void saddledEggBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();
		this.horizontalBlock(block, new ModelFile.UncheckedModelFile(CavernsAndChasms.location("block/saddled_egg")));
		this.generatedItem(block, "item");
	}

	public static String getStringForState(RefractorState state) {
		return state == RefractorState.OFF ? "" : ((state == RefractorState.NONE ? "" : "_" + state.getSerializedName()) + "_on");
	}

	public static String getStringForWeight(int weight) {
		return weight == 1 ? "_left" : weight == 2 ? "_center" : weight == 3 ? "_right" : "";
	}

	public static IntegerProperty getPropertyForDirection(Direction direction) {
		return switch (direction) {
			case EAST -> RefractorBlock.LEFT;
			default -> RefractorBlock.CENTER;
			case WEST -> RefractorBlock.RIGHT;
		};
	}

	public static RefractorState[] getStatesForDirection(Direction direction) {
		return switch (direction) {
			case EAST -> new RefractorState[]{RefractorState.OFF, RefractorState.NONE, RefractorState.CENTER, RefractorState.RIGHT};
			default -> new RefractorState[]{RefractorState.OFF, RefractorState.NONE, RefractorState.LEFT, RefractorState.RIGHT};
			case WEST -> new RefractorState[]{RefractorState.OFF, RefractorState.NONE, RefractorState.LEFT, RefractorState.CENTER};
		};
	}

	public static RefractorState getUnpoweredState(Direction direction) {
		return switch (direction) {
			case EAST -> RefractorState.LEFT;
			default -> RefractorState.CENTER;
			case WEST -> RefractorState.RIGHT;
		};
	}

	public void randomRotationBlock(RegistryObject<Block> block) {
		ModelFile model = cubeAll(block.get());
		this.getVariantBuilder(block.get()).partialState().addModels(ConfiguredModel.allYRotations(model, 0, false));
		this.blockItem(block);
	}

	public void randomRotationBlockBothAxis(RegistryObject<Block> block) {
		ModelFile model = cubeAll(block.get());
		this.getVariantBuilder(block.get()).partialState().addModels(ConfiguredModel.allRotations(model, false));
		this.blockItem(block);
	}

	@Override
	public void slabBlock(Block block, Block slab) {
		if (slab instanceof SlabBlock slabBlock && (slabBlock == POLISHED_CALCITE_SLAB.get() || slabBlock == POLISHED_TUFF_SLAB.get() || slabBlock == SMOOTH_TUFF_SLAB.get() || slabBlock == POLISHED_SUGILITE_SLAB.get() || slabBlock == POLISHED_CASSITERITE_SLAB.get() || slabBlock == POLISHED_DRIPSTONE_SLAB.get() || slabBlock == POLISHED_RHYOLITE_SLAB.get() || slabBlock == POLISHED_MAGMATIC_RHYOLITE_SLAB.get())) {
			ResourceLocation side = blockTexture(slab);
			ResourceLocation full = blockTexture(block);
			this.slabBlock(slabBlock, models().slab(name(slab), side, full, full), models().slabTop(name(slab) + "_top", side, full, full), models().cubeColumn(name(slab) + "_double", side, full));
			this.blockItem(slab);
		} else {
			super.slabBlock(block, slab);
		}
	}

	public void flintBlock(RegistryObject<Block> block) {
		ModelFile model = cubeAll(block.get());
		ModelFile litModel = models().cubeAll("flint_block_lit", modLoc("block/flint_block_lit"));

		this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.allRotations(state.getValue(FlintBlock.LIT) ? litModel : model, false));
		this.blockItem(block);
	}

	public void charcoalBlock(RegistryObject<Block> registryObject) {
		RotatedPillarBlock block = (RotatedPillarBlock) registryObject.get();

		ModelFile vertical = models().cubeColumn(name(block), blockTexture(block), blockTexture(block).withSuffix("_top"));
		ModelFile horizontal = models().cubeColumnHorizontal(name(block) + "_horizontal", blockTexture(block), blockTexture(block).withSuffix("_top"));
		ModelFile verticalLit = models().cubeColumn(name(block) + "_lit", blockTexture(block).withSuffix("_lit"), blockTexture(block).withSuffix("_top_lit"));
		ModelFile horizontalLit = models().cubeColumnHorizontal(name(block) + "_horizontal_lit", blockTexture(block).withSuffix("_lit"), blockTexture(block).withSuffix("_top_lit"));

		this.getVariantBuilder(block)
				.partialState().with(CharcoalBlock.LIT, false).with(RotatedPillarBlock.AXIS, Axis.Y).modelForState().modelFile(vertical).addModel()
				.partialState().with(CharcoalBlock.LIT, false).with(RotatedPillarBlock.AXIS, Axis.Z).modelForState().modelFile(horizontal).rotationX(90).addModel()
				.partialState().with(CharcoalBlock.LIT, false).with(RotatedPillarBlock.AXIS, Axis.X).modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel()
				.partialState().with(CharcoalBlock.LIT, true).with(RotatedPillarBlock.AXIS, Axis.Y).modelForState().modelFile(verticalLit).addModel()
				.partialState().with(CharcoalBlock.LIT, true).with(RotatedPillarBlock.AXIS, Axis.Z).modelForState().modelFile(horizontalLit).rotationX(90).addModel()
				.partialState().with(CharcoalBlock.LIT, true).with(RotatedPillarBlock.AXIS, Axis.X).modelForState().modelFile(horizontalLit).rotationX(90).rotationY(90).addModel();

		this.blockItem(registryObject);
	}

	public void coalBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();

		this.getVariantBuilder(block).forAllStatesExcept(state -> {
			String count = switch (state.getValue(CoalBlock.COAL)) {
				case 1 -> "_one";
				case 2 -> "_two";
				case 3 -> "_three";
				default -> "_four";
			};

			boolean isLit = state.getValue(CoalBlock.LIT);
			String lit = isLit ? "_lit" : "";
			String name = name(block) + count + lit;
			BlockModelBuilder model = models().withExistingParent(name, CavernsAndChasms.location("block/template_" + name))
					.texture("coal", blockTexture(block).withSuffix(lit));
			if (isLit) {
				model.texture("fire", blockTexture(block).withSuffix("_fire"));
			}
			return ConfiguredModel.builder()
					.modelFile(model).nextModel()
					.modelFile(model).rotationY(90).nextModel()
					.modelFile(model).rotationY(180).nextModel()
					.modelFile(model).rotationY(270)
					.build();
		}, CoalBlock.WATERLOGGED);

		this.placedItemModel(block);
	}

	public void ingotBlock(RegistryObject<Block> registryObject) {
		Block block = registryObject.get();

		MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block);
		this.addIngotLayer(builder, block, 1, 1, 2, 3);
		this.addIngotLayer(builder, block, 2, 2, 3);
		this.addIngotLayer(builder, block, 3, 3);
		this.addIngotLayer(builder, block, 4);

		this.placedItemModel(block);
	}

	public void placedItemModel(Block block) {
		this.itemModels().withExistingParent(ForgeRegistries.BLOCKS.getKey(block).withSuffix("_placed").getPath(), "item/generated").texture("layer0", ForgeRegistries.ITEMS.getKey(Items.BARRIER).withPrefix("item/"));
	}

	public void addIngotLayer(MultiPartBlockStateBuilder builder, Block block, int i, Integer... nums) {
		this.addIngotModel(builder, block, IngotLayer.LEFT, Axis.X, i, nums);
		this.addIngotModel(builder, block, IngotLayer.RIGHT, Axis.X, i, nums);
		this.addIngotModel(builder, block, IngotLayer.LEFT, Axis.Z, i, nums);
		this.addIngotModel(builder, block, IngotLayer.RIGHT, Axis.Z, i, nums);
	}

	public void addIngotModel(MultiPartBlockStateBuilder builder, Block block, IngotLayer ingotLayer, Axis axis, int layer, Integer... nums) {
		Axis visualAxis = IngotBlock.getAxisForLayer(layer, axis);
		String name = "_" + ingotLayer.getSerializedName() + "_" + visualAxis.getSerializedName() + "_layer" + layer;
		BlockModelBuilder model = models().withExistingParent(name(block) + name, CavernsAndChasms.location("block/template_ingot" + name)).texture("ingot", blockTexture(block).toString().replace("waxed_", ""));

		if (nums.length > 0) {
			builder.part().modelFile(model).addModel().useOr()
					.nestedGroup().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, layer - 1).condition(IngotBlock.TOP_INGOT, ingotLayer, IngotLayer.BOTH).end()
					.nestedGroup().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, nums).end();
		} else {
			builder.part().modelFile(model).addModel()
					.condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, layer - 1).condition(IngotBlock.TOP_INGOT, ingotLayer, IngotLayer.BOTH);
		}
	}

	public void baseBlockVariants(Block block, RegistryObject<Block> stairs, RegistryObject<Block> slab, RegistryObject<Block> wall) {
		this.stairsBlock(block, stairs.get());
		this.slabBlock(block, slab.get());
		this.wallBlock(block, wall.get());
	}

	public void weightedPressurePlateBlock(RegistryObject<Block> block, RegistryObject<Block> base) {
		ModelFile pressurePlate = models().pressurePlate(name(block.get()), blockTexture(base.get()));
		ModelFile pressurePlateDown = models().pressurePlateDown(name(block.get()) + "_down", blockTexture(base.get()));
		this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(WeightedPressurePlateBlock.POWER) == 0 ? pressurePlate : pressurePlateDown).build());
		this.blockItem(block);
	}

	public void blockFamilyWithChiseled(BlockFamily family) {
		this.blockFamily(family);
		this.block(family.get(Variant.CHISELED));
	}

	public void block(Block base, Block block) {
		this.simpleBlock(block, this.models().cubeAll(name(block), blockTexture(base)));
		this.blockItem(block);
	}

	public void copperBlocks(BlockFamily base, BlockFamily waxed) {
		this.blockFamilyWithChiseled(base);

		this.block(base.getBaseBlock(), waxed.getBaseBlock());
		this.stairsBlock(base.getBaseBlock(), waxed.get(Variant.STAIRS));
		this.slabBlock(base.getBaseBlock(), waxed.get(Variant.SLAB));
		this.wallBlock(base.getBaseBlock(), waxed.get(Variant.WALL));
		this.block(base.get(Variant.CHISELED), waxed.get(Variant.CHISELED));
	}

	@Override
	public void generatedItem(ItemLike item, ItemLike texture, String type) {
		this.generatedItem(item, remove(prefix(type + "/", BlueprintItemModelProvider.key(texture)), "waxed_"));
	}

	@Override
	public ResourceLocation blockTexture(Block block) {
		ResourceLocation name = remove(ForgeRegistries.BLOCKS.getKey(block), "waxed_");
		return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
	}

	@Override
	public void blockItem(Block block) {
		this.simpleBlockItem(block, new ExistingModelFile(blockModel(block), this.models().existingFileHelper));
	}

	public ResourceLocation blockModel(Block block) {
		ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
		return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
	}
}