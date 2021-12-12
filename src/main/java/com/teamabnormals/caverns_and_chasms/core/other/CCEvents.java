package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.block.BrazierBlock;
import com.teamabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.*;
import com.teamabnormals.caverns_and_chasms.common.item.TuningForkItem;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.*;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof Zombie zombie) {
			zombie.goalSelector.addGoal(1, new AvoidEntityGoal<>(zombie, Fly.class, 9.0F, 1.05D, 1.05D));
		}
		if (entity instanceof AbstractHorse horse && !(entity instanceof SkeletonHorse)) {
			horse.goalSelector.addGoal(1, new AvoidEntityGoal<>(horse, Fly.class, 8.0F, 1.1D, 1.1D));
		}
		if (entity instanceof Spider spider) {
			spider.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(spider, Fly.class, false));
		}
		if (entity instanceof IronGolem golem && !CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			golem.targetSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(goal -> {
				golem.targetSelector.removeGoal(goal);
				golem.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(golem, Mob.class, 5, false, false, (mob) -> mob instanceof Enemy));
			});
		}
		if (entity instanceof Creeper creeper && !CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			creeper.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(creeper, IronGolem.class, true));
		}
		if (entity.getType().is(CCEntityTypeTags.COLLAR_DROP_MOBS) && entity instanceof TamableAnimal pet) {
			List<Goal> goalsToRemove = Lists.newArrayList();
			pet.goalSelector.availableGoals.forEach((goal) -> {
				if (goal.getGoal() instanceof FloatGoal)
					goalsToRemove.add(goal.getGoal());
			});

			goalsToRemove.forEach(pet.goalSelector::removeGoal);
		}
	}

	@SubscribeEvent
	public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		Player player = event.getPlayer();
		ItemStack stack = player.getItemInHand(event.getHand());
		Level world = event.getWorld();
		InteractionHand hand = event.getHand();
		if (event.getTarget() instanceof LivingEntity entity && entity.getType().is(CCEntityTypeTags.MILKABLE)) {
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
					event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
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
			Level world = event.getWorld();
			Spiderling spiderling = CCEntityTypes.SPIDERLING.get().create(world);
			if (spiderling != null) {
				spiderling.copyPosition(target);
				if (stack.hasCustomHoverName()) {
					spiderling.setCustomName(stack.getHoverName());
				}
				if (!event.getPlayer().isCreative()) {
					stack.shrink(1);
				}
				world.addFreshEntity(spiderling);
				event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onExplosion(EntityMobGriefingEvent event) {
		if (!CCConfig.COMMON.creeperExplosionsDestroyBlocks.get() && event.getEntity() != null && event.getEntity().getType() == EntityType.CREEPER) {
			event.setResult(Event.Result.DENY);
		}
	}

	@SubscribeEvent
	public static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event) {
		Entity entity = event.getEntity();
		LevelAccessor world = event.getWorld();
		boolean validSpawn = event.getSpawnReason() == MobSpawnType.NATURAL || event.getSpawnReason() == MobSpawnType.CHUNK_GENERATION;
		if (event.getResult() != Event.Result.DENY) {
			if (validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < CCConfig.COMMON.deeperMaxSpawnHeight.get()) {
				Creeper creeper = (Creeper) entity;
				if (world.getBlockState(creeper.blockPosition().below()).is(CCBlockTags.DEEPER_SPAWN_BLOCKS)) {
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
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		Level world = entity.getCommandSenderWorld();

		if (entity instanceof Enemy && !world.isClientSide()) {
			AABB aabb = entity.getBoundingBox().inflate(5.0F, 2.0F, 5.0F);
			Stream<BlockPos> blocks = BlockPos.betweenClosedStream(new BlockPos(aabb.minX, aabb.minY, aabb.minZ), new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ));

			blocks.forEach(pos -> {
				BlockState state = world.getBlockState(pos);
				if (state.is(CCBlocks.GRAVESTONE.get())) {
					((GravestoneBlock) CCBlocks.GRAVESTONE.get()).powerBlock(state, world, pos);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(LeftClickBlock event) {
		Level world = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);

		if (event.getItemStack().getItem() instanceof TuningForkItem && state.getBlock() instanceof NoteBlock) {
			CompoundTag tag = event.getItemStack().getOrCreateTag();
			if (tag.contains("Note")) {
				world.setBlockAndUpdate(pos, state.setValue(NoteBlock.NOTE, tag.getInt("Note")));
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event) {
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		Level world = event.getWorld();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		BlockState state = world.getBlockState(pos);
		Direction face = event.getFace();
		Random random = world.getRandom();

		if (state.getBlock() instanceof BrazierBlock && face == Direction.UP) {
			if (stack.canPerformAction(ToolActions.SHOVEL_FLATTEN) && state.getValue(BrazierBlock.LIT)) {
				BlockState extinguishedState = BrazierBlock.extinguish(world, pos, state);
				if (!world.isClientSide()) {
					world.setBlock(pos, extinguishedState, 11);
					stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(event.getHand()));
				}

				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
			}

			if (BrazierBlock.canBeLit(state)) {
				if (stack.getItem() instanceof FlintAndSteelItem) {
					world.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
					if (!world.isClientSide()) {
						world.setBlock(pos, state.setValue(BrazierBlock.LIT, true), 11);
						stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(event.getHand()));
					}

					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
				} else if (stack.getItem() instanceof FireChargeItem) {
					world.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
					if (!world.isClientSide()) {
						world.setBlock(pos, state.setValue(BrazierBlock.LIT, true), 11);
						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}

					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
				}
			}

		}

		if (CCConfig.COMMON.betterRailPlacement.get() && state.getBlock() instanceof BaseRailBlock) {
			if (!stack.is(CCItemTags.IGNORE_RAIL_PLACEMENT) && item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BaseRailBlock && !state.is(CCBlockTags.IGNORE_RAIL_PLACEMENT)) {
					Direction direction = event.getPlayer().getDirection();
					BlockPos.MutableBlockPos currentPos = event.getPos().mutable().move(direction);
					for (int i = 0; i < CCConfig.COMMON.betterRailPlacementRange.get(); i++) {
						BlockPos nextPos = null;
						boolean isNextRail = false;
						BlockPos.MutableBlockPos yCheckingPos = currentPos.mutable().move(Direction.DOWN);
						for (int j = 0; j < 3; j++) {
							BlockState stateAtCheckPos = world.getBlockState(yCheckingPos);
							if (stateAtCheckPos.getBlock() instanceof BaseRailBlock && !stateAtCheckPos.is(CCBlockTags.IGNORE_RAIL_PLACEMENT)) {
								nextPos = yCheckingPos.move(direction).immutable();
								isNextRail = true;
							} else if (!isNextRail) {
								BlockPlaceContext context = new BlockPlaceContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(yCheckingPos));
								if (world.getBlockState(yCheckingPos).canBeReplaced(context)) {
									BlockState stateForPlacement = state.getBlock().getStateForPlacement(context);
									if (stateForPlacement != null && stateForPlacement.canSurvive(world, yCheckingPos))
										nextPos = yCheckingPos.immutable();
								}
							}
							yCheckingPos.move(Direction.UP);
						}
						if (!isNextRail) {
							if (nextPos != null) {
								BlockPlaceContext context = new BlockPlaceContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(nextPos));
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
	public static void bonusXPBlock(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		Item item = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
		if (item instanceof TieredItem && ((TieredItem) item).getTier() == Tiers.GOLD) {
			int droppedXP = event.getExpToDrop();
			event.setExpToDrop(droppedXP * 2);
		}
	}

	@SubscribeEvent
	public static void bonusXPMobs(LivingExperienceDropEvent event) {
		Player player = event.getAttackingPlayer();
		if (player != null) {
			Item item = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
			if (item instanceof TieredItem && ((TieredItem) item).getTier() == Tiers.GOLD) {
				int droppedXP = event.getDroppedExperience();
				event.setDroppedExperience(droppedXP * 2);
			}
		}
	}

	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getSlot() == EquipmentSlot.HEAD && event.getFrom().getItem() == CCItems.SPINEL_CROWN.get()) {
			event.getEntityLiving().curePotionEffects(new ItemStack(CCItems.SPINEL_CROWN.get()));
		}
	}

	@SubscribeEvent
	public static void potionAddedEvent(PotionEvent.PotionAddedEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (event.getPotionEffect().getEffect() == CCEffects.REWIND.get() && !entity.hasEffect(CCEffects.REWIND.get())) {
			CompoundTag data = entity.getPersistentData();
			data.putString("RewindDimension", entity.getCommandSenderWorld().dimension().location().toString());
			data.putDouble("RewindX", entity.getX());
			data.putDouble("RewindY", entity.getY());
			data.putDouble("RewindZ", entity.getZ());
		}
	}

	@SubscribeEvent
	public static void potionApplicableEvent(PotionEvent.PotionApplicableEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (event.getResult() != Result.DENY && event.getPotionEffect().getEffect() == CCEffects.AFFLICTION.get()) {
			if (entity.getEffect(CCEffects.AFFLICTION.get()) != null) {
				MobEffectInstance affliction = entity.getEffect(CCEffects.AFFLICTION.get());
				if (affliction.getAmplifier() < 9) {
					MobEffectInstance upgrade = new MobEffectInstance(affliction.getEffect(), affliction.getDuration() + 10, affliction.getAmplifier() + 1, affliction.isAmbient(), affliction.isVisible(), affliction.showIcon());
					entity.removeEffectNoUpdate(CCEffects.AFFLICTION.get());
					entity.addEffect(upgrade);

					event.setResult(Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public static void potionRemoveEvent(PotionEvent.PotionRemoveEvent event) {
		MobEffectInstance effectInstance = event.getPotionEffect();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntityLiving();

			if (effect == CCEffects.AFFLICTION.get() && entity.isInvertedHealAndHarm()) {
				entity.hurt(CCDamageSources.AFFLICTION, (effectInstance.getAmplifier() + 1) * 3);
			}

			if (effect == CCEffects.REWIND.get()) {
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
	public static void potionExpireEvent(PotionEvent.PotionExpiryEvent event) {
		MobEffectInstance effectInstance = event.getPotionEffect();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntityLiving();

			if (effect == CCEffects.AFFLICTION.get() && entity.isInvertedHealAndHarm()) {
				entity.hurt(CCDamageSources.AFFLICTION, (effectInstance.getAmplifier() + 1) * 3);
			}

			if (effect == CCEffects.REWIND.get()) {
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
		LivingEntity target = event.getEntityLiving();
		Random rand = new Random();

		if (event.getSource().getEntity() instanceof LivingEntity attacker) {
			float afflictionChance = 0.0F;
			float weaknessAmount = 0.0F;
			float lifeStealAmount = 0.0F;

			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = target.getItemBySlot(slot);

				Collection<AttributeModifier> afflictionModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.AFFLICTION_CHANCE.get());
				if (!afflictionModifiers.isEmpty())
					afflictionChance += afflictionModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();

				Collection<AttributeModifier> weaknessModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.WEAKNESS_AURA.get());
				if (!weaknessModifiers.isEmpty()) {
					weaknessAmount += weaknessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				}

				Collection<AttributeModifier> lifeStealModifiers = attacker.getItemBySlot(slot).getAttributeModifiers(slot).get(CCAttributes.LIFESTEAL.get());
				if (!lifeStealModifiers.isEmpty() && (target instanceof Enemy || target instanceof Player)) {
					lifeStealAmount += lifeStealModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				}
			}

			if (lifeStealAmount > 0.0F)
				attacker.heal(lifeStealAmount * event.getAmount());

			if (rand.nextFloat() < afflictionChance) {
				if (attacker.isInvertedHealAndHarm())
					attacker.addEffect(new MobEffectInstance(CCEffects.AFFLICTION.get(), 60));
			}

			if (weaknessAmount > 0.0F) {
				for (LivingEntity entity : target.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(weaknessAmount, 0.0D, weaknessAmount))) {
					if (entity != target)
						entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60));
				}
			}
		}

		if (target instanceof Horse) {
			Horse horse = (Horse) event.getEntityLiving();
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

	@SubscribeEvent
	public static void livingDeathEvent(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (event.getSource() == CCDamageSources.AFFLICTION && entity.hasEffect(CCEffects.AFFLICTION.get())) {
			MobEffectInstance effectInstance = entity.getEffect(CCEffects.AFFLICTION.get());

			AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(entity.level, entity.getX(), entity.getY(), entity.getZ());
			areaeffectcloudentity.setRadius(3.5F);
			areaeffectcloudentity.setRadiusOnUse(-0.5F);
			areaeffectcloudentity.setWaitTime(10);
			areaeffectcloudentity.setDuration(100);
			areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
			areaeffectcloudentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, effectInstance.getAmplifier()));

			entity.getCommandSenderWorld().addFreshEntity(areaeffectcloudentity);
		}
	}

	@SubscribeEvent
	public static void onItemModify(ItemAttributeModifierEvent event) {
		Item item = event.getItemStack().getItem();
		EquipmentSlot slot = event.getSlotType();
		UUID uuid = ArmorItem.ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
		if (CCConfig.COMMON.chainmailArmorBuff.get()) {
			if (item == Items.CHAINMAIL_HELMET && slot == EquipmentSlot.HEAD || item == Items.CHAINMAIL_BOOTS && slot == EquipmentSlot.FEET)
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 1.0D, AttributeModifier.Operation.ADDITION));
			else if (item == Items.CHAINMAIL_CHESTPLATE && slot == EquipmentSlot.CHEST || item == Items.CHAINMAIL_LEGGINGS && slot == EquipmentSlot.LEGS)
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 2.0D, AttributeModifier.Operation.ADDITION));
		}
	}
}
