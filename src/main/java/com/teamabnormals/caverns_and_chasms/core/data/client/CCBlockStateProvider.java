package com.teamabnormals.caverns_and_chasms.core.data.client;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.block.VerticalSlabBlock;
import com.teamabnormals.blueprint.common.block.VerticalSlabBlock.VerticalSlabType;
import com.teamabnormals.blueprint.common.block.chest.BlueprintChestBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintTrappedChestBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintStandingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallSignBlock;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;


public class CCBlockStateProvider extends BlockStateProvider {

	public CCBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CavernsAndChasms.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		this.simpleBlockWithItem(CCBlocks.RAW_SILVER_BLOCK.get());
		this.simpleBlockWithItem(CCBlocks.DEEPSLATE_SILVER_ORE.get());
		this.simpleBlockWithItem(CCBlocks.DEEPSLATE_SPINEL_ORE.get());

		this.registerBars(CCBlocks.COPPER_BARS.get());
		this.registerBars(CCBlocks.EXPOSED_COPPER_BARS.get());
		this.registerBars(CCBlocks.WEATHERED_COPPER_BARS.get());
		this.registerBars(CCBlocks.OXIDIZED_COPPER_BARS.get());
		this.registerBars(CCBlocks.WAXED_COPPER_BARS.get());
		this.registerBars(CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		this.registerBars(CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		this.registerBars(CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());
		this.registerBars(CCBlocks.GOLDEN_BARS.get());
		this.registerBars(CCBlocks.SILVER_BARS.get());

		this.simpleBlockWithItem(CCBlocks.FRAGILE_STONE.get());

		this.registerBlockWithVariants(CCBlocks.AZALEA_PLANKS.get(), CCBlocks.AZALEA_STAIRS.get(), CCBlocks.AZALEA_SLAB.get(), CCBlocks.AZALEA_VERTICAL_SLAB.get());
		this.registerLogBlocks(CCBlocks.AZALEA_LOG.get(), CCBlocks.AZALEA_WOOD.get());
		this.registerLogBlocks(CCBlocks.STRIPPED_AZALEA_LOG.get(), CCBlocks.STRIPPED_AZALEA_WOOD.get());
		this.registerFenceBlocks(CCBlocks.AZALEA_PLANKS.get(), CCBlocks.AZALEA_FENCE.get(), CCBlocks.AZALEA_FENCE_GATE.get());
		this.registerDoorBlocks(CCBlocks.AZALEA_DOOR.get(), CCBlocks.AZALEA_TRAPDOOR.get());
		this.registerCompatBlocks(CCBlocks.AZALEA_PLANKS.get(), CCBlocks.VERTICAL_AZALEA_PLANKS.get(), CCBlocks.AZALEA_LADDER.get(), CCBlocks.AZALEA_BOOKSHELF.get(), CCBlocks.AZALEA_BEEHIVE.get());
		this.registerSignBlocks(CCBlocks.AZALEA_PLANKS.get(), CCBlocks.AZALEA_SIGN);
		this.registerChestBlocks(CCBlocks.AZALEA_PLANKS.get(), CCBlocks.AZALEA_CHEST);
		this.registerLeafCarpet(Blocks.AZALEA_LEAVES, CCBlocks.AZALEA_LEAF_CARPET.get());
		this.registerLeafCarpet(Blocks.FLOWERING_AZALEA_LEAVES, CCBlocks.FLOWERING_AZALEA_LEAF_CARPET.get());
		this.registerButtons(CCBlocks.AZALEA_PLANKS.get(), CCBlocks.AZALEA_BUTTON.get(), CCBlocks.AZALEA_PRESSURE_PLATE.get());
		this.registerHedge(Blocks.AZALEA_LEAVES, CCBlocks.AZALEA_LOG.get(), CCBlocks.AZALEA_HEDGE.get());
		this.registerHedge(Blocks.FLOWERING_AZALEA_LEAVES, CCBlocks.AZALEA_LOG.get(), CCBlocks.FLOWERING_AZALEA_HEDGE.get());
		this.registerPost(CCBlocks.AZALEA_LOG.get(), CCBlocks.AZALEA_POST.get());
		this.registerPost(CCBlocks.STRIPPED_AZALEA_LOG.get(), CCBlocks.STRIPPED_AZALEA_POST.get());
	}

	public void simpleBlockWithItem(Block block) {
		ModelFile model = cubeAll(block);
		simpleBlock(block, model);
		simpleBlockItem(block, model);
	}

	public void registerItemModel(Block block) {
		this.simpleBlockItem(block, new ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
	}

	private void registerGeneratedItemModel(ItemLike item, String type) {
		ResourceLocation itemName = item.asItem().getRegistryName();
		itemModels().withExistingParent(itemName.getPath(), "item/generated").texture("layer0", new ResourceLocation(itemName.getNamespace(), type + "/" + itemName.getPath().replace("waxed_", "")));
	}

	public void registerFenceBlocks(Block block, Block fence, Block fenceGate) {
		this.fenceBlock((FenceBlock) fence, blockTexture(block));
		this.itemModels().getBuilder(name(fence)).parent(this.models().fenceInventory(name(fence) + "_inventory", blockTexture(block)));
		this.fenceGateBlock((FenceGateBlock) fenceGate, blockTexture(block));
		this.registerItemModel(fenceGate);
	}

	public void registerCompatBlocks(Block block, Block verticalPlanks, Block ladder, Block bookshelf, Block beehive) {
		this.horizontalBlock(ladder, models().withExistingParent(name(ladder), "block/ladder").texture("particle", blockTexture(ladder)).texture("texture", blockTexture(ladder)));
		this.registerGeneratedItemModel(ladder, "block");
		this.simpleBlock(bookshelf, this.models().cubeColumn(name(bookshelf), blockTexture(bookshelf), blockTexture(block)));
		this.registerItemModel(bookshelf);
		this.registerBeehive(beehive);
		this.simpleBlock(verticalPlanks, models().getBuilder(name(verticalPlanks)).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/vertical_planks"))).texture("all", blockTexture(block)));
		this.registerItemModel(verticalPlanks);
	}

	public void registerLeaves(Block leaves, Block leafCarpet) {
		this.simpleBlock(leaves, models().withExistingParent(name(leaves), "block/leaves").texture("all", blockTexture(leaves)));
		this.simpleBlock(leafCarpet, models().getBuilder(name(leafCarpet)).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/leaf_carpet"))).texture("all", blockTexture(leaves)));
		this.registerItemModel(leaves);
		this.registerItemModel(leafCarpet);
	}

	public void registerLeafCarpet(Block leaves, Block leafCarpet) {
		this.simpleBlock(leafCarpet, models().getBuilder(name(leafCarpet)).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/leaf_carpet"))).texture("all", blockTexture(leaves)));
		this.registerItemModel(leafCarpet);
	}

	public void registerBeehive(Block block) {
		ModelFile beehive = models().orientableWithBottom(name(block), suffix(blockTexture(block), "_side"), suffix(blockTexture(block), "_front"), suffix(blockTexture(block), "_end"), suffix(blockTexture(block), "_end")).texture("particle", suffix(blockTexture(block), "_side"));
		ModelFile beehiveHoney = models().orientableWithBottom(name(block) + "_honey", suffix(blockTexture(block), "_side"), suffix(blockTexture(block), "_front_honey"), suffix(blockTexture(block), "_end"), suffix(blockTexture(block), "_end")).texture("particle", suffix(blockTexture(block), "_side"));
		this.horizontalBlock(block, (state -> state.getValue(BlockStateProperties.LEVEL_HONEY) == 5 ? beehiveHoney : beehive));
		this.registerItemModel(block);
	}

	public void registerButtons(Block block, Block buttonBlock, Block pressurePlateBlock) {
		ResourceLocation texture = blockTexture(block);

		ModelFile button = models().withExistingParent(name(buttonBlock), "block/button").texture("texture", texture);
		ModelFile buttonPressed = models().withExistingParent(name(buttonBlock) + "_pressed", "block/button_pressed").texture("texture", texture);
		ModelFile buttonInventory = models().withExistingParent(name(buttonBlock) + "_inventory", "block/button_inventory").texture("texture", texture);
		this.buttonBlock(buttonBlock, (state -> state.getValue(BlockStateProperties.POWERED) ? buttonPressed : button));
		this.itemModels().getBuilder(name(buttonBlock)).parent(buttonInventory);

		ModelFile pressurePlate = models().withExistingParent(name(pressurePlateBlock), "block/pressure_plate_up").texture("texture", texture);
		ModelFile pressurePlateDown = models().withExistingParent(name(pressurePlateBlock) + "_down", "block/pressure_plate_down").texture("texture", texture);
		this.pressurePlateBlock(pressurePlateBlock, (state -> state.getValue(BlockStateProperties.POWERED) ? pressurePlateDown : pressurePlate));
		this.registerItemModel(pressurePlateBlock);
	}

	public void buttonBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
		this.getVariantBuilder(block)
				.forAllStates(state -> ConfiguredModel.builder()
						.modelFile(modelFunc.apply(state))
						.uvLock(state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.WALL)
						.rotationX(state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.WALL ? 90 : state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? 180 : 0)
						.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + (state.getValue(BlockStateProperties.ATTACH_FACE) != AttachFace.CEILING ? 180 : 0)) % 360)
						.build()
				);
	}

	public void pressurePlateBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
		this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(modelFunc.apply(state)).build());
	}

	public void registerBlockWithVariants(Block block, Block stairs, Block slab, Block verticalSlab) {
		this.simpleBlock(block);
		this.stairsBlock((StairBlock) stairs, blockTexture(block));
		this.slabBlock((SlabBlock) slab, blockTexture(block), blockTexture(block));
		this.verticalSlabBlock(block, (VerticalSlabBlock) verticalSlab);

		this.registerItemModel(block);
		this.registerItemModel(stairs);
		this.registerItemModel(slab);
		this.registerItemModel(verticalSlab);
	}

	public void registerDoorBlocks(Block door, Block trapdoor) {
		this.doorBlock((DoorBlock) door, suffix(blockTexture(door), "_bottom"), suffix(blockTexture(door), "_top"));
		this.registerGeneratedItemModel(door, "item");
		this.trapdoorBlock((TrapDoorBlock) trapdoor, blockTexture(trapdoor), true);
		this.itemModels().getBuilder(name(trapdoor)).parent(this.models().trapdoorOrientableBottom(name(trapdoor) + "_bottom", blockTexture(trapdoor)));
	}

	public void registerBars(Block bars) {
		this.registerGeneratedItemModel(bars, "block");
		this.ironBarsBlock(bars);
	}

	private void ironBarsBlock(Block block) {
		String name = name(block);
		ResourceLocation barsTexture = prefix("block/", new ResourceLocation(block.getRegistryName().getNamespace(), name(block).replace("waxed_", "")));
		ResourceLocation edgeTexture = suffix(barsTexture, "_edge");

		ModelFile post = barsBlock(name, "post", barsTexture).texture("bars", edgeTexture);
		ModelFile postEnds = barsBlock(name, "post_ends", barsTexture).texture("edge", edgeTexture);
		ModelFile side = barsBlock(name, "side", barsTexture).texture("bars", barsTexture).texture("edge", edgeTexture);
		ModelFile sideAlt = barsBlock(name, "side_alt", barsTexture).texture("bars", barsTexture).texture("edge", edgeTexture);
		ModelFile cap = barsBlock(name, "cap", barsTexture).texture("bars", barsTexture).texture("edge", edgeTexture);
		ModelFile capAlt = barsBlock(name, "cap_alt", barsTexture).texture("bars", barsTexture).texture("edge", edgeTexture);

		paneBlock(block, post, postEnds, side, sideAlt, cap, capAlt);
	}

	private BlockModelBuilder barsBlock(String name, String suffix, ResourceLocation barsTexture) {
		return models().getBuilder(name + "_" + suffix).parent(new UncheckedModelFile(new ResourceLocation("block/iron_bars_" + suffix))).texture("particle", barsTexture);
	}

	public void paneBlock(Block block, ModelFile post, ModelFile postEnds, ModelFile side, ModelFile sideAlt, ModelFile cap, ModelFile capAlt) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().modelFile(postEnds).addModel().end();
		builder.part().modelFile(post).addModel().condition(BlockStateProperties.NORTH, false).condition(BlockStateProperties.WEST, false).condition(BlockStateProperties.SOUTH, false).condition(BlockStateProperties.EAST, false).end();

		for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
			builder.part().modelFile(direction == Direction.SOUTH || direction == Direction.WEST ? capAlt : cap).rotationY(direction.getAxis() == Axis.X ? 90 : 0).addModel()
					.condition(BlockStateProperties.NORTH, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.NORTH)
					.condition(BlockStateProperties.WEST, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.WEST)
					.condition(BlockStateProperties.SOUTH, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.SOUTH)
					.condition(BlockStateProperties.EAST, PipeBlock.PROPERTY_BY_DIRECTION.get(direction) == BlockStateProperties.EAST)
					.end();

		}

		PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
			if (dir.getAxis().isHorizontal()) {
				builder.part().modelFile(dir == Direction.SOUTH || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Axis.X ? 90 : 0).addModel().condition(value, true).end();
			}
		});
	}

	public void registerHedge(Block leaves, Block log, Block hedge) {
		ModelFile hedgePost = this.hedgePost(name(hedge) + "_post", blockTexture(log), blockTexture(leaves));
		ModelFile hedgeSide = this.hedgeSide(name(hedge) + "_side", blockTexture(leaves));
		ModelFile hedgeExtend = this.hedgeExtend(name(hedge) + "_extend", blockTexture(leaves));
		this.hedgeBlock(hedge, hedgePost, hedgeSide, hedgeExtend);
		this.itemModels().getBuilder(name(hedge)).parent(hedgePost);
	}

	public ModelFile verticalSlab(String name, ResourceLocation texture) {
		return models().getBuilder(name).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/vertical_slab"))).texture("side", texture).texture("bottom", texture).texture("top", texture);
	}

	public static final BooleanProperty[] CHAINED = new BooleanProperty[]{BooleanProperty.create("chain_down"), BooleanProperty.create("chain_up"), BooleanProperty.create("chain_north"), BooleanProperty.create("chain_south"), BooleanProperty.create("chain_west"), BooleanProperty.create("chain_east")};

	public void registerPost(Block log, Block post) {
		ModelFile model = models().getBuilder(name(post)).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/post"))).texture("texture", blockTexture(log));
		ModelFile chainSmall = new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/chain_small"));
		ModelFile chainSmallTop = new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/chain_small_top"));
		this.getMultipartBuilder(post)
				.part().modelFile(model).addModel().condition(RotatedPillarBlock.AXIS, Axis.Y).end()
				.part().modelFile(model).rotationX(90).addModel().condition(RotatedPillarBlock.AXIS, Axis.Z).end()
				.part().modelFile(model).rotationX(90).rotationY(90).addModel().condition(RotatedPillarBlock.AXIS, Axis.X).end()
				.part().modelFile(chainSmall).addModel().condition(CHAINED[0], true).end()
				.part().modelFile(chainSmallTop).addModel().condition(CHAINED[1], true).end()
				.part().modelFile(chainSmallTop).rotationX(90).addModel().condition(CHAINED[2], true).end()
				.part().modelFile(chainSmall).rotationX(90).addModel().condition(CHAINED[3], true).end()
				.part().modelFile(chainSmall).rotationX(90).rotationY(90).addModel().condition(CHAINED[4], true).end()
				.part().modelFile(chainSmallTop).rotationX(90).rotationY(90).addModel().condition(CHAINED[5], true).end();
		this.registerItemModel(post);
	}

	public void hedgeBlock(Block block, ModelFile post, ModelFile side, ModelFile extend) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
		builder.part().modelFile(post).addModel().condition(BooleanProperty.create("extend"), false).end().part().modelFile(extend).addModel().condition(BooleanProperty.create("extend"), true);
		PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
			Direction dir = e.getKey();
			if (dir.getAxis().isHorizontal()) {
				builder.part().modelFile(side).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).addModel().condition(e.getValue(), true);
			}
		});
	}

	public ModelFile hedgePost(String name, ResourceLocation log, ResourceLocation leaf) {
		return models().getBuilder(name).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/hedge_post"))).texture("log", log).texture("leaf", leaf);
	}

	public ModelFile hedgeSide(String name, ResourceLocation leaf) {
		return models().getBuilder(name).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/hedge_side"))).texture("leaf", leaf);
	}

	public ModelFile hedgeExtend(String name, ResourceLocation leaf) {
		return models().getBuilder(name).parent(new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "block/hedge_extend"))).texture("leaf", leaf);
	}

	public void registerSignBlocks(Block planks, Pair<RegistryObject<BlueprintStandingSignBlock>, RegistryObject<BlueprintWallSignBlock>> pair) {
		this.simpleBlock(pair.getFirst().get(), particle(pair.getFirst().get(), blockTexture(planks)));
		this.simpleBlock(pair.getSecond().get(), particle(pair.getFirst().get(), blockTexture(planks)));
		this.registerGeneratedItemModel(pair.getFirst().get(), "item");
	}

	public void registerChestBlocks(Block planks, Pair<RegistryObject<BlueprintChestBlock>, RegistryObject<BlueprintTrappedChestBlock>> pair) {
		this.simpleBlock(pair.getFirst().get(), particle(pair.getFirst().get(), blockTexture(planks)));
		this.simpleBlock(pair.getSecond().get(), particle(pair.getFirst().get(), blockTexture(planks)));
		this.simpleBlockItem(pair.getFirst().get(), new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "item/template_chest")));
		this.simpleBlockItem(pair.getSecond().get(), new UncheckedModelFile(new ResourceLocation(Blueprint.MOD_ID, "item/template_chest")));
	}

	public ModelFile particle(Block block, ResourceLocation texture) {
		return this.models().getBuilder(name(block)).texture("particle", texture);
	}

	public void registerLogBlocks(Block log, Block wood) {
		this.logBlock((RotatedPillarBlock) log);
		this.registerItemModel(log);

		this.axisBlock((RotatedPillarBlock) wood, blockTexture(log), blockTexture(log));
		this.registerItemModel(wood);
	}

	public void verticalSlabBlock(Block planks, VerticalSlabBlock slab) {
		this.verticalSlab(name(slab), blockTexture(planks));
		UncheckedModelFile model = new UncheckedModelFile(prefix("block/", slab.getRegistryName()));
		this.getVariantBuilder(slab)
				.partialState().with(VerticalSlabBlock.TYPE, VerticalSlabType.NORTH).addModels(new ConfiguredModel(model, 0, 0, true))
				.partialState().with(VerticalSlabBlock.TYPE, VerticalSlabType.SOUTH).addModels(new ConfiguredModel(model, 0, 180, true))
				.partialState().with(VerticalSlabBlock.TYPE, VerticalSlabType.EAST).addModels(new ConfiguredModel(model, 0, 90, true))
				.partialState().with(VerticalSlabBlock.TYPE, VerticalSlabType.WEST).addModels(new ConfiguredModel(model, 0, 270, true))
				.partialState().with(VerticalSlabBlock.TYPE, VerticalSlabType.DOUBLE).addModels(new ConfiguredModel(models().cubeAll(name(planks), blockTexture(planks))));
	}

	private String name(Block block) {
		return block.getRegistryName().getPath();
	}

	private ResourceLocation prefix(String prefix, ResourceLocation rl) {
		return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
	}

	private ResourceLocation suffix(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}
}