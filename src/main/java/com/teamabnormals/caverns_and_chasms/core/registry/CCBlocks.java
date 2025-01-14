package com.teamabnormals.caverns_and_chasms.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.block.BlueprintBeehiveBlock;
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
import com.teamabnormals.caverns_and_chasms.common.block.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCConstants;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.helper.CCBlockSubRegistryHelper;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
import java.util.function.ToIntFunction;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCBlocks {
	public static final CCBlockSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> SILVER_BLOCK = HELPER.createBlock("silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> SILVER_ORE = HELPER.createBlock("silver_ore", () -> new Block(CCProperties.ORE));
	public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = HELPER.createBlock("deepslate_silver_ore", () -> new Block(CCProperties.DEEPSLATE_ORE));
	public static final RegistryObject<Block> SOUL_SILVER_ORE = HELPER.createBlock("soul_silver_ore", () -> new DropExperienceBlock(Block.Properties.copy(Blocks.SOUL_SOIL), UniformInt.of(0, 1)));
	public static final RegistryObject<Block> RAW_SILVER_BLOCK = HELPER.createBlock("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));
	public static final RegistryObject<Block> SILVER_BARS = HELPER.createBlock("silver_bars", () -> new IronBarsBlock(CCProperties.METAL_BARS));
	public static final RegistryObject<Block> MEDIUM_WEIGHTED_PRESSURE_PLATE = HELPER.createBlock("medium_weighted_pressure_plate", () -> new WeightedPressurePlateBlock(75, CCProperties.SILVER_PRESSURE_PLATE, CCProperties.SILVER_BLOCK_SET));

	public static final RegistryObject<Block> TIN_BLOCK = HELPER.createBlock("tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> TIN_ORE = HELPER.createBlock("tin_ore", () -> new Block(CCProperties.ORE));
	public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = HELPER.createBlock("deepslate_tin_ore", () -> new Block(CCProperties.DEEPSLATE_ORE));
	public static final RegistryObject<Block> RAW_TIN_BLOCK = HELPER.createBlock("raw_tin_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));
	public static final RegistryObject<Block> TIN_BARS = HELPER.createBlock("tin_bars", () -> new IronBarsBlock(CCProperties.METAL_BARS));

	public static final RegistryObject<Block> COPPER_RAIL = HELPER.createBlock("copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> EXPOSED_COPPER_RAIL = HELPER.createBlock("exposed_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> WEATHERED_COPPER_RAIL = HELPER.createBlock("weathered_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> OXIDIZED_COPPER_RAIL = HELPER.createBlock("oxidized_copper_rail", () -> new WeatheringCopperRailBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> WAXED_COPPER_RAIL = HELPER.createBlock("waxed_copper_rail", () -> new CopperRailBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_RAIL = HELPER.createBlock("waxed_exposed_copper_rail", () -> new CopperRailBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_RAIL = HELPER.createBlock("waxed_weathered_copper_rail", () -> new CopperRailBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.RAIL)));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_RAIL = HELPER.createBlock("waxed_oxidized_copper_rail", () -> new CopperRailBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.RAIL)));

	public static final RegistryObject<Block> HALT_RAIL = HELPER.createBlock("halt_rail", () -> new PoweredRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL)));
	public static final RegistryObject<Block> SPIKED_RAIL = HELPER.createBlock("spiked_rail", () -> new SpikedRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL)));
	public static final RegistryObject<Block> SLAUGHTER_RAIL = HELPER.createBlock("slaughter_rail", () -> new SlaughterRailBlock(BlockBehaviour.Properties.copy(Blocks.POWERED_RAIL)));

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

	public static final RegistryObject<Block> DEEPER_HEAD = HELPER.createBlockNoItem("deeper_head", () -> new CCSkullBlock(CCSkullTypes.DEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> DEEPER_WALL_HEAD = HELPER.createBlockNoItem("deeper_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.DEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(DEEPER_HEAD.get())));
	public static final RegistryObject<Block> PEEPER_HEAD = HELPER.createBlockNoItem("peeper_head", () -> new CCSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> PEEPER_WALL_HEAD = HELPER.createBlockNoItem("peeper_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.PEEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(PEEPER_HEAD.get())));
	public static final RegistryObject<Block> MIME_HEAD = HELPER.createBlockNoItem("mime_head", () -> new CCSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
	public static final RegistryObject<Block> MIME_WALL_HEAD = HELPER.createBlockNoItem("mime_wall_head", () -> new CCWallSkullBlock(CCSkullTypes.MIME, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).dropsLike(MIME_HEAD.get())));

	public static final RegistryObject<Block> TMT = HELPER.createBlock("tmt", () -> new TmtBlock(CCProperties.TMT));

	public static final RegistryObject<Block> FLOODLIGHT = HELPER.createBlock("floodlight", () -> new WeatheringFloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT));
	public static final RegistryObject<Block> EXPOSED_FLOODLIGHT = HELPER.createBlock("exposed_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT));
	public static final RegistryObject<Block> WEATHERED_FLOODLIGHT = HELPER.createBlock("weathered_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT));
	public static final RegistryObject<Block> OXIDIZED_FLOODLIGHT = HELPER.createBlock("oxidized_floodlight", () -> new WeatheringFloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_FLOODLIGHT = HELPER.createBlock("waxed_floodlight", () -> new FloodlightBlock(WeatherState.UNAFFECTED, CCProperties.FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_EXPOSED_FLOODLIGHT = HELPER.createBlock("waxed_exposed_floodlight", () -> new FloodlightBlock(WeatherState.EXPOSED, CCProperties.EXPOSED_FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_WEATHERED_FLOODLIGHT = HELPER.createBlock("waxed_weathered_floodlight", () -> new FloodlightBlock(WeatherState.WEATHERED, CCProperties.WEATHERED_FLOODLIGHT));
	public static final RegistryObject<Block> WAXED_OXIDIZED_FLOODLIGHT = HELPER.createBlock("waxed_oxidized_floodlight", () -> new FloodlightBlock(WeatherState.OXIDIZED, CCProperties.OXIDIZED_FLOODLIGHT));

	//	public static final RegistryObject<Block> INDUCTOR = HELPER.createBlock("inductor", () -> new InductorBlock(CCProperties.INDUCTOR));

	public static final RegistryObject<Block> TOOLBOX = HELPER.createToolboxBlock("toolbox", () -> new WeatheringToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> EXPOSED_TOOLBOX = HELPER.createToolboxBlock("exposed_toolbox", () -> new WeatheringToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WEATHERED_TOOLBOX = HELPER.createToolboxBlock("weathered_toolbox", () -> new WeatheringToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> OXIDIZED_TOOLBOX = HELPER.createToolboxBlock("oxidized_toolbox", () -> new WeatheringToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_TOOLBOX = HELPER.createToolboxBlock("waxed_toolbox", () -> new ToolboxBlock(WeatherState.UNAFFECTED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_EXPOSED_TOOLBOX = HELPER.createToolboxBlock("waxed_exposed_toolbox", () -> new ToolboxBlock(WeatherState.EXPOSED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_WEATHERED_TOOLBOX = HELPER.createToolboxBlock("waxed_weathered_toolbox", () -> new ToolboxBlock(WeatherState.WEATHERED, CCProperties.TOOLBOX));
	public static final RegistryObject<Block> WAXED_OXIDIZED_TOOLBOX = HELPER.createToolboxBlock("waxed_oxidized_toolbox", () -> new ToolboxBlock(WeatherState.OXIDIZED, CCProperties.TOOLBOX));

	public static final RegistryObject<Block> COPPER_BARS = HELPER.createBlock("copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
	public static final RegistryObject<Block> EXPOSED_COPPER_BARS = HELPER.createBlock("exposed_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)));
	public static final RegistryObject<Block> WEATHERED_COPPER_BARS = HELPER.createBlock("weathered_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)));
	public static final RegistryObject<Block> OXIDIZED_COPPER_BARS = HELPER.createBlock("oxidized_copper_bars", () -> new WeatheringCopperBarsBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)));
	public static final RegistryObject<Block> WAXED_COPPER_BARS = HELPER.createBlock("waxed_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
	public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BARS = HELPER.createBlock("waxed_exposed_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER)));
	public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BARS = HELPER.createBlock("waxed_weathered_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER)));
	public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BARS = HELPER.createBlock("waxed_oxidized_copper_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER)));

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

	public static final RegistryObject<Block> LAVA_LAMP = HELPER.createBlock("lava_lamp", () -> new LavaLampBlock(CCProperties.LAVA_LAMP));
	public static final RegistryObject<Block> GOLDEN_BARS = HELPER.createBlock("golden_bars", () -> new IronBarsBlock(CCProperties.METAL_BARS));

	public static final RegistryObject<Block> SPINEL_ORE = HELPER.createBlock("spinel_ore", () -> new DropExperienceBlock(CCProperties.ORE, UniformInt.of(2, 5)));
	public static final RegistryObject<Block> DEEPSLATE_SPINEL_ORE = HELPER.createBlock("deepslate_spinel_ore", () -> new DropExperienceBlock(CCProperties.DEEPSLATE_ORE, UniformInt.of(2, 5)));
	public static final RegistryObject<Block> SPINEL_BLOCK = HELPER.createBlock("spinel_block", () -> new Block(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICKS = HELPER.createBlock("spinel_bricks", () -> new Block(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICK_STAIRS = HELPER.createBlock("spinel_brick_stairs", () -> new StairBlock(() -> SPINEL_BRICKS.get().defaultBlockState(), CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICK_SLAB = HELPER.createBlock("spinel_brick_slab", () -> new SlabBlock(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_BRICK_WALL = HELPER.createBlock("spinel_brick_wall", () -> new WallBlock(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_PILLAR = HELPER.createBlock("spinel_pillar", () -> new RotatedPillarBlock(CCProperties.SPINEL));
	public static final RegistryObject<Block> SPINEL_LAMP = HELPER.createBlock("spinel_lamp", () -> new Block(CCProperties.LAMP));

	public static final RegistryObject<Block> DISMANTLING_TABLE = HELPER.createBlock("dismantling_table", () -> new DismantlingTableBlock(CCProperties.DISMANTLING_TABLE));
	public static final RegistryObject<Block> BEJEWELED_ANVIL = HELPER.createBlock("bejeweled_anvil", () -> new BejeweledAnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)));

	public static final RegistryObject<Block> ZIRCONIA_BLOCK = HELPER.createBlock("zirconia_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

	public static final RegistryObject<Block> LAPIS_LAZULI_BRICKS = HELPER.createBlock("lapis_bricks", () -> new Block(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_STAIRS = HELPER.createBlock("lapis_brick_stairs", () -> new StairBlock(() -> LAPIS_LAZULI_BRICKS.get().defaultBlockState(), CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_SLAB = HELPER.createBlock("lapis_brick_slab", () -> new SlabBlock(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_BRICK_WALL = HELPER.createBlock("lapis_brick_wall", () -> new WallBlock(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_PILLAR = HELPER.createBlock("lapis_pillar", () -> new RotatedPillarBlock(CCProperties.LAPIS_LAZULI));
	public static final RegistryObject<Block> LAPIS_LAZULI_LAMP = HELPER.createBlock("lapis_lamp", () -> new Block(CCProperties.LAMP));

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

	public static final RegistryObject<Block> CALCITE_STAIRS = HELPER.createBlock("calcite_stairs", () -> new StairBlock(() -> Blocks.CALCITE.defaultBlockState(), CCProperties.CALCITE));
	public static final RegistryObject<Block> CALCITE_SLAB = HELPER.createBlock("calcite_slab", () -> new SlabBlock(CCProperties.CALCITE));
	public static final RegistryObject<Block> CALCITE_WALL = HELPER.createBlock("calcite_wall", () -> new WallBlock(CCProperties.CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE = HELPER.createBlock("polished_calcite", () -> new Block(CCProperties.CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE_STAIRS = HELPER.createBlock("polished_calcite_stairs", () -> new StairBlock(() -> POLISHED_CALCITE.get().defaultBlockState(), CCProperties.CALCITE));
	public static final RegistryObject<Block> POLISHED_CALCITE_SLAB = HELPER.createBlock("polished_calcite_slab", () -> new SlabBlock(CCProperties.CALCITE));

	public static final RegistryObject<Block> TUFF_STAIRS = HELPER.createBlock("tuff_stairs", () -> new StairBlock(() -> Blocks.TUFF.defaultBlockState(), CCProperties.TUFF));
	public static final RegistryObject<Block> TUFF_SLAB = HELPER.createBlock("tuff_slab", () -> new SlabBlock(CCProperties.TUFF));
	public static final RegistryObject<Block> TUFF_WALL = HELPER.createBlock("tuff_wall", () -> new WallBlock(CCProperties.TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF = HELPER.createBlock("polished_tuff", () -> new Block(CCProperties.TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF_STAIRS = HELPER.createBlock("polished_tuff_stairs", () -> new StairBlock(() -> POLISHED_TUFF.get().defaultBlockState(), CCProperties.TUFF));
	public static final RegistryObject<Block> POLISHED_TUFF_SLAB = HELPER.createBlock("polished_tuff_slab", () -> new SlabBlock(CCProperties.TUFF));

	public static final RegistryObject<Block> SUGILITE = HELPER.createBlock("sugilite", () -> new Block(CCProperties.SUGILITE));
	public static final RegistryObject<Block> SUGILITE_STAIRS = HELPER.createBlock("sugilite_stairs", () -> new StairBlock(() -> SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE));
	public static final RegistryObject<Block> SUGILITE_SLAB = HELPER.createBlock("sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE));
	public static final RegistryObject<Block> SUGILITE_WALL = HELPER.createBlock("sugilite_wall", () -> new WallBlock(CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE = HELPER.createBlock("polished_sugilite", () -> new Block(CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE_STAIRS = HELPER.createBlock("polished_sugilite_stairs", () -> new StairBlock(() -> POLISHED_SUGILITE.get().defaultBlockState(), CCProperties.SUGILITE));
	public static final RegistryObject<Block> POLISHED_SUGILITE_SLAB = HELPER.createBlock("polished_sugilite_slab", () -> new SlabBlock(CCProperties.SUGILITE));

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

	public static final RegistryObject<Block> ECHO_BLOCK = HELPER.createBlock("echo_block", () -> new Block(CCProperties.ECHO_BLOCK));

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

	public static final RegistryObject<Block> MOSCHATEL = HELPER.createBlock("moschatel", () -> new MoschatelBlock(() -> MobEffects.NIGHT_VISION, 5, PropertyUtil.flower()));
	public static final RegistryObject<Block> CAVE_GROWTHS = HELPER.createBlock("cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_LIGHT_GREEN)));
	public static final RegistryObject<Block> LURID_CAVE_GROWTHS = HELPER.createBlock("lurid_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.GLOW_LICHEN)));
	public static final RegistryObject<Block> WISPY_CAVE_GROWTHS = HELPER.createBlock("wispy_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.STONE)));
	public static final RegistryObject<Block> GRAINY_CAVE_GROWTHS = HELPER.createBlock("grainy_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_PINK)));
	public static final RegistryObject<Block> WEIRD_CAVE_GROWTHS = HELPER.createBlock("weird_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.TERRACOTTA_MAGENTA)));
	public static final RegistryObject<Block> ZESTY_CAVE_GROWTHS = HELPER.createBlock("zesty_cave_growths", () -> new CaveGrowthsBlock(CCProperties.caveGrowths(MapColor.RAW_IRON)));

	public static final RegistryObject<Block> POTTED_FALSE_HOPE = HELPER.createBlockNoItem("potted_false_hope", () -> new FlowerPotBlock(FALSE_HOPE.get(), PropertyUtil.flowerPot()));

	public static final RegistryObject<Block> POTTED_MOSCHATEL = HELPER.createBlockNoItem("potted_moschatel", () -> new FlowerPotBlock(MOSCHATEL.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_cave_growths", () -> new FlowerPotBlock(CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_LURID_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_lurid_cave_growths", () -> new FlowerPotBlock(LURID_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_WISPY_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_wispy_cave_growths", () -> new FlowerPotBlock(WISPY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_GRAINY_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_grainy_cave_growths", () -> new FlowerPotBlock(GRAINY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_WEIRD_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_weird_cave_growths", () -> new FlowerPotBlock(WEIRD_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));
	public static final RegistryObject<Block> POTTED_ZESTY_CAVE_GROWTHS = HELPER.createBlockNoItem("potted_zesty_cave_growths", () -> new FlowerPotBlock(ZESTY_CAVE_GROWTHS.get(), PropertyUtil.flowerPot()));

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
				.addItemsBefore(of(Blocks.DEEPSLATE),
						() -> Blocks.CALCITE, CALCITE_STAIRS, CALCITE_SLAB, CALCITE_WALL, POLISHED_CALCITE, POLISHED_CALCITE_STAIRS, POLISHED_CALCITE_SLAB,
						() -> Blocks.TUFF, TUFF_STAIRS, TUFF_SLAB, TUFF_WALL, POLISHED_TUFF, POLISHED_TUFF_STAIRS, POLISHED_TUFF_SLAB,
						() -> Blocks.DRIPSTONE_BLOCK, DRIPSTONE_SHINGLES, FLOODED_DRIPSTONE_SHINGLES, DRIPSTONE_SHINGLE_STAIRS, DRIPSTONE_SHINGLE_SLAB, DRIPSTONE_SHINGLE_WALL, CHISELED_DRIPSTONE_SHINGLES,
						SUGILITE, SUGILITE_STAIRS, SUGILITE_SLAB, SUGILITE_WALL, POLISHED_SUGILITE, POLISHED_SUGILITE_STAIRS, POLISHED_SUGILITE_SLAB
				)
				.addItemsBefore(of(Blocks.CHISELED_DEEPSLATE),
						COBBLED_DEEPSLATE_BRICKS, COBBLED_DEEPSLATE_BRICK_STAIRS, COBBLED_DEEPSLATE_BRICK_SLAB, COBBLED_DEEPSLATE_BRICK_WALL,
						COBBLED_DEEPSLATE_TILES, COBBLED_DEEPSLATE_TILE_STAIRS, COBBLED_DEEPSLATE_TILE_SLAB, COBBLED_DEEPSLATE_TILE_WALL
				)
				.editor(event -> event.getEntries().remove(new ItemStack(Blocks.CHISELED_DEEPSLATE)))
				.addItemsBefore(of(Blocks.DEEPSLATE_TILES), () -> Blocks.CHISELED_DEEPSLATE)
				.addItemsBefore(of(Blocks.BASALT), SANGUINE_BLOCK, SANGUINE_TILES, SANGUINE_TILE_STAIRS, SANGUINE_TILE_SLAB, SANGUINE_TILE_WALL, FORTIFIED_SANGUINE_TILES, FORTIFIED_SANGUINE_TILE_STAIRS, FORTIFIED_SANGUINE_TILE_SLAB, FORTIFIED_SANGUINE_TILE_WALL)
				.addItemsAfter(of(Blocks.AMETHYST_BLOCK), AMETHYST_BLOCK, CUT_AMETHYST, CUT_AMETHYST_BRICKS, CUT_AMETHYST_BRICK_STAIRS, CUT_AMETHYST_BRICK_SLAB, CUT_AMETHYST_BRICK_WALL)
				.addItemsAfter(of(Blocks.GOLD_BLOCK), GOLDEN_BARS)
				.addItemsBefore(of(Blocks.GOLD_BLOCK), TIN_BLOCK, TIN_BARS)
				.addItemsBefore(of(Blocks.REDSTONE_BLOCK), SILVER_BLOCK, SILVER_BARS, MEDIUM_WEIGHTED_PRESSURE_PLATE)
				.addItemsAfter(of(Blocks.LAPIS_BLOCK),
						LAPIS_LAZULI_BRICKS, LAPIS_LAZULI_BRICK_STAIRS, LAPIS_LAZULI_BRICK_SLAB, LAPIS_LAZULI_BRICK_WALL, LAPIS_LAZULI_PILLAR, LAPIS_LAZULI_LAMP,
						SPINEL_BLOCK, SPINEL_BRICKS, SPINEL_BRICK_STAIRS, SPINEL_BRICK_SLAB, SPINEL_BRICK_WALL, SPINEL_PILLAR, SPINEL_LAMP,
						ZIRCONIA_BLOCK
				)
				.addItemsAfter(of(Blocks.NETHERITE_BLOCK), NECROMIUM_BLOCK)
				.addItemsAfter(of(Blocks.CUT_COPPER_SLAB), COPPER_BARS, () -> Blocks.LIGHTNING_ROD, COPPER_BUTTON)
				.addItemsAfter(of(Blocks.EXPOSED_CUT_COPPER_SLAB), EXPOSED_COPPER_BARS, EXPOSED_LIGHTNING_ROD, EXPOSED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.WEATHERED_CUT_COPPER_SLAB), WEATHERED_COPPER_BARS, WEATHERED_LIGHTNING_ROD, WEATHERED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.OXIDIZED_CUT_COPPER_SLAB), OXIDIZED_COPPER_BARS, OXIDIZED_LIGHTNING_ROD, OXIDIZED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.WAXED_CUT_COPPER_SLAB), WAXED_COPPER_BARS, WAXED_LIGHTNING_ROD, WAXED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB), WAXED_EXPOSED_COPPER_BARS, WAXED_EXPOSED_LIGHTNING_ROD, WAXED_EXPOSED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB), WAXED_WEATHERED_COPPER_BARS, WAXED_WEATHERED_LIGHTNING_ROD, WAXED_WEATHERED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB), WAXED_OXIDIZED_COPPER_BARS, WAXED_OXIDIZED_LIGHTNING_ROD, WAXED_OXIDIZED_COPPER_BUTTON)
				.tab(NATURAL_BLOCKS)
				.addItemsAfter(of(Blocks.ROOTED_DIRT), ROCKY_DIRT)
				.addItemsBefore(of(Blocks.MUSHROOM_STEM), AZALEA_LOG)
				.addItemsBefore(of(Blocks.GOLD_ORE), TIN_ORE, DEEPSLATE_TIN_ORE)
				.addItemsBefore(of(Blocks.REDSTONE_ORE), SILVER_ORE, DEEPSLATE_SILVER_ORE)
				.addItemsBefore(of(Blocks.DIAMOND_ORE), SPINEL_ORE, DEEPSLATE_SPINEL_ORE)
				.addItemsBefore(of(Blocks.ANCIENT_DEBRIS), SOUL_SILVER_ORE)
				.addItemsAfter(of(Blocks.RAW_COPPER_BLOCK), RAW_TIN_BLOCK)
				.addItemsAfter(of(Blocks.RAW_GOLD_BLOCK), RAW_SILVER_BLOCK)
				.addItemsAfter(of(Blocks.SCULK_SENSOR), ECHO_BLOCK)
				.addItemsBefore(of(Blocks.COBWEB), ROTTEN_FLESH_BLOCK)
				.addItemsBefore(of(Blocks.DEAD_BUSH), CAVE_GROWTHS, LURID_CAVE_GROWTHS, WISPY_CAVE_GROWTHS, WEIRD_CAVE_GROWTHS, GRAINY_CAVE_GROWTHS, ZESTY_CAVE_GROWTHS)
				.addItemsBefore(of(Blocks.TORCHFLOWER), MOSCHATEL, FALSE_HOPE)
				.tab(FUNCTIONAL_BLOCKS)
				.addItemsBefore(of(Blocks.BAMBOO_SIGN), AZALEA_SIGNS.getFirst(), AZALEA_HANGING_SIGNS.getFirst())
				.addItemsBefore(of(Blocks.REDSTONE_TORCH), CUPRIC_TORCH)
				.addItemsBefore(of(Blocks.ANVIL), CUPRIC_CAMPFIRE)
				.addItemsBefore(of(Blocks.CHAIN), CUPRIC_LANTERN, BRAZIER, SOUL_BRAZIER)
				.addItemsBefore(modLoaded(Blocks.CHAIN, "endergetic"), ENDER_BRAZIER)
				.addItemsBefore(of(Blocks.CHAIN), CUPRIC_BRAZIER)
				.addItemsAfter(of(Blocks.CHAIN),
						FLOODLIGHT, EXPOSED_FLOODLIGHT, WEATHERED_FLOODLIGHT, OXIDIZED_FLOODLIGHT,
						WAXED_FLOODLIGHT, WAXED_EXPOSED_FLOODLIGHT, WAXED_WEATHERED_FLOODLIGHT, WAXED_OXIDIZED_FLOODLIGHT,
						LAVA_LAMP
				)
				.addItemsAfter(of(Blocks.SEA_LANTERN), LAPIS_LAZULI_LAMP, SPINEL_LAMP)
				.addItemsBefore(of(Blocks.SHULKER_BOX), TOOLBOX, EXPOSED_TOOLBOX, WEATHERED_TOOLBOX, OXIDIZED_TOOLBOX, WAXED_TOOLBOX, WAXED_EXPOSED_TOOLBOX, WAXED_WEATHERED_TOOLBOX, WAXED_OXIDIZED_TOOLBOX)
				.addItemsBefore(of(Blocks.INFESTED_STONE), FRAGILE_STONE, FRAGILE_DEEPSLATE)
				.addItemsAfter(of(Blocks.SMITHING_TABLE), DISMANTLING_TABLE)
				.addItemsAfter(of(Blocks.DAMAGED_ANVIL), BEJEWELED_ANVIL)
				.tab(REDSTONE_BLOCKS)
				.addItemsAfter(of(Blocks.STONE_BUTTON), WAXED_COPPER_BUTTON, WAXED_EXPOSED_COPPER_BUTTON, WAXED_WEATHERED_COPPER_BUTTON, WAXED_OXIDIZED_COPPER_BUTTON)
				.addItemsAfter(of(Blocks.TNT), TMT)
				.addItemsAfter(of(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), MEDIUM_WEIGHTED_PRESSURE_PLATE)
				.addItemsBefore(of(Blocks.RAIL), COPPER_RAIL, EXPOSED_COPPER_RAIL, WEATHERED_COPPER_RAIL, OXIDIZED_COPPER_RAIL, WAXED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL)
				.addItemsAfter(of(Blocks.POWERED_RAIL), HALT_RAIL, SPIKED_RAIL, SLAUGHTER_RAIL)
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
		public static final BlockSetType COPPER_BLOCK_SET = BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":copper", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
		public static final BlockSetType SILVER_BLOCK_SET = BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":silver", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
		public static final BlockSetType TIN_BLOCK_SET = BlockSetType.register(new BlockSetType(CavernsAndChasms.MOD_ID + ":tin", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

		public static final WoodType AZALEA_WOOD_TYPE = WoodTypeRegistryHelper.registerWoodType(new WoodType(CavernsAndChasms.MOD_ID + ":azalea", AZALEA_BLOCK_SET));

		public static final BlockBehaviour.Properties ROCKY_DIRT = BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(CCSoundTypes.ROCKY_DIRT).requiresCorrectToolForDrops().strength(1.5F);
		public static final BlockBehaviour.Properties FRAGILE_STONE = BlockBehaviour.Properties.copy(Blocks.STONE);
		public static final BlockBehaviour.Properties FRAGILE_DEEPSLATE = BlockBehaviour.Properties.copy(Blocks.DEEPSLATE);
		public static final BlockBehaviour.Properties CALCITE = BlockBehaviour.Properties.copy(Blocks.CALCITE);
		public static final BlockBehaviour.Properties TUFF = BlockBehaviour.Properties.copy(Blocks.TUFF);
		public static final BlockBehaviour.Properties SUGILITE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties COBBLESTONE_BRICKS = BlockBehaviour.Properties.copy(Blocks.COBBLESTONE);
		public static final BlockBehaviour.Properties COBBLED_DEEPSLATE_BRICKS = BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE);
		public static final BlockBehaviour.Properties DRIPSTONE_SHINGLES = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F);
		public static final BlockBehaviour.Properties AMETHYST = BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK);
		public static final BlockBehaviour.Properties ECHO_BLOCK = BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.SCULK_CATALYST).requiresCorrectToolForDrops().lightLevel(state -> 6);

		public static final BlockBehaviour.Properties TMT = BlockBehaviour.Properties.copy(Blocks.TNT);
		public static final BlockBehaviour.Properties TOOLBOX = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2F, 6.0F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 10);
		public static final BlockBehaviour.Properties EXPOSED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 9);
		public static final BlockBehaviour.Properties WEATHERED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 8);
		public static final BlockBehaviour.Properties OXIDIZED_FLOODLIGHT = BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 7);
		public static final BlockBehaviour.Properties DISMANTLING_TABLE = BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava();

		public static final BlockBehaviour.Properties INDUCTOR = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 4.8F).sound(SoundType.COPPER);
		public static final BlockBehaviour.Properties LAVA_LAMP = BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 15);
		public static final BlockBehaviour.Properties METAL_BARS = BlockBehaviour.Properties.of().mapColor(MapColor.NONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion();
		public static final BlockBehaviour.Properties SILVER_PRESSURE_PLATE = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noCollission().strength(0.5F).sound(SoundType.WOOD);
		public static final BlockBehaviour.Properties COPPER_BUTTON = BlockBehaviour.Properties.of().noCollission().strength(0.5F).sound(SoundType.COPPER).pushReaction(PushReaction.DESTROY);
		public static final BlockBehaviour.Properties SANGUINE_TILES = Block.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.METAL);
		public static final BlockBehaviour.Properties FORTIFIED_SANGUINE_TILES = Block.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(5.0F, 9.0F).sound(SoundType.METAL);

		public static final BlockBehaviour.Properties BRAZIER = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(getLightValueLit(15)).noOcclusion();
		public static final BlockBehaviour.Properties BRAZIER_DIM = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(getLightValueLit(10)).noOcclusion();

		public static final BlockBehaviour.Properties ORE = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F);
		public static final BlockBehaviour.Properties DEEPSLATE_ORE = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE);
		public static final BlockBehaviour.Properties SPINEL = BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK).mapColor(MapColor.COLOR_PURPLE);
		public static final BlockBehaviour.Properties LAPIS_LAZULI = BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK);
		public static final BlockBehaviour.Properties LAMP = BlockBehaviour.Properties.of().lightLevel((state) -> 15).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(CCProperties::alwaysAllowSpawn);

		public static final BlockBehaviour.Properties ROTTEN_FLESH_BLOCK = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).strength(0.8F).sound(SoundType.CORAL_BLOCK);
		public static final BlockBehaviour.Properties NECROMIUM_BLOCK = BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GREEN);

		public static final BlockBehaviour.Properties FALSE_HOPE = PropertyUtil.flower().lightLevel((state) -> 15);

		public static final WoodSetProperties AZALEA = WoodSetProperties.builder(MapColor.TERRACOTTA_PURPLE).leavesSound(SoundType.AZALEA_LEAVES).build();

		private static boolean alwaysAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
			return true;
		}

		private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
			return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
		}

		private static BlockBehaviour.Properties caveGrowths(MapColor mapColor) {
			BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(mapColor).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY);
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
		MIME, DEEPER, PEEPER
	}
}