package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.blueprint.common.dispenser.FishBucketDispenseItemBehavior;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.item.PrimedTmt;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.Map;
import java.util.function.Predicate;

public class CCCompat {

	public static void registerCompat() {
		registerFlammables();
		registerDispenserBehaviors();
		changeLocalization();
		setFireproof();
		addWaxables();
		registerCauldronInteractions();
	}

	private static void registerFlammables() {
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
		DataUtil.registerFlammable(CCBlocks.AZALEA_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_BEEHIVE.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_POST.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.STRIPPED_AZALEA_POST.get(), 5, 20);
		DataUtil.registerFlammable(CCBlocks.AZALEA_HEDGE.get(), 5, 20);
	}

	private static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseItemBehavior());
		DispenserBlock.registerBehavior(CCItems.KUNAI.get(), new AbstractProjectileDispenseBehavior() {
			protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
				Kunai entity = new Kunai(worldIn, position.x(), position.y(), position.z());
				entity.pickup = AbstractArrow.Pickup.ALLOWED;
				return entity;
			}
		});

		DispenserBlock.registerBehavior(CCBlocks.TMT.get(), new DefaultDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level level = source.getLevel();
				BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
				PrimedTmt primedtmt = new PrimedTmt(level, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, null);
				level.addFreshEntity(primedtmt);
				level.playSound(null, primedtmt.getX(), primedtmt.getY(), primedtmt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
				stack.shrink(1);
				return stack;
			}
		});

		DispenserBlock.registerBehavior(CCItems.GOLDEN_BUCKET.get(), new DefaultDispenseItemBehavior() {
			private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

			public ItemStack execute(BlockSource source, ItemStack stack) {
				LevelAccessor level = source.getLevel();
				BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
				BlockState state = level.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BucketPickup bucketPickup) {
					ItemStack itemstack = GoldenBucketItem.getFilledBucket(state);
					bucketPickup.pickupBlock(level, pos, state);
					if (itemstack == null) {
						return super.execute(source, stack);
					} else {
						level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
						Item item = itemstack.getItem();
						stack.shrink(1);
						if (stack.isEmpty()) {
							return new ItemStack(item);
						} else {
							if (source.<DispenserBlockEntity>getEntity().addItem(new ItemStack(item)) < 0) {
								this.defaultDispenseItemBehavior.dispense(source, new ItemStack(item));
							}

							return stack;
						}
					}
				} else {
					return super.execute(source, stack);
				}
			}
		});

		DispenseItemBehavior goldenBucketDispenseBehavior = new DefaultDispenseItemBehavior() {
			private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

			public ItemStack execute(BlockSource source, ItemStack stack) {
				DispensibleContainerItem dispensibleItem = (DispensibleContainerItem) stack.getItem();
				BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
				Level level = source.getLevel();
				BlockState state = level.getBlockState(pos);
				ItemStack filled = GoldenBucketItem.getFilledBucket(state);
				if (state.getBlock() instanceof BucketPickup bucketPickup && filled != null && stack.is(filled.getItem()) && stack.getOrCreateTag().getInt("FluidLevel") < 3) {
					bucketPickup.pickupBlock(level, pos, state);
					level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
					ItemStack returnItem = stack.copy();
					GoldenBucketItem.increaseFluidLevel(returnItem);
					stack.shrink(1);
					if (stack.isEmpty()) {
						return returnItem;
					} else {
						if (source.<DispenserBlockEntity>getEntity().addItem(returnItem) < 0) {
							this.defaultDispenseItemBehavior.dispense(source, returnItem);
						}
						return stack;
					}
				} else if (dispensibleItem.emptyContents(null, level, pos, null)) {
					dispensibleItem.checkExtraContent(null, level, stack, pos);
					return GoldenBucketItem.getEmptySuccessItem(stack, null);
				} else {
					return this.defaultDispenseItemBehavior.dispense(source, stack);
				}
			}
		};

		DispenserBlock.registerBehavior(CCItems.GOLDEN_LAVA_BUCKET.get(), goldenBucketDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.GOLDEN_WATER_BUCKET.get(), goldenBucketDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), goldenBucketDispenseBehavior);

		DefaultDispenseItemBehavior horseArmorDispenseBehavior = new OptionalDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

				for(AbstractHorse abstracthorse : source.getLevel().getEntitiesOfClass(AbstractHorse.class, new AABB(blockpos), (p_123533_) -> p_123533_.isAlive() && p_123533_.canWearArmor())) {
					if (abstracthorse.isArmor(stack) && !abstracthorse.isWearingArmor() && abstracthorse.isTamed()) {
						abstracthorse.getSlot(401).set(stack.split(1));
						this.setSuccess(true);
						return stack;
					}
				}

				return super.execute(source, stack);
			}
		};

		DispenserBlock.registerBehavior(CCItems.SILVER_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.NETHERITE_HORSE_ARMOR.get(), horseArmorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.NECROMIUM_HORSE_ARMOR.get(), horseArmorDispenseBehavior);

		DispenseItemBehavior armorDispenseBehavior = new OptionalDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				this.setSuccess(ArmorItem.dispenseArmor(source, stack));
				return stack;
			}
		};

		DispenserBlock.registerBehavior(CCItems.MIME_HEAD.get(), armorDispenseBehavior);
		DispenserBlock.registerBehavior(CCItems.TETHER_POTION.get(), armorDispenseBehavior);
	}

	private static void changeLocalization() {
		DataUtil.changeItemLocalization(Items.NETHERITE_SCRAP, CavernsAndChasms.MOD_ID, "ancient_scrap");
	}

	private static void setFireproof() {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, CCBlocks.NECROMIUM_BLOCK.get().asItem(), true, "f_41372_");
	}

	private static void addWaxables() {
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
		HoneycombItem.WAXABLES = Suppliers.memoize(builder::build);
	}

	private static void registerCauldronInteractions() {
		addFillBucketInteractions();
		addDefaultInteractions(CauldronInteraction.EMPTY);
		addDefaultInteractions(CauldronInteraction.WATER);
		addDefaultInteractions(CauldronInteraction.LAVA);
		addDefaultInteractions(CauldronInteraction.POWDER_SNOW);
//		addDefaultInteractions(CauldronInteraction.MILK);
	}

	private static void addDefaultInteractions(Map<Item, CauldronInteraction> map) {
		map.put(CCItems.GOLDEN_LAVA_BUCKET.get(), FILL_LAVA);
		map.put(CCItems.GOLDEN_WATER_BUCKET.get(), FILL_WATER);
		map.put(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), FILL_POWDER_SNOW);
//		map.put(CCItems.GOLDEN_MILK_BUCKET.get(), FILL_MILK);
	}

	private static void addFillBucketInteractions() {
		CauldronInteraction.WATER.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_WATER_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		CauldronInteraction.LAVA.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_LAVA_BUCKET.get()), blockState -> true, SoundEvents.BUCKET_FILL_LAVA));
		CauldronInteraction.POWDER_SNOW.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));
//		CauldronInteraction.MILK.put(CCItems.GOLDEN_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillBucket(state, level, pos, player, hand, stack, new ItemStack(CCItems.GOLDEN_MILK_BUCKET.get()), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_MILK));

		CauldronInteraction.WATER.put(CCItems.GOLDEN_MILK_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
		CauldronInteraction.LAVA.put(CCItems.GOLDEN_LAVA_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> true, SoundEvents.BUCKET_FILL_LAVA));
		CauldronInteraction.POWDER_SNOW.put(CCItems.GOLDEN_POWDER_SNOW_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));
//		CauldronInteraction.MILK.put(CCItems.GOLDEN_MILK_BUCKET.get(), (state, level, pos, player, hand, stack) -> fillFilledBucket(state, level, pos, player, hand, stack, blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_MILK));
	}

	public static CauldronInteraction FILL_WATER = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY);
	public static CauldronInteraction FILL_LAVA = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.LAVA_CAULDRON.defaultBlockState(), blockState -> true, SoundEvents.BUCKET_EMPTY_LAVA);
	public static CauldronInteraction FILL_POWDER_SNOW = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
//	public static CauldronInteraction FILL_MILK = (state, level, pos, player, hand, stack) -> emptyBucket(level, pos, player, hand, stack, Blocks.MILK_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_EMPTY_MILK);

	public static InteractionResult fillBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, ItemStack output, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (!statePredicate.test(state)) {
			return InteractionResult.PASS;
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, output));
				player.awardStat(Stats.USE_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static InteractionResult fillFilledBucket(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (stack.getOrCreateTag().getInt("FluidLevel") == 3 || !statePredicate.test(state)) {
			return InteractionResult.PASS;
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, GoldenBucketItem.increaseFluidLevel(stack));
				player.awardStat(Stats.USE_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public static InteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, Predicate<BlockState> statePredicate, SoundEvent soundEvent) {
		if (state.equals(level.getBlockState(pos)) && stack.getOrCreateTag().getInt("FluidLevel") != 3 && statePredicate.test(state)) {
			return fillFilledBucket(state, level, pos, player, hand, stack, statePredicate, soundEvent);
		} else {
			if (!level.isClientSide) {
				Item item = stack.getItem();
				player.setItemInHand(hand, GoldenBucketItem.getEmptySuccessItem(stack, player));
				player.awardStat(Stats.FILL_CAULDRON);
				player.awardStat(Stats.ITEM_USED.get(item));
				level.setBlockAndUpdate(pos, state);
				level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}
}
