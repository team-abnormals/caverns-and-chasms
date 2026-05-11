package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.block.BlueprintBeehiveBlock;
import com.teamabnormals.blueprint.common.block.BlueprintDirectionalBlock;
import com.teamabnormals.blueprint.common.block.LogBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintChestBlock;
import com.teamabnormals.blueprint.common.block.chest.BlueprintTrappedChestBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintCeilingHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintStandingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallHangingSignBlock;
import com.teamabnormals.blueprint.common.block.sign.BlueprintWallSignBlock;
import com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper;
import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.PropertyUtil.WoodSetProperties;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.DeeperRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.EvendeeperRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.MimeRenderer;
import com.teamabnormals.caverns_and_chasms.client.renderer.entity.PeeperRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.*;
import com.teamabnormals.caverns_and_chasms.common.block.amethyst.AmethystSlabBlock;
import com.teamabnormals.caverns_and_chasms.common.block.amethyst.AmethystStairBlock;
import com.teamabnormals.caverns_and_chasms.common.block.amethyst.AmethystWallBlock;
import com.teamabnormals.caverns_and_chasms.common.block.cupric.CupricCampfireBlock;
import com.teamabnormals.caverns_and_chasms.common.block.cupric.CupricFireBlock;
import com.teamabnormals.caverns_and_chasms.common.block.cupric.CupricTorchBlock;
import com.teamabnormals.caverns_and_chasms.common.block.cupric.CupricWallTorchBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.*;
import com.teamabnormals.caverns_and_chasms.common.block.turquoise.*;
import com.teamabnormals.caverns_and_chasms.common.block.weathering.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.mixin.block.BlockBehaviourAccessor;
import com.teamabnormals.caverns_and_chasms.core.other.CCConstants;
import com.teamabnormals.caverns_and_chasms.core.other.CCEnums;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class CCBlocks {
	public static final CCBlockSubRegistryHelper BLOCKS = CavernsAndChasms.REGISTRY_HELPER.getBlockSubHelper();

	public static final DeferredBlock<Block> SILVER_BLOCK = BLOCKS.createBlock("silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(CCSoundTypes.SILVER)));
	public static final DeferredBlock<Block> SILVER_ORE = BLOCKS.createBlock("silver_ore", () -> new Block(CCProperties.ORE));
	public static final DeferredBlock<Block> DEEPSLATE_SILVER_ORE = BLOCKS.createBlock("deepslate_silver_ore", () -> new Block(CCProperties.DEEPSLATE_ORE));
	public static final DeferredBlock<Block> SOUL_SILVER_ORE = BLOCKS.createBlock("soul_silver_ore", () -> new DropExperienceBlock(UniformInt.of(0, 1), CCProperties.SOUL_SILVER_ORE));
	public static final DeferredBlock<Block> RAW_SILVER_BLOCK = BLOCKS.createBlock("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));
	public static final DeferredBlock<Block> SILVER_BARS = BLOCKS.createBlock("silver_bars", () -> new IronBarsBlock(CCProperties.SILVER_BARS));
	public static final DeferredBlock<Block> MEDIUM_WEIGHTED_PRESSURE_PLATE = BLOCKS.createBlock("medium_weighted_pressure_plate", () -> new WeightedPressurePlateBlock(75, CCProperties.SILVER_BLOCK_SET.get(), CCProperties.SILVER_PRESSURE_PLATE));
	public static final DeferredBlock<Block> SILVER_CHAIN = BLOCKS.createBlock("silver_chain", () -> new ChainBlock(CCProperties.SILVER_CHAIN));

	public static final DeferredBlock<Block> TIN_BLOCK = BLOCKS.createBlock("tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(CCSoundTypes.TIN)));
	public static final DeferredBlock<Block> TIN_ORE = BLOCKS.createBlock("tin_ore", () -> new Block(CCProperties.TIN_ORE));
	public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = BLOCKS.createBlock("deepslate_tin_ore", () -> new Block(CCProperties.DEEPSLATE_TIN_ORE));
	public static final DeferredBlock<Block> CYLINDRITE_TIN_ORE = BLOCKS.createBlock("cylindrite_tin_ore", () -> new Block(CCProperties.CYLINDRITE_TIN_ORE));
	public static final DeferredBlock<Block> CASSITERITE_TIN_ORE = BLOCKS.createBlock("cassiterite_tin_ore", () -> new Block(CCProperties.CASSITERITE_TIN_ORE));
	public static final DeferredBlock<Block> RAW_TIN_BLOCK = BLOCKS.createBlock("raw_tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.TIN_ORE)));
	public static final DeferredBlock<Block> TIN_BARS = BLOCKS.createBlock("tin_bars", () -> new IronBarsBlock(CCProperties.TIN_BARS));
	public static final DeferredBlock<Block> TIN_CHAIN = BLOCKS.createBlock("tin_chain", () -> new ChainBlock(CCProperties.TIN_CHAIN));
	public static final DeferredBlock<Block> TIN_BULB = BLOCKS.createBlock("tin_bulb", () -> new TinBulbBlock(CCProperties.TIN_BULB));
	public static final DeferredBlock<Block> FLOAT_GLASS = BLOCKS.createBlock("float_glass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).sound(CCSoundTypes.FLOAT_GLASS)));
	public static final DeferredBlock<Block> FLOAT_GLASS_PANE = BLOCKS.createBlock("float_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(CCSoundTypes.FLOAT_GLASS)));

	public static final DeferredBlock<Block> HOLD_PRESSURE_PLATE = BLOCKS.createBlock("hold_pressure_plate", () -> new HoldPlateBlock(CCProperties.TIN_BLOCK_SET.get(), CCProperties.HOLD_PRESSURE_PLATE));
	public static final DeferredBlock<Block> HOLD_BUTTON = BLOCKS.createBlock("hold_button", () -> new HoldButtonBlock(CCProperties.TIN_BLOCK_SET.get(), CCProperties.HOLD_BUTTON));
	public static final DeferredBlock<Block> WINCH = BLOCKS.createWinchBlock("winch", () -> new WinchBlock(CCProperties.WINCH));
	public static final DeferredBlock<Block> WALL_DIMMER = BLOCKS.createBlockNoItem("wall_dimmer", () -> new WallDimmerBlock(CCProperties.DIMMER));
	public static final DeferredBlock<Block> DIMMER = BLOCKS.createBlockNoItem("dimmer", () -> new DimmerBlock(CCProperties.DIMMER));
	public static final DeferredBlock<Block> BOUNCER = BLOCKS.createBlock("bouncer", () -> new BouncerBlock(BlockBehaviour.Properties.ofFullCopy(TIN_BLOCK.get()).sound(CCSoundTypes.BOUNCER)));
	public static final DeferredBlock<Block> HOOP = BLOCKS.createBlock("hoop", () -> new HoopBlock(CCProperties.HOOP));
	public static final DeferredBlock<Block> STORAGE_DUCT = BLOCKS.createBlock("storage_duct", () -> new StorageDuctBlock(CCProperties.STORAGE_DUCT));
	public static final DeferredBlock<Block> STORAGE_DUCT_HATCH = BLOCKS.createBlock("storage_duct_hatch", () -> new StorageDuctHatchBlock(CCProperties.STORAGE_DUCT_HATCH));
	public static final DeferredBlock<Block> TINPLATE_BLOCK = BLOCKS.createBlock("tinplate_block", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(TIN_BLOCK.get()).sound(CCSoundTypes.TINPLATE)));

	public static final DeferredBlock<Block> ROLLER_DOOR = BLOCKS.createBlockNoItem("roller_door", () -> new RollerDoorBlock(false, MovingDoorType.ROLLER_DOOR, CCProperties.ROLLER_DOOR));
	public static final DeferredBlock<Block> ROLLER_DOOR_HEADER = BLOCKS.createBlockNoItem("roller_door_header", () -> new RollerDoorBlock(true, MovingDoorType.ROLLER_DOOR, CCProperties.ROLLER_DOOR));

	public static final DeferredBlock<Block> COPPER_RAIL = BLOCKS.createBlock("copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> EXPOSED_COPPER_RAIL = BLOCKS.createBlock("exposed_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.EXPOSED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> WEATHERED_COPPER_RAIL = BLOCKS.createBlock("weathered_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.WEATHERED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_RAIL = BLOCKS.createBlock("oxidized_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.OXIDIZED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> WAXED_COPPER_RAIL = BLOCKS.createBlock("waxed_copper_rail", () -> new CopperRailBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_RAIL = BLOCKS.createBlock("waxed_exposed_copper_rail", () -> new CopperRailBlock(WeatherState.EXPOSED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_RAIL = BLOCKS.createBlock("waxed_weathered_copper_rail", () -> new CopperRailBlock(WeatherState.WEATHERED, CCProperties.COPPER_RAIL));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_RAIL = BLOCKS.createBlock("waxed_oxidized_copper_rail", () -> new CopperRailBlock(WeatherState.OXIDIZED, CCProperties.COPPER_RAIL));

	public static final DeferredBlock<Block> HALT_RAIL = BLOCKS.createBlock("halt_rail", () -> new HaltRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL)));
	public static final DeferredBlock<Block> SPIKED_RAIL = BLOCKS.createBlock("spiked_rail", () -> new SpikedRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).sound(CCSoundTypes.SILVER)));
	public static final DeferredBlock<Block> SLAUGHTER_RAIL = BLOCKS.createBlock("slaughter_rail", () -> new SlaughterRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL).sound(CCSoundTypes.SILVER)));

	public static final DeferredBlock<Block> RESISTOR = BLOCKS.createBlock("resistor", () -> new ResistorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER).sound(CCSoundTypes.DIMMER)));
	public static final DeferredBlock<Block> REFRACTOR = BLOCKS.createBlock("refractor", () -> new RefractorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER).sound(CCSoundTypes.REFRACTOR)));

	public static final DeferredBlock<Block> SANGUINE_BLOCK = BLOCKS.createBlock("sanguine_block", () -> new Block(CCProperties.SANGUINE_TILES));
	public static final DeferredBlock<Block> SANGUINE_TILES = BLOCKS.createBlock("sanguine_tiles", () -> new Block(CCProperties.SANGUINE_TILES));
	public static final DeferredBlock<Block> SANGUINE_TILE_STAIRS = BLOCKS.createBlock("sanguine_tile_stairs", () -> new StairBlock(SANGUINE_TILES.get().defaultBlockState(), CCProperties.SANGUINE_TILES));
	public static final DeferredBlock<Block> SANGUINE_TILE_SLAB = BLOCKS.createBlock("sanguine_tile_slab", () -> new SlabBlock(CCProperties.SANGUINE_TILES));
	public static final DeferredBlock<Block> SANGUINE_TILE_WALL = BLOCKS.createBlock("sanguine_tile_wall", () -> new WallBlock(CCProperties.SANGUINE_TILES));
	public static final DeferredBlock<Block> FORTIFIED_SANGUINE_TILES = BLOCKS.createBlock("fortified_sanguine_tiles", () -> new Block(CCProperties.FORTIFIED_SANGUINE_TILES));
	public static final DeferredBlock<Block> FORTIFIED_SANGUINE_TILE_STAIRS = BLOCKS.createBlock("fortified_sanguine_tile_stairs", () -> new StairBlock(FORTIFIED_SANGUINE_TILES.get().defaultBlockState(), CCProperties.FORTIFIED_SANGUINE_TILES));
	public static final DeferredBlock<Block> FORTIFIED_SANGUINE_TILE_SLAB = BLOCKS.createBlock("fortified_sanguine_tile_slab", () -> new SlabBlock(CCProperties.FORTIFIED_SANGUINE_TILES));
	public static final DeferredBlock<Block> FORTIFIED_SANGUINE_TILE_WALL = BLOCKS.createBlock("fortified_sanguine_tile_wall", () -> new WallBlock(CCProperties.FORTIFIED_SANGUINE_TILES));

	public static final DeferredBlock<Block> NECROMIUM_BLOCK = BLOCKS.createBlock("necromium_block", () -> new Block(CCProperties.NECROMIUM_BLOCK), new Item.Properties().fireResistant());

	public static final DeferredBlock<Block> BRAZIER = BLOCKS.createBlock("brazier", () -> new BrazierBlock(1.0F, CCProperties.BRAZIER));
	public static final DeferredBlock<Block> SOUL_BRAZIER = BLOCKS.createBlock("soul_brazier", () -> new BrazierBlock(2.0F, CCProperties.BRAZIER_DIM));
	public static final DeferredBlock<Block> ENDER_BRAZIER = BLOCKS.createBlock("ender_brazier", () -> new BrazierBlock(3.0F, CCProperties.BRAZIER));
	public static final DeferredBlock<Block> CUPRIC_BRAZIER = BLOCKS.createBlock("cupric_brazier", () -> new BrazierBlock(0.5F, CCProperties.BRAZIER_DIM));

	public static final DeferredBlock<Block> CUPRIC_FIRE = BLOCKS.createBlockNoItem("cupric_fire", () -> new CupricFireBlock(Block.Properties.ofFullCopy(Blocks.SOUL_FIRE)));
	public static final DeferredBlock<Block> CUPRIC_CAMPFIRE = BLOCKS.createBlock("cupric_campfire", () -> new CupricCampfireBlock(Block.Properties.ofFullCopy(Blocks.SOUL_CAMPFIRE)));
	public static final DeferredBlock<Block> CUPRIC_LANTERN = BLOCKS.createBlock("cupric_lantern", () -> new LanternBlock(Block.Properties.ofFullCopy(Blocks.SOUL_LANTERN)));
	public static final DeferredBlock<Block> CUPRIC_WALL_TORCH = BLOCKS.createBlockNoItem("cupric_wall_torch", () -> new CupricWallTorchBlock(Block.Properties.ofFullCopy(Blocks.SOUL_TORCH)));
	public static final DeferredBlock<Block> CUPRIC_TORCH = BLOCKS.createStandingAndWallBlock("cupric_torch", () -> new CupricTorchBlock(Block.Properties.ofFullCopy(Blocks.SOUL_TORCH)), CUPRIC_WALL_TORCH, Direction.DOWN);

	public static final DeferredBlock<Block> ROTTEN_FLESH_BLOCK = BLOCKS.createBlock("rotten_flesh_block", () -> new Block(CCProperties.ROTTEN_FLESH_BLOCK));

	public static final DeferredBlock<Block> GUNPOWDER_BLOCK = BLOCKS.createBlock("gunpowder_block", () -> new GunpowderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.SNARE).strength(0.5F, 0.0F).sound(CCSoundTypes.GUNPOWDER)));

	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> SPARKLER = BLOCKS.createSparklerBlock("sparkler", "wall_sparkler", CCParticleTypes.SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> WHITE_SPARKLER = BLOCKS.createSparklerBlock("white_sparkler", "white_wall_sparkler", CCParticleTypes.WHITE_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> ORANGE_SPARKLER = BLOCKS.createSparklerBlock("orange_sparkler", "orange_wall_sparkler", CCParticleTypes.ORANGE_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> MAGENTA_SPARKLER = BLOCKS.createSparklerBlock("magenta_sparkler", "magenta_wall_sparkler", CCParticleTypes.MAGENTA_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> LIGHT_BLUE_SPARKLER = BLOCKS.createSparklerBlock("light_blue_sparkler", "light_blue_wall_sparkler", CCParticleTypes.LIGHT_BLUE_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> YELLOW_SPARKLER = BLOCKS.createSparklerBlock("yellow_sparkler", "yellow_wall_sparkler", CCParticleTypes.YELLOW_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> LIME_SPARKLER = BLOCKS.createSparklerBlock("lime_sparkler", "lime_wall_sparkler", CCParticleTypes.LIME_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> PINK_SPARKLER = BLOCKS.createSparklerBlock("pink_sparkler", "pink_wall_sparkler", CCParticleTypes.PINK_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> GRAY_SPARKLER = BLOCKS.createSparklerBlock("gray_sparkler", "gray_wall_sparkler", CCParticleTypes.GRAY_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> LIGHT_GRAY_SPARKLER = BLOCKS.createSparklerBlock("light_gray_sparkler", "light_gray_wall_sparkler", CCParticleTypes.LIGHT_GRAY_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> CYAN_SPARKLER = BLOCKS.createSparklerBlock("cyan_sparkler", "cyan_wall_sparkler", CCParticleTypes.CYAN_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> PURPLE_SPARKLER = BLOCKS.createSparklerBlock("purple_sparkler", "purple_wall_sparkler", CCParticleTypes.PURPLE_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> BLUE_SPARKLER = BLOCKS.createSparklerBlock("blue_sparkler", "blue_wall_sparkler", CCParticleTypes.BLUE_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> BROWN_SPARKLER = BLOCKS.createSparklerBlock("brown_sparkler", "brown_wall_sparkler", CCParticleTypes.BROWN_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> GREEN_SPARKLER = BLOCKS.createSparklerBlock("green_sparkler", "green_wall_sparkler", CCParticleTypes.GREEN_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> RED_SPARKLER = BLOCKS.createSparklerBlock("red_sparkler", "red_wall_sparkler", CCParticleTypes.RED_SPARKLER_SPARK);
	public static final Pair<DeferredBlock<SparklerBlock>, DeferredBlock<WallSparklerBlock>> BLACK_SPARKLER = BLOCKS.createSparklerBlock("black_sparkler", "black_wall_sparkler", CCParticleTypes.BLACK_SPARKLER_SPARK);

	public static final DeferredBlock<Block> DEEPER_HEAD = BLOCKS.createBlockNoItem("deeper_head", () -> new DeeperSkullBlock(CCSkullTypes.DEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> DEEPER_WALL_HEAD = BLOCKS.createBlockNoItem("deeper_wall_head", () -> new DeeperWallSkullBlock(CCSkullTypes.DEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(DEEPER_HEAD.get())));
	public static final DeferredBlock<Block> EVENDEEPER_HEAD = BLOCKS.createBlockNoItem("evendeeper_head", () -> new DeeperSkullBlock(CCSkullTypes.EVENDEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> EVENDEEPER_WALL_HEAD = BLOCKS.createBlockNoItem("evendeeper_wall_head", () -> new DeeperWallSkullBlock(CCSkullTypes.EVENDEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(EVENDEEPER_HEAD.get())));
	public static final DeferredBlock<Block> PEEPER_HEAD = BLOCKS.createBlockNoItem("peeper_head", () -> new CCSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> PEEPER_WALL_HEAD = BLOCKS.createBlockNoItem("peeper_wall_head", () -> new WallSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(PEEPER_HEAD.get())));
	public static final DeferredBlock<Block> MIME_HEAD = BLOCKS.createBlockNoItem("mime_head", () -> new CCSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> MIME_WALL_HEAD = BLOCKS.createBlockNoItem("mime_wall_head", () -> new WallSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(MIME_HEAD.get())));

	public static final DeferredBlock<Block> TMT = BLOCKS.createBlock("tmt", () -> new TmtBlock(CCProperties.TMT));

	public static final DeferredBlock<Block> SCATTERER = BLOCKS.createBlock("scatterer", () -> new ScattererBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER).sound(CCSoundTypes.TIN)));
	public static final DeferredBlock<Block> SPLURTER = BLOCKS.createBlock("splurter", () -> new SplurterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER).sound(CCSoundTypes.TIN)));

	public static final DeferredBlock<Block> FLOODLIGHT = BLOCKS.createBlock("floodlight", () -> new WeatheringFloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT));
	public static final DeferredBlock<Block> EXPOSED_FLOODLIGHT = BLOCKS.createBlock("exposed_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT));
	public static final DeferredBlock<Block> WEATHERED_FLOODLIGHT = BLOCKS.createBlock("weathered_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT));
	public static final DeferredBlock<Block> OXIDIZED_FLOODLIGHT = BLOCKS.createBlock("oxidized_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_FLOODLIGHT));
	public static final DeferredBlock<Block> WAXED_FLOODLIGHT = BLOCKS.createBlock("waxed_floodlight", () -> new FloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT));
	public static final DeferredBlock<Block> WAXED_EXPOSED_FLOODLIGHT = BLOCKS.createBlock("waxed_exposed_floodlight", () -> new FloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT));
	public static final DeferredBlock<Block> WAXED_WEATHERED_FLOODLIGHT = BLOCKS.createBlock("waxed_weathered_floodlight", () -> new FloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_FLOODLIGHT = BLOCKS.createBlock("waxed_oxidized_floodlight", () -> new FloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_FLOODLIGHT));

	public static final DeferredBlock<Block> TOOLBOX = BLOCKS.createToolboxBlock("toolbox", () -> new WeatheringToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> EXPOSED_TOOLBOX = BLOCKS.createToolboxBlock("exposed_toolbox", () -> new WeatheringToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> WEATHERED_TOOLBOX = BLOCKS.createToolboxBlock("weathered_toolbox", () -> new WeatheringToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> OXIDIZED_TOOLBOX = BLOCKS.createToolboxBlock("oxidized_toolbox", () -> new WeatheringToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> WAXED_TOOLBOX = BLOCKS.createToolboxBlock("waxed_toolbox", () -> new ToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> WAXED_EXPOSED_TOOLBOX = BLOCKS.createToolboxBlock("waxed_exposed_toolbox", () -> new ToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> WAXED_WEATHERED_TOOLBOX = BLOCKS.createToolboxBlock("waxed_weathered_toolbox", () -> new ToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_TOOLBOX = BLOCKS.createToolboxBlock("waxed_oxidized_toolbox", () -> new ToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));

	public static final DeferredBlock<Block> COPPER_BARS = BLOCKS.createBlock("copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> EXPOSED_COPPER_BARS = BLOCKS.createBlock("exposed_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.EXPOSED, CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> WEATHERED_COPPER_BARS = BLOCKS.createBlock("weathered_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.WEATHERED, CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_BARS = BLOCKS.createBlock("oxidized_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.OXIDIZED, CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> WAXED_COPPER_BARS = BLOCKS.createBlock("waxed_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_BARS = BLOCKS.createBlock("waxed_exposed_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_BARS = BLOCKS.createBlock("waxed_weathered_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_BARS = BLOCKS.createBlock("waxed_oxidized_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));

	public static final DeferredBlock<Block> LIFT_BUTTON = BLOCKS.createBlock("lift_button", () -> new WeatheringLiftButtonBlock(WeatherState.UNAFFECTED, 10, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> EXPOSED_LIFT_BUTTON = BLOCKS.createBlock("exposed_lift_button", () -> new WeatheringLiftButtonBlock(WeatherState.EXPOSED, 20, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> WEATHERED_LIFT_BUTTON = BLOCKS.createBlock("weathered_lift_button", () -> new WeatheringLiftButtonBlock(WeatherState.WEATHERED, 30, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> OXIDIZED_LIFT_BUTTON = BLOCKS.createBlock("oxidized_lift_button", () -> new WeatheringLiftButtonBlock(WeatherState.OXIDIZED, 40, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> WAXED_LIFT_BUTTON = BLOCKS.createBlock("waxed_lift_button", () -> new LiftButtonBlock(WeatherState.UNAFFECTED, 10, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> WAXED_EXPOSED_LIFT_BUTTON = BLOCKS.createBlock("waxed_exposed_lift_button", () -> new LiftButtonBlock(WeatherState.EXPOSED, 20, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> WAXED_WEATHERED_LIFT_BUTTON = BLOCKS.createBlock("waxed_weathered_lift_button", () -> new LiftButtonBlock(WeatherState.WEATHERED, 30, CCProperties.LIFT_BUTTON));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_LIFT_BUTTON = BLOCKS.createBlock("waxed_oxidized_lift_button", () -> new LiftButtonBlock(WeatherState.OXIDIZED, 40, CCProperties.LIFT_BUTTON));

	public static final DeferredBlock<Block> LIFT_PRESSURE_PLATE = BLOCKS.createBlock("lift_pressure_plate", () -> new WeatheringLiftPlateBlock(WeatherState.UNAFFECTED, 10, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> EXPOSED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("exposed_lift_pressure_plate", () -> new WeatheringLiftPlateBlock(WeatherState.EXPOSED, 20, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> WEATHERED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("weathered_lift_pressure_plate", () -> new WeatheringLiftPlateBlock(WeatherState.WEATHERED, 30, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> OXIDIZED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("oxidized_lift_pressure_plate", () -> new WeatheringLiftPlateBlock(WeatherState.OXIDIZED, 40, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> WAXED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("waxed_lift_pressure_plate", () -> new LiftPlateBlock(WeatherState.UNAFFECTED, 10, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> WAXED_EXPOSED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("waxed_exposed_lift_pressure_plate", () -> new LiftPlateBlock(WeatherState.EXPOSED, 20, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> WAXED_WEATHERED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("waxed_weathered_lift_pressure_plate", () -> new LiftPlateBlock(WeatherState.WEATHERED, 30, CCProperties.LIFT_PRESSURE_PLATE));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_LIFT_PRESSURE_PLATE = BLOCKS.createBlock("waxed_oxidized_lift_pressure_plate", () -> new LiftPlateBlock(WeatherState.OXIDIZED, 40, CCProperties.LIFT_PRESSURE_PLATE));

	public static final DeferredBlock<Block> EXPOSED_LIGHTNING_ROD = BLOCKS.createBlock("exposed_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));
	public static final DeferredBlock<Block> WEATHERED_LIGHTNING_ROD = BLOCKS.createBlock("weathered_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));
	public static final DeferredBlock<Block> OXIDIZED_LIGHTNING_ROD = BLOCKS.createBlock("oxidized_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));
	public static final DeferredBlock<Block> WAXED_LIGHTNING_ROD = BLOCKS.createBlock("waxed_lightning_rod", () -> new LightningRodBlock(WeatheringLightningRodBlock.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));
	public static final DeferredBlock<Block> WAXED_EXPOSED_LIGHTNING_ROD = BLOCKS.createBlock("waxed_exposed_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));
	public static final DeferredBlock<Block> WAXED_WEATHERED_LIGHTNING_ROD = BLOCKS.createBlock("waxed_weathered_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_LIGHTNING_ROD = BLOCKS.createBlock("waxed_oxidized_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD)));

	public static final DeferredBlock<Block> COPPER_CHAIN = BLOCKS.createBlock("copper_chain", () -> new WeatheringChainBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> EXPOSED_COPPER_CHAIN = BLOCKS.createBlock("exposed_copper_chain", () -> new WeatheringChainBlock(WeatherState.EXPOSED, CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> WEATHERED_COPPER_CHAIN = BLOCKS.createBlock("weathered_copper_chain", () -> new WeatheringChainBlock(WeatherState.WEATHERED, CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_CHAIN = BLOCKS.createBlock("oxidized_copper_chain", () -> new WeatheringChainBlock(WeatherState.OXIDIZED, CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> WAXED_COPPER_CHAIN = BLOCKS.createBlock("waxed_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_CHAIN = BLOCKS.createBlock("waxed_exposed_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_CHAIN = BLOCKS.createBlock("waxed_weathered_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_CHAIN = BLOCKS.createBlock("waxed_oxidized_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));

	public static final DeferredBlock<Block> COPPER_LANTERN = BLOCKS.createBlock("copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> EXPOSED_COPPER_LANTERN = BLOCKS.createBlock("exposed_copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.EXPOSED, CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> WEATHERED_COPPER_LANTERN = BLOCKS.createBlock("weathered_copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.WEATHERED, CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_LANTERN = BLOCKS.createBlock("oxidized_copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.OXIDIZED, CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> WAXED_COPPER_LANTERN = BLOCKS.createBlock("waxed_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_LANTERN = BLOCKS.createBlock("waxed_exposed_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_LANTERN = BLOCKS.createBlock("waxed_weathered_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_LANTERN = BLOCKS.createBlock("waxed_oxidized_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));

	public static final DeferredBlock<Block> LAVA_LAMP = BLOCKS.createBlock("lava_lamp", () -> new LavaLampBlock(CCProperties.LAVA_LAMP));
	public static final DeferredBlock<Block> GOLDEN_BARS = BLOCKS.createBlock("golden_bars", () -> new IronBarsBlock(CCProperties.GOLDEN_BARS));
	public static final DeferredBlock<Block> GOLDEN_CHAIN = BLOCKS.createBlock("golden_chain", () -> new ChainBlock(CCProperties.GOLDEN_CHAIN));

	public static final DeferredBlock<Block> LAPIS_LAZULI_BRICKS = BLOCKS.createBlock("lapis_bricks", () -> new Block(CCProperties.LAPIS_LAZULI));
	public static final DeferredBlock<Block> LAPIS_LAZULI_BRICK_STAIRS = BLOCKS.createBlock("lapis_brick_stairs", () -> new StairBlock(LAPIS_LAZULI_BRICKS.get().defaultBlockState(), CCProperties.LAPIS_LAZULI));
	public static final DeferredBlock<Block> LAPIS_LAZULI_BRICK_SLAB = BLOCKS.createBlock("lapis_brick_slab", () -> new SlabBlock(CCProperties.LAPIS_LAZULI));
	public static final DeferredBlock<Block> LAPIS_LAZULI_BRICK_WALL = BLOCKS.createBlock("lapis_brick_wall", () -> new WallBlock(CCProperties.LAPIS_LAZULI));
	public static final DeferredBlock<Block> LAPIS_LAZULI_PILLAR = BLOCKS.createBlock("lapis_pillar", () -> new RotatedPillarBlock(CCProperties.LAPIS_LAZULI));
	public static final DeferredBlock<Block> LAPIS_LAZULI_LAMP = BLOCKS.createBlock("lapis_lamp", () -> new Block(CCProperties.LAMP));

	public static final DeferredBlock<Block> SPINEL_ORE = BLOCKS.createBlock("spinel_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), CCProperties.ORE));
	public static final DeferredBlock<Block> DEEPSLATE_SPINEL_ORE = BLOCKS.createBlock("deepslate_spinel_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), CCProperties.DEEPSLATE_ORE));
	public static final DeferredBlock<Block> SPINEL_BLOCK = BLOCKS.createBlock("spinel_block", () -> new Block(CCProperties.SPINEL));
	public static final DeferredBlock<Block> SPINEL_BRICKS = BLOCKS.createBlock("spinel_bricks", () -> new Block(CCProperties.SPINEL));
	public static final DeferredBlock<Block> SPINEL_BRICK_STAIRS = BLOCKS.createBlock("spinel_brick_stairs", () -> new StairBlock(SPINEL_BRICKS.get().defaultBlockState(), CCProperties.SPINEL));
	public static final DeferredBlock<Block> SPINEL_BRICK_SLAB = BLOCKS.createBlock("spinel_brick_slab", () -> new SlabBlock(CCProperties.SPINEL));
	public static final DeferredBlock<Block> SPINEL_BRICK_WALL = BLOCKS.createBlock("spinel_brick_wall", () -> new WallBlock(CCProperties.SPINEL));
	public static final DeferredBlock<Block> SPINEL_PILLAR = BLOCKS.createBlock("spinel_pillar", () -> new RotatedPillarBlock(CCProperties.SPINEL));
	public static final DeferredBlock<Block> SPINEL_LAMP = BLOCKS.createBlock("spinel_lamp", () -> new Block(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get()).sound(CCSoundTypes.SPINEL)));

	public static final DeferredBlock<Block> DISMANTLING_TABLE = BLOCKS.createBlock("dismantling_table", () -> new DismantlingTableBlock(CCProperties.DISMANTLING_TABLE));
	public static final DeferredBlock<Block> BEJEWELED_ANVIL = BLOCKS.createBlock("bejeweled_anvil", () -> new BejeweledAnvilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).sound(CCSoundTypes.BEJEWELED_ANVIL)));
	public static final DeferredBlock<Block> ATONING_TABLE = BLOCKS.createBlock("atoning_table", () -> new AtoningTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ENCHANTING_TABLE).sound(CCSoundTypes.ATONING_TABLE)));

	public static final DeferredBlock<Block> ZIRCONIA_BLOCK = BLOCKS.createBlock("zirconia_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.ZIRCONIA)));
	public static final DeferredBlock<Block> ZIRCONIA_LAMP = BLOCKS.createBlock("zirconia_lamp", () -> new Block(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get()).sound(CCSoundTypes.ORNATE_GLASS)));
	public static final DeferredBlock<Block> ORNATE_GLASS = BLOCKS.createBlock("ornate_glass", () -> new OrnateGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).sound(CCSoundTypes.ORNATE_GLASS)));
	public static final DeferredBlock<Block> ORNATE_GLASS_PANE = BLOCKS.createBlock("ornate_glass_pane", () -> new OrnateGlassPaneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(CCSoundTypes.ORNATE_GLASS)));

	public static final DeferredBlock<Block> TURQUOISE_ORE = BLOCKS.createBlock("turquoise_ore", () -> new TurquoiseOreBlock(UniformInt.of(4, 9), CCProperties.ORE), CCProperties.FANCY);
	public static final DeferredBlock<Block> DEEPSLATE_TURQUOISE_ORE = BLOCKS.createBlock("deepslate_turquoise_ore", () -> new TurquoiseOreBlock(UniformInt.of(4, 9), CCProperties.DEEPSLATE_ORE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_BLOCK = BLOCKS.createBlock("turquoise_block", () -> new TurquoiseBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_TILES = BLOCKS.createBlock("turquoise_tiles", () -> new TurquoiseBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_TILE_STAIRS = BLOCKS.createBlock("turquoise_tile_stairs", () -> new TurquoiseStairBlock(TURQUOISE_TILES.get().defaultBlockState(), CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_TILE_SLAB = BLOCKS.createBlock("turquoise_tile_slab", () -> new TurquoiseSlabBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_TILE_WALL = BLOCKS.createBlock("turquoise_tile_wall", () -> new TurquoiseWallBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_PILLAR = BLOCKS.createBlock("turquoise_pillar", () -> new TurquoisePillarBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final DeferredBlock<Block> TURQUOISE_LAMP = BLOCKS.createBlock("turquoise_lamp", () -> new TurquoiseBlock(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get()).sound(CCSoundTypes.TURQUOISE)), CCProperties.FANCY);

	public static final DeferredBlock<Block> CAVIAR = BLOCKS.createBlockNoItem("caviar", () -> new CaviarBlock(PropertyUtil.flowerPot().sound(CCSoundTypes.CAVIAR)));

	public static final DeferredBlock<Block> FROSTED_GLASS = BLOCKS.createBlock("frosted_glass", () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
	public static final DeferredBlock<Block> FROSTED_GLASS_PANE = BLOCKS.createBlock("frosted_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE)));

	public static final DeferredBlock<Block> QUARTZ_LAMP = BLOCKS.createBlock("quartz_lamp", () -> new Block(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get())));
	public static final DeferredBlock<Block> DIAMOND_LAMP = BLOCKS.createBlock("diamond_lamp", () -> new Block(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get())));
	public static final DeferredBlock<Block> EMERALD_LAMP = BLOCKS.createBlock("emerald_lamp", () -> new Block(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get())));

	public static final DeferredBlock<Block> ROCKY_DIRT = BLOCKS.createBlock("rocky_dirt", () -> new Block(CCProperties.ROCKY_DIRT));
	public static final DeferredBlock<Block> FRAGILE_STONE = BLOCKS.createBlock("fragile_stone", () -> new FragileStoneBlock(CCProperties.FRAGILE_STONE));
	public static final DeferredBlock<Block> FRAGILE_DEEPSLATE = BLOCKS.createBlock("fragile_deepslate", () -> new FragileDeepslateBlock(CCProperties.FRAGILE_DEEPSLATE));

	public static final DeferredBlock<Block> COBBLESTONE_BRICKS = BLOCKS.createBlock("cobblestone_bricks", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_BRICK_STAIRS = BLOCKS.createBlock("cobblestone_brick_stairs", () -> new StairBlock(COBBLESTONE_BRICKS.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_BRICK_SLAB = BLOCKS.createBlock("cobblestone_brick_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_BRICK_WALL = BLOCKS.createBlock("cobblestone_brick_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_TILES = BLOCKS.createBlock("cobblestone_tiles", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_TILE_STAIRS = BLOCKS.createBlock("cobblestone_tile_stairs", () -> new StairBlock(COBBLESTONE_TILES.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_TILE_SLAB = BLOCKS.createBlock("cobblestone_tile_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> COBBLESTONE_TILE_WALL = BLOCKS.createBlock("cobblestone_tile_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));

	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_BRICKS = BLOCKS.createBlock("mossy_cobblestone_bricks", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_BRICK_STAIRS = BLOCKS.createBlock("mossy_cobblestone_brick_stairs", () -> new StairBlock(COBBLESTONE_BRICKS.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_BRICK_SLAB = BLOCKS.createBlock("mossy_cobblestone_brick_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_BRICK_WALL = BLOCKS.createBlock("mossy_cobblestone_brick_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_TILES = BLOCKS.createBlock("mossy_cobblestone_tiles", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_TILE_STAIRS = BLOCKS.createBlock("mossy_cobblestone_tile_stairs", () -> new StairBlock(COBBLESTONE_TILES.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_TILE_SLAB = BLOCKS.createBlock("mossy_cobblestone_tile_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final DeferredBlock<Block> MOSSY_COBBLESTONE_TILE_WALL = BLOCKS.createBlock("mossy_cobblestone_tile_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));

	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_BRICKS = BLOCKS.createBlock("cobbled_deepslate_bricks", () -> new Block(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_BRICK_STAIRS = BLOCKS.createBlock("cobbled_deepslate_brick_stairs", () -> new StairBlock(COBBLED_DEEPSLATE_BRICKS.get().defaultBlockState(), CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_BRICK_SLAB = BLOCKS.createBlock("cobbled_deepslate_brick_slab", () -> new SlabBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_BRICK_WALL = BLOCKS.createBlock("cobbled_deepslate_brick_wall", () -> new WallBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_TILES = BLOCKS.createBlock("cobbled_deepslate_tiles", () -> new Block(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_TILE_STAIRS = BLOCKS.createBlock("cobbled_deepslate_tile_stairs", () -> new StairBlock(COBBLED_DEEPSLATE_TILES.get().defaultBlockState(), CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_TILE_SLAB = BLOCKS.createBlock("cobbled_deepslate_tile_slab", () -> new SlabBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final DeferredBlock<Block> COBBLED_DEEPSLATE_TILE_WALL = BLOCKS.createBlock("cobbled_deepslate_tile_wall", () -> new WallBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));

	public static final DeferredBlock<Block> STONE_WALL = BLOCKS.createBlock("stone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
	public static final DeferredBlock<Block> DEEPSLATE_STAIRS = BLOCKS.createBlock("deepslate_stairs", () -> new StairBlock(Blocks.DEEPSLATE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
	public static final DeferredBlock<Block> DEEPSLATE_SLAB = BLOCKS.createBlock("deepslate_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
	public static final DeferredBlock<Block> DEEPSLATE_WALL = BLOCKS.createBlock("deepslate_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));

	public static final DeferredBlock<Block> POLISHED_GRANITE_WALL = BLOCKS.createBlock("polished_granite_wall", () -> new WallBlock(CCProperties.GRANITE));
	public static final DeferredBlock<Block> CHISELED_POLISHED_GRANITE = BLOCKS.createBlock("chiseled_polished_granite", () -> new Block(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_BRICKS = BLOCKS.createBlock("granite_bricks", () -> new Block(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_BRICK_STAIRS = BLOCKS.createBlock("granite_brick_stairs", () -> new StairBlock(GRANITE_BRICKS.get().defaultBlockState(), CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_BRICK_SLAB = BLOCKS.createBlock("granite_brick_slab", () -> new SlabBlock(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_BRICK_WALL = BLOCKS.createBlock("granite_brick_wall", () -> new WallBlock(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_PILLAR = BLOCKS.createBlock("granite_pillar", () -> new RotatedPillarBlock(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_TILES = BLOCKS.createBlock("granite_tiles", () -> new Block(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_TILE_STAIRS = BLOCKS.createBlock("granite_tile_stairs", () -> new StairBlock(GRANITE_TILES.get().defaultBlockState(), CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_TILE_SLAB = BLOCKS.createBlock("granite_tile_slab", () -> new SlabBlock(CCProperties.GRANITE));
	public static final DeferredBlock<Block> GRANITE_TILE_WALL = BLOCKS.createBlock("granite_tile_wall", () -> new WallBlock(CCProperties.GRANITE));

	public static final DeferredBlock<Block> POLISHED_DIORITE_WALL = BLOCKS.createBlock("polished_diorite_wall", () -> new WallBlock(CCProperties.DIORITE));
	public static final DeferredBlock<Block> CHISELED_POLISHED_DIORITE = BLOCKS.createBlock("chiseled_polished_diorite", () -> new Block(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_BRICKS = BLOCKS.createBlock("diorite_bricks", () -> new Block(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_BRICK_STAIRS = BLOCKS.createBlock("diorite_brick_stairs", () -> new StairBlock(DIORITE_BRICKS.get().defaultBlockState(), CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_BRICK_SLAB = BLOCKS.createBlock("diorite_brick_slab", () -> new SlabBlock(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_BRICK_WALL = BLOCKS.createBlock("diorite_brick_wall", () -> new WallBlock(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_PILLAR = BLOCKS.createBlock("diorite_pillar", () -> new RotatedPillarBlock(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_TILES = BLOCKS.createBlock("diorite_tiles", () -> new Block(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_TILE_STAIRS = BLOCKS.createBlock("diorite_tile_stairs", () -> new StairBlock(DIORITE_TILES.get().defaultBlockState(), CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_TILE_SLAB = BLOCKS.createBlock("diorite_tile_slab", () -> new SlabBlock(CCProperties.DIORITE));
	public static final DeferredBlock<Block> DIORITE_TILE_WALL = BLOCKS.createBlock("diorite_tile_wall", () -> new WallBlock(CCProperties.DIORITE));

	public static final DeferredBlock<Block> POLISHED_ANDESITE_WALL = BLOCKS.createBlock("polished_andesite_wall", () -> new WallBlock(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> CHISELED_POLISHED_ANDESITE = BLOCKS.createBlock("chiseled_polished_andesite", () -> new Block(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_BRICKS = BLOCKS.createBlock("andesite_bricks", () -> new Block(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_BRICK_STAIRS = BLOCKS.createBlock("andesite_brick_stairs", () -> new StairBlock(ANDESITE_BRICKS.get().defaultBlockState(), CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_BRICK_SLAB = BLOCKS.createBlock("andesite_brick_slab", () -> new SlabBlock(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_BRICK_WALL = BLOCKS.createBlock("andesite_brick_wall", () -> new WallBlock(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_PILLAR = BLOCKS.createBlock("andesite_pillar", () -> new RotatedPillarBlock(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_TILES = BLOCKS.createBlock("andesite_tiles", () -> new Block(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_TILE_STAIRS = BLOCKS.createBlock("andesite_tile_stairs", () -> new StairBlock(ANDESITE_TILES.get().defaultBlockState(), CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_TILE_SLAB = BLOCKS.createBlock("andesite_tile_slab", () -> new SlabBlock(CCProperties.ANDESITE));
	public static final DeferredBlock<Block> ANDESITE_TILE_WALL = BLOCKS.createBlock("andesite_tile_wall", () -> new WallBlock(CCProperties.ANDESITE));

	public static final DeferredBlock<Block> CALCITE_STAIRS = BLOCKS.createBlock("calcite_stairs", () -> new StairBlock(Blocks.CALCITE.defaultBlockState(), CCProperties.CALCITE));
	public static final DeferredBlock<Block> CALCITE_SLAB = BLOCKS.createBlock("calcite_slab", () -> new SlabBlock(CCProperties.CALCITE));
	public static final DeferredBlock<Block> CALCITE_WALL = BLOCKS.createBlock("calcite_wall", () -> new WallBlock(CCProperties.CALCITE));
	public static final DeferredBlock<Block> POLISHED_CALCITE = BLOCKS.createBlock("polished_calcite", () -> new Block(CCProperties.POLISHED_CALCITE));
	public static final DeferredBlock<Block> POLISHED_CALCITE_STAIRS = BLOCKS.createBlock("polished_calcite_stairs", () -> new StairBlock(POLISHED_CALCITE.get().defaultBlockState(), CCProperties.POLISHED_CALCITE));
	public static final DeferredBlock<Block> POLISHED_CALCITE_SLAB = BLOCKS.createBlock("polished_calcite_slab", () -> new SlabBlock(CCProperties.POLISHED_CALCITE));
	public static final DeferredBlock<Block> POLISHED_CALCITE_WALL = BLOCKS.createBlock("polished_calcite_wall", () -> new WallBlock(CCProperties.POLISHED_CALCITE));
	public static final DeferredBlock<Block> CHISELED_POLISHED_CALCITE = BLOCKS.createBlock("chiseled_calcite", () -> new BlueprintDirectionalBlock(CCProperties.POLISHED_CALCITE));
	public static final DeferredBlock<Block> CALCITE_PILLAR = BLOCKS.createBlock("calcite_pillar", () -> new RotatedPillarBlock(CCProperties.CALCITE_BRICKS));
	public static final DeferredBlock<Block> CALCITE_BRICKS = BLOCKS.createBlock("calcite_bricks", () -> new Block(CCProperties.CALCITE_BRICKS));
	public static final DeferredBlock<Block> CALCITE_BRICK_STAIRS = BLOCKS.createBlock("calcite_brick_stairs", () -> new StairBlock(CALCITE_BRICKS.get().defaultBlockState(), CCProperties.CALCITE_BRICKS));
	public static final DeferredBlock<Block> CALCITE_BRICK_SLAB = BLOCKS.createBlock("calcite_brick_slab", () -> new SlabBlock(CCProperties.CALCITE_BRICKS));
	public static final DeferredBlock<Block> CALCITE_BRICK_WALL = BLOCKS.createBlock("calcite_brick_wall", () -> new WallBlock(CCProperties.CALCITE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_CALCITE_BRICKS = BLOCKS.createBlock("chiseled_calcite_bricks", () -> new Block(CCProperties.CALCITE_BRICKS));

	public static final DeferredBlock<Block> POLISHED_TUFF = BLOCKS.createBlock("polished_tuff", () -> new Block(CCProperties.POLISHED_TUFF));
	public static final DeferredBlock<Block> POLISHED_TUFF_STAIRS = BLOCKS.createBlock("polished_tuff_stairs", () -> new StairBlock(POLISHED_TUFF.get().defaultBlockState(), CCProperties.POLISHED_TUFF));
	public static final DeferredBlock<Block> POLISHED_TUFF_SLAB = BLOCKS.createBlock("polished_tuff_slab", () -> new SlabBlock(CCProperties.POLISHED_TUFF));
	public static final DeferredBlock<Block> POLISHED_TUFF_WALL = BLOCKS.createBlock("polished_tuff_wall", () -> new WallBlock(CCProperties.POLISHED_TUFF));
	public static final DeferredBlock<Block> TUFF_BRICKS = BLOCKS.createBlock("tuff_bricks", () -> new Block(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_BRICK_STAIRS = BLOCKS.createBlock("tuff_brick_stairs", () -> new StairBlock(TUFF_BRICKS.get().defaultBlockState(), CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_BRICK_SLAB = BLOCKS.createBlock("tuff_brick_slab", () -> new SlabBlock(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_BRICK_WALL = BLOCKS.createBlock("tuff_brick_wall", () -> new WallBlock(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> CHISELED_TUFF_BRICKS = BLOCKS.createBlock("chiseled_tuff_bricks", () -> new Block(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_PILLAR = BLOCKS.createBlock("tuff_pillar", () -> new RotatedPillarBlock(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_TILES = BLOCKS.createBlock("tuff_tiles", () -> new Block(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_TILE_STAIRS = BLOCKS.createBlock("tuff_tile_stairs", () -> new StairBlock(TUFF_TILES.get().defaultBlockState(), CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_TILE_SLAB = BLOCKS.createBlock("tuff_tile_slab", () -> new SlabBlock(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> TUFF_TILE_WALL = BLOCKS.createBlock("tuff_tile_wall", () -> new WallBlock(CCProperties.TUFF_BRICKS));
	public static final DeferredBlock<Block> SMOOTH_TUFF = BLOCKS.createBlock("smooth_tuff", () -> new Block(CCProperties.TUFF));
	public static final DeferredBlock<Block> SMOOTH_TUFF_STAIRS = BLOCKS.createBlock("smooth_tuff_stairs", () -> new StairBlock(SMOOTH_TUFF.get().defaultBlockState(), CCProperties.TUFF));
	public static final DeferredBlock<Block> SMOOTH_TUFF_SLAB = BLOCKS.createBlock("smooth_tuff_slab", () -> new SlabBlock(CCProperties.TUFF));
	public static final DeferredBlock<Block> SMOOTH_TUFF_WALL = BLOCKS.createBlock("smooth_tuff_wall", () -> new WallBlock(CCProperties.TUFF));

	public static final DeferredBlock<Block> SCHIST = BLOCKS.createBlock("schist", () -> new RotatedPillarBlock(CCProperties.SCHIST));
	public static final DeferredBlock<Block> SCHIST_STAIRS = BLOCKS.createBlock("schist_stairs", () -> new StairBlock(SCHIST.get().defaultBlockState(), CCProperties.SCHIST));
	public static final DeferredBlock<Block> SCHIST_SLAB = BLOCKS.createBlock("schist_slab", () -> new SlabBlock(CCProperties.SCHIST));
	public static final DeferredBlock<Block> SCHIST_WALL = BLOCKS.createBlock("schist_wall", () -> new WallBlock(CCProperties.SCHIST));
	public static final DeferredBlock<Block> SMOOTH_SCHIST = BLOCKS.createBlock("smooth_schist", () -> new Block(CCProperties.SCHIST));
	public static final DeferredBlock<Block> SMOOTH_SCHIST_STAIRS = BLOCKS.createBlock("smooth_schist_stairs", () -> new StairBlock(SCHIST.get().defaultBlockState(), CCProperties.SCHIST));
	public static final DeferredBlock<Block> SMOOTH_SCHIST_SLAB = BLOCKS.createBlock("smooth_schist_slab", () -> new SlabBlock(CCProperties.SCHIST));
	public static final DeferredBlock<Block> SMOOTH_SCHIST_WALL = BLOCKS.createBlock("smooth_schist_wall", () -> new WallBlock(CCProperties.SCHIST));
	public static final DeferredBlock<Block> SCHIST_PILLAR = BLOCKS.createBlock("schist_pillar", () -> new RotatedPillarBlock(CCProperties.SCHIST_BRICKS));

	public static final DeferredBlock<Block> SUGILITE = BLOCKS.createBlock("sugilite", () -> new Block(CCProperties.SUGILITE));
	public static final DeferredBlock<Block> SUGILITE_STAIRS = BLOCKS.createBlock("sugilite_stairs", () -> new StairBlock(SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE));
	public static final DeferredBlock<Block> SUGILITE_SLAB = BLOCKS.createBlock("sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE));
	public static final DeferredBlock<Block> SUGILITE_WALL = BLOCKS.createBlock("sugilite_wall", () -> new WallBlock(CCProperties.SUGILITE));
	public static final DeferredBlock<Block> POLISHED_SUGILITE = BLOCKS.createBlock("polished_sugilite", () -> new Block(CCProperties.POLISHED_SUGILITE));
	public static final DeferredBlock<Block> POLISHED_SUGILITE_STAIRS = BLOCKS.createBlock("polished_sugilite_stairs", () -> new StairBlock(POLISHED_SUGILITE.get().defaultBlockState(), CCProperties.POLISHED_SUGILITE));
	public static final DeferredBlock<Block> POLISHED_SUGILITE_SLAB = BLOCKS.createBlock("polished_sugilite_slab", () -> new SlabBlock(CCProperties.POLISHED_SUGILITE));
	public static final DeferredBlock<Block> POLISHED_SUGILITE_WALL = BLOCKS.createBlock("polished_sugilite_wall", () -> new WallBlock(CCProperties.POLISHED_SUGILITE));
	public static final DeferredBlock<Block> SUGILITE_BRICKS = BLOCKS.createBlock("sugilite_bricks", () -> new Block(CCProperties.SUGILITE_BRICKS));
	public static final DeferredBlock<Block> SUGILITE_BRICK_STAIRS = BLOCKS.createBlock("sugilite_brick_stairs", () -> new StairBlock(SUGILITE_BRICKS.get().defaultBlockState(), CCProperties.SUGILITE_BRICKS));
	public static final DeferredBlock<Block> SUGILITE_BRICK_SLAB = BLOCKS.createBlock("sugilite_brick_slab", () -> new SlabBlock(CCProperties.SUGILITE_BRICKS));
	public static final DeferredBlock<Block> SUGILITE_BRICK_WALL = BLOCKS.createBlock("sugilite_brick_wall", () -> new WallBlock(CCProperties.SUGILITE_BRICKS));
	public static final DeferredBlock<Block> SUGILITE_PILLAR = BLOCKS.createBlock("sugilite_pillar", () -> new RotatedPillarBlock(CCProperties.SUGILITE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_SUGILITE_BRICKS = BLOCKS.createBlock("chiseled_sugilite_bricks", () -> new Block(CCProperties.SUGILITE_BRICKS));

	public static final DeferredBlock<Block> CYLINDRITE = BLOCKS.createBlock("cylindrite", () -> new RotatedPillarBlock(CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> CYLINDRITE_STAIRS = BLOCKS.createBlock("cylindrite_stairs", () -> new StairBlock(CYLINDRITE.get().defaultBlockState(), CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> CYLINDRITE_SLAB = BLOCKS.createBlock("cylindrite_slab", () -> new SlabBlock(CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> CYLINDRITE_WALL = BLOCKS.createBlock("cylindrite_wall", () -> new WallBlock(CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> SMOOTH_CYLINDRITE = BLOCKS.createBlock("smooth_cylindrite", () -> new Block(CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> SMOOTH_CYLINDRITE_STAIRS = BLOCKS.createBlock("smooth_cylindrite_stairs", () -> new StairBlock(CYLINDRITE.get().defaultBlockState(), CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> SMOOTH_CYLINDRITE_SLAB = BLOCKS.createBlock("smooth_cylindrite_slab", () -> new SlabBlock(CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> SMOOTH_CYLINDRITE_WALL = BLOCKS.createBlock("smooth_cylindrite_wall", () -> new WallBlock(CCProperties.CYLINDRITE));
	public static final DeferredBlock<Block> CYLINDRITE_BRICKS = BLOCKS.createBlock("cylindrite_bricks", () -> new Block(CCProperties.CYLINDRITE_BRICKS));
	public static final DeferredBlock<Block> CYLINDRITE_BRICK_STAIRS = BLOCKS.createBlock("cylindrite_brick_stairs", () -> new StairBlock(CYLINDRITE.get().defaultBlockState(), CCProperties.CYLINDRITE_BRICKS));
	public static final DeferredBlock<Block> CYLINDRITE_BRICK_SLAB = BLOCKS.createBlock("cylindrite_brick_slab", () -> new SlabBlock(CCProperties.CYLINDRITE_BRICKS));
	public static final DeferredBlock<Block> CYLINDRITE_BRICK_WALL = BLOCKS.createBlock("cylindrite_brick_wall", () -> new WallBlock(CCProperties.CYLINDRITE_BRICKS));
	public static final DeferredBlock<Block> CYLINDRITE_PILLAR = BLOCKS.createBlock("cylindrite_pillar", () -> new RotatedPillarBlock(CCProperties.CYLINDRITE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_CYLINDRITE_BRICKS = BLOCKS.createBlock("chiseled_cylindrite_bricks", () -> new Block(CCProperties.CYLINDRITE_BRICKS));
	public static final DeferredBlock<Block> POLISHED_CYLINDRITE = BLOCKS.createBlock("polished_cylindrite", () -> new Block(CCProperties.POLISHED_CYLINDRITE));
	public static final DeferredBlock<Block> POLISHED_CYLINDRITE_STAIRS = BLOCKS.createBlock("polished_cylindrite_stairs", () -> new StairBlock(POLISHED_CYLINDRITE.get().defaultBlockState(), CCProperties.POLISHED_CYLINDRITE));
	public static final DeferredBlock<Block> POLISHED_CYLINDRITE_SLAB = BLOCKS.createBlock("polished_cylindrite_slab", () -> new SlabBlock(CCProperties.POLISHED_CYLINDRITE));
	public static final DeferredBlock<Block> POLISHED_CYLINDRITE_WALL = BLOCKS.createBlock("polished_cylindrite_wall", () -> new WallBlock(CCProperties.POLISHED_CYLINDRITE));

	public static final DeferredBlock<Block> CASSITERITE = BLOCKS.createBlock("cassiterite", () -> new Block(CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> CASSITERITE_STAIRS = BLOCKS.createBlock("cassiterite_stairs", () -> new StairBlock(CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> CASSITERITE_SLAB = BLOCKS.createBlock("cassiterite_slab", () -> new SlabBlock(CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> CASSITERITE_WALL = BLOCKS.createBlock("cassiterite_wall", () -> new WallBlock(CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> SMOOTH_CASSITERITE = BLOCKS.createBlock("smooth_cassiterite", () -> new Block(CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> SMOOTH_CASSITERITE_STAIRS = BLOCKS.createBlock("smooth_cassiterite_stairs", () -> new StairBlock(CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> SMOOTH_CASSITERITE_SLAB = BLOCKS.createBlock("smooth_cassiterite_slab", () -> new SlabBlock(CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> SMOOTH_CASSITERITE_WALL = BLOCKS.createBlock("smooth_cassiterite_wall", () -> new WallBlock(CCProperties.CASSITERITE));
	public static final DeferredBlock<Block> CASSITERITE_BRICKS = BLOCKS.createBlock("cassiterite_bricks", () -> new Block(CCProperties.CASSITERITE_BRICKS));
	public static final DeferredBlock<Block> CASSITERITE_BRICK_STAIRS = BLOCKS.createBlock("cassiterite_brick_stairs", () -> new StairBlock(CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE_BRICKS));
	public static final DeferredBlock<Block> CASSITERITE_BRICK_SLAB = BLOCKS.createBlock("cassiterite_brick_slab", () -> new SlabBlock(CCProperties.CASSITERITE_BRICKS));
	public static final DeferredBlock<Block> CASSITERITE_BRICK_WALL = BLOCKS.createBlock("cassiterite_brick_wall", () -> new WallBlock(CCProperties.CASSITERITE_BRICKS));
	public static final DeferredBlock<Block> CASSITERITE_PILLAR = BLOCKS.createBlock("cassiterite_pillar", () -> new RotatedPillarBlock(CCProperties.CASSITERITE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_CASSITERITE_BRICKS = BLOCKS.createBlock("chiseled_cassiterite_bricks", () -> new Block(CCProperties.CASSITERITE_BRICKS));
	public static final DeferredBlock<Block> POLISHED_CASSITERITE = BLOCKS.createBlock("polished_cassiterite", () -> new Block(CCProperties.POLISHED_CASSITERITE));
	public static final DeferredBlock<Block> POLISHED_CASSITERITE_STAIRS = BLOCKS.createBlock("polished_cassiterite_stairs", () -> new StairBlock(POLISHED_CASSITERITE.get().defaultBlockState(), CCProperties.POLISHED_CASSITERITE));
	public static final DeferredBlock<Block> POLISHED_CASSITERITE_SLAB = BLOCKS.createBlock("polished_cassiterite_slab", () -> new SlabBlock(CCProperties.POLISHED_CASSITERITE));
	public static final DeferredBlock<Block> POLISHED_CASSITERITE_WALL = BLOCKS.createBlock("polished_cassiterite_wall", () -> new WallBlock(CCProperties.POLISHED_CASSITERITE));

	public static final DeferredBlock<Block> RHYOLITE = BLOCKS.createBlock("rhyolite", () -> new Block(CCProperties.RHYOLITE));
	public static final DeferredBlock<Block> RHYOLITE_STAIRS = BLOCKS.createBlock("rhyolite_stairs", () -> new StairBlock(RHYOLITE.get().defaultBlockState(), CCProperties.RHYOLITE));
	public static final DeferredBlock<Block> RHYOLITE_SLAB = BLOCKS.createBlock("rhyolite_slab", () -> new SlabBlock(CCProperties.RHYOLITE));
	public static final DeferredBlock<Block> RHYOLITE_WALL = BLOCKS.createBlock("rhyolite_wall", () -> new WallBlock(CCProperties.RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_RHYOLITE = BLOCKS.createBlock("polished_rhyolite", () -> new Block(CCProperties.POLISHED_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_RHYOLITE_STAIRS = BLOCKS.createBlock("polished_rhyolite_stairs", () -> new StairBlock(POLISHED_RHYOLITE.get().defaultBlockState(), CCProperties.POLISHED_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_RHYOLITE_SLAB = BLOCKS.createBlock("polished_rhyolite_slab", () -> new SlabBlock(CCProperties.POLISHED_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_RHYOLITE_WALL = BLOCKS.createBlock("polished_rhyolite_wall", () -> new WallBlock(CCProperties.POLISHED_RHYOLITE));
	public static final DeferredBlock<Block> RHYOLITE_BRICKS = BLOCKS.createBlock("rhyolite_bricks", () -> new Block(CCProperties.RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> RHYOLITE_BRICK_STAIRS = BLOCKS.createBlock("rhyolite_brick_stairs", () -> new StairBlock(RHYOLITE_BRICKS.get().defaultBlockState(), CCProperties.RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> RHYOLITE_BRICK_SLAB = BLOCKS.createBlock("rhyolite_brick_slab", () -> new SlabBlock(CCProperties.RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> RHYOLITE_BRICK_WALL = BLOCKS.createBlock("rhyolite_brick_wall", () -> new WallBlock(CCProperties.RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_RHYOLITE_BRICKS = BLOCKS.createBlock("chiseled_rhyolite_bricks", () -> new Block(CCProperties.RHYOLITE_BRICKS));

	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE = BLOCKS.createBlock("magmatic_rhyolite", () -> new MagmaticRhyoliteBlock(CCProperties.MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_STAIRS = BLOCKS.createBlock("magmatic_rhyolite_stairs", () -> new StairBlock(MAGMATIC_RHYOLITE.get().defaultBlockState(), CCProperties.MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_SLAB = BLOCKS.createBlock("magmatic_rhyolite_slab", () -> new SlabBlock(CCProperties.MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_WALL = BLOCKS.createBlock("magmatic_rhyolite_wall", () -> new WallBlock(CCProperties.MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_MAGMATIC_RHYOLITE = BLOCKS.createBlock("polished_magmatic_rhyolite", () -> new Block(CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_MAGMATIC_RHYOLITE_STAIRS = BLOCKS.createBlock("polished_magmatic_rhyolite_stairs", () -> new StairBlock(POLISHED_MAGMATIC_RHYOLITE.get().defaultBlockState(), CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_MAGMATIC_RHYOLITE_SLAB = BLOCKS.createBlock("polished_magmatic_rhyolite_slab", () -> new SlabBlock(CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> POLISHED_MAGMATIC_RHYOLITE_WALL = BLOCKS.createBlock("polished_magmatic_rhyolite_wall", () -> new WallBlock(CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_BRICKS = BLOCKS.createBlock("magmatic_rhyolite_bricks", () -> new Block(CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_BRICK_STAIRS = BLOCKS.createBlock("magmatic_rhyolite_brick_stairs", () -> new StairBlock(MAGMATIC_RHYOLITE_BRICKS.get().defaultBlockState(), CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_BRICK_SLAB = BLOCKS.createBlock("magmatic_rhyolite_brick_slab", () -> new SlabBlock(CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> MAGMATIC_RHYOLITE_BRICK_WALL = BLOCKS.createBlock("magmatic_rhyolite_brick_wall", () -> new WallBlock(CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_MAGMATIC_RHYOLITE_BRICKS = BLOCKS.createBlock("chiseled_magmatic_rhyolite_bricks", () -> new Block(CCProperties.MAGMATIC_RHYOLITE_BRICKS));

	public static final DeferredBlock<Block> AMBIENT_BUBBLE_COLUMN = BLOCKS.createBlockNoItem("ambient_bubble_column", () -> new AmbientBubbleColumnBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUBBLE_COLUMN).noLootTable()));

	public static final DeferredBlock<Block> DRIPSTONE_STAIRS = BLOCKS.createBlock("dripstone_stairs", () -> new StairBlock(Blocks.DRIPSTONE_BLOCK.defaultBlockState(), CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> DRIPSTONE_SLAB = BLOCKS.createBlock("dripstone_slab", () -> new SlabBlock(CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> DRIPSTONE_WALL = BLOCKS.createBlock("dripstone_wall", () -> new WallBlock(CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> SMOOTH_DRIPSTONE = BLOCKS.createBlock("smooth_dripstone", () -> new Block(CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> SMOOTH_DRIPSTONE_STAIRS = BLOCKS.createBlock("smooth_dripstone_stairs", () -> new StairBlock(SMOOTH_DRIPSTONE.get().defaultBlockState(), CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> SMOOTH_DRIPSTONE_SLAB = BLOCKS.createBlock("smooth_dripstone_slab", () -> new SlabBlock(CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> SMOOTH_DRIPSTONE_WALL = BLOCKS.createBlock("smooth_dripstone_wall", () -> new WallBlock(CCProperties.DRIPSTONE));
	public static final DeferredBlock<Block> POLISHED_DRIPSTONE = BLOCKS.createBlock("polished_dripstone", () -> new Block(CCProperties.POLISHED_DRIPSTONE));
	public static final DeferredBlock<Block> POLISHED_DRIPSTONE_STAIRS = BLOCKS.createBlock("polished_dripstone_stairs", () -> new StairBlock(POLISHED_DRIPSTONE.get().defaultBlockState(), CCProperties.POLISHED_DRIPSTONE));
	public static final DeferredBlock<Block> POLISHED_DRIPSTONE_SLAB = BLOCKS.createBlock("polished_dripstone_slab", () -> new SlabBlock(CCProperties.POLISHED_DRIPSTONE));
	public static final DeferredBlock<Block> POLISHED_DRIPSTONE_WALL = BLOCKS.createBlock("polished_dripstone_wall", () -> new WallBlock(CCProperties.POLISHED_DRIPSTONE));
	public static final DeferredBlock<Block> DRIPSTONE_BRICKS = BLOCKS.createBlock("dripstone_bricks", () -> new Block(CCProperties.DRIPSTONE_BRICKS));
	public static final DeferredBlock<Block> DRIPSTONE_BRICK_STAIRS = BLOCKS.createBlock("dripstone_brick_stairs", () -> new StairBlock(DRIPSTONE_BRICKS.get().defaultBlockState(), CCProperties.DRIPSTONE_BRICKS));
	public static final DeferredBlock<Block> DRIPSTONE_BRICK_SLAB = BLOCKS.createBlock("dripstone_brick_slab", () -> new SlabBlock(CCProperties.DRIPSTONE_BRICKS));
	public static final DeferredBlock<Block> DRIPSTONE_BRICK_WALL = BLOCKS.createBlock("dripstone_brick_wall", () -> new WallBlock(CCProperties.DRIPSTONE_BRICKS));
	public static final DeferredBlock<Block> CHISELED_DRIPSTONE_BRICKS = BLOCKS.createBlock("chiseled_dripstone_bricks", () -> new Block(CCProperties.DRIPSTONE_BRICKS));
	public static final DeferredBlock<Block> CRACKED_DRIPSTONE_BRICKS = BLOCKS.createBlock("cracked_dripstone_bricks", () -> new Block(CCProperties.DRIPSTONE_BRICKS));
	public static final DeferredBlock<Block> DRIPSTONE_SHINGLES = BLOCKS.createBlock("dripstone_shingles", () -> new Block(CCProperties.DRIPSTONE_SHINGLES));
	public static final DeferredBlock<Block> DRIPSTONE_SHINGLE_STAIRS = BLOCKS.createBlock("dripstone_shingle_stairs", () -> new StairBlock(DRIPSTONE_SHINGLES.get().defaultBlockState(), CCProperties.DRIPSTONE_SHINGLES));
	public static final DeferredBlock<Block> DRIPSTONE_SHINGLE_SLAB = BLOCKS.createBlock("dripstone_shingle_slab", () -> new SlabBlock(CCProperties.DRIPSTONE_SHINGLES));
	public static final DeferredBlock<Block> DRIPSTONE_SHINGLE_WALL = BLOCKS.createBlock("dripstone_shingle_wall", () -> new WallBlock(CCProperties.DRIPSTONE_SHINGLES));
	public static final DeferredBlock<Block> CHISELED_DRIPSTONE_SHINGLES = BLOCKS.createBlock("chiseled_dripstone_shingles", () -> new DripstoneShingleBlock(CCProperties.DRIPSTONE_SHINGLES));
	public static final DeferredBlock<Block> FLOODED_DRIPSTONE_SHINGLES = BLOCKS.createBlock("flooded_dripstone_shingles", () -> new DripstoneShingleBlock(CCProperties.DRIPSTONE_SHINGLES));

	public static final DeferredBlock<Block> SMOOTH_BASALT_STAIRS = BLOCKS.createBlock("smooth_basalt_stairs", () -> new StairBlock(Blocks.SMOOTH_BASALT.defaultBlockState(), CCProperties.BASALT));
	public static final DeferredBlock<Block> SMOOTH_BASALT_SLAB = BLOCKS.createBlock("smooth_basalt_slab", () -> new SlabBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> SMOOTH_BASALT_WALL = BLOCKS.createBlock("smooth_basalt_wall", () -> new WallBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_BRICKS = BLOCKS.createBlock("basalt_bricks", () -> new RotatedPillarBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_BRICK_STAIRS = BLOCKS.createBlock("basalt_brick_stairs", () -> new StairBlock(BASALT_BRICKS.get().defaultBlockState(), CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_BRICK_SLAB = BLOCKS.createBlock("basalt_brick_slab", () -> new SlabBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_BRICK_WALL = BLOCKS.createBlock("basalt_brick_wall", () -> new WallBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> CHISELED_BASALT_BRICKS = BLOCKS.createBlock("chiseled_basalt_bricks", () -> new RotatedPillarBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_TILES = BLOCKS.createBlock("basalt_tiles", () -> new RotatedPillarBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_TILE_STAIRS = BLOCKS.createBlock("basalt_tile_stairs", () -> new StairBlock(BASALT_TILES.get().defaultBlockState(), CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_TILE_SLAB = BLOCKS.createBlock("basalt_tile_slab", () -> new SlabBlock(CCProperties.BASALT));
	public static final DeferredBlock<Block> BASALT_TILE_WALL = BLOCKS.createBlock("basalt_tile_wall", () -> new WallBlock(CCProperties.BASALT));

	public static final DeferredBlock<Block> AMETHYST_BLOCK = BLOCKS.createBlock("amethyst_block", () -> new AmethystBlock(CCProperties.AMETHYST));
	public static final DeferredBlock<Block> CUT_AMETHYST = BLOCKS.createBlock("cut_amethyst", () -> new AmethystBlock(CCProperties.AMETHYST));
	public static final DeferredBlock<Block> CUT_AMETHYST_BRICKS = BLOCKS.createBlock("cut_amethyst_bricks", () -> new AmethystBlock(CCProperties.AMETHYST));
	public static final DeferredBlock<Block> CUT_AMETHYST_BRICK_STAIRS = BLOCKS.createBlock("cut_amethyst_brick_stairs", () -> new AmethystStairBlock(CUT_AMETHYST_BRICKS.get().defaultBlockState(), CCProperties.AMETHYST));
	public static final DeferredBlock<Block> CUT_AMETHYST_BRICK_SLAB = BLOCKS.createBlock("cut_amethyst_brick_slab", () -> new AmethystSlabBlock(CCProperties.AMETHYST));
	public static final DeferredBlock<Block> CUT_AMETHYST_BRICK_WALL = BLOCKS.createBlock("cut_amethyst_brick_wall", () -> new AmethystWallBlock(CCProperties.AMETHYST));
	public static final DeferredBlock<Block> AMETHYST_LAMP = BLOCKS.createBlock("amethyst_lamp", () -> new AmethystBlock(Properties.ofFullCopy(LAPIS_LAZULI_LAMP.get()).sound(SoundType.AMETHYST)));

	public static final DeferredBlock<Block> ECHO_BLOCK = BLOCKS.createBlock("echo_block", () -> new Block(CCProperties.ECHO_BLOCK));

	public static final DeferredBlock<Block> IRON_BRICKS = BLOCKS.createBlock("iron_bricks", () -> new Block(CCProperties.IRON_PLATED_BRICKS));
	public static final DeferredBlock<Block> IRON_BRICK_STAIRS = BLOCKS.createBlock("iron_brick_stairs", () -> new StairBlock(IRON_BRICKS.get().defaultBlockState(), CCProperties.IRON_PLATED_BRICKS));
	public static final DeferredBlock<Block> IRON_BRICK_SLAB = BLOCKS.createBlock("iron_brick_slab", () -> new SlabBlock(CCProperties.IRON_PLATED_BRICKS));
	public static final DeferredBlock<Block> IRON_BRICK_WALL = BLOCKS.createBlock("iron_brick_wall", () -> new WallBlock(CCProperties.IRON_PLATED_BRICKS));
	public static final DeferredBlock<Block> CHISELED_IRON_BRICKS = BLOCKS.createBlock("chiseled_iron_bricks", () -> new Block(CCProperties.IRON_PLATED_BRICKS));

	public static final DeferredBlock<Block> TIN_BRICKS = BLOCKS.createBlock("tin_bricks", () -> new Block(CCProperties.TIN_PLATED_BRICKS));
	public static final DeferredBlock<Block> TIN_BRICK_STAIRS = BLOCKS.createBlock("tin_brick_stairs", () -> new StairBlock(TIN_BRICKS.get().defaultBlockState(), CCProperties.TIN_PLATED_BRICKS));
	public static final DeferredBlock<Block> TIN_BRICK_SLAB = BLOCKS.createBlock("tin_brick_slab", () -> new SlabBlock(CCProperties.TIN_PLATED_BRICKS));
	public static final DeferredBlock<Block> TIN_BRICK_WALL = BLOCKS.createBlock("tin_brick_wall", () -> new WallBlock(CCProperties.TIN_PLATED_BRICKS));
	public static final DeferredBlock<Block> CHISELED_TIN_BRICKS = BLOCKS.createBlock("chiseled_tin_bricks", () -> new Block(CCProperties.TIN_PLATED_BRICKS));

	public static final DeferredBlock<Block> GOLD_BRICKS = BLOCKS.createBlock("gold_bricks", () -> new Block(CCProperties.GOLD_PLATED_BRICKS));
	public static final DeferredBlock<Block> GOLD_BRICK_STAIRS = BLOCKS.createBlock("gold_brick_stairs", () -> new StairBlock(GOLD_BRICKS.get().defaultBlockState(), CCProperties.GOLD_PLATED_BRICKS));
	public static final DeferredBlock<Block> GOLD_BRICK_SLAB = BLOCKS.createBlock("gold_brick_slab", () -> new SlabBlock(CCProperties.GOLD_PLATED_BRICKS));
	public static final DeferredBlock<Block> GOLD_BRICK_WALL = BLOCKS.createBlock("gold_brick_wall", () -> new WallBlock(CCProperties.GOLD_PLATED_BRICKS));
	public static final DeferredBlock<Block> CHISELED_GOLD_BRICKS = BLOCKS.createBlock("chiseled_gold_bricks", () -> new Block(CCProperties.GOLD_PLATED_BRICKS));

	public static final DeferredBlock<Block> SILVER_BRICKS = BLOCKS.createBlock("silver_bricks", () -> new Block(CCProperties.SILVER_PLATED_BRICKS));
	public static final DeferredBlock<Block> SILVER_BRICK_STAIRS = BLOCKS.createBlock("silver_brick_stairs", () -> new StairBlock(SILVER_BRICKS.get().defaultBlockState(), CCProperties.SILVER_PLATED_BRICKS));
	public static final DeferredBlock<Block> SILVER_BRICK_SLAB = BLOCKS.createBlock("silver_brick_slab", () -> new SlabBlock(CCProperties.SILVER_PLATED_BRICKS));
	public static final DeferredBlock<Block> SILVER_BRICK_WALL = BLOCKS.createBlock("silver_brick_wall", () -> new WallBlock(CCProperties.SILVER_PLATED_BRICKS));
	public static final DeferredBlock<Block> CHISELED_SILVER_BRICKS = BLOCKS.createBlock("chiseled_silver_bricks", () -> new Block(CCProperties.SILVER_PLATED_BRICKS));

	public static final DeferredBlock<Block> COPPER_BRICKS = BLOCKS.createBlock("copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> COPPER_BRICK_STAIRS = BLOCKS.createBlock("copper_brick_stairs", () -> new WeatheringCopperStairBlock(WeatherState.UNAFFECTED, COPPER_BRICKS.get().defaultBlockState(), CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> COPPER_BRICK_SLAB = BLOCKS.createBlock("copper_brick_slab", () -> new WeatheringCopperSlabBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> COPPER_BRICK_WALL = BLOCKS.createBlock("copper_brick_wall", () -> new WeatheringCopperWallBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> CHISELED_COPPER_BRICKS = BLOCKS.createBlock("chiseled_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> EXPOSED_COPPER_BRICKS = BLOCKS.createBlock("exposed_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> EXPOSED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("exposed_copper_brick_stairs", () -> new WeatheringCopperStairBlock(WeatherState.EXPOSED, EXPOSED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> EXPOSED_COPPER_BRICK_SLAB = BLOCKS.createBlock("exposed_copper_brick_slab", () -> new WeatheringCopperSlabBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> EXPOSED_COPPER_BRICK_WALL = BLOCKS.createBlock("exposed_copper_brick_wall", () -> new WeatheringCopperWallBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> EXPOSED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("exposed_chiseled_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> WEATHERED_COPPER_BRICKS = BLOCKS.createBlock("weathered_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WEATHERED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("weathered_copper_brick_stairs", () -> new WeatheringCopperStairBlock(WeatherState.WEATHERED, WEATHERED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WEATHERED_COPPER_BRICK_SLAB = BLOCKS.createBlock("weathered_copper_brick_slab", () -> new WeatheringCopperSlabBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WEATHERED_COPPER_BRICK_WALL = BLOCKS.createBlock("weathered_copper_brick_wall", () -> new WeatheringCopperWallBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WEATHERED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("weathered_chiseled_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> OXIDIZED_COPPER_BRICKS = BLOCKS.createBlock("oxidized_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("oxidized_copper_brick_stairs", () -> new WeatheringCopperStairBlock(WeatherState.OXIDIZED, OXIDIZED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_BRICK_SLAB = BLOCKS.createBlock("oxidized_copper_brick_slab", () -> new WeatheringCopperSlabBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_BRICK_WALL = BLOCKS.createBlock("oxidized_copper_brick_wall", () -> new WeatheringCopperWallBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> OXIDIZED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("oxidized_chiseled_copper_bricks", () -> new WeatheringCopperFullBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> WAXED_COPPER_BRICKS = BLOCKS.createBlock("waxed_copper_bricks", () -> new Block(CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("waxed_copper_brick_stairs", () -> new StairBlock(WAXED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_COPPER_BRICK_SLAB = BLOCKS.createBlock("waxed_copper_brick_slab", () -> new SlabBlock(CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_COPPER_BRICK_WALL = BLOCKS.createBlock("waxed_copper_brick_wall", () -> new WallBlock(CCProperties.COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("waxed_chiseled_copper_bricks", () -> new Block(CCProperties.COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_BRICKS = BLOCKS.createBlock("waxed_exposed_copper_bricks", () -> new Block(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("waxed_exposed_copper_brick_stairs", () -> new StairBlock(WAXED_EXPOSED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_BRICK_SLAB = BLOCKS.createBlock("waxed_exposed_copper_brick_slab", () -> new SlabBlock(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_BRICK_WALL = BLOCKS.createBlock("waxed_exposed_copper_brick_wall", () -> new WallBlock(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_EXPOSED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("waxed_exposed_chiseled_copper_bricks", () -> new Block(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_BRICKS = BLOCKS.createBlock("waxed_weathered_copper_bricks", () -> new Block(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("waxed_weathered_copper_brick_stairs", () -> new StairBlock(WAXED_WEATHERED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_BRICK_SLAB = BLOCKS.createBlock("waxed_weathered_copper_brick_slab", () -> new SlabBlock(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_BRICK_WALL = BLOCKS.createBlock("waxed_weathered_copper_brick_wall", () -> new WallBlock(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_WEATHERED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("waxed_weathered_chiseled_copper_bricks", () -> new Block(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_BRICKS = BLOCKS.createBlock("waxed_oxidized_copper_bricks", () -> new Block(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_BRICK_STAIRS = BLOCKS.createBlock("waxed_oxidized_copper_brick_stairs", () -> new StairBlock(WAXED_OXIDIZED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_BRICK_SLAB = BLOCKS.createBlock("waxed_oxidized_copper_brick_slab", () -> new SlabBlock(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_BRICK_WALL = BLOCKS.createBlock("waxed_oxidized_copper_brick_wall", () -> new WallBlock(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_CHISELED_COPPER_BRICKS = BLOCKS.createBlock("waxed_oxidized_chiseled_copper_bricks", () -> new Block(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));

	public static final DeferredBlock<Block> STRIPPED_AZALEA_LOG = BLOCKS.createBlock("stripped_azalea_log", () -> new RotatedPillarBlock(CCProperties.AZALEA.log()));
	public static final DeferredBlock<Block> STRIPPED_AZALEA_WOOD = BLOCKS.createBlock("stripped_azalea_wood", () -> new RotatedPillarBlock(CCProperties.AZALEA.log()));
	public static final DeferredBlock<Block> AZALEA_LOG = BLOCKS.createBlock("azalea_log", () -> new LogBlock(STRIPPED_AZALEA_LOG, CCProperties.AZALEA.log()));
	public static final DeferredBlock<Block> AZALEA_WOOD = BLOCKS.createBlock("azalea_wood", () -> new LogBlock(STRIPPED_AZALEA_WOOD, CCProperties.AZALEA.log()));
	public static final DeferredBlock<Block> AZALEA_PLANKS = BLOCKS.createBlock("azalea_planks", () -> new Block(CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_DOOR = BLOCKS.createBlock("azalea_door", () -> new DoorBlock(CCProperties.AZALEA_BLOCK_SET, CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_SLAB = BLOCKS.createBlock("azalea_slab", () -> new SlabBlock(CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_STAIRS = BLOCKS.createBlock("azalea_stairs", () -> new StairBlock(AZALEA_PLANKS.get().defaultBlockState(), CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_FENCE = BLOCKS.createBlock("azalea_fence", () -> new FenceBlock(CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_FENCE_GATE = BLOCKS.createBlock("azalea_fence_gate", () -> new FenceGateBlock(CCProperties.AZALEA_WOOD_TYPE, CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_PRESSURE_PLATE = BLOCKS.createBlock("azalea_pressure_plate", () -> new PressurePlateBlock(CCProperties.AZALEA_BLOCK_SET, CCProperties.AZALEA.pressurePlate()));
	public static final DeferredBlock<Block> AZALEA_BUTTON = BLOCKS.createBlock("azalea_button", () -> new ButtonBlock(CCProperties.AZALEA_BLOCK_SET, 30, CCProperties.AZALEA.button()));
	public static final DeferredBlock<Block> AZALEA_TRAPDOOR = BLOCKS.createBlock("azalea_trapdoor", () -> new TrapDoorBlock(CCProperties.AZALEA_BLOCK_SET, CCProperties.AZALEA.trapdoor()));
	public static final Pair<DeferredBlock<BlueprintStandingSignBlock>, DeferredBlock<BlueprintWallSignBlock>> AZALEA_SIGNS = BLOCKS.createSignBlock("azalea", CCProperties.AZALEA_WOOD_TYPE, CCProperties.AZALEA.sign());
	public static final Pair<DeferredBlock<BlueprintCeilingHangingSignBlock>, DeferredBlock<BlueprintWallHangingSignBlock>> AZALEA_HANGING_SIGNS = BLOCKS.createHangingSignBlock("azalea", CCProperties.AZALEA_WOOD_TYPE, CCProperties.AZALEA.hangingSign());

	public static final DeferredBlock<Block> AZALEA_BOARDS = BLOCKS.createBlock("azalea_boards", () -> new RotatedPillarBlock(CCProperties.AZALEA.planks()));
	public static final DeferredBlock<Block> AZALEA_BOOKSHELF = BLOCKS.createBlock("azalea_bookshelf", () -> new Block(CCProperties.AZALEA.bookshelf()));
	public static final DeferredBlock<Block> CHISELED_AZALEA_BOOKSHELF = BLOCKS.createBlock("chiseled_azalea_bookshelf", () -> new ChiseledAzaleaBookShelfBlock(CCProperties.AZALEA.chiseledBookshelf()));
	public static final DeferredBlock<Block> AZALEA_LADDER = BLOCKS.createBlock("azalea_ladder", () -> new LadderBlock(CCProperties.AZALEA.ladder()));
	public static final DeferredBlock<Block> AZALEA_BEEHIVE = BLOCKS.createBlock("azalea_beehive", () -> new BlueprintBeehiveBlock(CCProperties.AZALEA.beehive()));
	public static final DeferredBlock<BlueprintChestBlock> AZALEA_CHEST = BLOCKS.createChestBlock("azalea", CCProperties.AZALEA.chest());
	public static final DeferredBlock<BlueprintTrappedChestBlock> TRAPPED_AZALEA_CHEST = BLOCKS.createTrappedChestBlock("azalea", CCProperties.AZALEA.chest());

	public static final DeferredBlock<Block> FALSE_HOPE = BLOCKS.createBlock("false_hope", () -> new FalseHopeBlock(MobEffects.BLINDNESS, 8, CCProperties.FALSE_HOPE));

	public static final DeferredBlock<Block> MOSCHATEL = BLOCKS.createBlock("moschatel", () -> new MoschatelBlock(MobEffects.NIGHT_VISION, 5, PropertyUtil.flower().sound(CCSoundTypes.MOSCHATEL)));
	public static final DeferredBlock<Block> CAVE_GROWTHS = BLOCKS.createBlock("cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_LIGHT_GREEN)));
	public static final DeferredBlock<Block> LURID_CAVE_GROWTHS = BLOCKS.createBlock("lurid_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.GLOW_LICHEN)));
	public static final DeferredBlock<Block> WISPY_CAVE_GROWTHS = BLOCKS.createBlock("wispy_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.STONE)));
	public static final DeferredBlock<Block> GRAINY_CAVE_GROWTHS = BLOCKS.createBlock("grainy_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_PINK)));
	public static final DeferredBlock<Block> WEIRD_CAVE_GROWTHS = BLOCKS.createBlock("weird_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_MAGENTA)));
	public static final DeferredBlock<Block> ZESTY_CAVE_GROWTHS = BLOCKS.createBlock("zesty_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.RAW_IRON)));

	public static final DeferredBlock<Block> POTTED_FALSE_HOPE = BLOCKS.createBlockNoItem("potted_false_hope", () -> new FlowerPotBlock(FALSE_HOPE.get(), PropertyUtil.flowerPot().lightLevel((state) -> 15)));

	public static final DeferredBlock<Block> POTTED_MOSCHATEL = BLOCKS.createBlockNoItem("potted_moschatel", () -> new FlowerPotBlock(MOSCHATEL.get(), PropertyUtil.flowerPot()));
	public static final DeferredBlock<Block> POTTED_CAVE_GROWTHS = BLOCKS.createBlockNoItem("potted_cave_growths", () -> new FlowerPotBlock(CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final DeferredBlock<Block> POTTED_LURID_CAVE_GROWTHS = BLOCKS.createBlockNoItem("potted_lurid_cave_growths", () -> new FlowerPotBlock(LURID_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final DeferredBlock<Block> POTTED_WISPY_CAVE_GROWTHS = BLOCKS.createBlockNoItem("potted_wispy_cave_growths", () -> new FlowerPotBlock(WISPY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final DeferredBlock<Block> POTTED_GRAINY_CAVE_GROWTHS = BLOCKS.createBlockNoItem("potted_grainy_cave_growths", () -> new FlowerPotBlock(GRAINY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final DeferredBlock<Block> POTTED_WEIRD_CAVE_GROWTHS = BLOCKS.createBlockNoItem("potted_weird_cave_growths", () -> new FlowerPotBlock(WEIRD_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final DeferredBlock<Block> POTTED_ZESTY_CAVE_GROWTHS = BLOCKS.createBlockNoItem("potted_zesty_cave_growths", () -> new FlowerPotBlock(ZESTY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));

	public static final DeferredBlock<Block> FLINT_BLOCK = BLOCKS.createBlock("flint_block", () -> new FlintBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL).sound(CCSoundTypes.FLINT_BLOCK)));

	public static final DeferredBlock<Block> COAL = BLOCKS.createPlacedItem("coal", () -> new CoalBlock(CCProperties.placedCoal(6)));
	public static final DeferredBlock<Block> CHARCOAL = BLOCKS.createPlacedItem("charcoal", () -> new CoalBlock(CCProperties.placedCoal(4).sound(CCSoundTypes.CHARCOAL)));
	public static final DeferredBlock<Block> CHARCOAL_BLOCK = BLOCKS.createBlock("charcoal_block", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK).sound(CCSoundTypes.CHARCOAL)));

	public static final DeferredBlock<Block> COPPER_INGOT = BLOCKS.createPlacedItem("copper_ingot", () -> new WeatheringIngotBlock(WeatherState.UNAFFECTED, () -> Items.COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
	public static final DeferredBlock<Block> EXPOSED_COPPER_INGOT = BLOCKS.createPlacedItem("exposed_copper_ingot", () -> new WeatheringIngotBlock(WeatherState.EXPOSED, CCItems.EXPOSED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.EXPOSED_COPPER)));
	public static final DeferredBlock<Block> WEATHERED_COPPER_INGOT = BLOCKS.createPlacedItem("weathered_copper_ingot", () -> new WeatheringIngotBlock(WeatherState.WEATHERED, CCItems.WEATHERED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.WEATHERED_COPPER)));
	public static final DeferredBlock<Block> OXIDIZED_COPPER_INGOT = BLOCKS.createPlacedItem("oxidized_copper_ingot", () -> new WeatheringIngotBlock(WeatherState.OXIDIZED, CCItems.OXIDIZED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.OXIDIZED_COPPER)));
	public static final DeferredBlock<Block> WAXED_COPPER_INGOT = BLOCKS.createPlacedItem("waxed_copper_ingot", () -> new IngotBlock(CCItems.WAXED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_COPPER_BLOCK)));
	public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_INGOT = BLOCKS.createPlacedItem("waxed_exposed_copper_ingot", () -> new IngotBlock(CCItems.WAXED_EXPOSED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_EXPOSED_COPPER)));
	public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_INGOT = BLOCKS.createPlacedItem("waxed_weathered_copper_ingot", () -> new IngotBlock(CCItems.WAXED_WEATHERED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_WEATHERED_COPPER)));
	public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_INGOT = BLOCKS.createPlacedItem("waxed_oxidized_copper_ingot", () -> new IngotBlock(CCItems.WAXED_OXIDIZED_COPPER_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_OXIDIZED_COPPER)));

	public static final DeferredBlock<Block> IRON_INGOT = BLOCKS.createPlacedItem("iron_ingot", () -> new IngotBlock(() -> Items.IRON_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredBlock<Block> GOLD_INGOT = BLOCKS.createPlacedItem("gold_ingot", () -> new IngotBlock(() -> Items.GOLD_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
	public static final DeferredBlock<Block> NETHERITE_INGOT = BLOCKS.createPlacedItem("netherite_ingot", () -> new IngotBlock(() -> Items.NETHERITE_INGOT, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));
	public static final DeferredBlock<Block> SILVER_INGOT = BLOCKS.createPlacedItem("silver_ingot", () -> new IngotBlock(CCItems.SILVER_INGOT, BlockBehaviour.Properties.ofFullCopy(SILVER_BLOCK.get())));
	public static final DeferredBlock<Block> TIN_INGOT = BLOCKS.createPlacedItem("tin_ingot", () -> new IngotBlock(CCItems.TIN_INGOT, BlockBehaviour.Properties.ofFullCopy(TIN_BLOCK.get())));
	public static final DeferredBlock<Block> NECROMIUM_INGOT = BLOCKS.createPlacedItem("necromium_ingot", () -> new IngotBlock(CCItems.NECROMIUM_INGOT, BlockBehaviour.Properties.ofFullCopy(NECROMIUM_BLOCK.get())));

	public static final DeferredBlock<Block> BRICK = BLOCKS.createPlacedItem("brick", () -> new IngotBlock(() -> Items.BRICK, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)));
	public static final DeferredBlock<Block> NETHER_BRICK = BLOCKS.createPlacedItem("nether_brick", () -> new IngotBlock(() -> Items.NETHER_BRICK, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)));
	public static final DeferredBlock<Block> EUMUS_BRICK = BLOCKS.createPlacedItem("eumus_brick", () -> new IngotBlock(() -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("endergetic", "eumus_brick")), Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.STONE).strength(2, 30)));

	public static final DeferredBlock<Block> SADDLED_EGG = BLOCKS.createBlock("saddled_egg", () -> new SaddledEggBlock(CCProperties.SADDLED_EGG));

	public static void setupTabEditors() {
		CreativeModeTabContentsPopulator.mod(CavernsAndChasms.MOD_ID)
				.tab(BUILDING_BLOCKS)
				.addItemsBefore(of(Blocks.BAMBOO_BLOCK), AZALEA_LOG, AZALEA_WOOD, STRIPPED_AZALEA_LOG, STRIPPED_AZALEA_WOOD, AZALEA_PLANKS)
				.addItemsBefore(modLoaded(Blocks.BAMBOO_BLOCK, "woodworks"), AZALEA_BOARDS)
				.addItemsBefore(of(Blocks.BAMBOO_BLOCK), AZALEA_STAIRS, AZALEA_SLAB, AZALEA_FENCE, AZALEA_FENCE_GATE, AZALEA_DOOR, AZALEA_TRAPDOOR, AZALEA_PRESSURE_PLATE, AZALEA_BUTTON)
				.addItemsBefore(of(Blocks.MOSSY_COBBLESTONE),
						COBBLESTONE_BRICKS, COBBLESTONE_BRICK_STAIRS, COBBLESTONE_BRICK_SLAB, COBBLESTONE_BRICK_WALL,
						COBBLESTONE_TILES, COBBLESTONE_TILE_STAIRS, COBBLESTONE_TILE_SLAB, COBBLESTONE_TILE_WALL
				)
				.addItemsBefore(of(Blocks.SMOOTH_STONE),
						MOSSY_COBBLESTONE_BRICKS, MOSSY_COBBLESTONE_BRICK_STAIRS, MOSSY_COBBLESTONE_BRICK_SLAB, MOSSY_COBBLESTONE_BRICK_WALL,
						MOSSY_COBBLESTONE_TILES, MOSSY_COBBLESTONE_TILE_STAIRS, MOSSY_COBBLESTONE_TILE_SLAB, MOSSY_COBBLESTONE_TILE_WALL
				)
				.addItemsAfter(of(Blocks.STONE_SLAB), STONE_WALL)
				.addItemsAfter(of(Blocks.POLISHED_GRANITE_SLAB),
						POLISHED_GRANITE_WALL, CHISELED_POLISHED_GRANITE,
						GRANITE_BRICKS, GRANITE_BRICK_STAIRS, GRANITE_BRICK_SLAB, GRANITE_BRICK_WALL, GRANITE_PILLAR,
						GRANITE_TILES, GRANITE_TILE_STAIRS, GRANITE_TILE_SLAB, GRANITE_TILE_WALL
				)
				.addItemsAfter(of(Blocks.POLISHED_DIORITE_SLAB),
						POLISHED_DIORITE_WALL, CHISELED_POLISHED_DIORITE,
						DIORITE_BRICKS, DIORITE_BRICK_STAIRS, DIORITE_BRICK_SLAB, DIORITE_BRICK_WALL, DIORITE_PILLAR,
						DIORITE_TILES, DIORITE_TILE_STAIRS, DIORITE_TILE_SLAB, DIORITE_TILE_WALL
				)
				.addItemsAfter(of(Blocks.POLISHED_ANDESITE_SLAB),
						POLISHED_ANDESITE_WALL, CHISELED_POLISHED_ANDESITE,
						ANDESITE_BRICKS, ANDESITE_BRICK_STAIRS, ANDESITE_BRICK_SLAB, ANDESITE_BRICK_WALL, ANDESITE_PILLAR,
						ANDESITE_TILES, ANDESITE_TILE_STAIRS, ANDESITE_TILE_SLAB, ANDESITE_TILE_WALL
				)
				.editor(CCBlocks::removeTuff)
				.editor(event -> remove(event, Blocks.CALCITE, Blocks.DRIPSTONE_BLOCK))
				.addItemsBefore(of(Blocks.DEEPSLATE),
						() -> Blocks.CALCITE, CALCITE_STAIRS, CALCITE_SLAB, CALCITE_WALL,
						POLISHED_CALCITE, POLISHED_CALCITE_STAIRS, POLISHED_CALCITE_SLAB, POLISHED_CALCITE_WALL, CHISELED_POLISHED_CALCITE, CALCITE_PILLAR, CALCITE_BRICKS, CALCITE_BRICK_STAIRS, CALCITE_BRICK_SLAB, CALCITE_BRICK_WALL, CHISELED_CALCITE_BRICKS,
						() -> Blocks.TUFF, () -> Blocks.TUFF_STAIRS, () -> Blocks.TUFF_SLAB, () -> Blocks.TUFF_WALL,
						SMOOTH_TUFF, SMOOTH_TUFF_STAIRS, SMOOTH_TUFF_SLAB, SMOOTH_TUFF_WALL,
						POLISHED_TUFF, POLISHED_TUFF_STAIRS, POLISHED_TUFF_SLAB, POLISHED_TUFF_WALL,
						TUFF_BRICKS, TUFF_BRICK_STAIRS, TUFF_BRICK_SLAB, TUFF_BRICK_WALL, CHISELED_TUFF_BRICKS, TUFF_PILLAR,
						TUFF_TILES, TUFF_TILE_STAIRS, TUFF_TILE_SLAB, TUFF_TILE_WALL,
						SCHIST, SCHIST_STAIRS, SCHIST_SLAB, SCHIST_WALL,
						SMOOTH_SCHIST, SMOOTH_SCHIST_STAIRS, SMOOTH_SCHIST_SLAB, SMOOTH_SCHIST_WALL,
						() -> Blocks.POLISHED_TUFF, () -> Blocks.POLISHED_TUFF_STAIRS, () -> Blocks.POLISHED_TUFF_SLAB, () -> Blocks.POLISHED_TUFF_WALL, () -> Blocks.CHISELED_TUFF,
						() -> Blocks.TUFF_BRICKS, () -> Blocks.TUFF_BRICK_STAIRS, () -> Blocks.TUFF_BRICK_SLAB, () -> Blocks.TUFF_BRICK_WALL, () -> Blocks.CHISELED_TUFF_BRICKS, SCHIST_PILLAR,
						() -> Blocks.DRIPSTONE_BLOCK, DRIPSTONE_STAIRS, DRIPSTONE_SLAB, DRIPSTONE_WALL,
						SMOOTH_DRIPSTONE, SMOOTH_DRIPSTONE_STAIRS, SMOOTH_DRIPSTONE_SLAB, SMOOTH_DRIPSTONE_WALL,
						POLISHED_DRIPSTONE, POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE_WALL,
						DRIPSTONE_BRICKS, CRACKED_DRIPSTONE_BRICKS, DRIPSTONE_BRICK_STAIRS, DRIPSTONE_BRICK_SLAB, DRIPSTONE_BRICK_WALL, CHISELED_DRIPSTONE_BRICKS,
						DRIPSTONE_SHINGLES, FLOODED_DRIPSTONE_SHINGLES, DRIPSTONE_SHINGLE_STAIRS, DRIPSTONE_SHINGLE_SLAB, DRIPSTONE_SHINGLE_WALL, CHISELED_DRIPSTONE_SHINGLES,
						SUGILITE, SUGILITE_STAIRS, SUGILITE_SLAB, SUGILITE_WALL, POLISHED_SUGILITE, POLISHED_SUGILITE_STAIRS, POLISHED_SUGILITE_SLAB, POLISHED_SUGILITE_WALL,
						SUGILITE_BRICKS, SUGILITE_BRICK_STAIRS, SUGILITE_BRICK_SLAB, SUGILITE_BRICK_WALL, CHISELED_SUGILITE_BRICKS, SUGILITE_PILLAR,
						CYLINDRITE, CYLINDRITE_STAIRS, CYLINDRITE_SLAB, CYLINDRITE_WALL, SMOOTH_CYLINDRITE, SMOOTH_CYLINDRITE_STAIRS, SMOOTH_CYLINDRITE_SLAB, SMOOTH_CYLINDRITE_WALL,
						POLISHED_CYLINDRITE, POLISHED_CYLINDRITE_STAIRS, POLISHED_CYLINDRITE_SLAB, POLISHED_CYLINDRITE_WALL,
						CYLINDRITE_BRICKS, CYLINDRITE_BRICK_STAIRS, CYLINDRITE_BRICK_SLAB, CYLINDRITE_BRICK_WALL, CHISELED_CYLINDRITE_BRICKS, CYLINDRITE_PILLAR,
						CASSITERITE, CASSITERITE_STAIRS, CASSITERITE_SLAB, CASSITERITE_WALL,
						SMOOTH_CASSITERITE, SMOOTH_CASSITERITE_STAIRS, SMOOTH_CASSITERITE_SLAB, SMOOTH_CASSITERITE_WALL,
						POLISHED_CASSITERITE, POLISHED_CASSITERITE_STAIRS, POLISHED_CASSITERITE_SLAB, POLISHED_CASSITERITE_WALL,
						CASSITERITE_BRICKS, CASSITERITE_BRICK_STAIRS, CASSITERITE_BRICK_SLAB, CASSITERITE_BRICK_WALL, CHISELED_CASSITERITE_BRICKS, CASSITERITE_PILLAR,
						RHYOLITE, RHYOLITE_STAIRS, RHYOLITE_SLAB, RHYOLITE_WALL,
						POLISHED_RHYOLITE, POLISHED_RHYOLITE_STAIRS, POLISHED_RHYOLITE_SLAB, POLISHED_RHYOLITE_WALL,
						RHYOLITE_BRICKS, RHYOLITE_BRICK_STAIRS, RHYOLITE_BRICK_SLAB, RHYOLITE_BRICK_WALL, CHISELED_RHYOLITE_BRICKS,
						MAGMATIC_RHYOLITE, MAGMATIC_RHYOLITE_STAIRS, MAGMATIC_RHYOLITE_SLAB, MAGMATIC_RHYOLITE_WALL,
						POLISHED_MAGMATIC_RHYOLITE, POLISHED_MAGMATIC_RHYOLITE_STAIRS, POLISHED_MAGMATIC_RHYOLITE_SLAB, POLISHED_MAGMATIC_RHYOLITE_WALL,
						MAGMATIC_RHYOLITE_BRICKS, MAGMATIC_RHYOLITE_BRICK_STAIRS, MAGMATIC_RHYOLITE_BRICK_SLAB, MAGMATIC_RHYOLITE_BRICK_WALL, CHISELED_MAGMATIC_RHYOLITE_BRICKS
				)
				.addItemsAfter(of(Blocks.DEEPSLATE), DEEPSLATE_STAIRS, DEEPSLATE_SLAB, DEEPSLATE_WALL)
				.addItemsBefore(of(Blocks.CHISELED_DEEPSLATE),
						COBBLED_DEEPSLATE_BRICKS, COBBLED_DEEPSLATE_BRICK_STAIRS, COBBLED_DEEPSLATE_BRICK_SLAB, COBBLED_DEEPSLATE_BRICK_WALL,
						COBBLED_DEEPSLATE_TILES, COBBLED_DEEPSLATE_TILE_STAIRS, COBBLED_DEEPSLATE_TILE_SLAB, COBBLED_DEEPSLATE_TILE_WALL
				)
				.editor(event -> remove(event, Blocks.CHISELED_DEEPSLATE, Blocks.LIGHTNING_ROD))
				.addItemsBefore(of(Blocks.DEEPSLATE_TILES), () -> Blocks.CHISELED_DEEPSLATE)
				.addItemsBefore(of(Blocks.BASALT), SANGUINE_BLOCK, SANGUINE_TILES, SANGUINE_TILE_STAIRS, SANGUINE_TILE_SLAB, SANGUINE_TILE_WALL, FORTIFIED_SANGUINE_TILES, FORTIFIED_SANGUINE_TILE_STAIRS, FORTIFIED_SANGUINE_TILE_SLAB, FORTIFIED_SANGUINE_TILE_WALL)
				.addItemsAfter(of(Blocks.POLISHED_BASALT), BASALT_BRICKS, BASALT_BRICK_STAIRS, BASALT_BRICK_SLAB, BASALT_BRICK_WALL, CHISELED_BASALT_BRICKS, BASALT_TILES, BASALT_TILE_STAIRS, BASALT_TILE_SLAB, BASALT_TILE_WALL)
				.addItemsAfter(of(Blocks.SMOOTH_BASALT), SMOOTH_BASALT_STAIRS, SMOOTH_BASALT_SLAB, SMOOTH_BASALT_WALL)
				.addItemsAfter(of(Blocks.AMETHYST_BLOCK), AMETHYST_BLOCK, CUT_AMETHYST, CUT_AMETHYST_BRICKS, CUT_AMETHYST_BRICK_STAIRS, CUT_AMETHYST_BRICK_SLAB, CUT_AMETHYST_BRICK_WALL, AMETHYST_LAMP)
				.addItemsAfter(of(Blocks.COAL_BLOCK), CHARCOAL_BLOCK)
				.addItemsAfter(of(Blocks.IRON_BLOCK), IRON_BRICKS, IRON_BRICK_STAIRS, IRON_BRICK_SLAB, IRON_BRICK_WALL, CHISELED_IRON_BRICKS)
				.addItemsAfter(of(Blocks.GOLD_BLOCK), GOLD_BRICKS, GOLD_BRICK_STAIRS, GOLD_BRICK_SLAB, GOLD_BRICK_WALL, CHISELED_GOLD_BRICKS, GOLDEN_BARS, GOLDEN_CHAIN)
				.addItemsBefore(of(Blocks.GOLD_BLOCK), TIN_BLOCK, TIN_BRICKS, TIN_BRICK_STAIRS, TIN_BRICK_SLAB, TIN_BRICK_WALL, CHISELED_TIN_BRICKS, TIN_BARS, TIN_CHAIN, CCItems.ROLLER_DOOR, CCItems.ROLLER_WINDOW, HOLD_PRESSURE_PLATE, HOLD_BUTTON, TIN_BULB, TINPLATE_BLOCK)
				.addItemsBefore(of(Blocks.REDSTONE_BLOCK), SILVER_BLOCK, SILVER_BRICKS, SILVER_BRICK_STAIRS, SILVER_BRICK_SLAB, SILVER_BRICK_WALL, CHISELED_SILVER_BRICKS, SILVER_BARS, SILVER_CHAIN, MEDIUM_WEIGHTED_PRESSURE_PLATE)
				.addItemsAfter(of(Blocks.EMERALD_BLOCK), EMERALD_LAMP)
				.addItemsAfter(of(Blocks.LAPIS_BLOCK),
						LAPIS_LAZULI_BRICKS, LAPIS_LAZULI_BRICK_STAIRS, LAPIS_LAZULI_BRICK_SLAB, LAPIS_LAZULI_BRICK_WALL, LAPIS_LAZULI_PILLAR, LAPIS_LAZULI_LAMP,
						SPINEL_BLOCK, SPINEL_BRICKS, SPINEL_BRICK_STAIRS, SPINEL_BRICK_SLAB, SPINEL_BRICK_WALL, SPINEL_PILLAR, SPINEL_LAMP,
						TURQUOISE_BLOCK, TURQUOISE_TILES, TURQUOISE_TILE_STAIRS, TURQUOISE_TILE_SLAB, TURQUOISE_TILE_WALL, TURQUOISE_PILLAR, TURQUOISE_LAMP,
						ZIRCONIA_BLOCK, ZIRCONIA_LAMP
				)
				.addItemsAfter(of(Blocks.DIAMOND_BLOCK), DIAMOND_LAMP)
				.addItemsAfter(of(Blocks.NETHERITE_BLOCK), NECROMIUM_BLOCK)
				.addItemsAfter(of(Blocks.SMOOTH_QUARTZ_SLAB), QUARTZ_LAMP)
				.addItemsAfter(of(Blocks.CUT_COPPER_SLAB), COPPER_BRICKS, COPPER_BRICK_STAIRS, COPPER_BRICK_SLAB, COPPER_BRICK_WALL, CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.COPPER_BULB), LIFT_PRESSURE_PLATE, LIFT_BUTTON, () -> Blocks.LIGHTNING_ROD, COPPER_BARS, COPPER_CHAIN)
				.addItemsAfter(of(Blocks.EXPOSED_CUT_COPPER_SLAB), EXPOSED_COPPER_BRICKS, EXPOSED_COPPER_BRICK_STAIRS, EXPOSED_COPPER_BRICK_SLAB, EXPOSED_COPPER_BRICK_WALL, EXPOSED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.EXPOSED_COPPER_BULB), EXPOSED_LIFT_PRESSURE_PLATE, EXPOSED_LIFT_BUTTON, EXPOSED_LIGHTNING_ROD, EXPOSED_COPPER_BARS, EXPOSED_COPPER_CHAIN)
				.addItemsAfter(of(Blocks.WEATHERED_CUT_COPPER_SLAB), WEATHERED_COPPER_BRICKS, WEATHERED_COPPER_BRICK_STAIRS, WEATHERED_COPPER_BRICK_SLAB, WEATHERED_COPPER_BRICK_WALL, WEATHERED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.WEATHERED_COPPER_BULB), WEATHERED_LIFT_PRESSURE_PLATE, WEATHERED_LIFT_BUTTON, WEATHERED_LIGHTNING_ROD, WEATHERED_COPPER_BARS, WEATHERED_COPPER_CHAIN)
				.addItemsAfter(of(Blocks.OXIDIZED_CUT_COPPER_SLAB), OXIDIZED_COPPER_BRICKS, OXIDIZED_COPPER_BRICK_STAIRS, OXIDIZED_COPPER_BRICK_SLAB, OXIDIZED_COPPER_BRICK_WALL, OXIDIZED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.OXIDIZED_COPPER_BULB), OXIDIZED_LIFT_PRESSURE_PLATE, OXIDIZED_LIFT_BUTTON, OXIDIZED_LIGHTNING_ROD, OXIDIZED_COPPER_BARS, OXIDIZED_COPPER_CHAIN)
				.addItemsAfter(of(Blocks.WAXED_CUT_COPPER_SLAB), WAXED_COPPER_BRICKS, WAXED_COPPER_BRICK_STAIRS, WAXED_COPPER_BRICK_SLAB, WAXED_COPPER_BRICK_WALL, WAXED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.WAXED_COPPER_BULB), WAXED_LIFT_PRESSURE_PLATE, WAXED_LIFT_BUTTON, WAXED_LIGHTNING_ROD, WAXED_COPPER_BARS, WAXED_COPPER_CHAIN)
				.addItemsAfter(of(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB), WAXED_EXPOSED_COPPER_BRICKS, WAXED_EXPOSED_COPPER_BRICK_STAIRS, WAXED_EXPOSED_COPPER_BRICK_SLAB, WAXED_EXPOSED_COPPER_BRICK_WALL, WAXED_EXPOSED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.WAXED_EXPOSED_COPPER_BULB), WAXED_EXPOSED_LIFT_PRESSURE_PLATE, WAXED_EXPOSED_LIFT_BUTTON, WAXED_EXPOSED_LIGHTNING_ROD, WAXED_EXPOSED_COPPER_BARS, WAXED_EXPOSED_COPPER_CHAIN)
				.addItemsAfter(of(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB), WAXED_WEATHERED_COPPER_BRICKS, WAXED_WEATHERED_COPPER_BRICK_STAIRS, WAXED_WEATHERED_COPPER_BRICK_SLAB, WAXED_WEATHERED_COPPER_BRICK_WALL, WAXED_WEATHERED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.WAXED_WEATHERED_COPPER_BULB), WAXED_WEATHERED_LIFT_PRESSURE_PLATE, WAXED_WEATHERED_LIFT_BUTTON, WAXED_WEATHERED_LIGHTNING_ROD, WAXED_WEATHERED_COPPER_BARS, WAXED_WEATHERED_COPPER_CHAIN)
				.addItemsAfter(of(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB), WAXED_OXIDIZED_COPPER_BRICKS, WAXED_OXIDIZED_COPPER_BRICK_STAIRS, WAXED_OXIDIZED_COPPER_BRICK_SLAB, WAXED_OXIDIZED_COPPER_BRICK_WALL, WAXED_OXIDIZED_CHISELED_COPPER_BRICKS)
				.addItemsBefore(of(Blocks.WAXED_OXIDIZED_COPPER_BULB), WAXED_OXIDIZED_LIFT_PRESSURE_PLATE, WAXED_OXIDIZED_LIFT_BUTTON, WAXED_OXIDIZED_LIGHTNING_ROD, WAXED_OXIDIZED_COPPER_BARS, WAXED_OXIDIZED_COPPER_CHAIN)
				.tab(COLORED_BLOCKS)
				.addItemsAfter(of(Blocks.TINTED_GLASS), FROSTED_GLASS, FLOAT_GLASS, ORNATE_GLASS)
				.addItemsAfter(of(Blocks.GLASS_PANE), FROSTED_GLASS_PANE, FLOAT_GLASS_PANE, ORNATE_GLASS_PANE)
				.addItems(SPARKLER.getFirst(), WHITE_SPARKLER.getFirst(), LIGHT_GRAY_SPARKLER.getFirst(), GRAY_SPARKLER.getFirst(), BLACK_SPARKLER.getFirst(), BROWN_SPARKLER.getFirst(), RED_SPARKLER.getFirst(), ORANGE_SPARKLER.getFirst(), YELLOW_SPARKLER.getFirst(), LIME_SPARKLER.getFirst(), GREEN_SPARKLER.getFirst(), CYAN_SPARKLER.getFirst(), LIGHT_BLUE_SPARKLER.getFirst(), BLUE_SPARKLER.getFirst(), PURPLE_SPARKLER.getFirst(), MAGENTA_SPARKLER.getFirst(), PINK_SPARKLER.getFirst())
				.tab(NATURAL_BLOCKS)
				.addItemsAfter(of(Blocks.ROOTED_DIRT), ROCKY_DIRT)
				.addItemsAfter(of(Blocks.GRAVEL), FLINT_BLOCK)
				.addItemsBefore(of(Blocks.MUSHROOM_STEM), AZALEA_LOG)
				.addItemsBefore(of(Blocks.GOLD_ORE), TIN_ORE, DEEPSLATE_TIN_ORE, CYLINDRITE_TIN_ORE, CASSITERITE_TIN_ORE)
				.addItemsBefore(of(Blocks.REDSTONE_ORE), SILVER_ORE, DEEPSLATE_SILVER_ORE)
				.addItemsBefore(of(Blocks.LAPIS_ORE), TURQUOISE_ORE, DEEPSLATE_TURQUOISE_ORE)
				.addItemsBefore(of(Blocks.DIAMOND_ORE), SPINEL_ORE, DEEPSLATE_SPINEL_ORE)
				.addItemsBefore(of(Blocks.ANCIENT_DEBRIS), SOUL_SILVER_ORE)
				.addItemsAfter(of(Blocks.RAW_COPPER_BLOCK), RAW_TIN_BLOCK)
				.addItemsAfter(of(Blocks.RAW_GOLD_BLOCK), RAW_SILVER_BLOCK)
				.addItemsAfter(of(Blocks.SCULK_SENSOR), ECHO_BLOCK)
				.addItemsBefore(of(Blocks.COBWEB), GUNPOWDER_BLOCK, ROTTEN_FLESH_BLOCK)
				.addItemsBefore(of(Blocks.DEAD_BUSH), CAVE_GROWTHS, LURID_CAVE_GROWTHS, WISPY_CAVE_GROWTHS, WEIRD_CAVE_GROWTHS, GRAINY_CAVE_GROWTHS, ZESTY_CAVE_GROWTHS)
				.addItemsBefore(of(Blocks.TORCHFLOWER), MOSCHATEL, FALSE_HOPE)
				.addItemsBefore(of(Blocks.PRISMARINE), SUGILITE, CASSITERITE, RHYOLITE, MAGMATIC_RHYOLITE)
				.addItemsAfter(of(Blocks.SNIFFER_EGG), SADDLED_EGG)
				.tab(FUNCTIONAL_BLOCKS)
				.addItemsBefore(of(Blocks.BAMBOO_SIGN), AZALEA_SIGNS.getFirst(), AZALEA_HANGING_SIGNS.getFirst())
				.addItemsBefore(of(Blocks.REDSTONE_TORCH), CUPRIC_TORCH)
				.addItemsBefore(of(Blocks.CANDLE), SPARKLER.getFirst(), WHITE_SPARKLER.getFirst(), LIGHT_GRAY_SPARKLER.getFirst(), GRAY_SPARKLER.getFirst(), BLACK_SPARKLER.getFirst(), BROWN_SPARKLER.getFirst(), RED_SPARKLER.getFirst(), ORANGE_SPARKLER.getFirst(), YELLOW_SPARKLER.getFirst(), LIME_SPARKLER.getFirst(), GREEN_SPARKLER.getFirst(), CYAN_SPARKLER.getFirst(), LIGHT_BLUE_SPARKLER.getFirst(), BLUE_SPARKLER.getFirst(), PURPLE_SPARKLER.getFirst(), MAGENTA_SPARKLER.getFirst(), PINK_SPARKLER.getFirst())
				.addItemsBefore(of(Blocks.ANVIL), CUPRIC_CAMPFIRE)
				.addItemsBefore(of(Blocks.CHAIN),
						CUPRIC_LANTERN,
						COPPER_LANTERN, EXPOSED_COPPER_LANTERN, WEATHERED_COPPER_LANTERN, OXIDIZED_COPPER_LANTERN, WAXED_COPPER_LANTERN, WAXED_EXPOSED_COPPER_LANTERN, WAXED_WEATHERED_COPPER_LANTERN, WAXED_OXIDIZED_COPPER_LANTERN,
						BRAZIER, SOUL_BRAZIER
				)
				.addItemsBefore(modLoaded(Blocks.CHAIN, "endergetic"), ENDER_BRAZIER)
				.addItemsBefore(of(Blocks.CHAIN), CUPRIC_BRAZIER)
				.addItemsAfter(of(Blocks.CHAIN),
						COPPER_CHAIN, EXPOSED_COPPER_CHAIN, WEATHERED_COPPER_CHAIN, OXIDIZED_COPPER_CHAIN, WAXED_COPPER_CHAIN, WAXED_EXPOSED_COPPER_CHAIN, WAXED_WEATHERED_COPPER_CHAIN, WAXED_OXIDIZED_COPPER_CHAIN, TIN_CHAIN,
						FLOODLIGHT, EXPOSED_FLOODLIGHT, WEATHERED_FLOODLIGHT, OXIDIZED_FLOODLIGHT, WAXED_FLOODLIGHT, WAXED_EXPOSED_FLOODLIGHT, WAXED_WEATHERED_FLOODLIGHT, WAXED_OXIDIZED_FLOODLIGHT,
						DIMMER, LAVA_LAMP
				)
				.addItemsAfter(of(Blocks.SEA_LANTERN), EMERALD_LAMP, LAPIS_LAZULI_LAMP, SPINEL_LAMP, TURQUOISE_LAMP, ZIRCONIA_LAMP, DIAMOND_LAMP, QUARTZ_LAMP, AMETHYST_LAMP)
				.addItemsBefore(of(Blocks.SHULKER_BOX), TOOLBOX, EXPOSED_TOOLBOX, WEATHERED_TOOLBOX, OXIDIZED_TOOLBOX, WAXED_TOOLBOX, WAXED_EXPOSED_TOOLBOX, WAXED_WEATHERED_TOOLBOX, WAXED_OXIDIZED_TOOLBOX, STORAGE_DUCT, STORAGE_DUCT_HATCH)
				.addItemsBefore(of(Blocks.INFESTED_STONE), FRAGILE_STONE, FRAGILE_DEEPSLATE)
				.addItemsAfter(of(Blocks.SMITHING_TABLE), DISMANTLING_TABLE)
				.addItemsAfter(of(Blocks.DAMAGED_ANVIL), BEJEWELED_ANVIL)
				.addItemsAfter(of(Blocks.ENCHANTING_TABLE), ATONING_TABLE, TINPLATE_BLOCK)
				.tab(REDSTONE_BLOCKS)
				.addItemsAfter(of(Blocks.WAXED_OXIDIZED_COPPER_BULB), TIN_BULB)
				.addItemsAfter(of(Blocks.COMPARATOR), REFRACTOR, RESISTOR)
				.addItemsAfter(of(Blocks.STONE_PRESSURE_PLATE), WAXED_LIFT_PRESSURE_PLATE, WAXED_EXPOSED_LIFT_PRESSURE_PLATE, WAXED_WEATHERED_LIFT_PRESSURE_PLATE, WAXED_OXIDIZED_LIFT_PRESSURE_PLATE)
				.addItemsAfter(of(Blocks.STONE_BUTTON), WAXED_LIFT_BUTTON, WAXED_EXPOSED_LIFT_BUTTON, WAXED_WEATHERED_LIFT_BUTTON, WAXED_OXIDIZED_LIFT_BUTTON, HOLD_BUTTON)
				.addItemsAfter(of(Blocks.TARGET), BOUNCER)
				.addItemsAfter(of(Blocks.LIGHTNING_ROD), DIMMER, HOOP, WINCH)
				.addItemsAfter(of(Blocks.TNT), TMT)
				.addItemsAfter(of(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), MEDIUM_WEIGHTED_PRESSURE_PLATE)
				.addItemsAfter(of(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), HOLD_PRESSURE_PLATE)
				.addItemsBefore(of(Blocks.RAIL), COPPER_RAIL, EXPOSED_COPPER_RAIL, WEATHERED_COPPER_RAIL, OXIDIZED_COPPER_RAIL, WAXED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL)
				.addItemsAfter(of(Blocks.POWERED_RAIL), HALT_RAIL, SPIKED_RAIL, SLAUGHTER_RAIL)
				.addItemsAfter(of(Blocks.DROPPER), SCATTERER, SPLURTER)
				.addItemsAfter(of(Blocks.HOPPER), STORAGE_DUCT, STORAGE_DUCT_HATCH)
				.addItemsBefore(of(Blocks.OAK_FENCE_GATE), CCItems.ROLLER_DOOR, CCItems.ROLLER_WINDOW)
				.tab(TOOLS_AND_UTILITIES)
				.addItemsBefore(of(Blocks.RAIL), COPPER_RAIL, EXPOSED_COPPER_RAIL, WEATHERED_COPPER_RAIL, OXIDIZED_COPPER_RAIL, WAXED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL)
				.addItemsAfter(of(Blocks.POWERED_RAIL), HALT_RAIL, SPIKED_RAIL, SLAUGHTER_RAIL)
				.tab(COMBAT)
				.addItemsAfter(of(Blocks.TNT), TMT);

		CreativeModeTabContentsPopulator.mod("woodworks_" + CavernsAndChasms.MOD_ID)
				.tab(FUNCTIONAL_BLOCKS)
				.addItemsBefore(ofID(CCConstants.BAMBOO_LADDER), AZALEA_LADDER)
				.addItemsBefore(ofID(CCConstants.BAMBOO_BEEHIVE), AZALEA_BEEHIVE)
				.addItemsBefore(ofID(CCConstants.BAMBOO_BOOKSHELF), AZALEA_BOOKSHELF, CHISELED_AZALEA_BOOKSHELF)
				.addItemsBefore(ofID(CCConstants.BAMBOO_CLOSET), AZALEA_CHEST)
				.tab(REDSTONE_BLOCKS)
				.addItemsBefore(ofID(CCConstants.TRAPPED_BAMBOO_CLOSET), TRAPPED_AZALEA_CHEST);
	}

	public static void removeTuff(BuildCreativeModeTabContentsEvent event) {
		Block[] toRemove = new Block[]{
				Blocks.TUFF, Blocks.TUFF_STAIRS, Blocks.TUFF_SLAB, Blocks.TUFF_WALL, Blocks.CHISELED_TUFF,
				Blocks.POLISHED_TUFF, Blocks.POLISHED_TUFF_STAIRS, Blocks.POLISHED_TUFF_SLAB, Blocks.POLISHED_TUFF_WALL,
				Blocks.TUFF_BRICKS, Blocks.TUFF_BRICK_STAIRS, Blocks.TUFF_BRICK_SLAB, Blocks.TUFF_BRICK_WALL, Blocks.CHISELED_TUFF_BRICKS
		};

		remove(event, toRemove);
	}

	public static void remove(BuildCreativeModeTabContentsEvent event, ItemLike... items) {
		for (ItemLike item : items) {
			event.remove(new ItemStack(item), TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}

	public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
		return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
	}

	public static Predicate<ItemStack> ofID(ResourceLocation location, ItemLike fallback, String... modids) {
		return stack -> (BlockSubRegistryHelper.areModsLoaded(modids) ? of(BuiltInRegistries.ITEM.get(location)) : of(fallback)).test(stack);
	}

	public static Predicate<ItemStack> ofID(ResourceLocation location, String... modids) {
		return stack -> (BlockSubRegistryHelper.areModsLoaded(modids) && of(BuiltInRegistries.ITEM.get(location)).test(stack));
	}

	public static class CCProperties {
		public static final BlockSetType AZALEA_BLOCK_SET = BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":azalea"));
		public static final Supplier<BlockSetType> COPPER_BLOCK_SET = () -> BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":copper", true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.COPPER, SoundEvents.COPPER_DOOR_CLOSE, SoundEvents.COPPER_DOOR_OPEN, SoundEvents.COPPER_TRAPDOOR_CLOSE, SoundEvents.COPPER_TRAPDOOR_OPEN, CCSoundEvents.LIFT_PRESSURE_PLATE_CLICK_OFF.get(), CCSoundEvents.LIFT_PRESSURE_PLATE_CLICK_ON.get(), CCSoundEvents.LIFT_BUTTON_CLICK_OFF.get(), CCSoundEvents.LIFT_BUTTON_CLICK_ON.get()));
		public static final Supplier<BlockSetType> SILVER_BLOCK_SET = () -> BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":silver", false, false, false, BlockSetType.PressurePlateSensitivity.EVERYTHING, CCSoundTypes.SILVER, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, CCSoundEvents.MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_OFF.get(), CCSoundEvents.MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_ON.get(), SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
		public static final Supplier<BlockSetType> TIN_BLOCK_SET = () -> BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":tin", false, false, false, BlockSetType.PressurePlateSensitivity.EVERYTHING, CCSoundTypes.TIN, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, CCSoundEvents.TIN_PRESSURE_PLATE_CLICK_OFF.get(), CCSoundEvents.TIN_PRESSURE_PLATE_CLICK_ON.get(), CCSoundEvents.TIN_BUTTON_CLICK_OFF.get(), CCSoundEvents.TIN_BUTTON_CLICK_ON.get()));

		public static final WoodType AZALEA_WOOD_TYPE = WoodTypeRegistryHelper.registerWoodType(new WoodType(CavernsAndChasms.MOD_ID + ":azalea", AZALEA_BLOCK_SET));

		public static final BlockBehaviour.Properties ROCKY_DIRT = BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(CCSoundTypes.ROCKY_DIRT).requiresCorrectToolForDrops().strength(1.5F);
		public static final BlockBehaviour.Properties FRAGILE_STONE = BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).sound(CCSoundTypes.FRAGILE_STONE);
		public static final BlockBehaviour.Properties FRAGILE_DEEPSLATE = BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(CCSoundTypes.FRAGILE_DEEPSLATE);
		public static final BlockBehaviour.Properties DRIPSTONE = BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK);
		public static final BlockBehaviour.Properties DRIPSTONE_BRICKS = BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK).sound(CCSoundTypes.DRIPSTONE_BRICKS);
		public static final BlockBehaviour.Properties POLISHED_DRIPSTONE = BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK).sound(CCSoundTypes.POLISHED_DRIPSTONE);
		public static final BlockBehaviour.Properties GRANITE = BlockBehaviour.Properties.ofFullCopy(Blocks.GRANITE);
		public static final BlockBehaviour.Properties ANDESITE = BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE);
		public static final BlockBehaviour.Properties DIORITE = BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE);
		public static final BlockBehaviour.Properties CALCITE = BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE);
		public static final BlockBehaviour.Properties POLISHED_CALCITE = BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE).sound(CCSoundTypes.POLISHED_CALCITE);
		public static final BlockBehaviour.Properties CALCITE_BRICKS = BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE).sound(CCSoundTypes.CALCITE_BRICKS);
		public static final BlockBehaviour.Properties TUFF = BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF);
		public static final BlockBehaviour.Properties POLISHED_TUFF = BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF).sound(SoundType.POLISHED_TUFF);
		public static final BlockBehaviour.Properties TUFF_BRICKS = BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF).sound(SoundType.TUFF_BRICKS);
		public static final BlockBehaviour.Properties SCHIST = BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF).sound(CCSoundTypes.SCHIST);
		public static final BlockBehaviour.Properties POLISHED_SCHIST = BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF).sound(CCSoundTypes.POLISHED_SCHIST);
		public static final BlockBehaviour.Properties SCHIST_BRICKS = BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF).sound(CCSoundTypes.SCHIST_BRICKS);
		public static final BlockBehaviour.Properties SUGILITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.SUGILITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties POLISHED_SUGILITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.POLISHED_SUGILITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties SUGILITE_BRICKS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.SUGILITE_BRICKS).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties CYLINDRITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.CYLINDRITE).requiresCorrectToolForDrops().strength(0.75F);
		public static final BlockBehaviour.Properties POLISHED_CYLINDRITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.POLISHED_CYLINDRITE).requiresCorrectToolForDrops().strength(0.75F);
		public static final BlockBehaviour.Properties CYLINDRITE_BRICKS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.CYLINDRITE_BRICKS).requiresCorrectToolForDrops().strength(0.75F);
		public static final BlockBehaviour.Properties CASSITERITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.CASSITERITE).requiresCorrectToolForDrops().strength(4.0F, 4.0F);
		public static final BlockBehaviour.Properties POLISHED_CASSITERITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.POLISHED_CASSITERITE).requiresCorrectToolForDrops().strength(4.0F, 4.0F);
		public static final BlockBehaviour.Properties CASSITERITE_BRICKS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.CASSITERITE_BRICKS).requiresCorrectToolForDrops().strength(4.0F, 4.0F);
		public static final BlockBehaviour.Properties COBBLESTONE_BRICKS = BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE);
		public static final BlockBehaviour.Properties COBBLED_DEEPSLATE_BRICKS = BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE);
		public static final BlockBehaviour.Properties DRIPSTONE_SHINGLES = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F);
		public static final BlockBehaviour.Properties AMETHYST = BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK);
		public static final BlockBehaviour.Properties ECHO_BLOCK = BlockBehaviour.Properties.of().strength(1.5F).sound(CCSoundTypes.ECHO_BLOCK).requiresCorrectToolForDrops().lightLevel(state -> 6);
		public static final BlockBehaviour.Properties RHYOLITE = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(CCSoundTypes.RHYOLITE);
		public static final BlockBehaviour.Properties POLISHED_RHYOLITE = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(CCSoundTypes.POLISHED_RHYOLITE);
		public static final BlockBehaviour.Properties RHYOLITE_BRICKS = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(CCSoundTypes.RHYOLITE_BRICKS);
		public static final BlockBehaviour.Properties MAGMATIC_RHYOLITE = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(state -> 3).sound(CCSoundTypes.MAGMATIC_RHYOLITE);
		public static final BlockBehaviour.Properties POLISHED_MAGMATIC_RHYOLITE = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).lightLevel(state -> 3).sound(CCSoundTypes.POLISHED_MAGMATIC_RHYOLITE);
		public static final BlockBehaviour.Properties MAGMATIC_RHYOLITE_BRICKS = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(state -> 3).sound(CCSoundTypes.MAGMATIC_RHYOLITE_BRICKS);
		public static final BlockBehaviour.Properties BASALT = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT);

		public static final BlockBehaviour.Properties IRON_PLATED_BRICKS = platedBricks(MapColor.RAW_IRON, SoundType.METAL);
		public static final BlockBehaviour.Properties TIN_PLATED_BRICKS = platedBricks(MapColor.TERRACOTTA_WHITE, CCSoundTypes.TIN);
		public static final BlockBehaviour.Properties GOLD_PLATED_BRICKS = platedBricks(MapColor.GOLD, SoundType.METAL);
		public static final BlockBehaviour.Properties SILVER_PLATED_BRICKS = platedBricks(MapColor.COLOR_LIGHT_GRAY, CCSoundTypes.SILVER);
		public static final BlockBehaviour.Properties COPPER_PLATED_BRICKS = platedBricks(MapColor.COLOR_ORANGE, SoundType.COPPER);
		public static final BlockBehaviour.Properties EXPOSED_COPPER_PLATED_BRICKS = platedBricks(MapColor.TERRACOTTA_LIGHT_GRAY, SoundType.COPPER);
		public static final BlockBehaviour.Properties WEATHERED_COPPER_PLATED_BRICKS = platedBricks(MapColor.WARPED_STEM, SoundType.COPPER);
		public static final BlockBehaviour.Properties OXIDIZED_COPPER_PLATED_BRICKS = platedBricks(MapColor.WARPED_NYLIUM, SoundType.COPPER);

		public static final BlockBehaviour.Properties TMT = BlockBehaviour.Properties.ofFullCopy(Blocks.TNT).sound(CCSoundTypes.TMT);
		public static final BlockBehaviour.Properties RAIL = BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL);
		public static final BlockBehaviour.Properties DISMANTLING_TABLE = BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava();

		public static final BlockBehaviour.Properties COPPER_RAIL = BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties COPPER_CHAIN = BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.COPPER_CHAIN).noOcclusion();
		public static final BlockBehaviour.Properties COPPER_LANTERN = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.COPPER_LANTERN).lightLevel((state) -> 15).noOcclusion().pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 10);
		public static final BlockBehaviour.Properties EXPOSED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 9);
		public static final BlockBehaviour.Properties WEATHERED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 8);
		public static final BlockBehaviour.Properties OXIDIZED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 7);
		public static final BlockBehaviour.Properties TOOLBOX = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2F, 6.0F).sound(SoundType.COPPER);

		public static final BlockBehaviour.Properties TIN_CHAIN = BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.TIN_CHAIN).noOcclusion();
		public static final BlockBehaviour.Properties TIN_BULB = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(3.0F, 6.0F).sound(CCSoundTypes.TIN_BULB).requiresCorrectToolForDrops().isRedstoneConductor(PropertyUtil::never).lightLevel(state -> state.getValue(TinBulbBlock.POWER));

		public static final BlockBehaviour.Properties LAVA_LAMP = BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.LAVA_LAMP).lightLevel((state) -> 15);
		public static final BlockBehaviour.Properties COPPER_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.SILVER).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_CHAIN = BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.SILVER_CHAIN).noOcclusion();
		public static final BlockBehaviour.Properties TIN_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.TIN).noOcclusion();
		public static final BlockBehaviour.Properties GOLDEN_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion();
		public static final BlockBehaviour.Properties GOLDEN_CHAIN = BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.GOLDEN_CHAIN).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_PRESSURE_PLATE = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(CCSoundTypes.SILVER).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties LIFT_PRESSURE_PLATE = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(SoundType.COPPER).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties LIFT_BUTTON = BlockBehaviour.Properties.of().noCollission().strength(0.5F).sound(SoundType.COPPER).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties SANGUINE_TILES = Block.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(CCSoundTypes.SANGUINE);
		public static final BlockBehaviour.Properties FORTIFIED_SANGUINE_TILES = Block.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 9.0F).sound(CCSoundTypes.FORTIFIED_SANGUINE);

		public static final BlockBehaviour.Properties BRAZIER = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.SILVER).lightLevel(litBlockEmission(15)).noOcclusion();
		public static final BlockBehaviour.Properties BRAZIER_DIM = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.SILVER).lightLevel(litBlockEmission(10)).noOcclusion();

		public static final BlockBehaviour.Properties HOLD_PRESSURE_PLATE = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties HOLD_BUTTON = BlockBehaviour.Properties.of().noCollission().strength(0.5F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties WINCH = BlockBehaviour.Properties.of().strength(0.5F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties DIMMER = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).noOcclusion().sound(CCSoundTypes.DIMMER).pushReaction(PushReaction.DESTROY).lightLevel((state) -> state.getValue(AbstractDimmerBlock.POWER));
		public static final BlockBehaviour.Properties HOOP = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.TIN);
		public static final BlockBehaviour.Properties STORAGE_DUCT = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(5.0F).sound(CCSoundTypes.STORAGE_DUCT);
		public static final BlockBehaviour.Properties STORAGE_DUCT_HATCH = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(5.0F).sound(CCSoundTypes.TIN);
		public static final BlockBehaviour.Properties ROLLER_DOOR = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(5.0F).sound(CCSoundTypes.ROLLER_DOOR).pushReaction(PushReaction.BLOCK).forceSolidOn();

		public static final BlockBehaviour.Properties ORE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final BlockBehaviour.Properties DEEPSLATE_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE);
		public static final BlockBehaviour.Properties SOUL_SILVER_ORE = Block.Properties.ofFullCopy(Blocks.SOUL_SOIL).sound(CCSoundTypes.SOUL_SILVER_ORE);
		public static final BlockBehaviour.Properties SPINEL = BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_BLOCK).sound(CCSoundTypes.SPINEL).mapColor(MapColor.COLOR_PURPLE);
		public static final BlockBehaviour.Properties TURQUOISE = BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_BLOCK).mapColor(MapColor.COLOR_CYAN).sound(CCSoundTypes.TURQUOISE);
		public static final BlockBehaviour.Properties LAPIS_LAZULI = BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_BLOCK);
		public static final BlockBehaviour.Properties LAMP = BlockBehaviour.Properties.of().lightLevel((state) -> 15).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(CCProperties::alwaysAllowSpawn);

		public static final BlockBehaviour.Properties TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(CCSoundTypes.TIN_ORE);
		public static final BlockBehaviour.Properties DEEPSLATE_TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(CCSoundTypes.DEEPSLATE_TIN_ORE);
		public static final BlockBehaviour.Properties CYLINDRITE_TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 1.0F).sound(CCSoundTypes.CYLINDRITE_TIN_ORE);
		public static final BlockBehaviour.Properties CASSITERITE_TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 1.0F).sound(CCSoundTypes.CASSITERITE_TIN_ORE);

		public static final BlockBehaviour.Properties ROTTEN_FLESH_BLOCK = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).strength(0.8F).sound(CCSoundTypes.ROTTEN_FLESH);
		public static final BlockBehaviour.Properties NECROMIUM_BLOCK = BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).sound(CCSoundTypes.NECROMIUM).mapColor(MapColor.TERRACOTTA_GREEN);

		public static final BlockBehaviour.Properties FALSE_HOPE = modifyOffset(PropertyUtil.flower().sound(CCSoundTypes.FALSE_HOPE).lightLevel((state) -> 15));

		public static final BlockBehaviour.Properties SADDLED_EGG = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).forceSolidOn().strength(0.5F).sound(CCSoundTypes.SADDLED_EGG).randomTicks().noOcclusion().pushReaction(PushReaction.DESTROY);

		public static final Item.Properties FANCY = new Item.Properties().rarity(CCEnums.FANCY.getValue());

		public static final WoodSetProperties AZALEA = WoodSetProperties.builder(MapColor.TERRACOTTA_PURPLE).leavesSound(SoundType.AZALEA_LEAVES).build();

		private static boolean alwaysAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
			return true;
		}

		public static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
			return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		}

		public static BlockBehaviour.Properties platedBricks(MapColor color, SoundType soundType) {
			return BlockBehaviour.Properties.of().mapColor(color).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(soundType);
		}

		private static BlockBehaviour.Properties placedCoal(int baseLight) {
			return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(2.0F, 6.0F).requiresCorrectToolForDrops().lightLevel(placedCoalLight(baseLight)).noOcclusion().pushReaction(PushReaction.DESTROY);
		}

		private static ToIntFunction<BlockState> placedCoalLight(int base) {
			return state -> {
				boolean warm = state.getValue(CoalBlock.WARM);
				boolean lit = state.getValue(CoalBlock.LIT);
				return (warm || lit) ? base + (lit ? 4 : 2) + state.getValue(CoalBlock.COAL) : 0;
			};
		}

		private static BlockBehaviour.Properties caveGrowths(MapColor mapColor) {
			return modifyOffset(BlockBehaviour.Properties.of().mapColor(mapColor).replaceable().noCollission().instabreak().sound(CCSoundTypes.CAVE_GROWTHS).ignitedByLava().pushReaction(PushReaction.DESTROY));
		}

		private static BlockBehaviour.Properties modifyOffset(BlockBehaviour.Properties properties) {
			properties.offsetFunction = (state, level, pos) -> {
				Block block = state.getBlock();
				BlockBehaviourAccessor accessor = (BlockBehaviourAccessor) block;
				long i = Mth.getSeed(pos.getX(), pos.getY(), pos.getZ());
				double d0 = ((double) ((float) (i >> 4 & 15L) / 15.0F) - 1.0D) * (double) accessor.invokeGetMaxVerticalOffset();
				float f = accessor.invokeGetMaxHorizontalOffset();
				double d1 = Mth.clamp(((double) ((float) (i & 15L) / 15.0F) - 0.5D) * 0.5D, -f, f);
				double d2 = Mth.clamp(((double) ((float) (i >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D, -f, f);

				Direction facing = state.getValue(CaveGrowthsBlock.FACING);
				Axis axis = facing.getAxis();
				Vec3 vec3 = axis == Axis.X ? new Vec3(d0, d1, d2) : axis == Axis.Y ? new Vec3(d1, d0, d2) : new Vec3(d1, d2, d0);
				if (facing.getAxisDirection() == AxisDirection.NEGATIVE)
					vec3 = vec3.reverse();

				return vec3;
			};
			return properties;
		}
	}

	public enum CCSkullTypes implements SkullBlock.Type {
		MIME("mime"), DEEPER("deeper"), EVENDEEPER("evendeeper"), PEEPER("peeper");

		private final String name;

		CCSkullTypes(String name) {
			this.name = name;
		}

		public static void registerSkullModels() {
			SkullBlockRenderer.SKIN_BY_TYPE.put(DEEPER, DeeperRenderer.DEEPER_TEXTURE);
			SkullBlockRenderer.SKIN_BY_TYPE.put(EVENDEEPER, EvendeeperRenderer.EVENDEEPER_TEXTURE);
			SkullBlockRenderer.SKIN_BY_TYPE.put(PEEPER, PeeperRenderer.PEEPER_TEXTURE);
			SkullBlockRenderer.SKIN_BY_TYPE.put(MIME, MimeRenderer.MIME_TEXTURE);
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}