package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.blueprint.core.other.tags.BlueprintEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.common.block.BrazierBlock;
import com.teamabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Fly;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Spiderling;
import com.teamabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.AfflictingItem;
import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverHorseArmorItem;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMobEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
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
	}

	@SubscribeEvent
	public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		Player player = event.getPlayer();
		ItemStack stack = player.getItemInHand(event.getHand());
		Level world = event.getWorld();
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
				if (world.getBlockState(creeper.blockPosition().below()).is(CCBlockTags.DEEPER_SPAWNABLE_BLOCKS)) {
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
	public static void onRightClickBlock(RightClickBlock event) {
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		Level level = event.getWorld();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		BlockState state = level.getBlockState(pos);
		Direction face = event.getFace();
		Random random = level.getRandom();

		if (state.getBlock() instanceof BrazierBlock && face == Direction.UP) {
			if (stack.canPerformAction(ToolActions.SHOVEL_FLATTEN) && state.getValue(BrazierBlock.LIT)) {
				BlockState extinguishedState = BrazierBlock.extinguish(level, pos, state);
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
						stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(event.getHand()));
					}

					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
				} else if (stack.getItem() instanceof FireChargeItem) {
					level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
					if (!level.isClientSide()) {
						level.setBlock(pos, state.setValue(BrazierBlock.LIT, true), 11);
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
				player.displayClientMessage(new TranslatableComponent(item.getDescriptionId() + ".change_note", new TranslatableComponent(item.getDescriptionId() + ".note." + note)).append(" (" + note + ")"), true);
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
					Direction direction = event.getPlayer().getDirection();
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
								BlockPlaceContext context = new BlockPlaceContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(yCheckingPos));
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
		if (event.getPotionEffect().getEffect() == CCMobEffects.REWIND.get() && !entity.hasEffect(CCMobEffects.REWIND.get())) {
			CompoundTag data = entity.getPersistentData();
			data.putString("RewindDimension", entity.getCommandSenderWorld().dimension().location().toString());
			data.putDouble("RewindX", entity.getX());
			data.putDouble("RewindY", entity.getY());
			data.putDouble("RewindZ", entity.getZ());
		}
	}

	@SubscribeEvent
	public static void potionRemoveEvent(PotionEvent.PotionRemoveEvent event) {
		MobEffectInstance effectInstance = event.getPotionEffect();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntityLiving();

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
	public static void potionExpireEvent(PotionEvent.PotionExpiryEvent event) {
		MobEffectInstance effectInstance = event.getPotionEffect();
		if (effectInstance != null) {
			MobEffect effect = effectInstance.getEffect();
			LivingEntity entity = event.getEntityLiving();

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
		LivingEntity target = event.getEntityLiving();

		if (event.getSource().getEntity() instanceof LivingEntity attacker) {
			float weaknessAmount = 0.0F;
			float lifeStealAmount = 0.0F;

			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = target.getItemBySlot(slot);

				if (stack.getItem() instanceof AfflictingItem afflictingItem && !target.level.isClientSide()) {
					afflictingItem.causeAfflictionDamage(attacker, true);
				}

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

			if (weaknessAmount > 0.0F) {
				for (LivingEntity entity : target.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(weaknessAmount, 0.0D, weaknessAmount))) {
					if (entity != target)
						entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60));
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

					if (stack.getItem() instanceof SilverHorseArmorItem silverHorseArmorItem) {
						silverHorseArmorItem.causeAfflictionDamage(attacker, true);
					}
				}
			}
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
