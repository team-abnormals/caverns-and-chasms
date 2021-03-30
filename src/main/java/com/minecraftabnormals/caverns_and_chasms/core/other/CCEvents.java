package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.common.block.BrazierBlock;
import com.minecraftabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.FlyEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.SpiderlingEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.ZombieChickenEntity;
import com.minecraftabnormals.caverns_and_chasms.common.item.ForgottenCollarItem;
import com.minecraftabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.*;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof ZombieEntity) {
			ZombieEntity zombie = (ZombieEntity) event.getEntity();
			zombie.goalSelector.addGoal(1, new AvoidEntityGoal<>(zombie, FlyEntity.class, 9.0F, 1.05D, 1.05D));
		}
		if (entity instanceof AbstractHorseEntity && !(entity instanceof SkeletonHorseEntity)) {
			AbstractHorseEntity horse = (AbstractHorseEntity) event.getEntity();
			horse.goalSelector.addGoal(1, new AvoidEntityGoal<>(horse, FlyEntity.class, 8.0F, 1.1D, 1.1D));
		}
		if (entity instanceof SpiderEntity) {
			SpiderEntity spider = (SpiderEntity) event.getEntity();
			spider.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(spider, FlyEntity.class, false));
		}
		if (entity instanceof ChickenEntity) {
			ChickenEntity chicken = (ChickenEntity) event.getEntity();
			chicken.goalSelector.addGoal(1, new AvoidEntityGoal<>(chicken, ZombieChickenEntity.class, 9.0F, 1.05D, 1.05D));
		}
		if (entity instanceof IronGolemEntity && !CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			IronGolemEntity golem = (IronGolemEntity) entity;
			golem.targetSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(goal -> {
				golem.targetSelector.removeGoal(goal);
				golem.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(golem, MobEntity.class, 5, false, false, (mob) -> mob instanceof IMob));
			});
		}
		if (entity instanceof CreeperEntity && !CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			CreeperEntity creeper = (CreeperEntity) entity;
			creeper.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(creeper, IronGolemEntity.class, true));
		}
	}

	@SubscribeEvent
	public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		PlayerEntity player = event.getPlayer();
		ItemStack stack = player.getHeldItem(event.getHand());
		World world = event.getWorld();
		Hand hand = event.getHand();
		if (event.getTarget() instanceof CowEntity && !((CowEntity) event.getTarget()).isChild()) {
			if (stack.getItem() == CCItems.GOLDEN_MILK_BUCKET.get() || stack.getItem() == CCItems.GOLDEN_BUCKET.get()) {
				CompoundNBT tag = stack.getOrCreateTag();
				ItemStack milkBucket = DrinkHelper.fill(stack.copy(), player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
				boolean flag = false;
				if (stack.getItem() == CCItems.GOLDEN_MILK_BUCKET.get()) {
					flag = tag.getInt("FluidLevel") >= 2;
					if (!flag) {
						if (!player.isCreative())
							milkBucket.getOrCreateTag().putInt("FluidLevel", tag.getInt("FluidLevel") + 1);
					}
				}
				if (!flag) {
					player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
					player.setHeldItem(hand, milkBucket);
					event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote()));
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
			World world = event.getWorld();
			SpiderlingEntity spiderling = CCEntities.SPIDERLING.get().create(world);
			if (spiderling != null) {
				spiderling.copyLocationAndAnglesFrom(target);
				if (stack.hasDisplayName()) {
					spiderling.setCustomName(stack.getDisplayName());
				}
				if (!event.getPlayer().isCreative()) {
					stack.shrink(1);
				}
				world.addEntity(spiderling);
				event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote()));
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
		IWorld world = event.getWorld();
		boolean validSpawn = event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION;
		if (event.getResult() != Event.Result.DENY) {
			if (validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < CCConfig.COMMON.deeperMaxSpawnHeight.get()) {
				CreeperEntity creeper = (CreeperEntity) entity;
				if (world.getBlockState(creeper.getPosition().down()).isIn(CCTags.Blocks.DEEPER_SPAWN_BLOCKS)) {
					DeeperEntity deeper = CCEntities.DEEPER.get().create((World) world);
					if (deeper != null) {
						deeper.copyLocationAndAnglesFrom(creeper);
						world.addEntity(deeper);
						entity.remove();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		EntityType<?> type = entity.getType();
		World world = entity.getEntityWorld();

		if (entity instanceof IMob && !world.isRemote()) {
			AxisAlignedBB aabb = entity.getBoundingBox().grow(5.0F, 2.0F, 5.0F);
			Stream<BlockPos> blocks = BlockPos.getAllInBox(new BlockPos(aabb.minX, aabb.minY, aabb.minZ), new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ));

			blocks.forEach(pos -> {
				BlockState state = world.getBlockState(pos);
				if (state.isIn(CCBlocks.GRAVESTONE.get())) {
					((GravestoneBlock) CCBlocks.GRAVESTONE.get()).powerBlock(state, world, pos);
				}
			});
		}

		if (type.isContained(CCTags.EntityTypes.COLLAR_DROP_MOBS)) {
			ItemStack collar = new ItemStack(CCItems.FORGOTTEN_COLLAR.get());
			CompoundNBT tag = collar.getOrCreateTag();

			tag.putString(ForgottenCollarItem.PET_ID, type.getRegistryName().toString());
			tag.putBoolean(ForgottenCollarItem.IS_CHILD, entity.isChild());
			if (entity.hasCustomName()) {
				tag.putString(ForgottenCollarItem.PET_NAME, entity.getCustomName().getString());
			}

			if (entity instanceof TameableEntity) {
				TameableEntity pet = (TameableEntity) entity;
				if (pet.isTamed()) {
					tag.putString(ForgottenCollarItem.OWNER_ID, pet.getOwnerId().toString());

					if (entity instanceof WolfEntity) {
						WolfEntity wolf = (WolfEntity) entity;
						tag.putInt(ForgottenCollarItem.COLLAR_COLOR, wolf.getCollarColor().getId());
					}

					if (entity instanceof CatEntity) {
						CatEntity cat = (CatEntity) entity;
						tag.putInt(ForgottenCollarItem.PET_VARIANT, cat.getCatType());
						tag.putInt(ForgottenCollarItem.COLLAR_COLOR, cat.getCollarColor().getId());
					}

					entity.entityDropItem(collar);
				}
			} else if (entity instanceof AbstractHorseEntity) {
				AbstractHorseEntity horse = (AbstractHorseEntity) entity;
				if (horse.isTame()) {
					tag.putString(ForgottenCollarItem.OWNER_ID, horse.getOwnerUniqueId().toString());
					tag.putDouble(ForgottenCollarItem.HORSE_SPEED, horse.getBaseAttributeValue(Attributes.MOVEMENT_SPEED));
					tag.putDouble(ForgottenCollarItem.HORSE_HEALTH, horse.getBaseAttributeValue(Attributes.MAX_HEALTH));
					tag.putDouble(ForgottenCollarItem.HORSE_STRENGTH, horse.getBaseAttributeValue(Attributes.HORSE_JUMP_STRENGTH));
					if (entity instanceof HorseEntity)
						tag.putInt(ForgottenCollarItem.PET_VARIANT, ((HorseEntity) entity).func_234241_eS_());

					entity.entityDropItem(collar);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		BlockState state = world.getBlockState(pos);
		Direction face = event.getFace();
		Random random = world.getRandom();

		if (state.getBlock() instanceof BrazierBlock && face == Direction.UP) {
			if (stack.getToolTypes().contains(ToolType.SHOVEL)) {
				BlockState extinguishedState = BrazierBlock.extinguish(world, pos, state);
				if (!world.isRemote()) {
					world.setBlockState(pos, extinguishedState, 11);
					stack.damageItem(1, player, (entity) -> entity.sendBreakAnimation(event.getHand()));
				}

				event.setCanceled(true);
				event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote()));
			}

			if (BrazierBlock.canBeLit(state)) {
				if (stack.getItem() instanceof FlintAndSteelItem) {
					world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
					if (!world.isRemote()) {
						world.setBlockState(pos, state.with(BrazierBlock.LIT, true), 11);
						stack.damageItem(1, player, (entity) -> entity.sendBreakAnimation(event.getHand()));
					}

					event.setCanceled(true);
					event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote()));
				} else if (stack.getItem() instanceof FireChargeItem) {
					world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
					if (!world.isRemote()) {
						world.setBlockState(pos, state.with(BrazierBlock.LIT, true), 11);
						if (!player.abilities.isCreativeMode) {
							stack.shrink(1);
						}
					}

					event.setCanceled(true);
					event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote()));
				}
			}

		}

		if (CCConfig.COMMON.betterRailPlacement.get() && state.getBlock() instanceof AbstractRailBlock) {
			if (!item.isIn(CCTags.Items.IGNORE_RAIL_PLACEMENT) && item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof AbstractRailBlock && !block.isIn(CCTags.Blocks.IGNORE_RAIL_PLACEMENT)) {
					Direction direction = event.getPlayer().getHorizontalFacing();
					BlockPos.Mutable currentPos = event.getPos().toMutable().move(direction);
					for (int i = 0; i < CCConfig.COMMON.betterRailPlacementRange.get(); i++) {
						BlockPos nextPos = null;
						boolean isNextRail = false;
						BlockPos.Mutable yCheckingPos = currentPos.toMutable().move(Direction.DOWN);
						for (int j = 0; j < 3; j++) {
							Block blockAtCheckPos = world.getBlockState(yCheckingPos).getBlock();
							if (blockAtCheckPos instanceof AbstractRailBlock && !blockAtCheckPos.isIn(CCTags.Blocks.IGNORE_RAIL_PLACEMENT)) {
								nextPos = yCheckingPos.move(direction).toImmutable();
								isNextRail = true;
							} else if (!isNextRail) {
								BlockItemUseContext context = new BlockItemUseContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(yCheckingPos));
								if (world.getBlockState(yCheckingPos).isReplaceable(context)) {
									BlockState stateForPlacement = state.getBlock().getStateForPlacement(context);
									if (stateForPlacement != null && stateForPlacement.isValidPosition(world, yCheckingPos))
										nextPos = yCheckingPos.toImmutable();
								}
							}
							yCheckingPos.move(Direction.UP);
						}
						if (!isNextRail) {
							if (nextPos != null) {
								BlockItemUseContext context = new BlockItemUseContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(nextPos));
								((BlockItem) item).tryPlace(context);
								event.setCancellationResult(ActionResultType.SUCCESS);
								event.setCanceled(true);
							}
							break;
						} else {
							currentPos.setPos(nextPos);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void bonusXPBlock(BlockEvent.BreakEvent event) {
		PlayerEntity player = event.getPlayer();
		Item item = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();
		if (item instanceof TieredItem && ((TieredItem) item).getTier() == ItemTier.GOLD) {
			int droppedXP = event.getExpToDrop();
			event.setExpToDrop(droppedXP * 2);
		}
	}

	@SubscribeEvent
	public static void bonusXPMobs(LivingExperienceDropEvent event) {
		PlayerEntity player = event.getAttackingPlayer();
		if (player != null) {
			Item item = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();
			if (item instanceof TieredItem && ((TieredItem) item).getTier() == ItemTier.GOLD) {
				int droppedXP = event.getDroppedExperience();
				event.setDroppedExperience(droppedXP * 2);
			}
		}
	}

	@SubscribeEvent
	public static void potionAddedEvent(PotionEvent.PotionAddedEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (event.getPotionEffect().getPotion() == CCEffects.REWIND.get()) {
			CompoundNBT data = entity.getPersistentData();
			data.putString("RewindDimension", entity.getEntityWorld().getDimensionKey().getLocation().toString());
			data.putDouble("RewindX", entity.getPosX());
			data.putDouble("RewindY", entity.getPosY());
			data.putDouble("RewindZ", entity.getPosZ());
		}
	}

	@SubscribeEvent
	public static void potionApplicableEvent(PotionEvent.PotionApplicableEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (event.getResult() != Result.DENY && event.getPotionEffect().getPotion() == CCEffects.AFFLICTION.get()) {
			if (entity.getActivePotionEffect(CCEffects.AFFLICTION.get()) != null) {
				EffectInstance affliction = entity.getActivePotionEffect(CCEffects.AFFLICTION.get());
				if (affliction.getAmplifier() < 9) {
					EffectInstance upgrade = new EffectInstance(affliction.getPotion(), affliction.getDuration() + 10, affliction.getAmplifier() + 1, affliction.isAmbient(), affliction.doesShowParticles(), affliction.isShowIcon());
					entity.removeActivePotionEffect(CCEffects.AFFLICTION.get());
					entity.addPotionEffect(upgrade);

					event.setResult(Result.DENY);
				}
			}
		}
	}

	@SubscribeEvent
	public static void potionRemoveEvent(PotionEvent.PotionRemoveEvent event) {
		EffectInstance effectInstance = event.getPotionEffect();
		if (effectInstance != null) {
			Effect effect = effectInstance.getPotion();
			LivingEntity entity = event.getEntityLiving();

			if (effect == CCEffects.AFFLICTION.get() && entity.isEntityUndead()) {
				entity.attackEntityFrom(CCDamageSources.AFFLICTION, (effectInstance.getAmplifier() + 1) * 3);
			}

			if (effect == CCEffects.REWIND.get()) {
				CompoundNBT data = entity.getPersistentData();
				if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
					if (data.contains("RewindDimension")) {
						ResourceLocation resourcelocation = new ResourceLocation(data.getString("RewindDimension"));
						RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, resourcelocation);
						ServerWorld dimension = entity.getServer().getWorld(key);

						if (dimension != entity.getEntityWorld())
							entity.changeDimension(dimension, new ITeleporter() {
								@Override
								public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
									return repositionEntity.apply(false);
								}
							});
					}
					entity.setPositionAndUpdate(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
					entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void potionExpireEvent(PotionEvent.PotionExpiryEvent event) {
		EffectInstance effectInstance = event.getPotionEffect();
		if (effectInstance != null) {
			Effect effect = effectInstance.getPotion();
			LivingEntity entity = event.getEntityLiving();

			if (effect == CCEffects.AFFLICTION.get() && entity.isEntityUndead()) {
				entity.attackEntityFrom(CCDamageSources.AFFLICTION, (effectInstance.getAmplifier() + 1) * 3);
			}

			if (effect == CCEffects.REWIND.get()) {
				CompoundNBT data = entity.getPersistentData();
				if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
					if (data.contains("RewindDimension")) {
						ResourceLocation resourcelocation = new ResourceLocation(data.getString("RewindDimension"));
						RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, resourcelocation);
						ServerWorld dimension = entity.getServer().getWorld(key);

						if (dimension != entity.getEntityWorld())
							entity.changeDimension(dimension, new ITeleporter() {
								@Override
								public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
									return repositionEntity.apply(false);
								}
							});
					}
					entity.setPositionAndUpdate(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
					entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingHurtEvent event) {
		LivingEntity target = event.getEntityLiving();
		Random rand = new Random();

		if (event.getSource().getTrueSource() instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
			float afflictionChance = 0.0F;
			float weaknessAmount = 0.0F;
			float lifeStealAmount = 0.0F;

			for (EquipmentSlotType slot : EquipmentSlotType.values()) {
				ItemStack stack = target.getItemStackFromSlot(slot);

				Collection<AttributeModifier> afflictionModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.AFFLICTION_CHANCE.get());
				if (!afflictionModifiers.isEmpty())
					afflictionChance += afflictionModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();

				Collection<AttributeModifier> weaknessModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.WEAKNESS_AURA.get());
				if (!weaknessModifiers.isEmpty()) {
					weaknessAmount += weaknessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				}

				Collection<AttributeModifier> lifeStealModifiers = attacker.getItemStackFromSlot(slot).getAttributeModifiers(slot).get(CCAttributes.LIFESTEAL.get());
				if (!lifeStealModifiers.isEmpty() && target instanceof IMob) {
					lifeStealAmount += lifeStealModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				}
			}

			if (lifeStealAmount > 0.0F)
				attacker.heal(lifeStealAmount * event.getAmount());

			if (rand.nextFloat() < afflictionChance) {
				if (attacker.isEntityUndead())
					attacker.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
			}

			if (weaknessAmount > 0.0F) {
				for (LivingEntity entity : target.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(weaknessAmount, 0.0D, weaknessAmount))) {
					if (entity != target)
						entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 60));
				}
			}
		}

		if (target instanceof HorseEntity) {
			HorseEntity horse = (HorseEntity) event.getEntityLiving();
			for (ItemStack stack : horse.getArmorInventoryList()) {
				if (stack.getItem() instanceof NecromiumHorseArmorItem) {
					for (LivingEntity entity : target.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(2.0D, 0.0D, 2.0D))) {
						if (entity != target && !target.isPassenger(entity))
							entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 60));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void livingDeathEvent(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (event.getSource() == CCDamageSources.AFFLICTION && entity.isPotionActive(CCEffects.AFFLICTION.get())) {
			EffectInstance effectInstance = entity.getActivePotionEffect(CCEffects.AFFLICTION.get());

			AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ());
			areaeffectcloudentity.setRadius(3.5F);
			areaeffectcloudentity.setRadiusOnUse(-0.5F);
			areaeffectcloudentity.setWaitTime(10);
			areaeffectcloudentity.setDuration(100);
			areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
			areaeffectcloudentity.addEffect(new EffectInstance(Effects.SLOWNESS, 100, effectInstance.getAmplifier()));

			entity.getEntityWorld().addEntity(areaeffectcloudentity);
		}
	}

	@SubscribeEvent
	public static void onItemModify(ItemAttributeModifierEvent event) {
		Item item = event.getItemStack().getItem();
		EquipmentSlotType slot = event.getSlotType();
		UUID uuid = ArmorItem.ARMOR_MODIFIERS[slot.getIndex()];
		if (CCConfig.COMMON.chainmailArmorBuff.get()) {
			if (item == Items.CHAINMAIL_HELMET && slot == EquipmentSlotType.HEAD || item == Items.CHAINMAIL_BOOTS && slot == EquipmentSlotType.FEET)
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 1.0D, AttributeModifier.Operation.ADDITION));
			else if (item == Items.CHAINMAIL_CHESTPLATE && slot == EquipmentSlotType.CHEST || item == Items.CHAINMAIL_LEGGINGS && slot == EquipmentSlotType.LEGS)
				event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage boost", 2.0D, AttributeModifier.Operation.ADDITION));
		}
	}

	@SubscribeEvent
	public static void onInjectLoot(LootTableLoadEvent event) {
		ResourceLocation name = event.getName();
		LootPool pool = event.getTable().getPool("main");

		if (name.equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR) || name.equals(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH) || name.equals(LootTables.CHESTS_END_CITY_TREASURE) || name.equals(LootTables.CHESTS_JUNGLE_TEMPLE)) {
			addEntry(pool, ItemLootEntry.builder(CCItems.SILVER_HORSE_ARMOR.get()).build());
		} else if (name.equals(LootTables.CHESTS_SIMPLE_DUNGEON) || name.equals(LootTables.CHESTS_DESERT_PYRAMID)) {
			addEntry(pool, ItemLootEntry.builder(CCItems.SILVER_HORSE_ARMOR.get()).weight(10).build());
		} else if (name.equals(LootTables.CHESTS_NETHER_BRIDGE)) {
			addEntry(pool, ItemLootEntry.builder(CCItems.SILVER_HORSE_ARMOR.get()).weight(6).build());
		}
	}

	@SuppressWarnings("unchecked")
	private static void addEntry(LootPool pool, LootEntry entry) {
		try {
			List<LootEntry> lootEntries = (List<LootEntry>) ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a").get(pool);
			if (lootEntries.stream().anyMatch(e -> e == entry)) {
				throw new RuntimeException("Attempted to add a duplicate entry to pool: " + entry);
			}
			lootEntries.add(entry);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
