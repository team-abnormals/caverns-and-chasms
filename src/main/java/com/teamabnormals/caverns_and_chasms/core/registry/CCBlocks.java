package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.block.*;
import com.teamabnormals.blueprint.common.block.chest.BlueprintChestBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintTrappedChestBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintStandingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallSignBlock;
import com.teamabnormals.blueprint.common.block.wood.*;
import com.teamabnormals.blueprint.core.util.PropertyUtil.WoodSetProperties;
import com.teamabnormals.caverns_and_chasms.common.block.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.ToIntFunction;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCBlocks {
	public static final CCBlockSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> SILVER_BLOCK = HELPER.createBlock("silver_block", () -> new InjectedBlock(Items.GOLD_BLOCK, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SILVER_ORE = HELPER.createBlock("silver_ore", () -> new OreBlock(Items.DEEPSLATE_GOLD_ORE, CCProperties.ORE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = HELPER.createBlock("deepslate_silver_ore", () -> new OreBlock(Items.DEEPSLATE_GOLD_ORE, CCProperties.DEEPSLATE_ORE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SOUL_SILVER_ORE = HELPER.createBlock("soul_silver_ore", () -> new OreBlock(Items.NETHER_QUARTZ_ORE, Block.Properties.copy(Blocks.SOUL_SOIL), UniformInt.of(0, 1)), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> RAW_SILVER_BLOCK = HELPER.createBlock("raw_silver_block", () -> new InjectedBlock(Items.RAW_GOLD_BLOCK, BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5.0F, 6.0F)), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SILVER_BARS = HELPER.createBlock("silver_bars", () -> new IronBarsBlock(CCProperties.METAL_BARS), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> MEDIUM_WEIGHTED_PRESSURE_PLATE = HELPER.createBlock("medium_weighted_pressure_plate", () -> new MediumWeightedPressurePlateBlock(CCProperties.SILVER_PRESSURE_PLATE), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> SILVER_BUTTON = HELPER.createCompatBlock("quark", "silver_button", () -> new SilverButtonBlock(CCProperties.SILVER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> SPIKED_RAIL = HELPER.createBlock("spiked_rail", () -> new SpikedRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL)), CreativeModeTab.TAB_TRANSPORTATION);

	public static final RegistryObject<Block> SANGUINE_PLATES = HELPER.createBlock("sanguine_plates", () -> new Block(CCProperties.SANGUINE_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SANGUINE_STAIRS = HELPER.createBlock("sanguine_plate_stairs", () -> new StairBlock(() -> SANGUINE_PLATES.get().defaultBlockState(), CCProperties.SANGUINE_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SANGUINE_SLAB = HELPER.createBlock("sanguine_plate_slab", () -> new SlabBlock(CCProperties.SANGUINE_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SANGUINE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "sanguine_plate_vertical_slab", () -> new VerticalSlabBlock(CCProperties.SANGUINE_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> NECROMIUM_BLOCK = HELPER.createBlock("necromium_block", () -> new InjectedBlock(Items.NETHERITE_BLOCK, CCProperties.NECROMIUM_BLOCK), new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));

	public static final RegistryObject<Block> BRAZIER = HELPER.createBlock("brazier", () -> new BrazierBlock(1.0F, CCProperties.BRAZIER), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> SOUL_BRAZIER = HELPER.createBlock("soul_brazier", () -> new BrazierBlock(2.0F, CCProperties.BRAZIER_DIM), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> ENDER_BRAZIER = HELPER.createCompatBlock("endergetic", "ender_brazier", () -> new BrazierBlock(3.0F, CCProperties.BRAZIER), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CUPRIC_BRAZIER = HELPER.createBlock("cupric_brazier", () -> new BrazierBlock(0.5F, CCProperties.BRAZIER_DIM), CreativeModeTab.TAB_DECORATIONS);

	public static final RegistryObject<Block> CUPRIC_FIRE = HELPER.createBlockNoItem("cupric_fire", () -> new CupricFireBlock(Block.Properties.copy(Blocks.SOUL_FIRE)));
	public static final RegistryObject<Block> CUPRIC_CAMPFIRE = HELPER.createBlock("cupric_campfire", () -> new CupricCampfireBlock(Block.Properties.copy(Blocks.SOUL_CAMPFIRE)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CUPRIC_LANTERN = HELPER.createBlock("cupric_lantern", () -> new LanternBlock(Block.Properties.copy(Blocks.SOUL_LANTERN)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CUPRIC_WALL_TORCH = HELPER.createBlockNoItem("cupric_wall_torch", () -> new CupricWallTorchBlock(Block.Properties.copy(Blocks.SOUL_TORCH)));
	public static final RegistryObject<Block> CUPRIC_TORCH = HELPER.createStandingAndWallBlock("cupric_torch", () -> new CupricTorchBlock(Block.Properties.copy(Blocks.SOUL_TORCH)), CUPRIC_WALL_TORCH, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CUPRIC_CANDLE = HELPER.createCompatBlock("buzzier_bees", "cupric_candle", () -> new CupricCandleBlock(CCProperties.CUPRIC_CANDLE), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CUPRIC_CANDLE_CAKE = HELPER.createBlockNoItem("cupric_candle_cake", () -> new CupricCandleCakeBlock(CUPRIC_CANDLE.get(), CCProperties.CUPRIC_CANDLE_CAKE));

	public static final RegistryObject<Block> ROTTEN_FLESH_BLOCK = HELPER.createBlock("rotten_flesh_block", () -> new Block(CCProperties.ROTTEN_FLESH_BLOCK), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> DEEPER_HEAD = HELPER.createBlockNoItem("deeper_head", () -> new CCSkullBlock(CCSkullTypes.DEEPER, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)));
	public static final RegistryObject<Block> DEEPER_WALL_HEAD = HELPER.createBlockNoItem("deeper_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.DEEPER, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F).dropsLike(DEEPER_HEAD.get())));
	public static final RegistryObject<Block> PEEPER_HEAD = HELPER.createBlockNoItem("peeper_head", () -> new CCSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)));
	public static final RegistryObject<Block> PEEPER_WALL_HEAD = HELPER.createBlockNoItem("peeper_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F).dropsLike(PEEPER_HEAD.get())));
	public static final RegistryObject<Block> MIME_HEAD = HELPER.createBlockNoItem("mime_head", () -> new CCSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)));
	public static final RegistryObject<Block> MIME_WALL_HEAD = HELPER.createBlockNoItem("mime_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F).dropsLike(MIME_HEAD.get())));

	public static final RegistryObject<Block> TMT = HELPER.createBlock("tmt", () -> new TmtBlock(CCProperties.TMT), CreativeModeTab.TAB_REDSTONE);

	public static final RegistryObject<Block> FLOODLIGHT = HELPER.createBlock("floodlight", () -> new WeatheringFloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> EXPOSED_FLOODLIGHT = HELPER.createBlock("exposed_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WEATHERED_FLOODLIGHT = HELPER.createBlock("weathered_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> OXIDIZED_FLOODLIGHT = HELPER.createBlock("oxidized_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDZIED_FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_FLOODLIGHT = HELPER.createBlock("waxed_floodlight", () -> new FloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_EXPOSED_FLOODLIGHT = HELPER.createBlock("waxed_exposed_floodlight", () -> new FloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_WEATHERED_FLOODLIGHT = HELPER.createBlock("waxed_weathered_floodlight", () -> new FloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_OXIDIZED_FLOODLIGHT = HELPER.createBlock("waxed_oxidized_floodlight", () -> new FloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDZIED_FLOODLIGHT), CreativeModeTab.TAB_DECORATIONS);

	//	public static final RegistryObject<Block> INDUCTOR = HELPER.createBlock("inductor", () -> new InductorBlock(CCProperties.INDUCTOR), CreativeModeTab.TAB_REDSTONE);

	public static final RegistryObject<Block> TOOLBOX = HELPER.createToolboxBlock("toolbox", () -> new WeatheringToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> EXPOSED_TOOLBOX = HELPER.createToolboxBlock("exposed_toolbox", () -> new WeatheringToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WEATHERED_TOOLBOX = HELPER.createToolboxBlock("weathered_toolbox", () -> new WeatheringToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> OXIDIZED_TOOLBOX = HELPER.createToolboxBlock("oxidized_toolbox", () -> new WeatheringToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_TOOLBOX = HELPER.createToolboxBlock("waxed_toolbox", () -> new ToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_EXPOSED_TOOLBOX = HELPER.createToolboxBlock("waxed_exposed_toolbox", () -> new ToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_WEATHERED_TOOLBOX = HELPER.createToolboxBlock("waxed_weathered_toolbox", () -> new ToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_OXIDIZED_TOOLBOX = HELPER.createToolboxBlock("waxed_oxidized_toolbox", () -> new ToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));

	public static final RegistryObject<Block> COPPER_BARS = HELPER.createBlock("copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> EXPOSED_COPPER_BARS = HELPER.createBlock("exposed_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WEATHERED_COPPER_BARS = HELPER.createBlock("weathered_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> OXIDIZED_COPPER_BARS = HELPER.createBlock("oxidized_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_COPPER_BARS = HELPER.createBlock("waxed_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BARS = HELPER.createBlock("waxed_exposed_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BARS = HELPER.createBlock("waxed_weathered_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BARS = HELPER.createBlock("waxed_oxidized_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)), CreativeModeTab.TAB_DECORATIONS);

	public static final RegistryObject<Block> COPPER_BUTTON = HELPER.createBlock("copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> EXPOSED_COPPER_BUTTON = HELPER.createBlock("exposed_copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.EXPOSED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> WEATHERED_COPPER_BUTTON = HELPER.createBlock("weathered_copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.WEATHERED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> OXIDIZED_COPPER_BUTTON = HELPER.createBlock("oxidized_copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.OXIDIZED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> WAXED_COPPER_BUTTON = HELPER.createBlock("waxed_copper_button", () -> new CopperButtonBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BUTTON = HELPER.createBlock("waxed_exposed_copper_button", () -> new CopperButtonBlock(WeatherState.EXPOSED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BUTTON = HELPER.createBlock("waxed_weathered_copper_button", () -> new CopperButtonBlock(WeatherState.WEATHERED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BUTTON = HELPER.createBlock("waxed_oxidized_copper_button", () -> new CopperButtonBlock(WeatherState.OXIDIZED, CCProperties.COPPER_BUTTON), CreativeModeTab.TAB_REDSTONE);

	public static final RegistryObject<Block> EXPOSED_LIGHTNING_ROD = HELPER.createBlock("exposed_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WEATHERED_LIGHTNING_ROD = HELPER.createBlock("weathered_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> OXIDIZED_LIGHTNING_ROD = HELPER.createBlock("oxidized_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_LIGHTNING_ROD = HELPER.createBlock("waxed_lightning_rod", () -> new LightningRodBlock(WeatheringLightningRodBlock.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_EXPOSED_LIGHTNING_ROD = HELPER.createBlock("waxed_exposed_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_WEATHERED_LIGHTNING_ROD = HELPER.createBlock("waxed_weathered_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> WAXED_OXIDIZED_LIGHTNING_ROD = HELPER.createBlock("waxed_oxidized_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)), CreativeModeTab.TAB_DECORATIONS);

	public static final RegistryObject<Block> LAVA_LAMP = HELPER.createBlock("lava_lamp", () -> new LavaLampBlock(CCProperties.LAVA_LAMP), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> GOLDEN_BARS = HELPER.createBlock("golden_bars", () -> new IronBarsBlock(CCProperties.METAL_BARS), CreativeModeTab.TAB_DECORATIONS);

	public static final RegistryObject<Block> SPINEL_ORE = HELPER.createBlock("spinel_ore", () -> new OreBlock(Items.DEEPSLATE_LAPIS_ORE, CCProperties.ORE, UniformInt.of(2, 5)), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DEEPSLATE_SPINEL_ORE = HELPER.createBlock("deepslate_spinel_ore", () -> new OreBlock(Items.DEEPSLATE_LAPIS_ORE, CCProperties.DEEPSLATE_ORE, UniformInt.of(2, 5)), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_BLOCK = HELPER.createBlock("spinel_block", () -> new InjectedBlock(Items.LAPIS_BLOCK, CCProperties.SPINEL), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_BRICKS = HELPER.createBlock("spinel_bricks", () -> new Block(CCProperties.SPINEL), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_BRICK_STAIRS = HELPER.createBlock("spinel_brick_stairs", () -> new StairBlock(() -> SPINEL_BRICKS.get().defaultBlockState(), CCProperties.SPINEL), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_BRICK_SLAB = HELPER.createBlock("spinel_brick_slab", () -> new SlabBlock(CCProperties.SPINEL), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "spinel_brick_vertical_slab", () -> new VerticalSlabBlock(CCProperties.SPINEL), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_BRICK_WALL = HELPER.createBlock("spinel_brick_wall", () -> new WallBlock(CCProperties.SPINEL), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> SPINEL_PILLAR = HELPER.createBlock("spinel_pillar", () -> new RotatedPillarBlock(CCProperties.SPINEL), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SPINEL_LAMP = HELPER.createBlock("spinel_lamp", () -> new Block(CCProperties.LAMP), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> LAPIS_LAZULI_BRICKS = HELPER.createBlock("lapis_bricks", () -> new Block(CCProperties.LAPIS_LAZULI), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_STAIRS = HELPER.createBlock("lapis_brick_stairs", () -> new StairBlock(() -> LAPIS_LAZULI_BRICKS.get().defaultBlockState(), CCProperties.LAPIS_LAZULI), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_SLAB = HELPER.createBlock("lapis_brick_slab", () -> new SlabBlock(CCProperties.LAPIS_LAZULI), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "lapis_brick_vertical_slab", () -> new VerticalSlabBlock(CCProperties.LAPIS_LAZULI), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_WALL = HELPER.createBlock("lapis_brick_wall", () -> new WallBlock(CCProperties.LAPIS_LAZULI), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> LAPIS_LAZULI_PILLAR = HELPER.createBlock("lapis_pillar", () -> new RotatedPillarBlock(CCProperties.LAPIS_LAZULI), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> LAPIS_LAZULI_LAMP = HELPER.createBlock("lapis_lamp", () -> new Block(CCProperties.LAMP), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> ROCKY_DIRT = HELPER.createBlock("rocky_dirt", () -> new RockyDirtBlock(CCProperties.ROCKY_DIRT), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> FRAGILE_STONE = HELPER.createBlock("fragile_stone", () -> new FragileStoneBlock(CCProperties.FRAGILE_STONE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> FRAGILE_DEEPSLATE = HELPER.createBlock("fragile_deepslate", () -> new FragileDeepslateBlock(CCProperties.FRAGILE_DEEPSLATE), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> COBBLESTONE_BRICKS = HELPER.createBlock("cobblestone_bricks", () -> new Block(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_STAIRS = HELPER.createBlock("cobblestone_brick_stairs", () -> new StairBlock(() -> COBBLESTONE_BRICKS.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_SLAB = HELPER.createBlock("cobblestone_brick_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_WALL = HELPER.createBlock("cobblestone_brick_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> COBBLESTONE_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "cobblestone_brick_vertical_slab", () -> new VerticalSlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICKS = HELPER.createBlock("mossy_cobblestone_bricks", () -> new Block(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_STAIRS = HELPER.createBlock("mossy_cobblestone_brick_stairs", () -> new StairBlock(() -> COBBLESTONE_BRICKS.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_SLAB = HELPER.createBlock("mossy_cobblestone_brick_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_WALL = HELPER.createBlock("mossy_cobblestone_brick_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "mossy_cobblestone_brick_vertical_slab", () -> new VerticalSlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> COBBLESTONE_TILES = HELPER.createBlock("cobblestone_tiles", () -> new Block(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_STAIRS = HELPER.createBlock("cobblestone_tile_stairs", () -> new StairBlock(() -> COBBLESTONE_TILES.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_SLAB = HELPER.createBlock("cobblestone_tile_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_WALL = HELPER.createBlock("cobblestone_tile_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> COBBLESTONE_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "cobblestone_tile_vertical_slab", () -> new VerticalSlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILES = HELPER.createBlock("mossy_cobblestone_tiles", () -> new Block(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_STAIRS = HELPER.createBlock("mossy_cobblestone_tile_stairs", () -> new StairBlock(() -> COBBLESTONE_TILES.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_SLAB = HELPER.createBlock("mossy_cobblestone_tile_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_WALL = HELPER.createBlock("mossy_cobblestone_tile_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "mossy_cobblestone_tile_vertical_slab", () -> new VerticalSlabBlock(CCProperties.COBBLESTONE_BRICKS), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> CALCITE_STAIRS = HELPER.createBlock("calcite_stairs", () -> new StairBlock(() -> Blocks.CALCITE.defaultBlockState(), CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CALCITE_SLAB = HELPER.createBlock("calcite_slab", () -> new SlabBlock(CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CALCITE_WALL = HELPER.createBlock("calcite_wall", () -> new WallBlock(CCProperties.CALCITE), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CALCITE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "calcite_vertical_slab", () -> new VerticalSlabBlock(CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_CALCITE = HELPER.createBlock("polished_calcite", () -> new InjectedBlock(Items.CALCITE, CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_CALCITE_STAIRS = HELPER.createBlock("polished_calcite_stairs", () -> new StairBlock(() -> POLISHED_CALCITE.get().defaultBlockState(), CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_CALCITE_SLAB = HELPER.createBlock("polished_calcite_slab", () -> new SlabBlock(CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_CALCITE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "polished_calcite_vertical_slab", () -> new VerticalSlabBlock(CCProperties.CALCITE), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> TUFF_STAIRS = HELPER.createBlock("tuff_stairs", () -> new StairBlock(() -> Blocks.TUFF.defaultBlockState(), CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> TUFF_SLAB = HELPER.createBlock("tuff_slab", () -> new SlabBlock(CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> TUFF_WALL = HELPER.createBlock("tuff_wall", () -> new WallBlock(CCProperties.TUFF), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> TUFF_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "tuff_vertical_slab", () -> new VerticalSlabBlock(CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_TUFF = HELPER.createBlock("polished_tuff", () -> new InjectedBlock(Items.TUFF, CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_TUFF_STAIRS = HELPER.createBlock("polished_tuff_stairs", () -> new StairBlock(() -> POLISHED_TUFF.get().defaultBlockState(), CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_TUFF_SLAB = HELPER.createBlock("polished_tuff_slab", () -> new SlabBlock(CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_TUFF_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "polished_tuff_vertical_slab", () -> new VerticalSlabBlock(CCProperties.TUFF), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> SUGILITE = HELPER.createBlock("sugilite", () -> new Block(CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_STAIRS = HELPER.createBlock("sugilite_stairs", () -> new StairBlock(() -> SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_SLAB = HELPER.createBlock("sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> SUGILITE_WALL = HELPER.createBlock("sugilite_wall", () -> new WallBlock(CCProperties.SUGILITE), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> SUGILITE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "sugilite_vertical_slab", () -> new VerticalSlabBlock(CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_SUGILITE = HELPER.createBlock("polished_sugilite", () -> new Block(CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_SUGILITE_STAIRS = HELPER.createBlock("polished_sugilite_stairs", () -> new StairBlock(() -> POLISHED_SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_SUGILITE_SLAB = HELPER.createBlock("polished_sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> POLISHED_SUGILITE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "polished_sugilite_vertical_slab", () -> new VerticalSlabBlock(CCProperties.SUGILITE), CreativeModeTab.TAB_BUILDING_BLOCKS);


	public static final RegistryObject<Block> DRIPSTONE_SHINGLES = HELPER.createBlock("dripstone_shingles", () -> new Block(CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_STAIRS = HELPER.createBlock("dripstone_shingle_stairs", () -> new StairBlock(() -> DRIPSTONE_SHINGLES.get().defaultBlockState(), CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_SLAB = HELPER.createBlock("dripstone_shingle_slab", () -> new SlabBlock(CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_WALL = HELPER.createBlock("dripstone_shingle_wall", () -> new WallBlock(CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "dripstone_shingle_vertical_slab", () -> new VerticalSlabBlock(CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CHISELED_DRIPSTONE_SHINGLES = HELPER.createBlock("chiseled_dripstone_shingles", () -> new DripstoneShingleBlock(CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> FLOODED_DRIPSTONE_SHINGLES = HELPER.createBlock("flooded_dripstone_shingles", () -> new DripstoneShingleBlock(CCProperties.DRIPSTONE_SHINGLES), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> AMETHYST_BLOCK = HELPER.createBlock("amethyst_block", () -> new AmethystBlock(CCProperties.AMETHYST), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CUT_AMETHYST = HELPER.createBlock("cut_amethyst", () -> new AmethystBlock(CCProperties.AMETHYST), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CUT_AMETHYST_BRICKS = HELPER.createBlock("cut_amethyst_bricks", () -> new AmethystBlock(CCProperties.AMETHYST), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_STAIRS = HELPER.createBlock("cut_amethyst_brick_stairs", () -> new AmethystStairBlock(() -> CUT_AMETHYST_BRICKS.get().defaultBlockState(), CCProperties.AMETHYST), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_SLAB = HELPER.createBlock("cut_amethyst_brick_slab", () -> new AmethystSlabBlock(CCProperties.AMETHYST), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_WALL = HELPER.createBlock("cut_amethyst_brick_wall", () -> new AmethystWallBlock(CCProperties.AMETHYST), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "cut_amethyst_brick_vertical_slab", () -> new AmethystVerticalSlabBlock(CCProperties.AMETHYST), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> ECHO_BLOCK = HELPER.createBlock("echo_block", () -> new Block(CCProperties.ECHO_BLOCK), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> STRIPPED_AZALEA_LOG = HELPER.createBlock("stripped_azalea_log", () -> new StrippedLogBlock(CCProperties.AZALEA.log()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> STRIPPED_AZALEA_WOOD = HELPER.createBlock("stripped_azalea_wood", () -> new StrippedWoodBlock(CCProperties.AZALEA.log()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_LOG = HELPER.createBlock("azalea_log", () -> new LogBlock(STRIPPED_AZALEA_LOG, CCProperties.AZALEA.log()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_WOOD = HELPER.createBlock("azalea_wood", () -> new WoodBlock(STRIPPED_AZALEA_WOOD, CCProperties.AZALEA.log()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_PLANKS = HELPER.createBlock("azalea_planks", () -> new PlanksBlock(CCProperties.AZALEA.planks()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_DOOR = HELPER.createBlock("azalea_door", () -> new WoodDoorBlock(CCProperties.AZALEA.planks()), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> AZALEA_SLAB = HELPER.createBlock("azalea_slab", () -> new WoodSlabBlock(CCProperties.AZALEA.planks()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_STAIRS = HELPER.createBlock("azalea_stairs", () -> new StairBlock(() -> AZALEA_PLANKS.get().defaultBlockState(), CCProperties.AZALEA.planks()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_FENCE = HELPER.createFuelBlock("azalea_fence", () -> new WoodFenceBlock(CCProperties.AZALEA.planks()), 300, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> AZALEA_FENCE_GATE = HELPER.createFuelBlock("azalea_fence_gate", () -> new WoodFenceGateBlock(CCProperties.AZALEA.planks()), 300, CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> AZALEA_PRESSURE_PLATE = HELPER.createBlock("azalea_pressure_plate", () -> new WoodPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, CCProperties.AZALEA.pressurePlate()), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> AZALEA_BUTTON = HELPER.createBlock("azalea_button", () -> new BlueprintWoodButtonBlock(CCProperties.AZALEA.button()), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> AZALEA_TRAPDOOR = HELPER.createBlock("azalea_trapdoor", () -> new WoodTrapDoorBlock(CCProperties.AZALEA.trapdoor()), CreativeModeTab.TAB_REDSTONE);
	public static final Pair<RegistryObject<BlueprintStandingSignBlock>, RegistryObject<BlueprintWallSignBlock>> AZALEA_SIGN = HELPER.createSignBlock("azalea", MaterialColor.TERRACOTTA_PURPLE);

	public static final RegistryObject<Block> AZALEA_BOARDS = HELPER.createCompatFuelBlock("woodworks", "azalea_boards", () -> new RotatedPillarBlock(CCProperties.AZALEA.planks()), 300, CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_BOOKSHELF = HELPER.createCompatFuelBlock("woodworks", "azalea_bookshelf", () -> new BookshelfBlock(CCProperties.AZALEA.bookshelf()), 300, CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_LADDER = HELPER.createCompatFuelBlock("woodworks", "azalea_ladder", () -> new BlueprintLadderBlock(CCProperties.AZALEA.ladder()), 300, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> AZALEA_BEEHIVE = HELPER.createCompatBlock("woodworks", "azalea_beehive", () -> new BlueprintBeehiveBlock(CCProperties.AZALEA.beehive()), CreativeModeTab.TAB_DECORATIONS);
	public static final Pair<RegistryObject<BlueprintChestBlock>, RegistryObject<BlueprintTrappedChestBlock>> AZALEA_CHEST = HELPER.createCompatChestBlocks("woodworks", "azalea", MaterialColor.TERRACOTTA_PURPLE);

	public static final RegistryObject<Block> VERTICAL_AZALEA_PLANKS = HELPER.createCompatBlock("quark", "vertical_azalea_planks", () -> new Block(CCProperties.AZALEA.planks()), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> AZALEA_VERTICAL_SLAB = HELPER.createCompatFuelBlock("quark", "azalea_vertical_slab", () -> new VerticalSlabBlock(CCProperties.AZALEA.planks()), 150, CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> STRIPPED_AZALEA_POST = HELPER.createCompatFuelBlock("quark", "stripped_azalea_post", () -> new WoodPostBlock(CCProperties.AZALEA.log()), 300, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> AZALEA_POST = HELPER.createCompatFuelBlock("quark", "azalea_post", () -> new WoodPostBlock(STRIPPED_AZALEA_POST, CCProperties.AZALEA.log()), 300, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> AZALEA_HEDGE = HELPER.createCompatFuelBlock("quark", "azalea_hedge", () -> new HedgeBlock(CCProperties.AZALEA.log()), 300, CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> FLOWERING_AZALEA_HEDGE = HELPER.createCompatFuelBlock("quark", "flowering_azalea_hedge", () -> new HedgeBlock(CCProperties.AZALEA.log()), 300, CreativeModeTab.TAB_DECORATIONS);

	public static class CCProperties {
		public static final BlockBehaviour.Properties ROCKY_DIRT = BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).requiresCorrectToolForDrops().strength(1.5F);
		public static final BlockBehaviour.Properties FRAGILE_STONE = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 3.0F);
		public static final BlockBehaviour.Properties FRAGILE_DEEPSLATE = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DEEPSLATE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE);
		public static final BlockBehaviour.Properties CALCITE = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).sound(SoundType.CALCITE).requiresCorrectToolForDrops().strength(0.75F);
		public static final BlockBehaviour.Properties TUFF = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).sound(SoundType.TUFF).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties SUGILITE = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties COBBLESTONE_BRICKS = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F);
		public static final BlockBehaviour.Properties DRIPSTONE_SHINGLES = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F);
		public static final BlockBehaviour.Properties AMETHYST = BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_PURPLE).strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops();
		public static final BlockBehaviour.Properties ECHO_BLOCK = BlockBehaviour.Properties.of(Material.SCULK).strength(1.5F).sound(SoundType.SCULK_CATALYST).requiresCorrectToolForDrops().lightLevel(state -> 6);

		public static final BlockBehaviour.Properties CUPRIC_CANDLE = BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_ORANGE).noOcclusion().strength(0.1F).sound(SoundType.CANDLE).lightLevel(CupricCandleBlock.DIM_LIGHT_EMISSION);
		public static final BlockBehaviour.Properties CUPRIC_CANDLE_CAKE = BlockBehaviour.Properties.copy(Blocks.CAKE).lightLevel(getLightValueLit(2));

		public static final BlockBehaviour.Properties TMT = BlockBehaviour.Properties.of(Material.EXPLOSIVE).instabreak().sound(SoundType.GRASS);
		public static final BlockBehaviour.Properties TOOLBOX = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).strength(0.2F, 6.0F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties FLOODLIGHT = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 10);
		public static final BlockBehaviour.Properties EXPOSED_FLOODLIGHT = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 9);
		public static final BlockBehaviour.Properties WEATHERED_FLOODLIGHT = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_STEM).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 8);
		public static final BlockBehaviour.Properties OXIDZIED_FLOODLIGHT = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 7);

		public static final BlockBehaviour.Properties INDUCTOR = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 4.8F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties LAVA_LAMP = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 15);
		public static final BlockBehaviour.Properties METAL_BARS = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_PRESSURE_PLATE = BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(SoundType.WOOD);
		public static final BlockBehaviour.Properties SILVER_BUTTON = BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.METAL);
		public static final BlockBehaviour.Properties COPPER_BUTTON = BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties SANGUINE_PLATES = Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.METAL);

		public static final BlockBehaviour.Properties BRAZIER = BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(getLightValueLit(15)).noOcclusion();
		public static final BlockBehaviour.Properties BRAZIER_DIM = BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(getLightValueLit(10)).noOcclusion();

		public static final BlockBehaviour.Properties ORE = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final BlockBehaviour.Properties DEEPSLATE_ORE = BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE);
		public static final BlockBehaviour.Properties SPINEL = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final BlockBehaviour.Properties LAPIS_LAZULI = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.LAPIS).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final BlockBehaviour.Properties LAMP = BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).lightLevel((state) -> 15).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(CCProperties::alwaysAllowSpawn);

		public static final BlockBehaviour.Properties ROTTEN_FLESH_BLOCK = BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.TERRACOTTA_ORANGE).strength(0.8F).sound(SoundType.CORAL_BLOCK);
		public static final BlockBehaviour.Properties NECROMIUM_BLOCK = BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK);

		public static final WoodSetProperties AZALEA = WoodSetProperties.builder(MaterialColor.TERRACOTTA_PURPLE).leavesSound(SoundType.AZALEA_LEAVES).build();

		private static boolean alwaysAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
			return true;
		}

		private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
			return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		}
	}

	public enum CCSkullTypes implements SkullBlock.Type {
		MIME, DEEPER, PEEPER
	}
}