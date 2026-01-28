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
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.roller_door.RollerDoorHeaderBlock;
import com.teamabnormals.caverns_and_chasms.common.block.turquoise.*;
import com.teamabnormals.caverns_and_chasms.common.block.weathering.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCConstants;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCBlocks {
	public static final CCBlockSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> SILVER_BLOCK = HELPER.createBlock("silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(CCSoundTypes.SILVER)));
	public static final RegistryObject<Block> SILVER_ORE = HELPER.createBlock("silver_ore", () -> new Block(CCProperties.ORE));
	public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = HELPER.createBlock("deepslate_silver_ore", () -> new Block(CCProperties.DEEPSLATE_ORE));
	public static final RegistryObject<Block> SOUL_SILVER_ORE = HELPER.createBlock("soul_silver_ore", () -> new DropExperienceBlock(CCProperties.SOUL_SILVER_ORE, UniformInt.of(0, 1)));
	public static final RegistryObject<Block> RAW_SILVER_BLOCK = HELPER.createBlock("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));
	public static final RegistryObject<Block> SILVER_BARS = HELPER.createBlock("silver_bars", () -> new IronBarsBlock(CCProperties.SILVER_BARS));
	public static final RegistryObject<Block> MEDIUM_WEIGHTED_PRESSURE_PLATE = HELPER.createBlock("medium_weighted_pressure_plate", () -> new WeightedPressurePlateBlock(75, CCProperties.SILVER_PRESSURE_PLATE, CCProperties.SILVER_BLOCK_SET.get()));

	public static final RegistryObject<Block> TIN_BLOCK = HELPER.createBlock("tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(CCSoundTypes.TIN)));
	public static final RegistryObject<Block> TIN_ORE = HELPER.createBlock("tin_ore", () -> new Block(CCProperties.TIN_ORE));
	public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = HELPER.createBlock("deepslate_tin_ore", () -> new Block(CCProperties.DEEPSLATE_TIN_ORE));
	public static final RegistryObject<Block> CASSITERITE_TIN_ORE = HELPER.createBlock("cassiterite_tin_ore", () -> new Block(CCProperties.CASSITERITE_TIN_ORE));
	public static final RegistryObject<Block> RAW_TIN_BLOCK = HELPER.createBlock("raw_tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.TIN_ORE)));
	public static final RegistryObject<Block> TIN_BARS = HELPER.createBlock("tin_bars", () -> new IronBarsBlock(CCProperties.TIN_BARS));
	public static final RegistryObject<Block> FLOAT_GLASS = HELPER.createBlock("float_glass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).sound(CCSoundTypes.FLOAT_GLASS)));
	public static final RegistryObject<Block> FLOAT_GLASS_PANE = HELPER.createBlock("float_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).sound(CCSoundTypes.FLOAT_GLASS)));

	public static final RegistryObject<Block> HOLD_PLATE = HELPER.createBlock("hold_plate", () -> new HoldPlateBlock(CCProperties.HOLD_PLATE));
	public static final RegistryObject<Block> HOLD_BUTTON = HELPER.createBlock("hold_button", () -> new HoldButtonBlock(CCProperties.HOLD_BUTTON));
	public static final RegistryObject<Block> WINCH = HELPER.createWinchBlock("winch", () -> new WinchBlock(CCProperties.WINCH));
	public static final RegistryObject<Block> WALL_DIMMER = HELPER.createBlockNoItem("wall_dimmer", () -> new WallDimmerBlock(CCProperties.DIMMER));
	public static final RegistryObject<Block> DIMMER = HELPER.createBlockNoItem("dimmer", () -> new DimmerBlock(CCProperties.DIMMER));
	public static final RegistryObject<Block> BOUNCER = HELPER.createBlock("bouncer", () -> new BouncerBlock(BlockBehaviour.Properties.copy(TIN_BLOCK.get())));
	public static final RegistryObject<Block> HOOP = HELPER.createBlock("hoop", () -> new HoopBlock(CCProperties.HOOP));
	public static final RegistryObject<Block> STORAGE_DUCT = HELPER.createBlock("storage_duct", () -> new StorageDuctBlock(CCProperties.STORAGE_DUCT));
	public static final RegistryObject<Block> STORAGE_DUCT_HATCH = HELPER.createBlock("storage_duct_hatch", () -> new StorageDuctHatchBlock(CCProperties.STORAGE_DUCT_HATCH));

	public static final RegistryObject<Block> ROLLER_DOOR = HELPER.createRollerDoorBlock("roller_door", () -> new RollerDoorBlock(CCProperties.ROLLER_DOOR));
	public static final RegistryObject<Block> ROLLER_DOOR_HEADER = HELPER.createBlockNoItem("roller_door_header", () -> new RollerDoorHeaderBlock(CCProperties.ROLLER_DOOR));

	public static final RegistryObject<Block> COPPER_RAIL = HELPER.createBlock("copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> EXPOSED_COPPER_RAIL = HELPER.createBlock("exposed_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.EXPOSED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> WEATHERED_COPPER_RAIL = HELPER.createBlock("weathered_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.WEATHERED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> OXIDIZED_COPPER_RAIL = HELPER.createBlock("oxidized_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.OXIDIZED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> WAXED_COPPER_RAIL = HELPER.createBlock("waxed_copper_rail", () -> new CopperRailBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_RAIL = HELPER.createBlock("waxed_exposed_copper_rail", () -> new CopperRailBlock(WeatherState.EXPOSED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_RAIL = HELPER.createBlock("waxed_weathered_copper_rail", () -> new CopperRailBlock(WeatherState.WEATHERED, CCProperties.COPPER_RAIL));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_RAIL = HELPER.createBlock("waxed_oxidized_copper_rail", () -> new CopperRailBlock(WeatherState.OXIDIZED, CCProperties.COPPER_RAIL));

	public static final RegistryObject<Block> HALT_RAIL = HELPER.createBlock("halt_rail", () -> new HaltRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL)));
	public static final RegistryObject<Block> SPIKED_RAIL = HELPER.createBlock("spiked_rail", () -> new SpikedRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL).sound(CCSoundTypes.SILVER)));
	public static final RegistryObject<Block> SLAUGHTER_RAIL = HELPER.createBlock("slaughter_rail", () -> new SlaughterRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL).sound(CCSoundTypes.SILVER)));

	public static final RegistryObject<Block> RESISTOR = HELPER.createBlock("resistor", () -> new ResistorBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER)));
	public static final RegistryObject<Block> REFRACTOR = HELPER.createBlock("refractor", () -> new RefractorBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER)));

	public static final RegistryObject<Block> SANGUINE_BLOCK = HELPER.createBlock("sanguine_block", () -> new Block(CCProperties.SANGUINE_TILES));
	public static final RegistryObject<Block> SANGUINE_TILES = HELPER.createBlock("sanguine_tiles", () -> new Block(CCProperties.SANGUINE_TILES));
	public static final RegistryObject<Block> SANGUINE_TILE_STAIRS = HELPER.createBlock("sanguine_tile_stairs", () -> new StairBlock(() -> SANGUINE_TILES.get().defaultBlockState(), CCProperties.SANGUINE_TILES));
	public static final RegistryObject<Block> SANGUINE_TILE_SLAB = HELPER.createBlock("sanguine_tile_slab", () -> new SlabBlock(CCProperties.SANGUINE_TILES));
	public static final RegistryObject<Block> SANGUINE_TILE_WALL = HELPER.createBlock("sanguine_tile_wall", () -> new WallBlock(CCProperties.SANGUINE_TILES));
	public static final RegistryObject<Block> FORTIFIED_SANGUINE_TILES = HELPER.createBlock("fortified_sanguine_tiles", () -> new Block(CCProperties.FORTIFIED_SANGUINE_TILES));
	public static final RegistryObject<Block> FORTIFIED_SANGUINE_TILE_STAIRS = HELPER.createBlock("fortified_sanguine_tile_stairs", () -> new StairBlock(() -> FORTIFIED_SANGUINE_TILES.get().defaultBlockState(), CCProperties.FORTIFIED_SANGUINE_TILES));
	public static final RegistryObject<Block> FORTIFIED_SANGUINE_TILE_SLAB = HELPER.createBlock("fortified_sanguine_tile_slab", () -> new SlabBlock(CCProperties.FORTIFIED_SANGUINE_TILES));
	public static final RegistryObject<Block> FORTIFIED_SANGUINE_TILE_WALL = HELPER.createBlock("fortified_sanguine_tile_wall", () -> new WallBlock(CCProperties.FORTIFIED_SANGUINE_TILES));

	public static final RegistryObject<Block> NECROMIUM_BLOCK = HELPER.createBlock("necromium_block", () -> new Block(CCProperties.NECROMIUM_BLOCK), new Item.Properties().fireResistant());

	public static final RegistryObject<Block> BRAZIER = HELPER.createBlock("brazier", () -> new BrazierBlock(1.0F, CCProperties.BRAZIER));
	public static final RegistryObject<Block> SOUL_BRAZIER = HELPER.createBlock("soul_brazier", () -> new BrazierBlock(2.0F, CCProperties.BRAZIER_DIM));
	public static final RegistryObject<Block> ENDER_BRAZIER = HELPER.createBlock("ender_brazier", () -> new BrazierBlock(3.0F, CCProperties.BRAZIER));
	public static final RegistryObject<Block> CUPRIC_BRAZIER = HELPER.createBlock("cupric_brazier", () -> new BrazierBlock(0.5F, CCProperties.BRAZIER_DIM));

	public static final RegistryObject<Block> CUPRIC_FIRE = HELPER.createBlockNoItem("cupric_fire", () -> new CupricFireBlock(Block.Properties.copy(Blocks.SOUL_FIRE)));
	public static final RegistryObject<Block> CUPRIC_CAMPFIRE = HELPER.createBlock("cupric_campfire", () -> new CupricCampfireBlock(Block.Properties.copy(Blocks.SOUL_CAMPFIRE)));
	public static final RegistryObject<Block> CUPRIC_LANTERN = HELPER.createBlock("cupric_lantern", () -> new LanternBlock(Block.Properties.copy(Blocks.SOUL_LANTERN)));
	public static final RegistryObject<Block> CUPRIC_WALL_TORCH = HELPER.createBlockNoItem("cupric_wall_torch", () -> new CupricWallTorchBlock(Block.Properties.copy(Blocks.SOUL_TORCH)));
	public static final RegistryObject<Block> CUPRIC_TORCH = HELPER.createStandingAndWallBlock("cupric_torch", () -> new CupricTorchBlock(Block.Properties.copy(Blocks.SOUL_TORCH)), CUPRIC_WALL_TORCH, Direction.DOWN);

	public static final RegistryObject<Block> ROTTEN_FLESH_BLOCK = HELPER.createBlock("rotten_flesh_block", () -> new Block(CCProperties.ROTTEN_FLESH_BLOCK));

	public static final RegistryObject<Block> DEEPER_HEAD = HELPER.createBlockNoItem("deeper_head", () -> new DeeperSkullBlock(BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> DEEPER_WALL_HEAD = HELPER.createBlockNoItem("deeper_wall_head", () -> new DeeperWallSkullBlock(BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(DEEPER_HEAD.get())));
	public static final RegistryObject<Block> PEEPER_HEAD = HELPER.createBlockNoItem("peeper_head", () -> new CCSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> PEEPER_WALL_HEAD = HELPER.createBlockNoItem("peeper_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(PEEPER_HEAD.get())));
	public static final RegistryObject<Block> MIME_HEAD = HELPER.createBlockNoItem("mime_head", () -> new CCSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> MIME_WALL_HEAD = HELPER.createBlockNoItem("mime_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(MIME_HEAD.get())));

	public static final RegistryObject<Block> TMT = HELPER.createBlock("tmt", () -> new TmtBlock(CCProperties.TMT));

	public static final RegistryObject<Block> SCATTERER = HELPER.createBlock("scatterer", () -> new ScattererBlock(BlockBehaviour.Properties.copy(Blocks.DISPENSER)));
	public static final RegistryObject<Block> SPLURTER = HELPER.createBlock("splurter", () -> new SplurterBlock(BlockBehaviour.Properties.copy(Blocks.DISPENSER)));

	public static final RegistryObject<Block> FLOODLIGHT = HELPER.createBlock("floodlight", () -> new WeatheringFloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT));
	public static final RegistryObject<Block> EXPOSED_FLOODLIGHT = HELPER.createBlock("exposed_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT));
	public static final RegistryObject<Block> WEATHERED_FLOODLIGHT = HELPER.createBlock("weathered_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT));
	public static final RegistryObject<Block> OXIDIZED_FLOODLIGHT = HELPER.createBlock("oxidized_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_FLOODLIGHT = HELPER.createBlock("waxed_floodlight", () -> new FloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_EXPOSED_FLOODLIGHT = HELPER.createBlock("waxed_exposed_floodlight", () -> new FloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_WEATHERED_FLOODLIGHT = HELPER.createBlock("waxed_weathered_floodlight", () -> new FloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_OXIDIZED_FLOODLIGHT = HELPER.createBlock("waxed_oxidized_floodlight", () -> new FloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_FLOODLIGHT));

	public static final RegistryObject<Block> TOOLBOX = HELPER.createToolboxBlock("toolbox", () -> new WeatheringToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> EXPOSED_TOOLBOX = HELPER.createToolboxBlock("exposed_toolbox", () -> new WeatheringToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WEATHERED_TOOLBOX = HELPER.createToolboxBlock("weathered_toolbox", () -> new WeatheringToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> OXIDIZED_TOOLBOX = HELPER.createToolboxBlock("oxidized_toolbox", () -> new WeatheringToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_TOOLBOX = HELPER.createToolboxBlock("waxed_toolbox", () -> new ToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_EXPOSED_TOOLBOX = HELPER.createToolboxBlock("waxed_exposed_toolbox", () -> new ToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_WEATHERED_TOOLBOX = HELPER.createToolboxBlock("waxed_weathered_toolbox", () -> new ToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_OXIDIZED_TOOLBOX = HELPER.createToolboxBlock("waxed_oxidized_toolbox", () -> new ToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));

	public static final RegistryObject<Block> CHISELED_COPPER = HELPER.createBlock("chiseled_copper", () -> new CCWeatheringCopperFullBlock(WeatherState.UNAFFECTED, CCProperties.COPPER));
	public static final RegistryObject<Block> EXPOSED_CHISELED_COPPER = HELPER.createBlock("exposed_chiseled_copper", () -> new CCWeatheringCopperFullBlock(WeatherState.EXPOSED, CCProperties.COPPER));
	public static final RegistryObject<Block> WEATHERED_CHISELED_COPPER = HELPER.createBlock("weathered_chiseled_copper", () -> new CCWeatheringCopperFullBlock(WeatherState.WEATHERED, CCProperties.COPPER));
	public static final RegistryObject<Block> OXIDIZED_CHISELED_COPPER = HELPER.createBlock("oxidized_chiseled_copper", () -> new CCWeatheringCopperFullBlock(WeatherState.OXIDIZED, CCProperties.COPPER));
	public static final RegistryObject<Block> WAXED_CHISELED_COPPER = HELPER.createBlock("waxed_chiseled_copper", () -> new Block(CCProperties.COPPER));
	public static final RegistryObject<Block> WAXED_EXPOSED_CHISELED_COPPER = HELPER.createBlock("waxed_exposed_chiseled_copper", () -> new Block(CCProperties.COPPER));
	public static final RegistryObject<Block> WAXED_WEATHERED_CHISELED_COPPER = HELPER.createBlock("waxed_weathered_chiseled_copper", () -> new Block(CCProperties.COPPER));
	public static final RegistryObject<Block> WAXED_OXIDIZED_CHISELED_COPPER = HELPER.createBlock("waxed_oxidized_chiseled_copper", () -> new Block(CCProperties.COPPER));

	public static final RegistryObject<Block> COPPER_GRATE = HELPER.createBlock("copper_grate", () -> new WeatheringCopperGrateBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> EXPOSED_COPPER_GRATE = HELPER.createBlock("exposed_copper_grate", () -> new WeatheringCopperGrateBlock(WeatherState.EXPOSED, CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> WEATHERED_COPPER_GRATE = HELPER.createBlock("weathered_copper_grate", () -> new WeatheringCopperGrateBlock(WeatherState.WEATHERED, CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> OXIDIZED_COPPER_GRATE = HELPER.createBlock("oxidized_copper_grate", () -> new WeatheringCopperGrateBlock(WeatherState.OXIDIZED, CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> WAXED_COPPER_GRATE = HELPER.createBlock("waxed_copper_grate", () -> new WaterloggedTransparentBlock(CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_GRATE = HELPER.createBlock("waxed_exposed_copper_grate", () -> new WaterloggedTransparentBlock(CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_GRATE = HELPER.createBlock("waxed_weathered_copper_grate", () -> new WaterloggedTransparentBlock(CCProperties.COPPER_GRATE));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_GRATE = HELPER.createBlock("waxed_oxidized_copper_grate", () -> new WaterloggedTransparentBlock(CCProperties.COPPER_GRATE));

	public static final RegistryObject<Block> COPPER_BULB = HELPER.createBlock("copper_bulb", () -> new WeatheringCopperBulbBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_BULB));
	public static final RegistryObject<Block> EXPOSED_COPPER_BULB = HELPER.createBlock("exposed_copper_bulb", () -> new WeatheringCopperBulbBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_BULB));
	public static final RegistryObject<Block> WEATHERED_COPPER_BULB = HELPER.createBlock("weathered_copper_bulb", () -> new WeatheringCopperBulbBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_BULB));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BULB = HELPER.createBlock("oxidized_copper_bulb", () -> new WeatheringCopperBulbBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_BULB));
	public static final RegistryObject<Block> WAXED_COPPER_BULB = HELPER.createBlock("waxed_copper_bulb", () -> new CopperBulbBlock(CCProperties.COPPER_BULB));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BULB = HELPER.createBlock("waxed_exposed_copper_bulb", () -> new CopperBulbBlock(CCProperties.EXPOSED_COPPER_BULB));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BULB = HELPER.createBlock("waxed_weathered_copper_bulb", () -> new CopperBulbBlock(CCProperties.WEATHERED_COPPER_BULB));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BULB = HELPER.createBlock("waxed_oxidized_copper_bulb", () -> new CopperBulbBlock(CCProperties.OXIDIZED_COPPER_BULB));

	public static final RegistryObject<Block> COPPER_DOOR = HELPER.createBlock("copper_door", () -> new WeatheringCopperDoorBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> EXPOSED_COPPER_DOOR = HELPER.createBlock("exposed_copper_door", () -> new WeatheringCopperDoorBlock(WeatherState.EXPOSED, CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WEATHERED_COPPER_DOOR = HELPER.createBlock("weathered_copper_door", () -> new WeatheringCopperDoorBlock(WeatherState.WEATHERED, CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> OXIDIZED_COPPER_DOOR = HELPER.createBlock("oxidized_copper_door", () -> new WeatheringCopperDoorBlock(WeatherState.OXIDIZED, CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_COPPER_DOOR = HELPER.createBlock("waxed_copper_door", () -> new CopperDoorBlock(CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_DOOR = HELPER.createBlock("waxed_exposed_copper_door", () -> new CopperDoorBlock(CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_DOOR = HELPER.createBlock("waxed_weathered_copper_door", () -> new CopperDoorBlock(CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_DOOR = HELPER.createBlock("waxed_oxidized_copper_door", () -> new CopperDoorBlock(CCProperties.COPPER_DOOR, CCProperties.COPPER_BLOCK_SET.get()));

	public static final RegistryObject<Block> COPPER_TRAPDOOR = HELPER.createBlock("copper_trapdoor", () -> new WeatheringCopperTrapDoorBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> EXPOSED_COPPER_TRAPDOOR = HELPER.createBlock("exposed_copper_trapdoor", () -> new WeatheringCopperTrapDoorBlock(WeatherState.EXPOSED, CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WEATHERED_COPPER_TRAPDOOR = HELPER.createBlock("weathered_copper_trapdoor", () -> new WeatheringCopperTrapDoorBlock(WeatherState.WEATHERED, CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> OXIDIZED_COPPER_TRAPDOOR = HELPER.createBlock("oxidized_copper_trapdoor", () -> new WeatheringCopperTrapDoorBlock(WeatherState.OXIDIZED, CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_COPPER_TRAPDOOR = HELPER.createBlock("waxed_copper_trapdoor", () -> new TrapDoorBlock(CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_TRAPDOOR = HELPER.createBlock("waxed_exposed_copper_trapdoor", () -> new TrapDoorBlock(CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_TRAPDOOR = HELPER.createBlock("waxed_weathered_copper_trapdoor", () -> new TrapDoorBlock(CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_TRAPDOOR = HELPER.createBlock("waxed_oxidized_copper_trapdoor", () -> new TrapDoorBlock(CCProperties.COPPER_TRAPDOOR, CCProperties.COPPER_BLOCK_SET.get()));

	public static final RegistryObject<Block> COPPER_BARS = HELPER.createBlock("copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> EXPOSED_COPPER_BARS = HELPER.createBlock("exposed_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.EXPOSED, CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> WEATHERED_COPPER_BARS = HELPER.createBlock("weathered_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.WEATHERED, CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BARS = HELPER.createBlock("oxidized_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.OXIDIZED, CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> WAXED_COPPER_BARS = HELPER.createBlock("waxed_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BARS = HELPER.createBlock("waxed_exposed_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BARS = HELPER.createBlock("waxed_weathered_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BARS = HELPER.createBlock("waxed_oxidized_copper_bars", () -> new IronBarsBlock(CCProperties.COPPER_BARS));

	public static final RegistryObject<Block> COPPER_BUTTON = HELPER.createBlock("copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.UNAFFECTED, 20, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> EXPOSED_COPPER_BUTTON = HELPER.createBlock("exposed_copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.EXPOSED, 30, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> WEATHERED_COPPER_BUTTON = HELPER.createBlock("weathered_copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.WEATHERED, 40, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BUTTON = HELPER.createBlock("oxidized_copper_button", () -> new WeatheringCopperButtonBlock(WeatherState.OXIDIZED, 50, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> WAXED_COPPER_BUTTON = HELPER.createBlock("waxed_copper_button", () -> new CopperButtonBlock(WeatherState.UNAFFECTED, 20, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BUTTON = HELPER.createBlock("waxed_exposed_copper_button", () -> new CopperButtonBlock(WeatherState.EXPOSED, 30, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BUTTON = HELPER.createBlock("waxed_weathered_copper_button", () -> new CopperButtonBlock(WeatherState.WEATHERED, 40, CCProperties.COPPER_BUTTON));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BUTTON = HELPER.createBlock("waxed_oxidized_copper_button", () -> new CopperButtonBlock(WeatherState.OXIDIZED, 50, CCProperties.COPPER_BUTTON));

	public static final RegistryObject<Block> EXPOSED_LIGHTNING_ROD = HELPER.createBlock("exposed_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)));
	public static final RegistryObject<Block> WEATHERED_LIGHTNING_ROD = HELPER.createBlock("weathered_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)));
	public static final RegistryObject<Block> OXIDIZED_LIGHTNING_ROD = HELPER.createBlock("oxidized_lightning_rod", () -> new WeatheringLightningRodBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)));
	public static final RegistryObject<Block> WAXED_LIGHTNING_ROD = HELPER.createBlock("waxed_lightning_rod", () -> new LightningRodBlock(WeatheringLightningRodBlock.Properties.copy(Blocks.LIGHTNING_ROD)));
	public static final RegistryObject<Block> WAXED_EXPOSED_LIGHTNING_ROD = HELPER.createBlock("waxed_exposed_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)));
	public static final RegistryObject<Block> WAXED_WEATHERED_LIGHTNING_ROD = HELPER.createBlock("waxed_weathered_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)));
	public static final RegistryObject<Block> WAXED_OXIDIZED_LIGHTNING_ROD = HELPER.createBlock("waxed_oxidized_lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties.copy(Blocks.LIGHTNING_ROD)));

	public static final RegistryObject<Block> COPPER_CHAIN = HELPER.createBlock("copper_chain", () -> new WeatheringChainBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> EXPOSED_COPPER_CHAIN = HELPER.createBlock("exposed_copper_chain", () -> new WeatheringChainBlock(WeatherState.EXPOSED, CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> WEATHERED_COPPER_CHAIN = HELPER.createBlock("weathered_copper_chain", () -> new WeatheringChainBlock(WeatherState.WEATHERED, CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> OXIDIZED_COPPER_CHAIN = HELPER.createBlock("oxidized_copper_chain", () -> new WeatheringChainBlock(WeatherState.OXIDIZED, CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> WAXED_COPPER_CHAIN = HELPER.createBlock("waxed_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_CHAIN = HELPER.createBlock("waxed_exposed_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_CHAIN = HELPER.createBlock("waxed_weathered_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_CHAIN = HELPER.createBlock("waxed_oxidized_copper_chain", () -> new ChainBlock(CCProperties.COPPER_CHAIN));

	public static final RegistryObject<Block> COPPER_LANTERN = HELPER.createBlock("copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> EXPOSED_COPPER_LANTERN = HELPER.createBlock("exposed_copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.EXPOSED, CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> WEATHERED_COPPER_LANTERN = HELPER.createBlock("weathered_copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.WEATHERED, CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> OXIDIZED_COPPER_LANTERN = HELPER.createBlock("oxidized_copper_lantern", () -> new WeatheringCopperLanternBlock(WeatherState.OXIDIZED, CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> WAXED_COPPER_LANTERN = HELPER.createBlock("waxed_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_LANTERN = HELPER.createBlock("waxed_exposed_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_LANTERN = HELPER.createBlock("waxed_weathered_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_LANTERN = HELPER.createBlock("waxed_oxidized_copper_lantern", () -> new CopperLanternBlock(CCProperties.COPPER_LANTERN));

	public static final RegistryObject<Block> LAVA_LAMP = HELPER.createBlock("lava_lamp", () -> new LavaLampBlock(CCProperties.LAVA_LAMP));
	public static final RegistryObject<Block> GOLDEN_BARS = HELPER.createBlock("golden_bars", () -> new IronBarsBlock(CCProperties.METAL_BARS));

	public static final RegistryObject<Block> LAPIS_LAZULI_BRICKS = HELPER.createBlock("lapis_bricks", () -> new Block(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_STAIRS = HELPER.createBlock("lapis_brick_stairs", () -> new StairBlock(() -> LAPIS_LAZULI_BRICKS.get().defaultBlockState(), CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_SLAB = HELPER.createBlock("lapis_brick_slab", () -> new SlabBlock(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_WALL = HELPER.createBlock("lapis_brick_wall", () -> new WallBlock(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_PILLAR = HELPER.createBlock("lapis_pillar", () -> new RotatedPillarBlock(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_LAMP = HELPER.createBlock("lapis_lamp", () -> new Block(CCProperties.LAMP));

	public static final RegistryObject<Block> SPINEL_ORE = HELPER.createBlock("spinel_ore", () -> new DropExperienceBlock(CCProperties.ORE, UniformInt.of(2, 5)));
	public static final RegistryObject<Block> DEEPSLATE_SPINEL_ORE = HELPER.createBlock("deepslate_spinel_ore", () -> new DropExperienceBlock(CCProperties.DEEPSLATE_ORE, UniformInt.of(2, 5)));
	public static final RegistryObject<Block> SPINEL_BLOCK = HELPER.createBlock("spinel_block", () -> new Block(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICKS = HELPER.createBlock("spinel_bricks", () -> new Block(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICK_STAIRS = HELPER.createBlock("spinel_brick_stairs", () -> new StairBlock(() -> SPINEL_BRICKS.get().defaultBlockState(), CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICK_SLAB = HELPER.createBlock("spinel_brick_slab", () -> new SlabBlock(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICK_WALL = HELPER.createBlock("spinel_brick_wall", () -> new WallBlock(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_PILLAR = HELPER.createBlock("spinel_pillar", () -> new RotatedPillarBlock(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_LAMP = HELPER.createBlock("spinel_lamp", () -> new Block(Properties.copy(LAPIS_LAZULI_LAMP.get()).sound(CCSoundTypes.SPINEL)));

	public static final RegistryObject<Block> DISMANTLING_TABLE = HELPER.createBlock("dismantling_table", () -> new DismantlingTableBlock(CCProperties.DISMANTLING_TABLE));
	public static final RegistryObject<Block> BEJEWELED_ANVIL = HELPER.createBlock("bejeweled_anvil", () -> new BejeweledAnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL).sound(CCSoundTypes.BEJEWELED_ANVIL)));
	public static final RegistryObject<Block> ATONING_TABLE = HELPER.createBlock("atoning_table", () -> new AtoningTableBlock(BlockBehaviour.Properties.copy(Blocks.ENCHANTING_TABLE).sound(CCSoundTypes.ATONING_TABLE)));

	public static final RegistryObject<Block> ZIRCONIA_BLOCK = HELPER.createBlock("zirconia_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.ZIRCONIA)));
	public static final RegistryObject<Block> ZIRCONIA_LAMP = HELPER.createBlock("zirconia_lamp", () -> new Block(Properties.copy(LAPIS_LAZULI_LAMP.get()).sound(CCSoundTypes.ORNATE_GLASS)));
	public static final RegistryObject<Block> ORNATE_GLASS = HELPER.createBlock("ornate_glass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).sound(CCSoundTypes.ORNATE_GLASS)));
	public static final RegistryObject<Block> ORNATE_GLASS_PANE = HELPER.createBlock("ornate_glass_pane", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE).sound(CCSoundTypes.ORNATE_GLASS)));

	public static final RegistryObject<Block> TURQUOISE_ORE = HELPER.createBlock("turquoise_ore", () -> new TurquoiseOreBlock(CCProperties.ORE, UniformInt.of(4, 9)), CCProperties.FANCY);
	public static final RegistryObject<Block> DEEPSLATE_TURQUOISE_ORE = HELPER.createBlock("deepslate_turquoise_ore", () -> new TurquoiseOreBlock(CCProperties.DEEPSLATE_ORE, UniformInt.of(4, 9)), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_BLOCK = HELPER.createBlock("turquoise_block", () -> new TurquoiseBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_TILES = HELPER.createBlock("turquoise_tiles", () -> new TurquoiseBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_TILE_STAIRS = HELPER.createBlock("turquoise_tile_stairs", () -> new TurquoiseStairBlock(() -> TURQUOISE_TILES.get().defaultBlockState(), CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_TILE_SLAB = HELPER.createBlock("turquoise_tile_slab", () -> new TurquoiseSlabBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_TILE_WALL = HELPER.createBlock("turquoise_tile_wall", () -> new TurquoiseWallBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_PILLAR = HELPER.createBlock("turquoise_pillar", () -> new TurquoisePillarBlock(CCProperties.TURQUOISE), CCProperties.FANCY);
	public static final RegistryObject<Block> TURQUOISE_LAMP = HELPER.createBlock("turquoise_lamp", () -> new Block(Properties.copy(LAPIS_LAZULI_LAMP.get())));

	public static final RegistryObject<Block> CAVIAR = HELPER.createBlockNoItem("caviar", () -> new CaviarBlock(PropertyUtil.flowerPot()));

	public static final RegistryObject<Block> QUARTZ_LAMP = HELPER.createBlock("quartz_lamp", () -> new Block(Properties.copy(LAPIS_LAZULI_LAMP.get())));
	public static final RegistryObject<Block> DIAMOND_LAMP = HELPER.createBlock("diamond_lamp", () -> new Block(Properties.copy(LAPIS_LAZULI_LAMP.get())));
	public static final RegistryObject<Block> EMERALD_LAMP = HELPER.createBlock("emerald_lamp", () -> new Block(Properties.copy(LAPIS_LAZULI_LAMP.get())));

	public static final RegistryObject<Block> ROCKY_DIRT = HELPER.createBlock("rocky_dirt", () -> new Block(CCProperties.ROCKY_DIRT));
	public static final RegistryObject<Block> FRAGILE_STONE = HELPER.createBlock("fragile_stone", () -> new FragileStoneBlock(CCProperties.FRAGILE_STONE));
	public static final RegistryObject<Block> FRAGILE_DEEPSLATE = HELPER.createBlock("fragile_deepslate", () -> new FragileDeepslateBlock(CCProperties.FRAGILE_DEEPSLATE));

	public static final RegistryObject<Block> COBBLESTONE_BRICKS = HELPER.createBlock("cobblestone_bricks", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> COBBLESTONE_BRICK_STAIRS = HELPER.createBlock("cobblestone_brick_stairs", () -> new StairBlock(() -> COBBLESTONE_BRICKS.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> COBBLESTONE_BRICK_SLAB = HELPER.createBlock("cobblestone_brick_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> COBBLESTONE_BRICK_WALL = HELPER.createBlock("cobblestone_brick_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICKS = HELPER.createBlock("mossy_cobblestone_bricks", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_STAIRS = HELPER.createBlock("mossy_cobblestone_brick_stairs", () -> new StairBlock(() -> COBBLESTONE_BRICKS.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_SLAB = HELPER.createBlock("mossy_cobblestone_brick_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_BRICK_WALL = HELPER.createBlock("mossy_cobblestone_brick_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));

	public static final RegistryObject<Block> COBBLESTONE_TILES = HELPER.createBlock("cobblestone_tiles", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> COBBLESTONE_TILE_STAIRS = HELPER.createBlock("cobblestone_tile_stairs", () -> new StairBlock(() -> COBBLESTONE_TILES.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> COBBLESTONE_TILE_SLAB = HELPER.createBlock("cobblestone_tile_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> COBBLESTONE_TILE_WALL = HELPER.createBlock("cobblestone_tile_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));

	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILES = HELPER.createBlock("mossy_cobblestone_tiles", () -> new Block(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_STAIRS = HELPER.createBlock("mossy_cobblestone_tile_stairs", () -> new StairBlock(() -> COBBLESTONE_TILES.get().defaultBlockState(), CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_SLAB = HELPER.createBlock("mossy_cobblestone_tile_slab", () -> new SlabBlock(CCProperties.COBBLESTONE_BRICKS));
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_TILE_WALL = HELPER.createBlock("mossy_cobblestone_tile_wall", () -> new WallBlock(CCProperties.COBBLESTONE_BRICKS));

	public static final RegistryObject<Block> COBBLED_DEEPSLATE_BRICKS = HELPER.createBlock("cobbled_deepslate_bricks", () -> new Block(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final RegistryObject<Block> COBBLED_DEEPSLATE_BRICK_STAIRS = HELPER.createBlock("cobbled_deepslate_brick_stairs", () -> new StairBlock(() -> COBBLED_DEEPSLATE_BRICKS.get().defaultBlockState(), CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final RegistryObject<Block> COBBLED_DEEPSLATE_BRICK_SLAB = HELPER.createBlock("cobbled_deepslate_brick_slab", () -> new SlabBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final RegistryObject<Block> COBBLED_DEEPSLATE_BRICK_WALL = HELPER.createBlock("cobbled_deepslate_brick_wall", () -> new WallBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));

	public static final RegistryObject<Block> COBBLED_DEEPSLATE_TILES = HELPER.createBlock("cobbled_deepslate_tiles", () -> new Block(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final RegistryObject<Block> COBBLED_DEEPSLATE_TILE_STAIRS = HELPER.createBlock("cobbled_deepslate_tile_stairs", () -> new StairBlock(() -> COBBLED_DEEPSLATE_TILES.get().defaultBlockState(), CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final RegistryObject<Block> COBBLED_DEEPSLATE_TILE_SLAB = HELPER.createBlock("cobbled_deepslate_tile_slab", () -> new SlabBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));
	public static final RegistryObject<Block> COBBLED_DEEPSLATE_TILE_WALL = HELPER.createBlock("cobbled_deepslate_tile_wall", () -> new WallBlock(CCProperties.COBBLED_DEEPSLATE_BRICKS));

	public static final RegistryObject<Block> STONE_WALL = HELPER.createBlock("stone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
	public static final RegistryObject<Block> POLISHED_GRANITE_WALL = HELPER.createBlock("polished_granite_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_GRANITE)));
	public static final RegistryObject<Block> POLISHED_DIORITE_WALL = HELPER.createBlock("polished_diorite_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_DIORITE)));
	public static final RegistryObject<Block> POLISHED_ANDESITE_WALL = HELPER.createBlock("polished_andesite_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_ANDESITE)));

	public static final RegistryObject<Block> CALCITE_STAIRS = HELPER.createBlock("calcite_stairs", () -> new StairBlock(() -> Blocks.CALCITE.defaultBlockState(), CCProperties.CALCITE));
	public static final RegistryObject<Block> CALCITE_SLAB = HELPER.createBlock("calcite_slab", () -> new SlabBlock(CCProperties.CALCITE));
	public static final RegistryObject<Block> CALCITE_WALL = HELPER.createBlock("calcite_wall", () -> new WallBlock(CCProperties.CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE = HELPER.createBlock("polished_calcite", () -> new Block(CCProperties.POLISHED_CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE_STAIRS = HELPER.createBlock("polished_calcite_stairs", () -> new StairBlock(() -> POLISHED_CALCITE.get().defaultBlockState(), CCProperties.POLISHED_CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE_SLAB = HELPER.createBlock("polished_calcite_slab", () -> new SlabBlock(CCProperties.POLISHED_CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE_WALL = HELPER.createBlock("polished_calcite_wall", () -> new WallBlock(CCProperties.POLISHED_CALCITE));
	public static final RegistryObject<Block> CHISELED_CALCITE = HELPER.createBlock("chiseled_calcite", () -> new BlueprintDirectionalBlock(CCProperties.POLISHED_CALCITE));
	public static final RegistryObject<Block> CALCITE_PILLAR = HELPER.createBlock("calcite_pillar", () -> new RotatedPillarBlock(CCProperties.POLISHED_CALCITE));
	public static final RegistryObject<Block> CALCITE_BRICKS = HELPER.createBlock("calcite_bricks", () -> new Block(CCProperties.CALCITE_BRICKS));
	public static final RegistryObject<Block> CALCITE_BRICK_STAIRS = HELPER.createBlock("calcite_brick_stairs", () -> new StairBlock(() -> CALCITE_BRICKS.get().defaultBlockState(), CCProperties.CALCITE_BRICKS));
	public static final RegistryObject<Block> CALCITE_BRICK_SLAB = HELPER.createBlock("calcite_brick_slab", () -> new SlabBlock(CCProperties.CALCITE_BRICKS));
	public static final RegistryObject<Block> CALCITE_BRICK_WALL = HELPER.createBlock("calcite_brick_wall", () -> new WallBlock(CCProperties.CALCITE_BRICKS));
	public static final RegistryObject<Block> CHISELED_CALCITE_BRICKS = HELPER.createBlock("chiseled_calcite_bricks", () -> new Block(CCProperties.CALCITE_BRICKS));
	public static final RegistryObject<Block> SMOOTH_CALCITE = HELPER.createBlock("smooth_calcite", () -> new Block(CCProperties.CALCITE));
	public static final RegistryObject<Block> SMOOTH_CALCITE_STAIRS = HELPER.createBlock("smooth_calcite_stairs", () -> new StairBlock(() -> SMOOTH_CALCITE.get().defaultBlockState(), CCProperties.CALCITE));
	public static final RegistryObject<Block> SMOOTH_CALCITE_SLAB = HELPER.createBlock("smooth_calcite_slab", () -> new SlabBlock(CCProperties.CALCITE));

	public static final RegistryObject<Block> TUFF_STAIRS = HELPER.createBlock("tuff_stairs", () -> new StairBlock(() -> Blocks.TUFF.defaultBlockState(), CCProperties.TUFF));
	public static final RegistryObject<Block> TUFF_SLAB = HELPER.createBlock("tuff_slab", () -> new SlabBlock(CCProperties.TUFF));
	public static final RegistryObject<Block> TUFF_WALL = HELPER.createBlock("tuff_wall", () -> new WallBlock(CCProperties.TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF = HELPER.createBlock("polished_tuff", () -> new Block(CCProperties.POLISHED_TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF_STAIRS = HELPER.createBlock("polished_tuff_stairs", () -> new StairBlock(() -> POLISHED_TUFF.get().defaultBlockState(), CCProperties.POLISHED_TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF_SLAB = HELPER.createBlock("polished_tuff_slab", () -> new SlabBlock(CCProperties.POLISHED_TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF_WALL = HELPER.createBlock("polished_tuff_wall", () -> new WallBlock(CCProperties.POLISHED_TUFF));
	public static final RegistryObject<Block> CHISELED_TUFF = HELPER.createBlock("chiseled_tuff", () -> new Block(CCProperties.POLISHED_TUFF));
	public static final RegistryObject<Block> TUFF_BRICKS = HELPER.createBlock("tuff_bricks", () -> new Block(CCProperties.TUFF_BRICKS));
	public static final RegistryObject<Block> TUFF_BRICK_STAIRS = HELPER.createBlock("tuff_brick_stairs", () -> new StairBlock(() -> TUFF_BRICKS.get().defaultBlockState(), CCProperties.TUFF_BRICKS));
	public static final RegistryObject<Block> TUFF_BRICK_SLAB = HELPER.createBlock("tuff_brick_slab", () -> new SlabBlock(CCProperties.TUFF_BRICKS));
	public static final RegistryObject<Block> TUFF_BRICK_WALL = HELPER.createBlock("tuff_brick_wall", () -> new WallBlock(CCProperties.TUFF_BRICKS));
	public static final RegistryObject<Block> CHISELED_TUFF_BRICKS = HELPER.createBlock("chiseled_tuff_bricks", () -> new Block(CCProperties.TUFF_BRICKS));
	public static final RegistryObject<Block> SMOOTH_TUFF = HELPER.createBlock("smooth_tuff", () -> new Block(CCProperties.TUFF));
	public static final RegistryObject<Block> SMOOTH_TUFF_STAIRS = HELPER.createBlock("smooth_tuff_stairs", () -> new StairBlock(() -> SMOOTH_TUFF.get().defaultBlockState(), CCProperties.TUFF));
	public static final RegistryObject<Block> SMOOTH_TUFF_SLAB = HELPER.createBlock("smooth_tuff_slab", () -> new SlabBlock(CCProperties.TUFF));

	public static final RegistryObject<Block> SUGILITE = HELPER.createBlock("sugilite", () -> new Block(CCProperties.SUGILITE));
	public static final RegistryObject<Block> SUGILITE_STAIRS = HELPER.createBlock("sugilite_stairs", () -> new StairBlock(() -> SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE));
	public static final RegistryObject<Block> SUGILITE_SLAB = HELPER.createBlock("sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE));
	public static final RegistryObject<Block> SUGILITE_WALL = HELPER.createBlock("sugilite_wall", () -> new WallBlock(CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE = HELPER.createBlock("polished_sugilite", () -> new Block(CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE_STAIRS = HELPER.createBlock("polished_sugilite_stairs", () -> new StairBlock(() -> POLISHED_SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE_SLAB = HELPER.createBlock("polished_sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE_WALL = HELPER.createBlock("polished_sugilite_wall", () -> new WallBlock(CCProperties.SUGILITE));

	public static final RegistryObject<Block> CASSITERITE = HELPER.createBlock("cassiterite", () -> new Block(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_STAIRS = HELPER.createBlock("cassiterite_stairs", () -> new StairBlock(() -> CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_SLAB = HELPER.createBlock("cassiterite_slab", () -> new SlabBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_WALL = HELPER.createBlock("cassiterite_wall", () -> new WallBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> SMOOTH_CASSITERITE = HELPER.createBlock("smooth_cassiterite", () -> new Block(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> SMOOTH_CASSITERITE_STAIRS = HELPER.createBlock("smooth_cassiterite_stairs", () -> new StairBlock(() -> CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE));
	public static final RegistryObject<Block> SMOOTH_CASSITERITE_SLAB = HELPER.createBlock("smooth_cassiterite_slab", () -> new SlabBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_BRICKS = HELPER.createBlock("cassiterite_bricks", () -> new Block(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_BRICK_STAIRS = HELPER.createBlock("cassiterite_brick_stairs", () -> new StairBlock(() -> CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_BRICK_SLAB = HELPER.createBlock("cassiterite_brick_slab", () -> new SlabBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_BRICK_WALL = HELPER.createBlock("cassiterite_brick_wall", () -> new WallBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CASSITERITE_PILLAR = HELPER.createBlock("cassiterite_pillar", () -> new RotatedPillarBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> CHISELED_CASSITERITE_BRICKS = HELPER.createBlock("chiseled_cassiterite_bricks", () -> new Block(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> POLISHED_CASSITERITE = HELPER.createBlock("polished_cassiterite", () -> new Block(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> POLISHED_CASSITERITE_STAIRS = HELPER.createBlock("polished_cassiterite_stairs", () -> new StairBlock(() -> POLISHED_CASSITERITE.get().defaultBlockState(), CCProperties.CASSITERITE));
	public static final RegistryObject<Block> POLISHED_CASSITERITE_SLAB = HELPER.createBlock("polished_cassiterite_slab", () -> new SlabBlock(CCProperties.CASSITERITE));
	public static final RegistryObject<Block> POLISHED_CASSITERITE_WALL = HELPER.createBlock("polished_cassiterite_wall", () -> new WallBlock(CCProperties.CASSITERITE));

	public static final RegistryObject<Block> RHYOLITE = HELPER.createBlock("rhyolite", () -> new Block(CCProperties.RHYOLITE));
	public static final RegistryObject<Block> RHYOLITE_STAIRS = HELPER.createBlock("rhyolite_stairs", () -> new StairBlock(() -> RHYOLITE.get().defaultBlockState(), CCProperties.RHYOLITE));
	public static final RegistryObject<Block> RHYOLITE_SLAB = HELPER.createBlock("rhyolite_slab", () -> new SlabBlock(CCProperties.RHYOLITE));
	public static final RegistryObject<Block> RHYOLITE_WALL = HELPER.createBlock("rhyolite_wall", () -> new WallBlock(CCProperties.RHYOLITE));
	public static final RegistryObject<Block> POLISHED_RHYOLITE = HELPER.createBlock("polished_rhyolite", () -> new Block(CCProperties.POLISHED_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_RHYOLITE_STAIRS = HELPER.createBlock("polished_rhyolite_stairs", () -> new StairBlock(() -> POLISHED_RHYOLITE.get().defaultBlockState(), CCProperties.POLISHED_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_RHYOLITE_SLAB = HELPER.createBlock("polished_rhyolite_slab", () -> new SlabBlock(CCProperties.POLISHED_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_RHYOLITE_WALL = HELPER.createBlock("polished_rhyolite_wall", () -> new WallBlock(CCProperties.POLISHED_RHYOLITE));
	public static final RegistryObject<Block> RHYOLITE_BRICKS = HELPER.createBlock("rhyolite_bricks", () -> new Block(CCProperties.RHYOLITE_BRICKS));
	public static final RegistryObject<Block> RHYOLITE_BRICK_STAIRS = HELPER.createBlock("rhyolite_brick_stairs", () -> new StairBlock(() -> RHYOLITE_BRICKS.get().defaultBlockState(), CCProperties.RHYOLITE_BRICKS));
	public static final RegistryObject<Block> RHYOLITE_BRICK_SLAB = HELPER.createBlock("rhyolite_brick_slab", () -> new SlabBlock(CCProperties.RHYOLITE_BRICKS));
	public static final RegistryObject<Block> RHYOLITE_BRICK_WALL = HELPER.createBlock("rhyolite_brick_wall", () -> new WallBlock(CCProperties.RHYOLITE_BRICKS));
	public static final RegistryObject<Block> CHISELED_RHYOLITE_BRICKS = HELPER.createBlock("chiseled_rhyolite_bricks", () -> new Block(CCProperties.RHYOLITE_BRICKS));

	public static final RegistryObject<Block> MAGMATIC_RHYOLITE = HELPER.createBlock("magmatic_rhyolite", () -> new MagmaBlock(CCProperties.MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_STAIRS = HELPER.createBlock("magmatic_rhyolite_stairs", () -> new StairBlock(() -> MAGMATIC_RHYOLITE.get().defaultBlockState(), CCProperties.MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_SLAB = HELPER.createBlock("magmatic_rhyolite_slab", () -> new SlabBlock(CCProperties.MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_WALL = HELPER.createBlock("magmatic_rhyolite_wall", () -> new WallBlock(CCProperties.MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_MAGMATIC_RHYOLITE = HELPER.createBlock("polished_magmatic_rhyolite", () -> new Block(CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_MAGMATIC_RHYOLITE_STAIRS = HELPER.createBlock("polished_magmatic_rhyolite_stairs", () -> new StairBlock(() -> POLISHED_MAGMATIC_RHYOLITE.get().defaultBlockState(), CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_MAGMATIC_RHYOLITE_SLAB = HELPER.createBlock("polished_magmatic_rhyolite_slab", () -> new SlabBlock(CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> POLISHED_MAGMATIC_RHYOLITE_WALL = HELPER.createBlock("polished_magmatic_rhyolite_wall", () -> new WallBlock(CCProperties.POLISHED_MAGMATIC_RHYOLITE));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_BRICKS = HELPER.createBlock("magmatic_rhyolite_bricks", () -> new Block(CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_BRICK_STAIRS = HELPER.createBlock("magmatic_rhyolite_brick_stairs", () -> new StairBlock(() -> MAGMATIC_RHYOLITE_BRICKS.get().defaultBlockState(), CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_BRICK_SLAB = HELPER.createBlock("magmatic_rhyolite_brick_slab", () -> new SlabBlock(CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final RegistryObject<Block> MAGMATIC_RHYOLITE_BRICK_WALL = HELPER.createBlock("magmatic_rhyolite_brick_wall", () -> new WallBlock(CCProperties.MAGMATIC_RHYOLITE_BRICKS));
	public static final RegistryObject<Block> CHISELED_MAGMATIC_RHYOLITE_BRICKS = HELPER.createBlock("chiseled_magmatic_rhyolite_bricks", () -> new Block(CCProperties.MAGMATIC_RHYOLITE_BRICKS));

	public static final RegistryObject<Block> DRIPSTONE_STAIRS = HELPER.createBlock("dripstone_stairs", () -> new StairBlock(() -> Blocks.DRIPSTONE_BLOCK.defaultBlockState(), CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_SLAB = HELPER.createBlock("dripstone_slab", () -> new SlabBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_WALL = HELPER.createBlock("dripstone_wall", () -> new WallBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> SMOOTH_DRIPSTONE = HELPER.createBlock("smooth_dripstone", () -> new Block(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> SMOOTH_DRIPSTONE_STAIRS = HELPER.createBlock("smooth_dripstone_stairs", () -> new StairBlock(() -> SMOOTH_DRIPSTONE.get().defaultBlockState(), CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> SMOOTH_DRIPSTONE_SLAB = HELPER.createBlock("smooth_dripstone_slab", () -> new SlabBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> POLISHED_DRIPSTONE = HELPER.createBlock("polished_dripstone", () -> new Block(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> POLISHED_DRIPSTONE_STAIRS = HELPER.createBlock("polished_dripstone_stairs", () -> new StairBlock(() -> POLISHED_DRIPSTONE.get().defaultBlockState(), CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> POLISHED_DRIPSTONE_SLAB = HELPER.createBlock("polished_dripstone_slab", () -> new SlabBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> POLISHED_DRIPSTONE_WALL = HELPER.createBlock("polished_dripstone_wall", () -> new WallBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_BRICKS = HELPER.createBlock("dripstone_bricks", () -> new Block(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_BRICK_STAIRS = HELPER.createBlock("dripstone_brick_stairs", () -> new StairBlock(() -> DRIPSTONE_BRICKS.get().defaultBlockState(), CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_BRICK_SLAB = HELPER.createBlock("dripstone_brick_slab", () -> new SlabBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_BRICK_WALL = HELPER.createBlock("dripstone_brick_wall", () -> new WallBlock(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> CHISELED_DRIPSTONE_BRICKS = HELPER.createBlock("chiseled_dripstone_bricks", () -> new Block(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> CRACKED_DRIPSTONE_BRICKS = HELPER.createBlock("cracked_dripstone_bricks", () -> new Block(CCProperties.DRIPSTONE));
	public static final RegistryObject<Block> DRIPSTONE_SHINGLES = HELPER.createBlock("dripstone_shingles", () -> new Block(CCProperties.DRIPSTONE_SHINGLES));
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_STAIRS = HELPER.createBlock("dripstone_shingle_stairs", () -> new StairBlock(() -> DRIPSTONE_SHINGLES.get().defaultBlockState(), CCProperties.DRIPSTONE_SHINGLES));
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_SLAB = HELPER.createBlock("dripstone_shingle_slab", () -> new SlabBlock(CCProperties.DRIPSTONE_SHINGLES));
	public static final RegistryObject<Block> DRIPSTONE_SHINGLE_WALL = HELPER.createBlock("dripstone_shingle_wall", () -> new WallBlock(CCProperties.DRIPSTONE_SHINGLES));
	public static final RegistryObject<Block> CHISELED_DRIPSTONE_SHINGLES = HELPER.createBlock("chiseled_dripstone_shingles", () -> new DripstoneShingleBlock(CCProperties.DRIPSTONE_SHINGLES));
	public static final RegistryObject<Block> FLOODED_DRIPSTONE_SHINGLES = HELPER.createBlock("flooded_dripstone_shingles", () -> new DripstoneShingleBlock(CCProperties.DRIPSTONE_SHINGLES));

	public static final RegistryObject<Block> AMETHYST_BLOCK = HELPER.createBlock("amethyst_block", () -> new AmethystBlock(CCProperties.AMETHYST));
	public static final RegistryObject<Block> CUT_AMETHYST = HELPER.createBlock("cut_amethyst", () -> new AmethystBlock(CCProperties.AMETHYST));
	public static final RegistryObject<Block> CUT_AMETHYST_BRICKS = HELPER.createBlock("cut_amethyst_bricks", () -> new AmethystBlock(CCProperties.AMETHYST));
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_STAIRS = HELPER.createBlock("cut_amethyst_brick_stairs", () -> new AmethystStairBlock(() -> CUT_AMETHYST_BRICKS.get().defaultBlockState(), CCProperties.AMETHYST));
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_SLAB = HELPER.createBlock("cut_amethyst_brick_slab", () -> new AmethystSlabBlock(CCProperties.AMETHYST));
	public static final RegistryObject<Block> CUT_AMETHYST_BRICK_WALL = HELPER.createBlock("cut_amethyst_brick_wall", () -> new AmethystWallBlock(CCProperties.AMETHYST));
	public static final RegistryObject<Block> AMETHYST_LAMP = HELPER.createBlock("amethyst_lamp", () -> new AmethystBlock(Properties.copy(LAPIS_LAZULI_LAMP.get()).sound(SoundType.AMETHYST)));

	public static final RegistryObject<Block> ECHO_BLOCK = HELPER.createBlock("echo_block", () -> new Block(CCProperties.ECHO_BLOCK));

	public static final RegistryObject<Block> IRON_BRICKS = HELPER.createBlock("iron_bricks", () -> new Block(CCProperties.IRON_PLATED_BRICKS));
	public static final RegistryObject<Block> IRON_BRICK_STAIRS = HELPER.createBlock("iron_brick_stairs", () -> new StairBlock(() -> IRON_BRICKS.get().defaultBlockState(), CCProperties.IRON_PLATED_BRICKS));
	public static final RegistryObject<Block> IRON_BRICK_SLAB = HELPER.createBlock("iron_brick_slab", () -> new SlabBlock(CCProperties.IRON_PLATED_BRICKS));
	public static final RegistryObject<Block> IRON_BRICK_WALL = HELPER.createBlock("iron_brick_wall", () -> new WallBlock(CCProperties.IRON_PLATED_BRICKS));
	public static final RegistryObject<Block> CHISELED_IRON_BRICKS = HELPER.createBlock("chiseled_iron_bricks", () -> new Block(CCProperties.IRON_PLATED_BRICKS));

	public static final RegistryObject<Block> TIN_BRICKS = HELPER.createBlock("tin_bricks", () -> new Block(CCProperties.TIN_PLATED_BRICKS));
	public static final RegistryObject<Block> TIN_BRICK_STAIRS = HELPER.createBlock("tin_brick_stairs", () -> new StairBlock(() -> TIN_BRICKS.get().defaultBlockState(), CCProperties.TIN_PLATED_BRICKS));
	public static final RegistryObject<Block> TIN_BRICK_SLAB = HELPER.createBlock("tin_brick_slab", () -> new SlabBlock(CCProperties.TIN_PLATED_BRICKS));
	public static final RegistryObject<Block> TIN_BRICK_WALL = HELPER.createBlock("tin_brick_wall", () -> new WallBlock(CCProperties.TIN_PLATED_BRICKS));
	public static final RegistryObject<Block> CHISELED_TIN_BRICKS = HELPER.createBlock("chiseled_tin_bricks", () -> new Block(CCProperties.TIN_PLATED_BRICKS));

	public static final RegistryObject<Block> GOLD_BRICKS = HELPER.createBlock("gold_bricks", () -> new Block(CCProperties.GOLD_PLATED_BRICKS));
	public static final RegistryObject<Block> GOLD_BRICK_STAIRS = HELPER.createBlock("gold_brick_stairs", () -> new StairBlock(() -> GOLD_BRICKS.get().defaultBlockState(), CCProperties.GOLD_PLATED_BRICKS));
	public static final RegistryObject<Block> GOLD_BRICK_SLAB = HELPER.createBlock("gold_brick_slab", () -> new SlabBlock(CCProperties.GOLD_PLATED_BRICKS));
	public static final RegistryObject<Block> GOLD_BRICK_WALL = HELPER.createBlock("gold_brick_wall", () -> new WallBlock(CCProperties.GOLD_PLATED_BRICKS));
	public static final RegistryObject<Block> CHISELED_GOLD_BRICKS = HELPER.createBlock("chiseled_gold_bricks", () -> new Block(CCProperties.GOLD_PLATED_BRICKS));

	public static final RegistryObject<Block> SILVER_BRICKS = HELPER.createBlock("silver_bricks", () -> new Block(CCProperties.SILVER_PLATED_BRICKS));
	public static final RegistryObject<Block> SILVER_BRICK_STAIRS = HELPER.createBlock("silver_brick_stairs", () -> new StairBlock(() -> SILVER_BRICKS.get().defaultBlockState(), CCProperties.SILVER_PLATED_BRICKS));
	public static final RegistryObject<Block> SILVER_BRICK_SLAB = HELPER.createBlock("silver_brick_slab", () -> new SlabBlock(CCProperties.SILVER_PLATED_BRICKS));
	public static final RegistryObject<Block> SILVER_BRICK_WALL = HELPER.createBlock("silver_brick_wall", () -> new WallBlock(CCProperties.SILVER_PLATED_BRICKS));
	public static final RegistryObject<Block> CHISELED_SILVER_BRICKS = HELPER.createBlock("chiseled_silver_bricks", () -> new Block(CCProperties.SILVER_PLATED_BRICKS));

	public static final RegistryObject<Block> COPPER_BRICKS = HELPER.createBlock("copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> COPPER_BRICK_STAIRS = HELPER.createBlock("copper_brick_stairs", () -> new CCWeatheringCopperStairBlock(WeatherState.UNAFFECTED, () -> COPPER_BRICKS.get().defaultBlockState(), CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> COPPER_BRICK_SLAB = HELPER.createBlock("copper_brick_slab", () -> new CCWeatheringCopperSlabBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> COPPER_BRICK_WALL = HELPER.createBlock("copper_brick_wall", () -> new CCWeatheringCopperWallBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> CHISELED_COPPER_BRICKS = HELPER.createBlock("chiseled_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.UNAFFECTED, CCProperties.COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> EXPOSED_COPPER_BRICKS = HELPER.createBlock("exposed_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> EXPOSED_COPPER_BRICK_STAIRS = HELPER.createBlock("exposed_copper_brick_stairs", () -> new CCWeatheringCopperStairBlock(WeatherState.EXPOSED, () -> EXPOSED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> EXPOSED_COPPER_BRICK_SLAB = HELPER.createBlock("exposed_copper_brick_slab", () -> new CCWeatheringCopperSlabBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> EXPOSED_COPPER_BRICK_WALL = HELPER.createBlock("exposed_copper_brick_wall", () -> new CCWeatheringCopperWallBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> EXPOSED_CHISELED_COPPER_BRICKS = HELPER.createBlock("exposed_chiseled_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> WEATHERED_COPPER_BRICKS = HELPER.createBlock("weathered_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WEATHERED_COPPER_BRICK_STAIRS = HELPER.createBlock("weathered_copper_brick_stairs", () -> new CCWeatheringCopperStairBlock(WeatherState.WEATHERED, () -> WEATHERED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WEATHERED_COPPER_BRICK_SLAB = HELPER.createBlock("weathered_copper_brick_slab", () -> new CCWeatheringCopperSlabBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WEATHERED_COPPER_BRICK_WALL = HELPER.createBlock("weathered_copper_brick_wall", () -> new CCWeatheringCopperWallBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WEATHERED_CHISELED_COPPER_BRICKS = HELPER.createBlock("weathered_chiseled_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> OXIDIZED_COPPER_BRICKS = HELPER.createBlock("oxidized_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BRICK_STAIRS = HELPER.createBlock("oxidized_copper_brick_stairs", () -> new CCWeatheringCopperStairBlock(WeatherState.OXIDIZED, () -> OXIDIZED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BRICK_SLAB = HELPER.createBlock("oxidized_copper_brick_slab", () -> new CCWeatheringCopperSlabBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BRICK_WALL = HELPER.createBlock("oxidized_copper_brick_wall", () -> new CCWeatheringCopperWallBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> OXIDIZED_CHISELED_COPPER_BRICKS = HELPER.createBlock("oxidized_chiseled_copper_bricks", () -> new CCWeatheringCopperFullBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> WAXED_COPPER_BRICKS = HELPER.createBlock("waxed_copper_bricks", () -> new Block(CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_COPPER_BRICK_STAIRS = HELPER.createBlock("waxed_copper_brick_stairs", () -> new StairBlock(() -> WAXED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_COPPER_BRICK_SLAB = HELPER.createBlock("waxed_copper_brick_slab", () -> new SlabBlock(CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_COPPER_BRICK_WALL = HELPER.createBlock("waxed_copper_brick_wall", () -> new WallBlock(CCProperties.COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_CHISELED_COPPER_BRICKS = HELPER.createBlock("waxed_chiseled_copper_bricks", () -> new Block(CCProperties.COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BRICKS = HELPER.createBlock("waxed_exposed_copper_bricks", () -> new Block(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BRICK_STAIRS = HELPER.createBlock("waxed_exposed_copper_brick_stairs", () -> new StairBlock(() -> WAXED_EXPOSED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BRICK_SLAB = HELPER.createBlock("waxed_exposed_copper_brick_slab", () -> new SlabBlock(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BRICK_WALL = HELPER.createBlock("waxed_exposed_copper_brick_wall", () -> new WallBlock(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_EXPOSED_CHISELED_COPPER_BRICKS = HELPER.createBlock("waxed_exposed_chiseled_copper_bricks", () -> new Block(CCProperties.EXPOSED_COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BRICKS = HELPER.createBlock("waxed_weathered_copper_bricks", () -> new Block(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BRICK_STAIRS = HELPER.createBlock("waxed_weathered_copper_brick_stairs", () -> new StairBlock(() -> WAXED_WEATHERED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BRICK_SLAB = HELPER.createBlock("waxed_weathered_copper_brick_slab", () -> new SlabBlock(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BRICK_WALL = HELPER.createBlock("waxed_weathered_copper_brick_wall", () -> new WallBlock(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_WEATHERED_CHISELED_COPPER_BRICKS = HELPER.createBlock("waxed_weathered_chiseled_copper_bricks", () -> new Block(CCProperties.WEATHERED_COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BRICKS = HELPER.createBlock("waxed_oxidized_copper_bricks", () -> new Block(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BRICK_STAIRS = HELPER.createBlock("waxed_oxidized_copper_brick_stairs", () -> new StairBlock(() -> WAXED_OXIDIZED_COPPER_BRICKS.get().defaultBlockState(), CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BRICK_SLAB = HELPER.createBlock("waxed_oxidized_copper_brick_slab", () -> new SlabBlock(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BRICK_WALL = HELPER.createBlock("waxed_oxidized_copper_brick_wall", () -> new WallBlock(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));
	public static final RegistryObject<Block> WAXED_OXIDIZED_CHISELED_COPPER_BRICKS = HELPER.createBlock("waxed_oxidized_chiseled_copper_bricks", () -> new Block(CCProperties.OXIDIZED_COPPER_PLATED_BRICKS));

	public static final RegistryObject<Block> STRIPPED_AZALEA_LOG = HELPER.createBlock("stripped_azalea_log", () -> new RotatedPillarBlock(CCProperties.AZALEA.log()));
	public static final RegistryObject<Block> STRIPPED_AZALEA_WOOD = HELPER.createBlock("stripped_azalea_wood", () -> new RotatedPillarBlock(CCProperties.AZALEA.log()));
	public static final RegistryObject<Block> AZALEA_LOG = HELPER.createBlock("azalea_log", () -> new LogBlock(STRIPPED_AZALEA_LOG, CCProperties.AZALEA.log()));
	public static final RegistryObject<Block> AZALEA_WOOD = HELPER.createBlock("azalea_wood", () -> new LogBlock(STRIPPED_AZALEA_WOOD, CCProperties.AZALEA.log()));
	public static final RegistryObject<Block> AZALEA_PLANKS = HELPER.createBlock("azalea_planks", () -> new Block(CCProperties.AZALEA.planks()));
	public static final RegistryObject<Block> AZALEA_DOOR = HELPER.createBlock("azalea_door", () -> new DoorBlock(CCProperties.AZALEA.planks(), CCProperties.AZALEA_BLOCK_SET));
	public static final RegistryObject<Block> AZALEA_SLAB = HELPER.createBlock("azalea_slab", () -> new SlabBlock(CCProperties.AZALEA.planks()));
	public static final RegistryObject<Block> AZALEA_STAIRS = HELPER.createBlock("azalea_stairs", () -> new StairBlock(() -> AZALEA_PLANKS.get().defaultBlockState(), CCProperties.AZALEA.planks()));
	public static final RegistryObject<Block> AZALEA_FENCE = HELPER.createFuelBlock("azalea_fence", () -> new FenceBlock(CCProperties.AZALEA.planks()), 300);
	public static final RegistryObject<Block> AZALEA_FENCE_GATE = HELPER.createFuelBlock("azalea_fence_gate", () -> new FenceGateBlock(CCProperties.AZALEA.planks(), CCProperties.AZALEA_WOOD_TYPE), 300);
	public static final RegistryObject<Block> AZALEA_PRESSURE_PLATE = HELPER.createBlock("azalea_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, CCProperties.AZALEA.pressurePlate(), CCProperties.AZALEA_BLOCK_SET));
	public static final RegistryObject<Block> AZALEA_BUTTON = HELPER.createBlock("azalea_button", () -> new ButtonBlock(CCProperties.AZALEA.button(), CCProperties.AZALEA_BLOCK_SET, 30, true));
	public static final RegistryObject<Block> AZALEA_TRAPDOOR = HELPER.createBlock("azalea_trapdoor", () -> new TrapDoorBlock(CCProperties.AZALEA.trapdoor(), CCProperties.AZALEA_BLOCK_SET));
	public static final Pair<RegistryObject<BlueprintStandingSignBlock>, RegistryObject<BlueprintWallSignBlock>> AZALEA_SIGNS = HELPER.createSignBlock("azalea", CCProperties.AZALEA_WOOD_TYPE, CCProperties.AZALEA.sign());
	public static final Pair<RegistryObject<BlueprintCeilingHangingSignBlock>, RegistryObject<BlueprintWallHangingSignBlock>> AZALEA_HANGING_SIGNS = HELPER.createHangingSignBlock("azalea", CCProperties.AZALEA_WOOD_TYPE, CCProperties.AZALEA.hangingSign());

	public static final RegistryObject<Block> AZALEA_BOARDS = HELPER.createFuelBlock("azalea_boards", () -> new RotatedPillarBlock(CCProperties.AZALEA.planks()), 300);
	public static final RegistryObject<Block> AZALEA_BOOKSHELF = HELPER.createFuelBlock("azalea_bookshelf", () -> new Block(CCProperties.AZALEA.bookshelf()), 300);
	public static final RegistryObject<Block> CHISELED_AZALEA_BOOKSHELF = HELPER.createFuelBlock("chiseled_azalea_bookshelf", () -> new ChiseledAzaleaBookShelfBlock(CCProperties.AZALEA.chiseledBookshelf()), 300);
	public static final RegistryObject<Block> AZALEA_LADDER = HELPER.createFuelBlock("azalea_ladder", () -> new LadderBlock(CCProperties.AZALEA.ladder()), 300);
	public static final RegistryObject<Block> AZALEA_BEEHIVE = HELPER.createBlock("azalea_beehive", () -> new BlueprintBeehiveBlock(CCProperties.AZALEA.beehive()));
	public static final RegistryObject<BlueprintChestBlock> AZALEA_CHEST = HELPER.createChestBlock("azalea", CCProperties.AZALEA.chest());
	public static final RegistryObject<BlueprintTrappedChestBlock> TRAPPED_AZALEA_CHEST = HELPER.createTrappedChestBlockNamed("azalea", CCProperties.AZALEA.chest());

	public static final RegistryObject<Block> FALSE_HOPE = HELPER.createBlock("false_hope", () -> new FalseHopeBlock(() -> MobEffects.BLINDNESS, 8, CCProperties.FALSE_HOPE));

	public static final RegistryObject<Block> MOSCHATEL = HELPER.createBlock("moschatel", () -> new MoschatelBlock(() -> MobEffects.NIGHT_VISION, 5, PropertyUtil.flower().sound(CCSoundTypes.MOSCHATEL)));
	public static final RegistryObject<Block> CAVE_GROWTHS = HELPER.createBlock("cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_LIGHT_GREEN)));
	public static final RegistryObject<Block> LURID_CAVE_GROWTHS = HELPER.createBlock("lurid_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.GLOW_LICHEN)));
	public static final RegistryObject<Block> WISPY_CAVE_GROWTHS = HELPER.createBlock("wispy_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.STONE)));
	public static final RegistryObject<Block> GRAINY_CAVE_GROWTHS = HELPER.createBlock("grainy_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_PINK)));
	public static final RegistryObject<Block> WEIRD_CAVE_GROWTHS = HELPER.createBlock("weird_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_MAGENTA)));
	public static final RegistryObject<Block> ZESTY_CAVE_GROWTHS = HELPER.createBlock("zesty_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.RAW_IRON)));

	public static final RegistryObject<Block> POTTED_FALSE_HOPE = HELPER.createBlockNoItem("potted_false_hope", () -> new FlowerPotBlock(FALSE_HOPE.get(), PropertyUtil.flowerPot().lightLevel((state) -> 15)));

	public static final RegistryObject<Block> POTTED_MOSCHATEL = HELPER.createBlockNoItem("potted_moschatel", () -> new FlowerPotBlock(MOSCHATEL.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_cave_growths", () -> new FlowerPotBlock(CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_LURID_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_lurid_cave_growths", () -> new FlowerPotBlock(LURID_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_WISPY_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_wispy_cave_growths", () -> new FlowerPotBlock(WISPY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_GRAINY_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_grainy_cave_growths", () -> new FlowerPotBlock(GRAINY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_WEIRD_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_weird_cave_growths", () -> new FlowerPotBlock(WEIRD_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_ZESTY_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_zesty_cave_growths", () -> new FlowerPotBlock(ZESTY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));

	public static final RegistryObject<Block> FLINT_BLOCK = HELPER.createBlock("flint_block", () -> new FlintBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).sound(CCSoundTypes.FLINT_BLOCK)));

	public static final RegistryObject<Block> COAL = HELPER.createPlacedItem("coal", () -> new CoalBlock(CCProperties.placedCoal(6)));
	public static final RegistryObject<Block> CHARCOAL = HELPER.createPlacedItem("charcoal", () -> new CoalBlock(CCProperties.placedCoal(4)));
	public static final RegistryObject<Block> CHARCOAL_BLOCK = HELPER.createFuelBlock("charcoal_block", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)), 12800);

	public static final RegistryObject<Block> COPPER_INGOT = HELPER.createPlacedItem("copper_ingot", () -> new CCWeatheringIngotBlock(WeatherState.UNAFFECTED, () -> Items.COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
	public static final RegistryObject<Block> EXPOSED_COPPER_INGOT = HELPER.createPlacedItem("exposed_copper_ingot", () -> new CCWeatheringIngotBlock(WeatherState.EXPOSED, CCItems.EXPOSED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)));
	public static final RegistryObject<Block> WEATHERED_COPPER_INGOT = HELPER.createPlacedItem("weathered_copper_ingot", () -> new CCWeatheringIngotBlock(WeatherState.WEATHERED, CCItems.WEATHERED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)));
	public static final RegistryObject<Block> OXIDIZED_COPPER_INGOT = HELPER.createPlacedItem("oxidized_copper_ingot", () -> new CCWeatheringIngotBlock(WeatherState.OXIDIZED, CCItems.OXIDIZED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)));
	public static final RegistryObject<Block> WAXED_COPPER_INGOT = HELPER.createPlacedItem("waxed_copper_ingot", () -> new IngotBlock(CCItems.WAXED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.WAXED_COPPER_BLOCK)));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_INGOT = HELPER.createPlacedItem("waxed_exposed_copper_ingot", () -> new IngotBlock(CCItems.WAXED_EXPOSED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.WAXED_EXPOSED_COPPER)));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_INGOT = HELPER.createPlacedItem("waxed_weathered_copper_ingot", () -> new IngotBlock(CCItems.WAXED_WEATHERED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.WAXED_WEATHERED_COPPER)));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_INGOT = HELPER.createPlacedItem("waxed_oxidized_copper_ingot", () -> new IngotBlock(CCItems.WAXED_OXIDIZED_COPPER_INGOT, BlockBehaviour.Properties.copy(Blocks.WAXED_OXIDIZED_COPPER)));

	public static final RegistryObject<Block> IRON_INGOT = HELPER.createPlacedItem("iron_ingot", () -> new IngotBlock(() -> Items.IRON_INGOT, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> GOLD_INGOT = HELPER.createPlacedItem("gold_ingot", () -> new IngotBlock(() -> Items.GOLD_INGOT, BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
	public static final RegistryObject<Block> NETHERITE_INGOT = HELPER.createPlacedItem("netherite_ingot", () -> new IngotBlock(() -> Items.NETHERITE_INGOT, BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)));
	public static final RegistryObject<Block> SILVER_INGOT = HELPER.createPlacedItem("silver_ingot", () -> new IngotBlock(CCItems.SILVER_INGOT, BlockBehaviour.Properties.copy(SILVER_BLOCK.get())));
	public static final RegistryObject<Block> TIN_INGOT = HELPER.createPlacedItem("tin_ingot", () -> new IngotBlock(CCItems.TIN_INGOT, BlockBehaviour.Properties.copy(TIN_BLOCK.get())));
	public static final RegistryObject<Block> NECROMIUM_INGOT = HELPER.createPlacedItem("necromium_ingot", () -> new IngotBlock(CCItems.NECROMIUM_INGOT, BlockBehaviour.Properties.copy(NECROMIUM_BLOCK.get())));

	public static final RegistryObject<Block> BRICK = HELPER.createPlacedItem("brick", () -> new IngotBlock(() -> Items.BRICK, BlockBehaviour.Properties.copy(Blocks.BRICKS)));
	public static final RegistryObject<Block> NETHER_BRICK = HELPER.createPlacedItem("nether_brick", () -> new IngotBlock(() -> Items.NETHER_BRICK, BlockBehaviour.Properties.copy(Blocks.NETHER_BRICKS)));
	public static final RegistryObject<Block> EUMUS_BRICK = HELPER.createPlacedItem("eumus_brick", () -> new IngotBlock(() -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("endergetic", "eumus_brick")), Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.STONE).strength(2, 30)));

	public static final RegistryObject<Block> SADDLED_EGG = HELPER.createBlock("saddled_egg", () -> new SaddledEggBlock(CCProperties.SADDLED_EGG));

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
				.addItemsAfter(of(Blocks.POLISHED_GRANITE_SLAB), POLISHED_GRANITE_WALL)
				.addItemsAfter(of(Blocks.POLISHED_DIORITE_SLAB), POLISHED_DIORITE_WALL)
				.addItemsAfter(of(Blocks.POLISHED_ANDESITE_SLAB), POLISHED_ANDESITE_WALL)
				.addItemsBefore(of(Blocks.DEEPSLATE),
						() -> Blocks.CALCITE, CALCITE_STAIRS, CALCITE_SLAB, CALCITE_WALL, SMOOTH_CALCITE, SMOOTH_CALCITE_STAIRS, SMOOTH_CALCITE_SLAB,
						POLISHED_CALCITE, POLISHED_CALCITE_STAIRS, POLISHED_CALCITE_SLAB, POLISHED_CALCITE_WALL, CHISELED_CALCITE, CALCITE_PILLAR, CALCITE_BRICKS, CALCITE_BRICK_STAIRS, CALCITE_BRICK_SLAB, CALCITE_BRICK_WALL, CHISELED_CALCITE_BRICKS,
						() -> Blocks.TUFF, TUFF_STAIRS, TUFF_SLAB, TUFF_WALL, SMOOTH_TUFF, SMOOTH_TUFF_STAIRS, SMOOTH_TUFF_SLAB,
						POLISHED_TUFF, POLISHED_TUFF_STAIRS, POLISHED_TUFF_SLAB, POLISHED_TUFF_WALL, CHISELED_TUFF, TUFF_BRICKS, TUFF_BRICK_STAIRS, TUFF_BRICK_SLAB, TUFF_BRICK_WALL, CHISELED_TUFF_BRICKS,
						() -> Blocks.DRIPSTONE_BLOCK, DRIPSTONE_STAIRS, DRIPSTONE_SLAB, DRIPSTONE_WALL,
						SMOOTH_DRIPSTONE, SMOOTH_DRIPSTONE_STAIRS, SMOOTH_DRIPSTONE_SLAB,
						POLISHED_DRIPSTONE, POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE_WALL,
						DRIPSTONE_BRICKS, CRACKED_DRIPSTONE_BRICKS, DRIPSTONE_BRICK_STAIRS, DRIPSTONE_BRICK_SLAB, DRIPSTONE_BRICK_WALL, CHISELED_DRIPSTONE_BRICKS,
						DRIPSTONE_SHINGLES, FLOODED_DRIPSTONE_SHINGLES, DRIPSTONE_SHINGLE_STAIRS, DRIPSTONE_SHINGLE_SLAB, DRIPSTONE_SHINGLE_WALL, CHISELED_DRIPSTONE_SHINGLES,
						SUGILITE, SUGILITE_STAIRS, SUGILITE_SLAB, SUGILITE_WALL, POLISHED_SUGILITE, POLISHED_SUGILITE_STAIRS, POLISHED_SUGILITE_SLAB, POLISHED_SUGILITE_WALL,
						CASSITERITE, CASSITERITE_STAIRS, CASSITERITE_SLAB, CASSITERITE_WALL,
						SMOOTH_CASSITERITE, SMOOTH_CASSITERITE_STAIRS, SMOOTH_CASSITERITE_SLAB,
						POLISHED_CASSITERITE, POLISHED_CASSITERITE_STAIRS, POLISHED_CASSITERITE_SLAB, POLISHED_CASSITERITE_WALL,
						CASSITERITE_BRICKS, CASSITERITE_BRICK_STAIRS, CASSITERITE_BRICK_SLAB, CASSITERITE_BRICK_WALL, CHISELED_CASSITERITE_BRICKS, CASSITERITE_PILLAR,
						RHYOLITE, RHYOLITE_STAIRS, RHYOLITE_SLAB, RHYOLITE_WALL,
						POLISHED_RHYOLITE, POLISHED_RHYOLITE_STAIRS, POLISHED_RHYOLITE_SLAB, POLISHED_RHYOLITE_WALL,
						RHYOLITE_BRICKS, RHYOLITE_BRICK_STAIRS, RHYOLITE_BRICK_SLAB, RHYOLITE_BRICK_WALL, CHISELED_RHYOLITE_BRICKS,
						MAGMATIC_RHYOLITE, MAGMATIC_RHYOLITE_STAIRS, MAGMATIC_RHYOLITE_SLAB, MAGMATIC_RHYOLITE_WALL,
						POLISHED_MAGMATIC_RHYOLITE, POLISHED_MAGMATIC_RHYOLITE_STAIRS, POLISHED_MAGMATIC_RHYOLITE_SLAB, POLISHED_MAGMATIC_RHYOLITE_WALL,
						MAGMATIC_RHYOLITE_BRICKS, MAGMATIC_RHYOLITE_BRICK_STAIRS, MAGMATIC_RHYOLITE_BRICK_SLAB, MAGMATIC_RHYOLITE_BRICK_WALL, CHISELED_MAGMATIC_RHYOLITE_BRICKS
				)
				.addItemsBefore(of(Blocks.CHISELED_DEEPSLATE),
						COBBLED_DEEPSLATE_BRICKS, COBBLED_DEEPSLATE_BRICK_STAIRS, COBBLED_DEEPSLATE_BRICK_SLAB, COBBLED_DEEPSLATE_BRICK_WALL,
						COBBLED_DEEPSLATE_TILES, COBBLED_DEEPSLATE_TILE_STAIRS, COBBLED_DEEPSLATE_TILE_SLAB, COBBLED_DEEPSLATE_TILE_WALL
				)
				.editor(event -> event.getEntries().remove(new ItemStack(Blocks.CHISELED_DEEPSLATE)))
				.addItemsBefore(of(Blocks.DEEPSLATE_TILES), () -> Blocks.CHISELED_DEEPSLATE)
				.addItemsBefore(of(Blocks.BASALT), SANGUINE_BLOCK, SANGUINE_TILES, SANGUINE_TILE_STAIRS, SANGUINE_TILE_SLAB, SANGUINE_TILE_WALL, FORTIFIED_SANGUINE_TILES, FORTIFIED_SANGUINE_TILE_STAIRS, FORTIFIED_SANGUINE_TILE_SLAB, FORTIFIED_SANGUINE_TILE_WALL)
				.addItemsAfter(of(Blocks.AMETHYST_BLOCK), AMETHYST_BLOCK, CUT_AMETHYST, CUT_AMETHYST_BRICKS, CUT_AMETHYST_BRICK_STAIRS, CUT_AMETHYST_BRICK_SLAB, CUT_AMETHYST_BRICK_WALL, AMETHYST_LAMP)
				.addItemsAfter(of(Blocks.COAL_BLOCK), CHARCOAL_BLOCK)
				.addItemsAfter(of(Blocks.IRON_BLOCK), IRON_BRICKS, IRON_BRICK_STAIRS, IRON_BRICK_SLAB, IRON_BRICK_WALL, CHISELED_IRON_BRICKS)
				.addItemsAfter(of(Blocks.GOLD_BLOCK), GOLD_BRICKS, GOLD_BRICK_STAIRS, GOLD_BRICK_SLAB, GOLD_BRICK_WALL, CHISELED_GOLD_BRICKS, GOLDEN_BARS)
				.addItemsBefore(of(Blocks.GOLD_BLOCK), TIN_BLOCK, TIN_BRICKS, TIN_BRICK_STAIRS, TIN_BRICK_SLAB, TIN_BRICK_WALL, CHISELED_TIN_BRICKS, TIN_BARS, ROLLER_DOOR, HOLD_PLATE, HOLD_BUTTON)
				.addItemsBefore(of(Blocks.REDSTONE_BLOCK), SILVER_BLOCK, SILVER_BRICKS, SILVER_BRICK_STAIRS, SILVER_BRICK_SLAB, SILVER_BRICK_WALL, CHISELED_SILVER_BRICKS, SILVER_BARS, MEDIUM_WEIGHTED_PRESSURE_PLATE)
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
				.addItemsAfter(of(Blocks.COPPER_BLOCK), CHISELED_COPPER, COPPER_GRATE)
				.addItemsAfter(of(Blocks.CUT_COPPER_SLAB), CHISELED_COPPER_BRICKS, COPPER_BRICK_STAIRS, COPPER_BRICK_SLAB, COPPER_BRICK_WALL, CHISELED_COPPER_BRICKS, COPPER_DOOR, COPPER_TRAPDOOR, COPPER_BUTTON, () -> Blocks.LIGHTNING_ROD, COPPER_BARS, COPPER_CHAIN, COPPER_BULB)
				.addItemsAfter(of(Blocks.EXPOSED_COPPER), EXPOSED_CHISELED_COPPER, EXPOSED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.EXPOSED_CUT_COPPER_SLAB), EXPOSED_COPPER_BRICKS, EXPOSED_COPPER_BRICK_STAIRS, EXPOSED_COPPER_BRICK_SLAB, EXPOSED_COPPER_BRICK_WALL, EXPOSED_CHISELED_COPPER_BRICKS, EXPOSED_COPPER_DOOR, EXPOSED_COPPER_TRAPDOOR, EXPOSED_COPPER_BUTTON, EXPOSED_LIGHTNING_ROD, EXPOSED_COPPER_BARS, EXPOSED_COPPER_CHAIN, EXPOSED_COPPER_BULB)
				.addItemsAfter(of(Blocks.WEATHERED_COPPER), WEATHERED_CHISELED_COPPER, WEATHERED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.WEATHERED_CUT_COPPER_SLAB), WEATHERED_COPPER_BRICKS, WEATHERED_COPPER_BRICK_STAIRS, WEATHERED_COPPER_BRICK_SLAB, WEATHERED_COPPER_BRICK_WALL, WEATHERED_CHISELED_COPPER_BRICKS, WEATHERED_COPPER_DOOR, WEATHERED_COPPER_TRAPDOOR, WEATHERED_COPPER_BUTTON, WEATHERED_LIGHTNING_ROD, WEATHERED_COPPER_BARS, WEATHERED_COPPER_CHAIN, WEATHERED_COPPER_BULB)
				.addItemsAfter(of(Blocks.OXIDIZED_COPPER), OXIDIZED_CHISELED_COPPER, OXIDIZED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.OXIDIZED_CUT_COPPER_SLAB), OXIDIZED_COPPER_BRICKS, OXIDIZED_COPPER_BRICK_STAIRS, OXIDIZED_COPPER_BRICK_SLAB, OXIDIZED_COPPER_BRICK_WALL, OXIDIZED_CHISELED_COPPER_BRICKS, OXIDIZED_COPPER_DOOR, OXIDIZED_COPPER_TRAPDOOR, OXIDIZED_COPPER_BUTTON, OXIDIZED_LIGHTNING_ROD, OXIDIZED_COPPER_BARS, OXIDIZED_COPPER_CHAIN, OXIDIZED_COPPER_BULB)
				.addItemsAfter(of(Blocks.WAXED_COPPER_BLOCK), WAXED_CHISELED_COPPER, WAXED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.WAXED_CUT_COPPER_SLAB), WAXED_COPPER_BRICKS, WAXED_COPPER_BRICK_STAIRS, WAXED_COPPER_BRICK_SLAB, WAXED_COPPER_BRICK_WALL, WAXED_CHISELED_COPPER_BRICKS, WAXED_COPPER_DOOR, WAXED_COPPER_TRAPDOOR, WAXED_COPPER_BUTTON, WAXED_LIGHTNING_ROD, WAXED_COPPER_BARS, WAXED_COPPER_CHAIN, WAXED_COPPER_BULB)
				.addItemsAfter(of(Blocks.WAXED_EXPOSED_COPPER), WAXED_EXPOSED_CHISELED_COPPER, WAXED_EXPOSED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB), WAXED_EXPOSED_COPPER_BRICKS, WAXED_EXPOSED_COPPER_BRICK_STAIRS, WAXED_EXPOSED_COPPER_BRICK_SLAB, WAXED_EXPOSED_COPPER_BRICK_WALL, WAXED_EXPOSED_CHISELED_COPPER_BRICKS, WAXED_EXPOSED_COPPER_DOOR, WAXED_EXPOSED_COPPER_TRAPDOOR, WAXED_EXPOSED_COPPER_BUTTON, WAXED_EXPOSED_LIGHTNING_ROD, WAXED_EXPOSED_COPPER_BARS, WAXED_EXPOSED_COPPER_CHAIN, WAXED_EXPOSED_COPPER_BULB)
				.addItemsAfter(of(Blocks.WAXED_WEATHERED_COPPER), WAXED_WEATHERED_CHISELED_COPPER, WAXED_WEATHERED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB), WAXED_WEATHERED_COPPER_BRICKS, WAXED_WEATHERED_COPPER_BRICK_STAIRS, WAXED_WEATHERED_COPPER_BRICK_SLAB, WAXED_WEATHERED_COPPER_BRICK_WALL, WAXED_WEATHERED_CHISELED_COPPER_BRICKS, WAXED_WEATHERED_COPPER_DOOR, WAXED_WEATHERED_COPPER_TRAPDOOR, WAXED_WEATHERED_COPPER_BUTTON, WAXED_WEATHERED_LIGHTNING_ROD, WAXED_WEATHERED_COPPER_BARS, WAXED_WEATHERED_COPPER_CHAIN, WAXED_WEATHERED_COPPER_BULB)
				.addItemsAfter(of(Blocks.WAXED_OXIDIZED_COPPER), WAXED_OXIDIZED_CHISELED_COPPER, WAXED_OXIDIZED_COPPER_GRATE)
				.addItemsAfter(of(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB), WAXED_OXIDIZED_COPPER_BRICKS, WAXED_OXIDIZED_COPPER_BRICK_STAIRS, WAXED_OXIDIZED_COPPER_BRICK_SLAB, WAXED_OXIDIZED_COPPER_BRICK_WALL, WAXED_OXIDIZED_CHISELED_COPPER_BRICKS, WAXED_OXIDIZED_COPPER_DOOR, WAXED_OXIDIZED_COPPER_TRAPDOOR, WAXED_OXIDIZED_COPPER_BUTTON, WAXED_OXIDIZED_LIGHTNING_ROD, WAXED_OXIDIZED_COPPER_BARS, WAXED_OXIDIZED_COPPER_CHAIN, WAXED_OXIDIZED_COPPER_BULB)
				.tab(COLORED_BLOCKS)
				.addItemsAfter(of(Blocks.TINTED_GLASS), FLOAT_GLASS, ORNATE_GLASS)
				.addItemsAfter(of(Blocks.GLASS_PANE), FLOAT_GLASS_PANE, ORNATE_GLASS_PANE)
				.tab(NATURAL_BLOCKS)
				.addItemsAfter(of(Blocks.ROOTED_DIRT), ROCKY_DIRT)
				.addItemsAfter(of(Blocks.GRAVEL), FLINT_BLOCK)
				.addItemsBefore(of(Blocks.MUSHROOM_STEM), AZALEA_LOG)
				.addItemsBefore(of(Blocks.GOLD_ORE), TIN_ORE, DEEPSLATE_TIN_ORE, CASSITERITE_TIN_ORE)
				.addItemsBefore(of(Blocks.REDSTONE_ORE), SILVER_ORE, DEEPSLATE_SILVER_ORE)
				.addItemsBefore(of(Blocks.LAPIS_ORE), TURQUOISE_ORE, DEEPSLATE_TURQUOISE_ORE)
				.addItemsBefore(of(Blocks.DIAMOND_ORE), SPINEL_ORE, DEEPSLATE_SPINEL_ORE)
				.addItemsBefore(of(Blocks.ANCIENT_DEBRIS), SOUL_SILVER_ORE)
				.addItemsAfter(of(Blocks.RAW_COPPER_BLOCK), RAW_TIN_BLOCK)
				.addItemsAfter(of(Blocks.RAW_GOLD_BLOCK), RAW_SILVER_BLOCK)
				.addItemsAfter(of(Blocks.SCULK_SENSOR), ECHO_BLOCK)
				.addItemsBefore(of(Blocks.COBWEB), ROTTEN_FLESH_BLOCK)
				.addItemsBefore(of(Blocks.DEAD_BUSH), CAVE_GROWTHS, LURID_CAVE_GROWTHS, WISPY_CAVE_GROWTHS, WEIRD_CAVE_GROWTHS, GRAINY_CAVE_GROWTHS, ZESTY_CAVE_GROWTHS)
				.addItemsBefore(of(Blocks.TORCHFLOWER), MOSCHATEL, FALSE_HOPE)
				.addItemsBefore(of(Blocks.PRISMARINE), SUGILITE, CASSITERITE, RHYOLITE, MAGMATIC_RHYOLITE)
				.addItemsAfter(of(Blocks.SNIFFER_EGG), SADDLED_EGG)
				.tab(FUNCTIONAL_BLOCKS)
				.addItemsBefore(of(Blocks.BAMBOO_SIGN), AZALEA_SIGNS.getFirst(), AZALEA_HANGING_SIGNS.getFirst())
				.addItemsBefore(of(Blocks.REDSTONE_TORCH), CUPRIC_TORCH)
				.addItemsBefore(of(Blocks.ANVIL), CUPRIC_CAMPFIRE)
				.addItemsBefore(of(Blocks.CHAIN),
						CUPRIC_LANTERN,
						COPPER_LANTERN, EXPOSED_COPPER_LANTERN, WEATHERED_COPPER_LANTERN, OXIDIZED_COPPER_LANTERN, WAXED_COPPER_LANTERN, WAXED_EXPOSED_COPPER_LANTERN, WAXED_WEATHERED_COPPER_LANTERN, WAXED_OXIDIZED_COPPER_LANTERN,
						BRAZIER, SOUL_BRAZIER
				)
				.addItemsBefore(modLoaded(Blocks.CHAIN, "endergetic"), ENDER_BRAZIER)
				.addItemsBefore(of(Blocks.CHAIN), CUPRIC_BRAZIER)
				.addItemsAfter(of(Blocks.CHAIN),
						COPPER_CHAIN, EXPOSED_COPPER_CHAIN, WEATHERED_COPPER_CHAIN, OXIDIZED_COPPER_CHAIN, WAXED_COPPER_CHAIN, WAXED_EXPOSED_COPPER_CHAIN, WAXED_WEATHERED_COPPER_CHAIN, WAXED_OXIDIZED_COPPER_CHAIN,
						FLOODLIGHT, EXPOSED_FLOODLIGHT, WEATHERED_FLOODLIGHT, OXIDIZED_FLOODLIGHT, WAXED_FLOODLIGHT, WAXED_EXPOSED_FLOODLIGHT, WAXED_WEATHERED_FLOODLIGHT, WAXED_OXIDIZED_FLOODLIGHT,
						DIMMER, LAVA_LAMP
				)
				.addItemsAfter(of(Blocks.SEA_LANTERN), LAPIS_LAZULI_LAMP, SPINEL_LAMP)
				.addItemsBefore(of(Blocks.SHULKER_BOX), TOOLBOX, EXPOSED_TOOLBOX, WEATHERED_TOOLBOX, OXIDIZED_TOOLBOX, WAXED_TOOLBOX, WAXED_EXPOSED_TOOLBOX, WAXED_WEATHERED_TOOLBOX, WAXED_OXIDIZED_TOOLBOX, STORAGE_DUCT, STORAGE_DUCT_HATCH)
				.addItemsBefore(of(Blocks.INFESTED_STONE), FRAGILE_STONE, FRAGILE_DEEPSLATE)
				.addItemsAfter(of(Blocks.SMITHING_TABLE), DISMANTLING_TABLE)
				.addItemsAfter(of(Blocks.DAMAGED_ANVIL), BEJEWELED_ANVIL)
				.addItemsAfter(of(Blocks.ENCHANTING_TABLE), ATONING_TABLE)
				.tab(REDSTONE_BLOCKS)
				.addItemsAfter(of(Blocks.TARGET), WAXED_COPPER_BULB, WAXED_EXPOSED_COPPER_BULB, WAXED_WEATHERED_COPPER_BULB, WAXED_OXIDIZED_COPPER_BULB)
				.addItemsAfter(of(Blocks.COMPARATOR), REFRACTOR, RESISTOR)
				.addItemsAfter(of(Blocks.STONE_BUTTON), WAXED_COPPER_BUTTON, WAXED_EXPOSED_COPPER_BUTTON, WAXED_WEATHERED_COPPER_BUTTON, WAXED_OXIDIZED_COPPER_BUTTON, HOLD_BUTTON)
				.addItemsAfter(of(Blocks.TARGET), BOUNCER)
				.addItemsAfter(of(Blocks.LIGHTNING_ROD), DIMMER, HOOP, WINCH)
				.addItemsAfter(of(Blocks.TNT), TMT)
				.addItemsAfter(of(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), MEDIUM_WEIGHTED_PRESSURE_PLATE)
				.addItemsAfter(of(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), HOLD_PLATE)
				.addItemsBefore(of(Blocks.RAIL), COPPER_RAIL, EXPOSED_COPPER_RAIL, WEATHERED_COPPER_RAIL, OXIDIZED_COPPER_RAIL, WAXED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL)
				.addItemsAfter(of(Blocks.POWERED_RAIL), HALT_RAIL, SPIKED_RAIL, SLAUGHTER_RAIL)
				.addItemsAfter(of(Blocks.DROPPER), SCATTERER, SPLURTER)
				.addItemsAfter(of(Blocks.HOPPER), STORAGE_DUCT, STORAGE_DUCT_HATCH)
				.addItemsBefore(of(Blocks.OAK_FENCE_GATE), ROLLER_DOOR)
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

	public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
		return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
	}

	public static Predicate<ItemStack> ofID(ResourceLocation location, ItemLike fallback, String... modids) {
		return stack -> (BlockSubRegistryHelper.areModsLoaded(modids) ? of(ForgeRegistries.ITEMS.getValue(location)) : of(fallback)).test(stack);
	}

	public static Predicate<ItemStack> ofID(ResourceLocation location, String... modids) {
		return stack -> (BlockSubRegistryHelper.areModsLoaded(modids) && of(ForgeRegistries.ITEMS.getValue(location)).test(stack));
	}

	public static class CCProperties {
		public static final BlockSetType AZALEA_BLOCK_SET = BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":azalea"));
		public static final Supplier<BlockSetType> COPPER_BLOCK_SET = () -> BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":copper", true, SoundType.COPPER, CCSoundEvents.COPPER_DOOR_CLOSE.get(), CCSoundEvents.COPPER_DOOR_OPEN.get(), CCSoundEvents.COPPER_TRAPDOOR_CLOSE.get(), CCSoundEvents.COPPER_TRAPDOOR_OPEN.get(), SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, CCSoundEvents.COPPER_BUTTON_CLICK_OFF.get(), CCSoundEvents.COPPER_BUTTON_CLICK_ON.get()));
		public static final Supplier<BlockSetType> SILVER_BLOCK_SET = () -> BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":silver", false, CCSoundTypes.SILVER, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, CCSoundEvents.MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_OFF.get(), CCSoundEvents.MEDIUM_WEIGHTED_PRESSURE_PLATE_CLICK_ON.get(), SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
		public static final BlockSetType TIN_BLOCK_SET = BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":tin", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

		public static final WoodType AZALEA_WOOD_TYPE = WoodTypeRegistryHelper.registerWoodType(new WoodType(CavernsAndChasms.MOD_ID + ":azalea", AZALEA_BLOCK_SET));

		public static final BlockBehaviour.Properties ROCKY_DIRT = BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(CCSoundTypes.ROCKY_DIRT).requiresCorrectToolForDrops().strength(1.5F);
		public static final BlockBehaviour.Properties FRAGILE_STONE = BlockBehaviour.Properties.copy(Blocks.STONE);
		public static final BlockBehaviour.Properties FRAGILE_DEEPSLATE = BlockBehaviour.Properties.copy(Blocks.DEEPSLATE);
		public static final BlockBehaviour.Properties DRIPSTONE = BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK);
		public static final BlockBehaviour.Properties CALCITE = BlockBehaviour.Properties.copy(Blocks.CALCITE);
		public static final BlockBehaviour.Properties POLISHED_CALCITE = BlockBehaviour.Properties.copy(Blocks.CALCITE);
		public static final BlockBehaviour.Properties CALCITE_BRICKS = BlockBehaviour.Properties.copy(Blocks.CALCITE);
		public static final BlockBehaviour.Properties TUFF = BlockBehaviour.Properties.copy(Blocks.TUFF);
		public static final BlockBehaviour.Properties POLISHED_TUFF = BlockBehaviour.Properties.copy(Blocks.TUFF).sound(CCSoundTypes.POLISHED_TUFF);
		public static final BlockBehaviour.Properties TUFF_BRICKS = BlockBehaviour.Properties.copy(Blocks.TUFF).sound(CCSoundTypes.TUFF_BRICKS);
		public static final BlockBehaviour.Properties SUGILITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).sound(CCSoundTypes.SUGILITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties CASSITERITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.0F, 4.0F).sound(CCSoundTypes.CASSITERITE);
		public static final BlockBehaviour.Properties COBBLESTONE_BRICKS = BlockBehaviour.Properties.copy(Blocks.COBBLESTONE);
		public static final BlockBehaviour.Properties COBBLED_DEEPSLATE_BRICKS = BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE);
		public static final BlockBehaviour.Properties DRIPSTONE_SHINGLES = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F);
		public static final BlockBehaviour.Properties AMETHYST = BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK);
		public static final BlockBehaviour.Properties ECHO_BLOCK = BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.SCULK_CATALYST).requiresCorrectToolForDrops().lightLevel(state -> 6);
		public static final BlockBehaviour.Properties RHYOLITE = BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).sound(CCSoundTypes.RHYOLITE);
		public static final BlockBehaviour.Properties POLISHED_RHYOLITE = BlockBehaviour.Properties.copy(Blocks.POLISHED_BLACKSTONE).sound(CCSoundTypes.RHYOLITE);
		public static final BlockBehaviour.Properties RHYOLITE_BRICKS = BlockBehaviour.Properties.copy(Blocks.POLISHED_BLACKSTONE_BRICKS).sound(CCSoundTypes.RHYOLITE);
		public static final BlockBehaviour.Properties MAGMATIC_RHYOLITE = BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).lightLevel(state -> 3).strength(0.5F).isValidSpawn((state, level, pos, entity) -> entity.fireImmune()).hasPostProcess(PropertyUtil::always).emissiveRendering(PropertyUtil::always).sound(CCSoundTypes.RHYOLITE);
		public static final BlockBehaviour.Properties POLISHED_MAGMATIC_RHYOLITE = BlockBehaviour.Properties.copy(Blocks.POLISHED_BLACKSTONE).lightLevel(state -> 3).strength(0.5F).isValidSpawn((state, level, pos, entity) -> entity.fireImmune()).hasPostProcess(PropertyUtil::always).emissiveRendering(PropertyUtil::always).sound(CCSoundTypes.RHYOLITE);
		public static final BlockBehaviour.Properties MAGMATIC_RHYOLITE_BRICKS = BlockBehaviour.Properties.copy(Blocks.POLISHED_BLACKSTONE_BRICKS).lightLevel(state -> 3).strength(0.5F).isValidSpawn((state, level, pos, entity) -> entity.fireImmune()).hasPostProcess(PropertyUtil::always).emissiveRendering(PropertyUtil::always).sound(CCSoundTypes.RHYOLITE);

		public static final BlockBehaviour.Properties IRON_PLATED_BRICKS = platedBricks(MapColor.RAW_IRON, SoundType.METAL);
		public static final BlockBehaviour.Properties TIN_PLATED_BRICKS = platedBricks(MapColor.TERRACOTTA_WHITE, CCSoundTypes.TIN);
		public static final BlockBehaviour.Properties GOLD_PLATED_BRICKS = platedBricks(MapColor.GOLD, SoundType.METAL);
		public static final BlockBehaviour.Properties SILVER_PLATED_BRICKS = platedBricks(MapColor.COLOR_LIGHT_GRAY, CCSoundTypes.SILVER);
		public static final BlockBehaviour.Properties COPPER_PLATED_BRICKS = platedBricks(MapColor.COLOR_ORANGE, SoundType.COPPER);
		public static final BlockBehaviour.Properties EXPOSED_COPPER_PLATED_BRICKS = platedBricks(MapColor.TERRACOTTA_LIGHT_GRAY, SoundType.COPPER);
		public static final BlockBehaviour.Properties WEATHERED_COPPER_PLATED_BRICKS = platedBricks(MapColor.WARPED_STEM, SoundType.COPPER);
		public static final BlockBehaviour.Properties OXIDIZED_COPPER_PLATED_BRICKS = platedBricks(MapColor.WARPED_NYLIUM, SoundType.COPPER);

		public static final BlockBehaviour.Properties TMT = BlockBehaviour.Properties.copy(Blocks.TNT).sound(CCSoundTypes.TMT);
		public static final BlockBehaviour.Properties RAIL = BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL);
		public static final BlockBehaviour.Properties DISMANTLING_TABLE = BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava();

		public static final BlockBehaviour.Properties COPPER = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties COPPER_RAIL = BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties COPPER_CHAIN = BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.COPPER_CHAIN).noOcclusion();
		public static final BlockBehaviour.Properties COPPER_LANTERN = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.COPPER_LANTERN).lightLevel((state) -> 15).noOcclusion().pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties COPPER_DOOR = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).noOcclusion().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties COPPER_TRAPDOOR = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion().isValidSpawn(PropertyUtil::never);
		public static final BlockBehaviour.Properties COPPER_GRATE = BlockBehaviour.Properties.of().strength(3.0F, 6.0F).sound(CCSoundTypes.COPPER_GRATE).mapColor(MapColor.COLOR_ORANGE).noOcclusion().requiresCorrectToolForDrops().isValidSpawn(PropertyUtil::never).isRedstoneConductor(PropertyUtil::never).isSuffocating(PropertyUtil::never).isViewBlocking(PropertyUtil::never);
		public static final BlockBehaviour.Properties COPPER_BULB = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).sound(CCSoundTypes.COPPER_BULB).requiresCorrectToolForDrops().isRedstoneConductor(PropertyUtil::never).lightLevel(litBlockEmission(15));
		public static final BlockBehaviour.Properties EXPOSED_COPPER_BULB = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).strength(3.0F, 6.0F).sound(CCSoundTypes.COPPER_BULB).requiresCorrectToolForDrops().isRedstoneConductor(PropertyUtil::never).lightLevel(litBlockEmission(12));
		public static final BlockBehaviour.Properties WEATHERED_COPPER_BULB = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).strength(3.0F, 6.0F).sound(CCSoundTypes.COPPER_BULB).requiresCorrectToolForDrops().isRedstoneConductor(PropertyUtil::never).lightLevel(litBlockEmission(8));
		public static final BlockBehaviour.Properties OXIDIZED_COPPER_BULB = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).strength(3.0F, 6.0F).sound(CCSoundTypes.COPPER_BULB).requiresCorrectToolForDrops().isRedstoneConductor(PropertyUtil::never).lightLevel(litBlockEmission(4));
		public static final BlockBehaviour.Properties FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 10);
		public static final BlockBehaviour.Properties EXPOSED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 9);
		public static final BlockBehaviour.Properties WEATHERED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 8);
		public static final BlockBehaviour.Properties OXIDIZED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.FLOODLIGHT).lightLevel((state) -> 7);
		public static final BlockBehaviour.Properties TOOLBOX = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2F, 6.0F).sound(SoundType.COPPER);

		public static final BlockBehaviour.Properties LAVA_LAMP = BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.LAVA_LAMP).lightLevel((state) -> 15);
		public static final BlockBehaviour.Properties COPPER_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.SILVER).noOcclusion();
		public static final BlockBehaviour.Properties TIN_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(CCSoundTypes.TIN).noOcclusion();
		public static final BlockBehaviour.Properties METAL_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_PRESSURE_PLATE = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(CCSoundTypes.SILVER).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties COPPER_BUTTON = BlockBehaviour.Properties.of().noCollission().strength(0.5F).sound(SoundType.COPPER).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties SANGUINE_TILES = Block.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(CCSoundTypes.SANGUINE);
		public static final BlockBehaviour.Properties FORTIFIED_SANGUINE_TILES = Block.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 9.0F).sound(CCSoundTypes.SANGUINE);

		public static final BlockBehaviour.Properties BRAZIER = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.SILVER).lightLevel(litBlockEmission(15)).noOcclusion();
		public static final BlockBehaviour.Properties BRAZIER_DIM = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.SILVER).lightLevel(litBlockEmission(10)).noOcclusion();

		public static final BlockBehaviour.Properties HOLD_PLATE = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties HOLD_BUTTON = BlockBehaviour.Properties.of().noCollission().strength(0.5F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties WINCH = BlockBehaviour.Properties.of().strength(0.5F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties DIMMER = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).noOcclusion().sound(CCSoundTypes.TIN).pushReaction(PushReaction.DESTROY).lightLevel((state) -> state.getValue(AbstractDimmerBlock.POWER));
		public static final BlockBehaviour.Properties HOOP = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(CCSoundTypes.TIN);
		public static final BlockBehaviour.Properties STORAGE_DUCT = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(5.0F).sound(CCSoundTypes.STORAGE_DUCT);
		public static final BlockBehaviour.Properties STORAGE_DUCT_HATCH = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(5.0F).sound(CCSoundTypes.TIN);
		public static final BlockBehaviour.Properties ROLLER_DOOR = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(5.0F).sound(CCSoundTypes.TIN).pushReaction(PushReaction.BLOCK).forceSolidOn();

		public static final BlockBehaviour.Properties ORE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final BlockBehaviour.Properties DEEPSLATE_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE);
		public static final BlockBehaviour.Properties SOUL_SILVER_ORE = Block.Properties.copy(Blocks.SOUL_SOIL).sound(CCSoundTypes.SOUL_SILVER_ORE);
		public static final BlockBehaviour.Properties SPINEL = BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK).sound(CCSoundTypes.SPINEL).mapColor(MapColor.COLOR_PURPLE);
		public static final BlockBehaviour.Properties TURQUOISE = BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK).mapColor(MapColor.COLOR_CYAN);
		public static final BlockBehaviour.Properties LAPIS_LAZULI = BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK);
		public static final BlockBehaviour.Properties LAMP = BlockBehaviour.Properties.of().lightLevel((state) -> 15).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(CCProperties::alwaysAllowSpawn);

		public static final BlockBehaviour.Properties TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(CCSoundTypes.TIN_ORE);
		public static final BlockBehaviour.Properties DEEPSLATE_TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(CCSoundTypes.DEEPSLATE_TIN_ORE);
		public static final BlockBehaviour.Properties CASSITERITE_TIN_ORE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 1.0F).sound(CCSoundTypes.TIN_ORE);

		public static final BlockBehaviour.Properties ROTTEN_FLESH_BLOCK = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).strength(0.8F).sound(CCSoundTypes.ROTTEN_FLESH);
		public static final BlockBehaviour.Properties NECROMIUM_BLOCK = BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).sound(CCSoundTypes.NECROMIUM).mapColor(MapColor.TERRACOTTA_GREEN);

		public static final BlockBehaviour.Properties FALSE_HOPE = PropertyUtil.flower().sound(CCSoundTypes.FALSE_HOPE).lightLevel((state) -> 15);

		public static final BlockBehaviour.Properties SADDLED_EGG = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).forceSolidOn().strength(0.5F).sound(SoundType.METAL).randomTicks().noOcclusion().pushReaction(PushReaction.DESTROY);

		public static final Item.Properties FANCY = new Item.Properties().rarity(CCItems.FANCY);

		public static final WoodSetProperties AZALEA = WoodSetProperties.builder(MapColor.TERRACOTTA_PURPLE).leavesSound(SoundType.AZALEA_LEAVES).build();

		private static boolean alwaysAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
			return true;
		}

		private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
			return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		}

		public static BlockBehaviour.Properties platedBricks(MapColor color, SoundType soundType) {
			return BlockBehaviour.Properties.of().mapColor(color).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(soundType);
		}

		private static BlockBehaviour.Properties placedCoal(int baseLight) {
			return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(5.0F, 6.0F).requiresCorrectToolForDrops().lightLevel(placedCoalLight(baseLight)).noOcclusion().pushReaction(PushReaction.DESTROY);
		}

		private static ToIntFunction<BlockState> placedCoalLight(int base) {
			return state -> {
				int heat = state.getValue(CoalBlock.HEAT);
				return heat > 0 ? base + (heat * 2) + state.getValue(CoalBlock.COAL) : 0;
			};
		}

		private static BlockBehaviour.Properties caveGrowths(MapColor mapColor) {
			BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(mapColor).replaceable().noCollission().instabreak().sound(CCSoundTypes.CAVE_GROWTHS).ignitedByLava().pushReaction(PushReaction.DESTROY);
			properties.offsetFunction = Optional.of((state, level, pos) -> {
				Block block = state.getBlock();
				long i = Mth.getSeed(pos.getX(), pos.getY(), pos.getZ());
				double d0 = ((double) ((float) (i >> 4 & 15L) / 15.0F) - 1.0D) * (double) block.getMaxVerticalOffset();
				float f = block.getMaxHorizontalOffset();
				double d1 = Mth.clamp(((double) ((float) (i & 15L) / 15.0F) - 0.5D) * 0.5D, -f, f);
				double d2 = Mth.clamp(((double) ((float) (i >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D, -f, f);

				Direction facing = state.getValue(CaveGrowthsBlock.FACING);
				Axis axis = facing.getAxis();
				Vec3 vec3 = axis == Axis.X ? new Vec3(d0, d1, d2) : axis == Axis.Y ? new Vec3(d1, d0, d2) : new Vec3(d1, d2, d0);
				if (facing.getAxisDirection() == AxisDirection.NEGATIVE)
					vec3 = vec3.reverse();

				return vec3;
			});
			return properties;
		}
	}

	public enum CCSkullTypes implements SkullBlock.Type {
		MIME, DEEPER, PEEPER;

		public static void registerSkullModels() {
			SkullBlockRenderer.SKIN_BY_TYPE.put(DEEPER, DeeperRenderer.DEEPER_TEXTURE);
			SkullBlockRenderer.SKIN_BY_TYPE.put(PEEPER, PeeperRenderer.PEEPER_TEXTURE);
			SkullBlockRenderer.SKIN_BY_TYPE.put(MIME, MimeRenderer.MIME_TEXTURE);
		}
	}
}