package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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
		this.block(TIN_BLOCK);
		this.block(FLOAT_GLASS);

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

		this.blockFamily(COBBLESTONE_BRICKS_FAMILY);
		this.blockFamily(COBBLESTONE_TILES_FAMILY);
		this.blockFamily(MOSSY_COBBLESTONE_BRICKS_FAMILY);
		this.blockFamily(MOSSY_COBBLESTONE_TILES_FAMILY);
		this.blockFamily(COBBLED_DEEPSLATE_BRICKS_FAMILY);
		this.blockFamily(COBBLED_DEEPSLATE_TILES_FAMILY);

		this.baseBlockVariants(Blocks.CALCITE, CALCITE_STAIRS, CALCITE_SLAB, CALCITE_WALL);
		this.blockFamily(POLISHED_CALCITE_FAMILY);

		this.baseBlockVariants(Blocks.TUFF, TUFF_STAIRS, TUFF_SLAB, TUFF_WALL);
		this.blockFamily(POLISHED_TUFF_FAMILY);

		this.blockFamily(SUGILITE_FAMILY);
		this.blockFamily(POLISHED_SUGILITE_FAMILY);

		this.blockFamily(DRIPSTONE_SHINGLES_FAMILY);
		this.block(CHISELED_DRIPSTONE_SHINGLES);
		this.block(FLOODED_DRIPSTONE_SHINGLES);

		this.block(SANGUINE_BLOCK);
		this.blockFamily(SANGUINE_TILES_FAMILY);
		this.blockFamily(FORTIFIED_SANGUINE_TILES_FAMILY);

		this.block(ECHO_BLOCK);

		this.cubeBottomTopBlock(TMT);

		this.poweredRailBlock(HALT_RAIL, "rail", false, "");
		this.poweredRailBlock(SPIKED_RAIL, "spiked_rail", true, "spikes");
		this.poweredRailBlock(SLAUGHTER_RAIL, "slaughter_rail", true, "axe");

		this.copperRailBlock(COPPER_RAIL, WAXED_COPPER_RAIL);
		this.copperRailBlock(EXPOSED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL);
		this.copperRailBlock(WEATHERED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL);
		this.copperRailBlock(OXIDIZED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL);

		this.ironBarsBlock(COPPER_BARS);
		this.ironBarsBlock(EXPOSED_COPPER_BARS);
		this.ironBarsBlock(WEATHERED_COPPER_BARS);
		this.ironBarsBlock(OXIDIZED_COPPER_BARS);
		this.waxedCopperBarsBlock(WAXED_COPPER_BARS, COPPER_BARS);
		this.waxedCopperBarsBlock(WAXED_EXPOSED_COPPER_BARS, EXPOSED_COPPER_BARS);
		this.waxedCopperBarsBlock(WAXED_WEATHERED_COPPER_BARS, WEATHERED_COPPER_BARS);
		this.waxedCopperBarsBlock(WAXED_OXIDIZED_COPPER_BARS, OXIDIZED_COPPER_BARS);
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
	}

	public void caveGrowthsBlock(RegistryObject<Block> caveGrowths, RegistryObject<Block> flowerPot) {
		this.directionalBlock(caveGrowths.get(), this.models().cross(name(caveGrowths.get()), this.blockTexture(caveGrowths.get())));
		this.generatedItem(caveGrowths.get(), "block");
		this.simpleBlock(flowerPot.get(), this.models().singleTexture(name(flowerPot.get()), new ResourceLocation("block/flower_pot_cross"), "plant", new ResourceLocation(CavernsAndChasms.MOD_ID, "block/potted_" + name(caveGrowths.get()))));
	}

	public void waxedCopperBarsBlock(RegistryObject<Block> waxedCopperBars, RegistryObject<Block> copperBars) {
		this.ironBarsBlock(waxedCopperBars.get(), blockTexture(copperBars.get()));
		this.waxedGeneratedItem(waxedCopperBars.get(), "block");
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
		this.simpleBlockItem(toolbox.get(), new UncheckedModelFile(new ResourceLocation(CavernsAndChasms.MOD_ID, "item/template_toolbox")));
		this.simpleBlockItem(waxedToolbox.get(), new UncheckedModelFile(new ResourceLocation(CavernsAndChasms.MOD_ID, "item/template_toolbox")));
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
		this.waxedGeneratedItem(waxedRailBlock.get(), "block");
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
			String on = state.getValue(BlockStateProperties.POWERED) ? "_on" : "";

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

		this.waxedGeneratedItem(block, "item");
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

	public void randomRotationBlock(RegistryObject<Block> block) {
		ModelFile model = cubeAll(block.get());
		this.getVariantBuilder(block.get()).partialState().addModels(ConfiguredModel.allYRotations(model, 0, false));
		this.blockItem(block);
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

	public void waxedGeneratedItem(Block block, String type) {
		this.generatedItem(block, remove(prefix(type + "/", BlueprintItemModelProvider.key(block)), "waxed_"));
	}
}