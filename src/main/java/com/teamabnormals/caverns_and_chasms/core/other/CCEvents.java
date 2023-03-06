package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.other.tags.BlueprintEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.common.block.BrazierBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.FollowTuningForkGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Fly;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Spiderling;
import com.teamabnormals.caverns_and_chasms.common.item.NetheriteHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.SanguineArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.TetherPotionItem;
import com.teamabnormals.caverns_and_chasms.common.item.TuningForkItem;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverItem;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
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
			if (!CCConfig.COMMON.creeperExplosionNerf.get()) {
				golem.targetSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(goal -> {
					golem.targetSelector.removeGoal(goal);
					golem.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(golem, Mob.class, 5, false, false, (mob) -> mob instanceof Enemy));
				});
			}
			golem.goalSelector.addGoal(0, new FollowTuningForkGoal((ControllableGolem) golem, 0.9D));
			golem.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(golem, LivingEntity.class, 5, false, false, (target) -> {
				return ((ControllableGolem) golem).isTuningForkTarget(target);
			}));
		} else if (entity instanceof Cat cat) {
			cat.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(cat, Rat.class, false, null));
		} else if (entity instanceof Ocelot ocelot) {
			ocelot.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(ocelot, Rat.class, false));
		}
	}

	@SubscribeEvent
	public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());
		Level level = event.getLevel();
		InteractionHand hand = event.getHand();
		if (event.getTarget() instanceof LivingEntity entity && entity.getType().is(BlueprintEntityTypeTags.MILKABLE)) {
			if (!entity.isBaby() && (stack.getItem() == CCItems.GOLDEN_MILK_BUCKET.get() || stack.getItem() == CCItems.GOLDEN_BUCKET.get())) {
				CompoundTag tag = stack.getOrCreateTag();
				ItemStack milkBucket = ItemUtils.createFilledResult(stack.copy(), player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
				boolean fullBucket = false;
				if (stack.getItem() == CCItems.GOLDEN_MILK_BUCKET.get()) {
					fullBucket = tag.getInt("FluidLevel") >= 2;
					if (!fullBucket && !player.isCreative()) {
						milkBucket.getOrCreateTag().putInt("FluidLevel", tag.getInt("FluidLevel") + 1);
					}
				}
				if (!fullBucket) {
					player.playSound(entity instanceof Goat goat ? goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_MILK : SoundEvents.GOAT_MILK : SoundEvents.COW_MILK, 1.0F, 1.0F);
					player.setItemInHand(hand, milkBucket);
					event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
		ItemStack stack = event.getItemStack();
		Entity target = event.getTarget();
		if (target.getType() == EntityType.SPIDER && stack.getItem() == Items.SPIDER_SPAWN_EGG) {
			Level world = event.getLevel();
			Spiderling spiderling = CCEntityTypes.SPIDERLING.get().create(world);
			if (spiderling != null) {
				spiderling.copyPosition(target);
				if (stack.hasCustomHoverName()) {
					spiderling.setCustomName(stack.getHoverName());
				}
				if (!event.getEntity().isCreative()) {
					stack.shrink(1);
				}
				world.addFreshEntity(spiderling);
				event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event) {
		Entity entity = event.getEntity();
		LevelAccessor world = event.getLevel();
		boolean validSpawn = event.getSpawnReason() == MobSpawnType.NATURAL || event.getSpawnReason() == MobSpawnType.CHUNK_GENERATION;
		if (event.getResult() != Event.Result.DENY) {
			if (validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < CCConfig.COMMON.deeperMaxSpawnHeight.get()) {
				Creeper creeper = (Creeper) entity;
				if (world.getBlockState(creeper.blockPosition().below()).is(CCBlockTags.DEEPER_SPAWNABLE_ON)) {
					Deeper deeper = CCEntityTypes.DEEPER.get().create((Level) world);
					if (deeper != null) {
						deeper.copyPosition(creeper);
						world.addFreshEntity(deeper);
						entity.discard();
					}
				}
			}
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

		if (state.getBlock() instanceof BrazierBlock && face == Direction.UP) {
			if (stack.canPerformAction(ToolActions.SHOVEL_FLATTEN) && state.getValue(BrazierBlock.LIT)) {
				level.levelEvent(null, 1009, pos, 0);
				BlockState extinguishedState = BrazierBlock.extinguish(player, level, pos, state);
				if (!level.isClientSide()) {
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

		if (state.getBlock() instanceof NoteBlock && item == CCItems.TUNING_FORK.get()) {
			CompoundTag tag = stack.getOrCreateTag();
			if (!player.isCrouching() && tag.contains("Note")) {
				int note = tag.getInt("Note");
				level.setBlockAndUpdate(pos, state.setValue(NoteBlock.NOTE, Mth.clamp(note, 0, 24)));
				player.displayClientMessage(Component.translatable(item.getDescriptionId() + ".change_note", Component.translatable(item.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
				if (!level.isClientSide()) {
					level.blockEvent(pos, state.getBlock(), 0, 0);
				}
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
			}
		}

		if (CCConfig.COMMON.betterRailPlacement.get() && state.getBlock() instanceof BaseRailBlock) {
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
	public static void onEntityPlaceBlock(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() != null) {
			Level level = event.getEntity().getLevel();
			BlockPos pos = event.getPos();
			BlockState state = event.getPlacedBlock();

			if (state.is(Blocks.LIGHTNING_ROD) && state.getValue(LightningRodBlock.FACING) == Direction.UP) {
				BlockPos belowPos = pos.below();
				BlockState belowState = level.getBlockState(belowPos);
				if (belowState.getBlock() instanceof CarvedPumpkinBlock) {
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					level.setBlock(belowPos, Blocks.AIR.defaultBlockState(), 2);
					level.levelEvent(2001, pos, Block.getId(state));
					level.levelEvent(2001, belowPos, Block.getId(state));

					CopperGolem coppergolem = CCEntityTypes.COPPER_GOLEM.get().create(level);
					coppergolem.moveTo((double) belowPos.getX() + 0.5D, (double) belowPos.getY() + 0.05D, (double) belowPos.getZ() + 0.5D, 0.0F, 0.0F);
					level.addFreshEntity(coppergolem);

					for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, coppergolem.getBoundingBox().inflate(5.0D))) {
						CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, coppergolem);
					}

					level.blockUpdated(pos, Blocks.AIR);
					level.blockUpdated(belowPos, Blocks.AIR);
				}
			}
		}
	}

	@SubscribeEvent
	public static void bonusXPBlock(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
		Collection<AttributeModifier> experienceModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.EXPERIENCE_BOOST.get());
		if (!experienceModifiers.isEmpty()) {
			int xp = event.getExpToDrop();
			double experienceBoost = experienceModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
			event.setExpToDrop((int) (xp + xp * experienceBoost));
		}
	}

	@SubscribeEvent
	public static void bonusXPMobs(LivingExperienceDropEvent event) {
		Player player = event.getAttackingPlayer();
		if (player != null) {
			ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
			Collection<AttributeModifier> experienceModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(CCAttributes.EXPERIENCE_BOOST.get());
			if (!experienceModifiers.isEmpty()) {
				int xp = event.getDroppedExperience();
				double experienceBoost = experienceModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				event.setDroppedExperience((int) (xp + xp * experienceBoost));
			}
		}
	}

	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getSlot() == EquipmentSlot.HEAD) {
			ItemStack stack = event.getFrom();
			if (stack.getItem() == CCItems.TETHER_POTION.get()) {
				LivingEntity entity = event.getEntity();
				for (MobEffectInstance instance : PotionUtils.getMobEffects(stack)) {
					if (!instance.getEffect().isInstantenous()) {
						entity.removeEffectNoUpdate(instance.getEffect());
						entity.forceAddEffect(new MobEffectInstance(instance.getEffect(), TetherPotionItem.getTetherPotionDuration(instance.getDuration()), instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon()), null);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void potionAddedEvent(MobEffectEvent.Added event) {
		LivingEntity entity = event.getEntity();
		if (event.getEffectInstance().getEffect() == CCMobEffects.REWIND.get() && !entity.hasEffect(CCMobEffects.REWIND.get())) {
			CompoundTag data = entity.getPersistentData();
			data.putString("RewindDimension", entity.getCommandSenderWorld().dimension().location().toString());
			data.putDouble("RewindX", entity.getX());
			data.putDouble("RewindY", entity.getY());
			data.putDouble("RewindZ", entity.getZ());
		}
	}

	@SubscribeEvent
	public static void potionRemoveEvent(MobEffectEvent.Remove event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntity();

			if (effect == CCMobEffects.REWIND.get()) {
				CompoundTag data = entity.getPersistentData();
				if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
					if (data.contains("RewindDimension")) {
						ResourceLocation resourcelocation = new ResourceLocation(data.getString("RewindDimension"));
						ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourcelocation);
						ServerLevel dimension = entity.getServer().getLevel(key);

						if (dimension != entity.getCommandSenderWorld())
							entity.changeDimension(dimension, new ITeleporter() {
								@Override
								public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
									return repositionEntity.apply(false);
								}
							});
					}
					entity.teleportTo(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
					entity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void potionExpireEvent(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntity();

			if (effect == CCMobEffects.REWIND.get()) {
				CompoundTag data = entity.getPersistentData();
				if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
					if (data.contains("RewindDimension")) {
						ResourceLocation resourcelocation = new ResourceLocation(data.getString("RewindDimension"));
						ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourcelocation);
						ServerLevel dimension = entity.getServer().getLevel(key);

						if (dimension != entity.getCommandSenderWorld())
							entity.changeDimension(dimension, new ITeleporter() {
								@Override
								public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
									return repositionEntity.apply(false);
								}
							});
					}
					entity.teleportTo(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
					entity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingHurtEvent event) {
		LivingEntity target = event.getEntity();
		DamageSource source = event.getSource();
		Level level = target.getLevel();

		if (source.isMagic()) {
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

			if (target instanceof Horse horse) {
				for (ItemStack stack : horse.getArmorSlots()) {
					if (stack.getItem() instanceof SilverHorseArmorItem) {
						event.setAmount(event.getAmount() - event.getAmount() * 0.30F);
						SilverItem.causeMagicProtectionParticles(horse);
					}
				}
			}
		}

		if (source.getEntity() instanceof LivingEntity attacker) {
			float weaknessAmount = 0.0F;
			float lifeStealAmount = 0.0F;

			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack stack = target.getItemBySlot(slot);

					Collection<AttributeModifier> weaknessModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.WEAKNESS_AURA.get());
					if (!weaknessModifiers.isEmpty()) {
						weaknessAmount += weaknessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
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

			if (weaknessAmount > 0.0F) {
				for (LivingEntity entity : target.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(weaknessAmount, 0.0D, weaknessAmount))) {
					if (entity != target)
						entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60));
				}
			}

			if (target instanceof Horse horse) {
				for (ItemStack stack : horse.getArmorSlots()) {
					if (stack.getItem() instanceof NecromiumHorseArmorItem) {
						for (LivingEntity entity : target.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(2.0D, 0.0D, 2.0D))) {
							if (entity != target && !target.hasPassenger(entity))
								entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60));
						}
					}
				}
			}
		}

		ItemStack stack = target.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() == CCItems.TETHER_POTION.get() && !source.isBypassArmor()) {
			Player player = target instanceof Player ? (Player) target : null;

			for (MobEffectInstance instance : PotionUtils.getMobEffects(stack)) {
				if (instance.getEffect().isInstantenous()) {
					instance.getEffect().applyInstantenousEffect(player, player, target, instance.getAmplifier(), 1.0D);
				}
			}

			target.broadcastBreakEvent(EquipmentSlot.HEAD);
			int i = PotionUtils.getPotion(stack).hasInstantEffects() ? 2007 : 2002;
			level.levelEvent(i, new BlockPos(target.getEyePosition(1.0F)), PotionUtils.getColor(stack));
			target.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
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
	public static void knockbackEvent(LivingKnockBackEvent event) {
		if (event.getEntity() instanceof Horse horse) {
			for (ItemStack stack : horse.getArmorSlots()) {
				if (stack.getItem() instanceof NetheriteHorseArmorItem horseArmorItem) {
					event.setStrength(event.getStrength() * (1.0F - horseArmorItem.getKnockbackResistance()));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onItemModify(ItemAttributeModifierEvent event) {
		ItemStack stack = event.getItemStack();
		EquipmentSlot slot = event.getSlotType();
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
		if (CCConfig.COMMON.chainmailArmorBuff.get()) {
			if (stack.is(Items.CHAINMAIL_HELMET) && slot == EquipmentSlot.HEAD || stack.is(Items.CHAINMAIL_BOOTS) && slot == EquipmentSlot.FEET)
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 1.0D, AttributeModifier.Operation.ADDITION));
			else if (stack.is(Items.CHAINMAIL_CHESTPLATE) && slot == EquipmentSlot.CHEST || stack.is(Items.CHAINMAIL_LEGGINGS) && slot == EquipmentSlot.LEGS)
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 2.0D, AttributeModifier.Operation.ADDITION));
		}

		if (slot == EquipmentSlot.MAINHAND) {
			if (stack.is(CCItemTags.EXPERIENCE_BOOST_ITEMS)) {
				event.addModifier(CCAttributes.EXPERIENCE_BOOST.get(), new AttributeModifier(UUID.fromString("1B1C193D-1484-4CB9-8DC7-FE226C77657A"), "Exerience boost", 0.75D, AttributeModifier.Operation.MULTIPLY_BASE));
			}

			if (stack.is(CCItemTags.MAGIC_DAMAGE_ITEMS)) {
				float damage = stack.getItem() instanceof AxeItem || stack.getItem() instanceof SwordItem ? 3.0F : 1.0F;
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
		if (entity instanceof Player player) {
			IDataManager data = (IDataManager) entity;
			if (data.getValue(CCDataProcessors.CONTROLLED_GOLEM_UUID).isPresent()) {
				ControllableGolem golem = TuningForkItem.findControlledGolem(player);
				if (golem != null) {
					int forgettime = TuningForkItem.getForgetGolemTime(player);
					if (forgettime > 0) {
						if (!TuningForkItem.isTuningForkWithNote(player.getMainHandItem()) && !TuningForkItem.isTuningForkWithNote(player.getOffhandItem()))
							TuningForkItem.setForgetGolemTime(player, forgettime - 1);
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
	}
}