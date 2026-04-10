package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.teamabnormals.blueprint.core.util.BlockUtil;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.DataUtil.AlternativeDispenseBehavior;
import com.teamabnormals.caverns_and_chasms.common.block.BrazierBlock;
import com.teamabnormals.caverns_and_chasms.common.block.CoalBlock;
import com.teamabnormals.caverns_and_chasms.common.block.Sparkler;
import com.teamabnormals.caverns_and_chasms.common.dispenser.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry.InteractionInformation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCCompat {

	@SubscribeEvent
	public static void onModifyComponents(ModifyDefaultComponentsEvent event) {
		event.modifyMatching(item -> new ItemStack(item).is(CCItemTags.INGOTS_SILVER), c -> c.set(DataComponents.RARITY, CCEnums.FANCY.getValue()));
	}

	public static void registerCompat() {
		registerCompostables();
		registerFlammables();
		registerDispenserBehaviors();
		registerWaxables();
		registerFireworkIngredients();
		changeLocalization();
		makeVillagersScaredOfRats();
		CCDecoratedPotPatterns.registerDecoratedPotPatterns();
		CCCauldronInteractions.registerCauldronInteractions();
		CCSoundEvents.registerNoteBlocks();

		FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new InteractionInformation((level, currentPos, relativePos, currentState) -> {
			return level.getBlockState(currentPos.below()).is(Blocks.BUBBLE_COLUMN);
		}, CCBlocks.RHYOLITE.get().defaultBlockState()));
	}

	public static void registerCompostables() {

	}

	private static void registerFlammables() {
		DataUtil.registerFlammable(CCBlocks.CHARCOAL_BLOCK.get(), 5, 5);

		DataUtil.registerFlammable(CCBlocks.AZALEA_LOG.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.AZALEA_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_LOG.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(CCBlocks.AZALEA_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOARDS.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BEEHIVE.get(), 5, 20);

		DataUtil.registerFlammable(CCBlocks.FALSE_HOPE.get(), 60, 100);

		DataUtil.registerFlammable(CCBlocks.MOSCHATEL.get(), 60, 100);
		DataUtil.registerFlammable(CCBlocks.CAVE_GROWTHS.get(), 60, 100);
		DataUtil.registerFlammable(CCBlocks.LURID_CAVE_GROWTHS.get(), 60, 100);
		DataUtil.registerFlammable(CCBlocks.WISPY_CAVE_GROWTHS.get(), 60, 100);
		DataUtil.registerFlammable(CCBlocks.GRAINY_CAVE_GROWTHS.get(), 60, 100);
		DataUtil.registerFlammable(CCBlocks.WEIRD_CAVE_GROWTHS.get(), 60, 100);
		DataUtil.registerFlammable(CCBlocks.ZESTY_CAVE_GROWTHS.get(), 60, 100);

		DataUtil.registerFlammable(CCBlocks.TMT.get(), 15, 100);
		DataUtil.registerFlammable(CCBlocks.GUNPOWDER_BLOCK.get(), 15, 100);
	}

	private static void registerDispenserBehaviors() {
		DispenserBlock.registerProjectileBehavior(CCItems.KUNAI.get());
		DispenserBlock.registerProjectileBehavior(CCItems.BLUNT_ARROW.get());
		DispenserBlock.registerProjectileBehavior(CCItems.RICOCHET_ARROW.get());
		DispenserBlock.registerProjectileBehavior(CCItems.LARGE_ARROW.get());

		DispenserBlock.registerBehavior(CCBlocks.TMT.get(), new TMTDispenseBehavior());
		DispenserBlock.registerBehavior(CCItems.GOLDEN_BUCKET.get(), new GoldenBucketDispenseBehavior());

		DispenseItemBehavior goldenBucketDispenseBehavior = new FilledGoldenBucketDispenseBehavior();
		DispenserBlock.registerBehavior(CCItems.GOLDEN_LAVA_BUCKET.get(), goldenBucketDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.GOLDEN_WATER_BUCKET.get(), goldenBucketDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), goldenBucketDispenseBehavior);

		DefaultDispenseItemBehavior horseArmorDispenseBehavior = new HorseArmorDispenseBehavior();
		DispenserBlock.registerBehavior(CCItems.SILVER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.NETHERITE_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.NECROMIUM_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.EXPOSED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.WEATHERED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.OXIDIZED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.WAXED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.WAXED_EXPOSED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.WAXED_WEATHERED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.WAXED_OXIDIZED_COPPER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);

		DispenseItemBehavior armorDispenseBehavior = new ArmorDispenseBehavior();
		DispenserBlock.registerBehavior(CCItems.DEEPER_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.EVENDEEPER_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.PEEPER_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.MIME_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.TETHER_POTION.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.IMPACT_POTION.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.TRAIL_POTION.get(), armorDispenseBehavior);

		DispenserBlock.registerBehavior(CCItems.TINPLATE.get(), new OptionalDispenseItemBehavior() {
			public ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
				Level level = source.level();
				BlockState blockstate = level.getBlockState(blockpos);
				Optional<BlockState> optional = HoneycombItem.getWaxed(blockstate);
				if (optional.isPresent()) {
					level.setBlockAndUpdate(blockpos, optional.get());
					level.levelEvent(3003, blockpos, 0);
					stack.shrink(1);
					this.setSuccess(true);
					return stack;
				} else {
					return super.execute(source, stack);
				}
			}
		});


		DataUtil.registerAlternativeDispenseBehavior(new AlternativeDispenseBehavior(CavernsAndChasms.MOD_ID, Items.FLINT_AND_STEEL, (source, stack) -> {
			BlockState state = source.level().getBlockState(BlockUtil.offsetPos(source));
			Block block = state.getBlock();
			if (block instanceof CoalBlock || block instanceof BrazierBlock || block instanceof Sparkler) {
				return state.hasProperty(BlockStateProperties.LIT) && (!state.getValue(BlockStateProperties.LIT) || block instanceof Sparkler) && (!state.hasProperty(BlockStateProperties.WATERLOGGED) || !state.getValue(BlockStateProperties.WATERLOGGED));
			} else {
				return false;
			}
		}, new OptionalDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				ServerLevel level = source.level();
				BlockPos pos = BlockUtil.offsetPos(source);
				BlockState state = level.getBlockState(pos);
				if (!state.getValue(BlockStateProperties.LIT)) {
					level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
				} else if (state.getBlock() instanceof Sparkler sparkler) {
					sparkler.explodeSparkler(state, level, pos);
				}
				level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
				stack.hurtAndBreak(1, level, null, item -> {
				});

				return stack;
			}
		}));
	}

	private static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
		DataUtil.changeBlockLocalization(Blocks.RAIL, CavernsAndChasms.MOD_ID, "iron_rail");
		DataUtil.changeBlockLocalization(Blocks.AMETHYST_BLOCK, CavernsAndChasms.MOD_ID, "amethyst");
		DataUtil.changeBlockLocalization(Blocks.DRIPSTONE_BLOCK, CavernsAndChasms.MOD_ID, "dripstone");
		DataUtil.changeBlockLocalization(CCBlocks.AMETHYST_BLOCK.get(), "minecraft", "amethyst_block");
		DataUtil.changeBlockLocalization(Blocks.CHISELED_DEEPSLATE, CavernsAndChasms.MOD_ID, "chiseled_deepslate_bricks");
	}

	public static void registerWaxables() {
		ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
		HoneycombItem.WAXABLES.get().forEach(builder::put);
		builder.put(CCBlocks.COPPER_BARS.get(), CCBlocks.WAXED_COPPER_BARS.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BARS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BARS.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BARS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BARS.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BARS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BARS.get());
		builder.put(CCBlocks.COPPER_BUTTON.get(), CCBlocks.WAXED_COPPER_BUTTON.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BUTTON.get(), CCBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BUTTON.get(), CCBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BUTTON.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
		builder.put(CCBlocks.TOOLBOX.get(), CCBlocks.WAXED_TOOLBOX.get());
		builder.put(CCBlocks.EXPOSED_TOOLBOX.get(), CCBlocks.WAXED_EXPOSED_TOOLBOX.get());
		builder.put(CCBlocks.WEATHERED_TOOLBOX.get(), CCBlocks.WAXED_WEATHERED_TOOLBOX.get());
		builder.put(CCBlocks.OXIDIZED_TOOLBOX.get(), CCBlocks.WAXED_OXIDIZED_TOOLBOX.get());
		builder.put(Blocks.LIGHTNING_ROD, CCBlocks.WAXED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.EXPOSED_LIGHTNING_ROD.get(), CCBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.WEATHERED_LIGHTNING_ROD.get(), CCBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.OXIDIZED_LIGHTNING_ROD.get(), CCBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
		builder.put(CCBlocks.FLOODLIGHT.get(), CCBlocks.WAXED_FLOODLIGHT.get());
		builder.put(CCBlocks.EXPOSED_FLOODLIGHT.get(), CCBlocks.WAXED_EXPOSED_FLOODLIGHT.get());
		builder.put(CCBlocks.WEATHERED_FLOODLIGHT.get(), CCBlocks.WAXED_WEATHERED_FLOODLIGHT.get());
		builder.put(CCBlocks.OXIDIZED_FLOODLIGHT.get(), CCBlocks.WAXED_OXIDIZED_FLOODLIGHT.get());
		builder.put(CCBlocks.COPPER_RAIL.get(), CCBlocks.WAXED_COPPER_RAIL.get());
		builder.put(CCBlocks.EXPOSED_COPPER_RAIL.get(), CCBlocks.WAXED_EXPOSED_COPPER_RAIL.get());
		builder.put(CCBlocks.WEATHERED_COPPER_RAIL.get(), CCBlocks.WAXED_WEATHERED_COPPER_RAIL.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_RAIL.get(), CCBlocks.WAXED_OXIDIZED_COPPER_RAIL.get());
		builder.put(CCBlocks.COPPER_BRICKS.get(), CCBlocks.WAXED_COPPER_BRICKS.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BRICKS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BRICKS.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BRICKS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BRICKS.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BRICKS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BRICKS.get());
		builder.put(CCBlocks.COPPER_BRICK_STAIRS.get(), CCBlocks.WAXED_COPPER_BRICK_STAIRS.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BRICK_STAIRS.get(), CCBlocks.WAXED_EXPOSED_COPPER_BRICK_STAIRS.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BRICK_STAIRS.get(), CCBlocks.WAXED_WEATHERED_COPPER_BRICK_STAIRS.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BRICK_STAIRS.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BRICK_STAIRS.get());
		builder.put(CCBlocks.COPPER_BRICK_SLAB.get(), CCBlocks.WAXED_COPPER_BRICK_SLAB.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BRICK_SLAB.get(), CCBlocks.WAXED_EXPOSED_COPPER_BRICK_SLAB.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BRICK_SLAB.get(), CCBlocks.WAXED_WEATHERED_COPPER_BRICK_SLAB.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BRICK_SLAB.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BRICK_SLAB.get());
		builder.put(CCBlocks.COPPER_BRICK_WALL.get(), CCBlocks.WAXED_COPPER_BRICK_WALL.get());
		builder.put(CCBlocks.EXPOSED_COPPER_BRICK_WALL.get(), CCBlocks.WAXED_EXPOSED_COPPER_BRICK_WALL.get());
		builder.put(CCBlocks.WEATHERED_COPPER_BRICK_WALL.get(), CCBlocks.WAXED_WEATHERED_COPPER_BRICK_WALL.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_BRICK_WALL.get(), CCBlocks.WAXED_OXIDIZED_COPPER_BRICK_WALL.get());
		builder.put(CCBlocks.CHISELED_COPPER_BRICKS.get(), CCBlocks.WAXED_CHISELED_COPPER_BRICKS.get());
		builder.put(CCBlocks.EXPOSED_CHISELED_COPPER_BRICKS.get(), CCBlocks.WAXED_EXPOSED_CHISELED_COPPER_BRICKS.get());
		builder.put(CCBlocks.WEATHERED_CHISELED_COPPER_BRICKS.get(), CCBlocks.WAXED_WEATHERED_CHISELED_COPPER_BRICKS.get());
		builder.put(CCBlocks.OXIDIZED_CHISELED_COPPER_BRICKS.get(), CCBlocks.WAXED_OXIDIZED_CHISELED_COPPER_BRICKS.get());
		builder.put(CCBlocks.COPPER_INGOT.get(), CCBlocks.WAXED_COPPER_INGOT.get());
		builder.put(CCBlocks.EXPOSED_COPPER_INGOT.get(), CCBlocks.WAXED_EXPOSED_COPPER_INGOT.get());
		builder.put(CCBlocks.WEATHERED_COPPER_INGOT.get(), CCBlocks.WAXED_WEATHERED_COPPER_INGOT.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_INGOT.get(), CCBlocks.WAXED_OXIDIZED_COPPER_INGOT.get());
		builder.put(CCBlocks.COPPER_CHAIN.get(), CCBlocks.WAXED_COPPER_CHAIN.get());
		builder.put(CCBlocks.EXPOSED_COPPER_CHAIN.get(), CCBlocks.WAXED_EXPOSED_COPPER_CHAIN.get());
		builder.put(CCBlocks.WEATHERED_COPPER_CHAIN.get(), CCBlocks.WAXED_WEATHERED_COPPER_CHAIN.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_CHAIN.get(), CCBlocks.WAXED_OXIDIZED_COPPER_CHAIN.get());
		builder.put(CCBlocks.COPPER_LANTERN.get(), CCBlocks.WAXED_COPPER_LANTERN.get());
		builder.put(CCBlocks.EXPOSED_COPPER_LANTERN.get(), CCBlocks.WAXED_EXPOSED_COPPER_LANTERN.get());
		builder.put(CCBlocks.WEATHERED_COPPER_LANTERN.get(), CCBlocks.WAXED_WEATHERED_COPPER_LANTERN.get());
		builder.put(CCBlocks.OXIDIZED_COPPER_LANTERN.get(), CCBlocks.WAXED_OXIDIZED_COPPER_LANTERN.get());
		HoneycombItem.WAXABLES = Suppliers.memoize(builder::build);
	}

	private static void registerFireworkIngredients() {
		FireworkStarRecipe.SHAPE_INGREDIENT = Ingredient.of(Stream.concat(Arrays.stream(FireworkStarRecipe.SHAPE_INGREDIENT.getItems()), Stream.of(new ItemStack(CCItems.DEEPER_HEAD.get()), new ItemStack(CCItems.EVENDEEPER_HEAD.get()), new ItemStack(CCItems.PEEPER_HEAD.get()), new ItemStack(CCItems.MIME_HEAD.get()))));
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.DEEPER_HEAD.get(), FireworkExplosion.Shape.CREEPER);
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.EVENDEEPER_HEAD.get(), FireworkExplosion.Shape.CREEPER);
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.PEEPER_HEAD.get(), FireworkExplosion.Shape.CREEPER);
		FireworkStarRecipe.SHAPE_BY_ITEM.put(CCItems.MIME_HEAD.get(), FireworkExplosion.Shape.CREEPER);
		FireworkStarRecipe.TRAIL_INGREDIENT = Ingredient.of(Stream.concat(Arrays.stream(FireworkStarRecipe.TRAIL_INGREDIENT.getItems()), Stream.of(new ItemStack(CCItems.ZIRCONIA.get()))));
	}

	private static void makeVillagersScaredOfRats() {
		ImmutableMap.Builder<EntityType<?>, Float> builder = ImmutableMap.builder();
		VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES.forEach(builder::put);
		builder.put(CCEntityTypes.RAT.get(), 5.0F);
		VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES = builder.build();
	}
}
