package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.events.FallingBlockEvent.FallingBlockTickEvent;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.common.block.BrazierBlock;
import com.teamabnormals.caverns_and_chasms.common.block.CoalBlock;
import com.teamabnormals.caverns_and_chasms.common.block.FlintBlock;
import com.teamabnormals.caverns_and_chasms.common.block.weathering.CCWeatheringCopper;
import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.FollowTuningForkGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Fly;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.MovingPlayer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.BluntArrow;
import com.teamabnormals.caverns_and_chasms.common.item.SanguineArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
import com.teamabnormals.caverns_and_chasms.common.item.TrailPotionItem;
import com.teamabnormals.caverns_and_chasms.common.item.copper.TuningForkItem;
import com.teamabnormals.caverns_and_chasms.common.item.copper.WeatheringCopperItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.FoilItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverItem;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.mixin.LivingEntityAccessor;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCDamageTypeTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof Zombie zombie) {
			zombie.goalSelector.addGoal(1, new AvoidEntityGoal<>(zombie, Fly.class, 9.0F, 1.05D, 1.05D));
		} else if (entity instanceof AbstractHorse horse && !(entity instanceof SkeletonHorse)) {
			horse.goalSelector.addGoal(1, new AvoidEntityGoal<>(horse, Fly.class, 8.0F, 1.1D, 1.1D));
		} else if (entity instanceof Spider spider) {
			spider.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(spider, Fly.class, false));
		} else if (entity instanceof IronGolem golem) {
			golem.goalSelector.addGoal(0, new FollowTuningForkGoal((ControllableGolem) golem, 0.9D));
			golem.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(golem, LivingEntity.class, 5, false, false, (target) -> {
				return ((ControllableGolem) golem).isTuningForkTarget(target);
			}));
		} else if (entity instanceof Cat cat) {
			cat.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(cat, Rat.class, false, target -> target instanceof Rat rat && !rat.isTame()));
		} else if (entity instanceof Ocelot ocelot) {
			ocelot.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(ocelot, Rat.class, false));
		} else if (entity instanceof WanderingTrader trader) {
			trader.goalSelector.addGoal(1, new AvoidEntityGoal<>(trader, Rat.class, 5.0F, 0.5D, 0.5D));
		}
	}

	//TODO: Make work properly with Creeper weights and stuff
	@SubscribeEvent
	public static void onLivingSpawn(MobSpawnEvent.FinalizeSpawn event) {
		LivingEntity entity = event.getEntity();
		LevelAccessor world = event.getLevel();
		boolean validSpawn = event.getSpawnType() == MobSpawnType.NATURAL || event.getSpawnType() == MobSpawnType.CHUNK_GENERATION;
		if (event.getResult() != Result.DENY) {
			if (validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < CCConfig.COMMON.deeperMaxSpawnHeight.get()) {
				Creeper creeper = (Creeper) entity;
				if (world.getBlockState(creeper.blockPosition().below()).is(CCBlockTags.DEEPER_SPAWNABLE_ON)) {
					Deeper deeper = CCEntityTypes.DEEPER.get().create((Level) world);
					if (deeper != null) {
						deeper.copyPosition(creeper);
						world.addFreshEntity(deeper);
					}
					event.setSpawnCancelled(true);
					event.setResult(Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof Player player && event.getPlacedBlock().is(CCBlocks.ROTTEN_FLESH_BLOCK.get())) {
			LevelAccessor level = event.getLevel();
			BlockPos pos = event.getPos();
			List<Rat> rats = level.getEntitiesOfClass(Rat.class, new AABB(pos).inflate(8.0D, 4.0D, 8.0D), entity -> entity.isAlive() && !entity.isTame());
			rats.stream().limit(9).toList().forEach(rat -> rat.setTamer(player));
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event) {
		Player player = event.getEntity();
		BlockPos pos = event.getPos();
		Level level = event.getLevel();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		BlockState state = level.getBlockState(pos);
		Direction face = event.getFace();
		RandomSource random = level.getRandom();

		if (state.getBlock() instanceof GrindstoneBlock && player.isSecondaryUseActive() && !event.isCanceled() && stack.getItem() instanceof CCWeatheringCopper) {
			if (WeatheringCopperItem.getUnwaxed(stack).isPresent()) {
				WeatheringCopperItem.copyStackToNewItem(stack, WeatheringCopperItem.getUnwaxed(stack).get());
				level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.levelEvent(player, 3004, pos, 0);

				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));

			} else if (WeatheringCopperItem.getPrevious(stack).isPresent()) {
				WeatheringCopperItem.copyStackToNewItem(stack, WeatheringCopperItem.getPrevious(stack).get());
				level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.levelEvent(player, 3005, pos, 0);

				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			}
		}

		placeableItems:
		if (stack.is(CCItemTags.PLACEABLE_ITEMS) && !event.isCanceled()) {
			boolean sneakBypassesUse = !player.getMainHandItem().doesSneakBypassUse(player.level(), pos, player) || !player.getOffhandItem().doesSneakBypassUse(player.level(), pos, player);
			boolean isSneaking = player.isSecondaryUseActive() && sneakBypassesUse;

			if (event.getUseBlock() == Result.ALLOW || (event.getUseBlock() != Result.DENY && !isSneaking)) {
				InteractionResult blockResult = state.use(level, player, event.getHand(), event.getHitVec());
				if (blockResult.consumesAction()) {
					event.setCanceled(true);
					event.setCancellationResult(blockResult);
					break placeableItems;
				}
			}

			UseOnContext context = new UseOnContext(level, player, event.getHand(), stack, event.getHitVec());
			Collection<RegistryObject<Item>> items = CavernsAndChasms.REGISTRY_HELPER.getItemSubHelper().getDeferredRegister().getEntries();
			for (RegistryObject<Item> reg : items) {
				if (reg.get() instanceof BlockItem blockItem && stack.is(blockItem.getBlock().asItem())) {
					InteractionResult itemResult = reg.get().useOn(context);
					if (itemResult.consumesAction()) {
						event.setCanceled(true);
						event.setCancellationResult(itemResult);
					}
					break;
				}
			}
		}

		boolean fireCharge = stack.getItem() instanceof FireChargeItem;
		boolean flintAndSteel = stack.getItem() instanceof FlintAndSteelItem;
		if ((fireCharge || flintAndSteel) && !event.isCanceled()) {
			boolean valid = state.getBlock() instanceof CoalBlock && !state.getValue(CoalBlock.LIT) && !state.getValue(CoalBlock.WATERLOGGED);
			if (valid) {
				BlockState returnState = state.setValue(CoalBlock.LIT, true);
				if (flintAndSteel) {
					level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
					level.setBlock(pos, returnState, 11);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(event.getHand()));
				} else {
					level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
					level.setBlockAndUpdate(pos, returnState);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					if (!player.getAbilities().instabuild)
						stack.shrink(1);
				}

				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
				event.setCanceled(true);
			}
		}

		if (state.getBlock() instanceof BrazierBlock && face == Direction.UP && !event.isCanceled()) {
			if (stack.canPerformAction(ToolActions.SHOVEL_FLATTEN) && state.getValue(BrazierBlock.LIT)) {
				BlockState extinguishedState = BrazierBlock.extinguish(player, level, pos, state);
				if (!level.isClientSide()) {
					level.levelEvent(null, 1009, pos, 0);
					level.setBlock(pos, extinguishedState, 11);
					stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(event.getHand()));
				}

				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			}

			if (BrazierBlock.canBeLit(state)) {
				if (stack.getItem() instanceof FlintAndSteelItem) {
					level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
					if (!level.isClientSide()) {
						level.setBlock(pos, state.setValue(BrazierBlock.LIT, true), 11);
						level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
						stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(event.getHand()));
					}

					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
				} else if (stack.getItem() instanceof FireChargeItem) {
					level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
					if (!level.isClientSide()) {
						level.setBlock(pos, state.setValue(BrazierBlock.LIT, true), 11);
						level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}

					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
				}
			}
		}

		if (state.getBlock() instanceof NoteBlock && item == CCItems.TUNING_FORK.get() && !event.isCanceled()) {
			CompoundTag tag = stack.getOrCreateTag();
			if (!player.isCrouching() && tag.contains("Note")) {
				int note = tag.getInt("Note");
				level.setBlockAndUpdate(pos, state.setValue(NoteBlock.NOTE, Mth.clamp(note, 0, 24)));
				player.displayClientMessage(Component.translatable(item.getDescriptionId() + ".change_note", Component.translatable(item.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
				if (!level.isClientSide() && level.getBlockState(pos.above()).isAir()) {
					level.blockEvent(pos, state.getBlock(), 0, 0);
					level.gameEvent(player, GameEvent.NOTE_BLOCK_PLAY, pos);
				}
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			}
		}


		if (CCConfig.COMMON.betterRailPlacement.get() && state.getBlock() instanceof BaseRailBlock && !event.isCanceled()) {
			if (!stack.is(CCItemTags.IGNORE_RAIL_PLACEMENT) && item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BaseRailBlock && !state.is(CCBlockTags.IGNORE_RAIL_PLACEMENT)) {
					Direction direction = player.getDirection();
					BlockPos.MutableBlockPos currentPos = event.getPos().mutable().move(direction);
					for (int i = 0; i < CCConfig.COMMON.betterRailPlacementRange.get(); i++) {
						BlockPos nextPos = null;
						boolean isNextRail = false;
						BlockPos.MutableBlockPos yCheckingPos = currentPos.mutable().move(Direction.DOWN);
						for (int j = 0; j < 3; j++) {
							BlockState stateAtCheckPos = level.getBlockState(yCheckingPos);
							if (stateAtCheckPos.getBlock() instanceof BaseRailBlock && !stateAtCheckPos.is(CCBlockTags.IGNORE_RAIL_PLACEMENT)) {
								nextPos = yCheckingPos.move(direction).immutable();
								isNextRail = true;
							} else if (!isNextRail) {
								BlockPlaceContext context = new BlockPlaceContext(player, event.getHand(), event.getItemStack(), event.getHitVec().withPosition(yCheckingPos));
								if (level.getBlockState(yCheckingPos).canBeReplaced(context)) {
									BlockState stateForPlacement = state.getBlock().getStateForPlacement(context);
									if (stateForPlacement != null && stateForPlacement.canSurvive(level, yCheckingPos))
										nextPos = yCheckingPos.immutable();
								}
							}
							yCheckingPos.move(Direction.UP);
						}
						if (!isNextRail) {
							if (nextPos != null) {
								BlockPlaceContext context = new BlockPlaceContext(player, event.getHand(), event.getItemStack(), event.getHitVec().withPosition(nextPos));
								((BlockItem) item).place(context);
								event.setCancellationResult(InteractionResult.SUCCESS);
								event.setCanceled(true);
							}
							break;
						} else {
							currentPos.set(nextPos);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		Level level = entity.getCommandSenderWorld();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = entity.getItemBySlot(slot);
			if (stack.getItem() instanceof WeatheringCopperItem item) {
				boolean armor = slot.isArmor() && item instanceof ArmorItem;
				boolean tool = !slot.isArmor() && item instanceof TieredItem;
				if (armor || tool) {
					item.updateOxidation(stack, level);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLightningStrike(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof LivingEntity entity && !entity.level().isClientSide()) {
			ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
			if (helmet.getItem() instanceof WeatheringCopperItem) {
				WeatheringCopperItem.copyStackToNewItem(helmet, WeatheringCopperItem.getFirst(helmet));
			}

			Level level = entity.level();
			RandomSource random = level.getRandom();
			int k = random.nextInt(3) + 1;
			for (int z = 0; z < k; z++) {
				EquipmentSlot slot = EquipmentSlot.values()[random.nextInt(EquipmentSlot.values().length)];
				ItemStack stack = entity.getItemBySlot(slot);
				if (stack.getItem() instanceof WeatheringCopperItem) {
					Optional<ItemStack> previous = WeatheringCopperItem.getPrevious(stack);
					previous.ifPresent(itemStack -> WeatheringCopperItem.copyStackToNewItem(stack, itemStack));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBreakSpeed(BreakSpeed event) {
		if (event.getState().is(Blocks.BUDDING_AMETHYST)) {
			event.setNewSpeed(event.getOriginalSpeed() / 8.0F);
		}
	}

	@SubscribeEvent
	public static void bonusXPBlock(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
		Collection<AttributeModifier> experienceModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.EXPERIENCE_BOOST.get());
		if (!experienceModifiers.isEmpty()) {
			float experienceBoost = event.getExpToDrop() * (float) experienceModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
			int base = Mth.floor(experienceBoost);
			float bonus = Mth.frac(experienceBoost);
			if (bonus != 0.0F && Math.random() < bonus) {
				++base;
			}

			event.setExpToDrop(event.getExpToDrop() + base);
		}
	}

	@SubscribeEvent
	public static void bonusXPMobs(LivingExperienceDropEvent event) {
		Player player = event.getAttackingPlayer();
		if (player != null) {
			ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
			Collection<AttributeModifier> experienceModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.EXPERIENCE_BOOST.get());
			if (!experienceModifiers.isEmpty()) {
				float experienceBoost = (float) (event.getDroppedExperience() * experienceModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum());
				int base = Mth.floor(experienceBoost);
				float bonus = Mth.frac(experienceBoost);
				if (bonus != 0.0F && Math.random() < bonus) {
					++base;
				}

				event.setDroppedExperience(event.getDroppedExperience() + base);
			}
		}
	}

	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getSlot() == EquipmentSlot.HEAD) {
			ItemStack stack = event.getFrom();
			if (stack.getItem() == CCItems.TETHER_POTION.get()) {
				TetherPotionItem.updateTetherPotionEffects(event.getEntity(), stack, false);
				stack.getOrCreateTag().putInt("cooldown", 600);
			}
		}
	}

	@SubscribeEvent
	public static void potionAddedEvent(MobEffectEvent.Added event) {
		LivingEntity entity = event.getEntity();
		if (event.getEffectInstance().getEffect() == CCMobEffects.REWIND.get() && !entity.hasEffect(CCMobEffects.REWIND.get())) {
			IDataManager data = ((IDataManager) entity);
			data.setValue(CCDataProcessors.REWIND_DIMENSION, entity.getCommandSenderWorld().dimension().location());
			data.setValue(CCDataProcessors.REWIND_X, entity.getX());
			data.setValue(CCDataProcessors.REWIND_Y, entity.getY());
			data.setValue(CCDataProcessors.REWIND_Z, entity.getZ());
		}
	}

	@SubscribeEvent
	public static void potionRemoveEvent(MobEffectEvent.Remove event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntity();
			if (effect == CCMobEffects.REWIND.get())
				rewindTeleport(entity);
		}
	}

	@SubscribeEvent
	public static void potionExpireEvent(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntity();
			if (effect == CCMobEffects.REWIND.get())
				rewindTeleport(entity);
		}
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		LivingEntity target = event.getEntity();
		DamageSource source = event.getSource();

		if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
			float magicProtection = 0.0F;
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack stack = target.getItemBySlot(slot);
					Collection<AttributeModifier> magicProt = stack.getAttributeModifiers(slot).get(CCAttributes.MAGIC_PROTECTION.get());
					if (!magicProt.isEmpty()) {
						magicProtection += magicProt.stream().mapToDouble(AttributeModifier::getAmount).sum();
					}
				}
			}

			if (magicProtection > 0.0F) {
				event.setAmount(event.getAmount() - event.getAmount() * magicProtection);
				SilverItem.causeMagicProtectionParticles(target);
			}
		}

		if (source.getEntity() instanceof LivingEntity attacker) {
			ItemStack mainHandItem = attacker.getMainHandItem();

			if (TuningForkItem.isTuningForkWithNote(mainHandItem)) {
				int note = mainHandItem.getTag().getInt("Note");

				TuningForkItem.playNote(target.level(), attacker, target.getX(), target.getEyeY(), target.getZ(), note);
				NetworkUtil.spawnParticle("minecraft:note", target.getX(), target.getEyeY(), target.getZ(), (double) note / 24.0D, 0.0D, 0.0D);

				if (attacker instanceof Player player) {
					player.displayClientMessage(Component.translatable(CCItems.TUNING_FORK.get().getDescriptionId() + ".note").append(": ").append(Component.translatable(CCItems.TUNING_FORK.get().getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
					TuningForkItem.orderGolemToAttackEntity(target, player);
				}
			}

			float slownessInfliction = 0.0F;
			float lifeStealAmount = 0.0F;

			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack stack = target.getItemBySlot(slot);

					Collection<AttributeModifier> slownessModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.SLOWNESS_INFLICTION.get());
					if (!slownessModifiers.isEmpty()) {
						slownessInfliction += slownessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
					}

					Collection<AttributeModifier> lifeStealModifiers = attacker.getItemBySlot(slot).getAttributeModifiers(slot).get(CCAttributes.LIFESTEAL.get());
					if (!lifeStealModifiers.isEmpty() && (target instanceof Enemy || target instanceof Player)) {
						lifeStealAmount += lifeStealModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
					}
				}
			}

			if (lifeStealAmount > 0.0F) {
				attacker.heal(lifeStealAmount * event.getAmount());
				SanguineArmorItem.causeHealParticles(attacker, lifeStealAmount);
			}

			if (slownessInfliction > 0.0F) {
				attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (60 * slownessInfliction), (int) slownessInfliction / 2 - 1));
			}
		}

		if (source.getDirectEntity() instanceof BluntArrow || source.getEntity() instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof FoilItem) {
			if (source.getDirectEntity() instanceof BluntArrow) {
				event.setAmount(0.0F);
			}

			if (source.getEntity() instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof FoilItem) {
				event.setAmount(0.0F);
				if (target.isPassenger() && target.level().getRandom().nextInt(3) == 0 && target.getVehicle() != null) {
					float rot = living.getVisualRotationYInDegrees();
					float x = Mth.sin(rot * Mth.DEG_TO_RAD);
					float z = -Mth.cos(rot * Mth.DEG_TO_RAD);

					target.removeVehicle();
					Vec3 vec3 = (new Vec3(x, 0.0D, z)).scale(-0.2F);
					target.push(vec3.x, 0.8D, vec3.z);
					target.hurtMarked = true;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		LivingEntity target = event.getEntity();
		DamageSource source = event.getSource();
		Level level = target.level();
		ItemStack headstack = target.getItemBySlot(EquipmentSlot.HEAD);

		if (headstack.getItem() instanceof TetherPotionItem && !source.is(DamageTypeTags.BYPASSES_ARMOR) && !source.is(CCDamageTypeTags.BYPASSES_TETHER_POTIONS)) {
			Player player = target instanceof Player ? (Player) target : null;
			target.broadcastBreakEvent(EquipmentSlot.HEAD);
			target.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);

			if (headstack.getItem() == CCItems.IMPACT_POTION.get()) {
				for (MobEffectInstance instance : PotionUtils.getMobEffects(headstack)) {
					if (instance.getEffect().isInstantenous()) {
						instance.getEffect().applyInstantenousEffect(player, player, target, instance.getAmplifier(), 1.0D);
					} else {
						target.addEffect(new MobEffectInstance(instance));
					}
				}
			} else if (headstack.getItem() == CCItems.TRAIL_POTION.get()) {
				TrailPotionItem.makeAreaOfEffectCloud(headstack, PotionUtils.getPotion(headstack), target, level, true);
			} else {
				for (MobEffectInstance instance : PotionUtils.getMobEffects(headstack)) {
					if (instance.getEffect().isInstantenous()) {
						instance.getEffect().applyInstantenousEffect(player, player, target, instance.getAmplifier(), 1.0D);
					}
				}
			}

			int i = PotionUtils.getPotion(headstack).hasInstantEffects() ? 2007 : 2002;
			level.levelEvent(i, BlockPos.containing(target.getEyePosition(1.0F)), PotionUtils.getColor(headstack));
		}
	}

	@SubscribeEvent
	public static void visibilityEvent(LivingVisibilityEvent event) {
		LivingEntity entity = event.getEntity();
		EntityType<?> looking = event.getLookingEntity().getType();
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (
				(looking == CCEntityTypes.DEEPER.get() && stack.is(CCItems.DEEPER_HEAD.get())) ||
						(looking == CCEntityTypes.PEEPER.get() && stack.is(CCItems.PEEPER_HEAD.get())) ||
						(looking == CCEntityTypes.MIME.get() && stack.is(CCItems.MIME_HEAD.get()))
		) {
			event.modifyVisibility(0.5F);
		}
	}

	@SubscribeEvent
	public static void onProjectileImpact(ProjectileImpactEvent event) {
		Level level = event.getEntity().level();
		RandomSource random = level.getRandom();
		Projectile projectile = event.getProjectile();
		IDataManager data = (IDataManager) projectile;
		HitResult hitResult = event.getRayTraceResult();
		Vec3 movement = projectile.getDeltaMovement();

		if (hitResult.getType() == HitResult.Type.BLOCK && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
			BlockHitResult blockHitResult = (BlockHitResult) hitResult;
			BlockPos pos = blockHitResult.getBlockPos();
			BlockState state = level.getBlockState(pos);
			Direction direction = blockHitResult.getDirection();

			boolean flag = state.is(CCBlockTags.DEFLECTS_PROJECTILES);

			if (!flag) {
				BlockPos blockpos1 = pos.relative(direction.getOpposite());
				BlockState blockstate1 = level.getBlockState(blockpos1);
				if (blockstate1.is(CCBlockTags.DEFLECTS_PROJECTILES) && blockstate1.isFaceSturdy(level, blockpos1, direction)) {
					flag = true;
					pos = blockpos1;
					state = blockstate1;
				}
			}

			if (!flag) {
				BlockPos blockpos1 = pos.relative(direction);
				BlockState blockstate1 = level.getBlockState(blockpos1);
				if (blockstate1.is(CCBlockTags.DEFLECTS_PROJECTILES) && blockstate1.getCollisionShape(level, blockpos1, CollisionContext.of(projectile)).isEmpty() && blockstate1.getShape(level, blockpos1).bounds().inflate(1.0E-7D).move(blockpos1).contains(blockHitResult.getLocation())) {
					flag = true;
					pos = blockpos1;
					state = blockstate1;
				}
			}

			if (flag) {
				double speed = movement.lengthSqr();
				if (direction != Direction.UP || speed > 0.04D) {
					Vec3 location = hitResult.getLocation();
					Axis axis = direction.getAxis();
					int i = blockHitResult.getDirection().getAxisDirection().getStep();

					double j = 0.65D;
					double k = 0.75D;

					if (state.is(CCBlockTags.MAINTAINS_DEFLECT_VELOCITY)) {
						j = 0.9D;
						k = 0.9D;
					}

					if (state.is(CCBlockTags.WEAKER_DEFLECT_VELOCITY)) {
						j -= 0.25D;
						k -= 0.25D;
					}

					if (state.getBlock() instanceof TargetBlock targetBlock) {
						targetBlock.onProjectileHit(level, state, blockHitResult.withPosition(pos), projectile);
					}

					if (axis == Axis.X) {
						data.setValue(CCDataProcessors.DEFLECT_X, -movement.x * j);
						data.setValue(CCDataProcessors.DEFLECT_Y, movement.y * k);
						data.setValue(CCDataProcessors.DEFLECT_Z, movement.z * k);
						projectile.setPos(location.x + 0.01D * i, location.y, location.z);
					} else if (axis == Axis.Y) {
						data.setValue(CCDataProcessors.DEFLECT_X, movement.x * k);
						data.setValue(CCDataProcessors.DEFLECT_Y, -movement.y * j);
						data.setValue(CCDataProcessors.DEFLECT_Z, movement.z * k);
						projectile.setPos(location.x, i == 1 ? location.y : location.y - 0.01D, location.z);
					} else if (axis == Axis.Z) {
						data.setValue(CCDataProcessors.DEFLECT_X, movement.x * k);
						data.setValue(CCDataProcessors.DEFLECT_Y, movement.y * k);
						data.setValue(CCDataProcessors.DEFLECT_Z, -movement.z * j);
						projectile.setPos(location.x, location.y, location.z + 0.01D * i);
					}
					data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
					projectile.setDeltaMovement(Vec3.ZERO);
					projectile.checkInsideBlocks();

					SoundType soundtype = state.getBlock().getSoundType(state, level, pos, null);
					SoundEvent soundevent = soundtype == CCSoundTypes.STORAGE_DUCT ? CCSoundEvents.STORAGE_DUCT_DEFLECT.get() : soundtype == CCSoundTypes.TIN_ORE ? CCSoundEvents.TIN_ORE_DEFLECT.get() : soundtype == CCSoundTypes.DEEPSLATE_TIN_ORE ? CCSoundEvents.DEEPSLATE_TIN_ORE_DEFLECT.get() : CCSoundEvents.TIN_DEFLECT.get();
					float pitchmultiplier = soundtype == CCSoundTypes.STORAGE_DUCT ? 0.5F : 1.0F;

					playTinDeflectEffects(level, location, movement.reverse().normalize(), speed, soundevent, pitchmultiplier, random);

					for (int l = 0; l < 3; ++l) {
						Vec3 vec3 = movement.reverse().normalize();
						double d1 = vec3.x * 0.2D + random.nextGaussian() * 0.05D;
						double d2 = vec3.y * 0.2D + random.nextGaussian() * 0.05D;
						double d3 = vec3.z * 0.2D + random.nextGaussian() * 0.05D;
						level.addParticle(CCParticleTypes.SPARK.get(), location.x, location.y, location.z, d1, d2, d3);
					}

					event.setCanceled(true);
				}
			}
		} else if (hitResult.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHitResult = (EntityHitResult) hitResult;
			if (entityHitResult.getEntity() instanceof GrazerPart grazerpart && grazerpart.deflectsAttacks()) {
				AbstractGrazer grazer = grazerpart.getParent();

				if (!grazer.projectileJustDeflected(projectile)) {
					AABB aabb = grazerpart.getBoundingBox().inflate(0.3D);
					Vec3 location = aabb.clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement())).or(() -> aabb.clip(projectile.position(), new Vec3(grazerpart.getX(), grazerpart.getY(0.5D), grazerpart.getZ()))).orElse(projectile.position());
					Vec3 normal = grazer.calculateDeflectionNormal(location);
					Vec3 reflect = movement.subtract(normal.scale(movement.dot(normal) * 2.0D));

					data.setValue(CCDataProcessors.DEFLECT_X, reflect.x * 0.65D);
					data.setValue(CCDataProcessors.DEFLECT_Y, reflect.y * 0.65D);
					data.setValue(CCDataProcessors.DEFLECT_Z, reflect.z * 0.65D);
					projectile.setPos(location.x + normal.x * 0.01D, location.y + normal.y * 0.01D, location.z + normal.z * 0.01D);

					data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
					projectile.setDeltaMovement(Vec3.ZERO);
					projectile.checkInsideBlocks();

					CCEvents.playTinDeflectEffects(level, location, reflect.reverse().normalize(), movement.lengthSqr(), random);
				}

				grazer.addDeflectedProjectile(projectile);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onItemModify(ItemAttributeModifierEvent event) {
		ItemStack stack = event.getItemStack();
		EquipmentSlot slot = event.getSlotType();

		ArmorItem.Type type = switch (slot) {
			case HEAD -> Type.HELMET;
			case CHEST -> Type.CHESTPLATE;
			case LEGS -> Type.LEGGINGS;
			case FEET -> Type.BOOTS;
			case MAINHAND, OFFHAND -> null;
		};

		if (type != null) {
			UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(type);

			if (CCConfig.COMMON.chainmailArmorIncreasesDamage.get() && (stack.is(Items.CHAINMAIL_HELMET) && slot == EquipmentSlot.HEAD || stack.is(Items.CHAINMAIL_BOOTS) && slot == EquipmentSlot.FEET || stack.is(Items.CHAINMAIL_CHESTPLATE) && slot == EquipmentSlot.CHEST || stack.is(Items.CHAINMAIL_LEGGINGS) && slot == EquipmentSlot.LEGS)) {
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 1.0D, AttributeModifier.Operation.ADDITION));
			}

			if (CCConfig.COMMON.goldenArmorIncreasesSpeed.get() && (stack.is(Items.GOLDEN_HELMET) && slot == EquipmentSlot.HEAD || stack.is(Items.GOLDEN_BOOTS) && slot == EquipmentSlot.FEET || (stack.is(Items.GOLDEN_CHESTPLATE) || stack.is(Items.GOLDEN_HORSE_ARMOR)) && slot == EquipmentSlot.CHEST || stack.is(Items.GOLDEN_LEGGINGS) && slot == EquipmentSlot.LEGS)) {
				event.addModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Speed boost", 0.1D, AttributeModifier.Operation.MULTIPLY_BASE));
			}
		}

		if (slot == EquipmentSlot.MAINHAND) {
			if (stack.is(CCItemTags.EXPERIENCE_BOOST_ITEMS)) {
				event.addModifier(CCAttributes.EXPERIENCE_BOOST.get(), new AttributeModifier(UUID.fromString("1B1C193D-1484-4CB9-8DC7-FE226C77657A"), "Exerience boost", 0.75D, AttributeModifier.Operation.MULTIPLY_BASE));
			}

			if (stack.is(CCItemTags.MAGIC_DAMAGE_ITEMS)) {
				float damage = stack.getItem() instanceof AxeItem || stack.getItem() instanceof SwordItem ? 4.0F : 2.0F;
				event.addModifier(CCAttributes.MAGIC_DAMAGE.get(), new AttributeModifier(UUID.fromString("b3406524-886c-49c3-94e6-88edd0e8e63b"), "Magic damage", damage, AttributeModifier.Operation.ADDITION));
			}

			if (stack.is(CCItemTags.SLOWNESS_INFLICTING_ITEMS)) {
				event.addModifier(CCAttributes.SLOWNESS_INFLICTION.get(), new AttributeModifier(UUID.fromString("47b62d26-2010-4a6a-9f87-ebe11c50f467"), "Slowness infliction", 1.0F, AttributeModifier.Operation.ADDITION));
			}
		}
	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		Level level = entity.level();

		if (entity instanceof Player player) {
			IDataManager data = (IDataManager) entity;
			if (data.getValue(CCDataProcessors.CONTROLLED_GOLEM_UUID).isPresent()) {
				ControllableGolem golem = TuningForkItem.findControlledGolem(player);
				if (golem != null) {
					int forgettime = TuningForkItem.getForgetGolemTime(player);
					if (forgettime > 0) {
						if (!TuningForkItem.isTuningForkWithNote(player.getMainHandItem()) && !TuningForkItem.isTuningForkWithNote(player.getOffhandItem())) TuningForkItem.setForgetGolemTime(player, forgettime - 1);
					} else {
						TuningForkItem.setControlledGolem(player, null);
					}
				} else {
					TuningForkItem.setControlledGolem(player, null);
				}
			}
		} else if (entity instanceof ControllableGolem golem) {
			boolean controlled = golem.getTuningForkController() != null;
			golem.setBeingTuningForkControlled(controlled);
			if (controlled) {
				golem.tungingForkControlTick();
			} else {
				golem.setTuningForkPos(null);
				golem.setTuningForkTarget(null);
			}
		}

		ItemStack headstack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (!level.isClientSide() && headstack.getItem() == CCItems.TETHER_POTION.get()) {
			TetherPotionItem.updateTetherPotionEffects(entity, headstack, true);

			for (MobEffectInstance instance : PotionUtils.getMobEffects(headstack)) {
				if (instance.getEffect().isInstantenous()) {
					if (headstack.getTag().getInt("cooldown") > 0) {
						headstack.getTag().putInt("cooldown", headstack.getTag().getInt("cooldown") - 1);
					} else {
						instance.getEffect().applyInstantenousEffect(entity, entity, entity, instance.getAmplifier(), 1.0D);
						TetherPotionItem.instantEffectParticlesAndSound(level, BlockPos.containing(entity.getEyePosition(1.0F)), PotionUtils.getColor(headstack));
						headstack.getTag().putInt("cooldown", 600);
					}
				}
			}
		}
		if (!level.isClientSide && headstack.getItem() == CCItems.TRAIL_POTION.get()) {
			MovingPlayer player = entity instanceof MovingPlayer ? (MovingPlayer) entity : null;
			boolean moving = player == null && (!entity.getDeltaMovement().equals(new Vec3(0, -0.0784000015258789, 0)) && !entity.getDeltaMovement().equals(Vec3.ZERO));
			if (entity.tickCount % 5 == 0) {
				if (player != null && player.isMoving() || moving) {
					TrailPotionItem.makeAreaOfEffectCloud(headstack, PotionUtils.getPotion(headstack), entity, level, false);
				}
			}
		}

		ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (headStack.is(CCItems.COWL.get()) && headStack.getEnchantmentLevel(CCEnchantments.OBSCURITY.get()) > 0) {
			entity.setInvisible(entity.isCrouching());
			if (!entity.isCrouching() && entity instanceof LivingEntityAccessor accessor) {
				accessor.invokeUpdateInvisibilityStatus();
			}
		}
	}

	@SubscribeEvent
	public static void onFallingBlockTick(FallingBlockTickEvent event) {
		FallingBlockEntity entity = event.getEntity();
		Level level = entity.level();
		if (!level.isClientSide) {
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				BlockPos pos = entity.blockPosition().relative(dir);
				if (level.getBlockState(pos).is(CCBlocks.FLINT_BLOCK.get())) {
					FlintBlock.spark(level, pos, true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onAnvilRepair(AnvilRepairEvent event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (event.getRight().is(CCItems.ZIRCONIA.get()))
			player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), CCSoundEvents.ZIRCONIA_ANVIL_USE.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F, false);
	}

	public static void playTinDeflectEffects(Level level, Vec3 location, Vec3 normal, double speed, SoundEvent soundEvent, float pitchMultiplier, RandomSource random) {
		playTinDeflectSound(level, location, speed, soundEvent, pitchMultiplier);

		for (int i = 0; i < 3; ++i) {
			;
			double d1 = normal.x * 0.2D + random.nextGaussian() * 0.05D;
			double d2 = normal.y * 0.2D + random.nextGaussian() * 0.05D;
			double d3 = normal.z * 0.2D + random.nextGaussian() * 0.05D;
			level.addParticle(CCParticleTypes.SPARK.get(), location.x, location.y, location.z, d1, d2, d3);
		}
	}

	public static void playTinDeflectEffects(Level level, Vec3 location, Vec3 normal, double speed, RandomSource random) {
		playTinDeflectEffects(level, location, normal, speed, CCSoundEvents.TIN_DEFLECT.get(), 1.0F, random);
	}

	public static void playTinDeflectSound(Level level, Vec3 location, double speed, SoundEvent soundEvent, float pitchMultiplier) {
		level.playSound(null, location.x, location.y, location.z, soundEvent, SoundSource.BLOCKS, Math.min((float) speed * 0.7F + 0.2F, 1.0F), Math.min(0.5F + (float) speed * 0.8F * pitchMultiplier, 1.8F));
	}

	public static void playTinDeflectSound(Level level, Vec3 location, double speed) {
		playTinDeflectSound(level, location, speed, CCSoundEvents.TIN_DEFLECT.get(), 1.0F);
	}

	private static void rewindTeleport(LivingEntity entity) {
		IDataManager data = ((IDataManager) entity);
		ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, data.getValue(CCDataProcessors.REWIND_DIMENSION));
		ServerLevel level = entity.getServer().getLevel(key);

		if (level != entity.getCommandSenderWorld()) {
			entity.changeDimension(level, new ITeleporter() {
				@Override
				public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
					return repositionEntity.apply(false);
				}
			});
		}

		double x = data.getValue(CCDataProcessors.REWIND_X);
		double y = data.getValue(CCDataProcessors.REWIND_Y);
		double z = data.getValue(CCDataProcessors.REWIND_Z);

		if (entity.isPassenger())
			entity.dismountTo(x, y, z);
		else
			entity.teleportTo(x, y, z);

		entity.resetFallDistance();
		entity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
	}
}