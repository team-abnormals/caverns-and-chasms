package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
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
		this.block(DEEPSLATE_SILVER_ORE);
		this.block(DEEPSLATE_SPINEL_ORE);

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

	public void stoneBlock(Block block) {
		ModelFile model = models().cubeAll(name(block), blockTexture(block));
		ModelFile mirroredModel = models().singleTexture(name(block) + "_mirrored", mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_mirrored_all"), "all", blockTexture(block));
		this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).nextModel().modelFile(mirroredModel).nextModel().modelFile(model).rotationY(180).nextModel().modelFile(mirroredModel).rotationY(180).build());
		this.simpleBlockItem(block, model);
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

	public void baseBlockVariants(Block block, RegistryObject<Block> stairs, RegistryObject<Block> slab, RegistryObject<Block> wall) {
		this.stairsBlock(block, stairs.get());
		this.slabBlock(block, slab.get());
		this.wallBlock(block, wall.get());
	}

	public void waxedGeneratedItem(Block block, String type) {
		this.generatedItem(block, remove(prefix(type + "/", BlueprintItemModelProvider.key(block)), "waxed_"));
	}
}