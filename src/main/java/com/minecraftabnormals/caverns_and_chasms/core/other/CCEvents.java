package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.FlyEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.SpiderlingEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.ZombieChickenEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID)
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
				ItemStack milkBucket = DrinkHelper.func_241445_a_(stack.copy(), player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
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

	private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
	private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);

	@SubscribeEvent
	public static void onEvent(LivingSpawnEvent.CheckSpawn event) {
		Entity entity = event.getEntity();
		IWorld world = event.getWorld();
		boolean validSpawn = event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION;
		if (event.getResult() != Event.Result.DENY) {
			if (validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < CCConfig.COMMON.deeperMaxSpawnHeight.get()) {
				CreeperEntity creeper = (CreeperEntity) entity;
				if (world.getBlockState(creeper.getPosition().down()).isIn(CCTags.Blocks.DEEPER_SPAWN_BLOCKS)) {
					DeeperEntity deeper = CCEntities.DEEPER.get().create(world.getWorld());
					if (deeper != null) {
						deeper.copyLocationAndAnglesFrom(creeper);
						world.addEntity(deeper);
						entity.remove();
					}
				}
			}

			if (event.getSpawnReason() == SpawnReason.NATURAL && entity instanceof ZombieEntity) {
				ZombieEntity zombie = (ZombieEntity) entity;
				if (zombie.isChild() && zombie.getRidingEntity() != null && zombie.getRidingEntity().getType() == EntityType.CHICKEN) {

					ChickenEntity chicken = (ChickenEntity)zombie.getRidingEntity();
					zombie.getRidingEntity().remove();
					zombie.stopRiding();
					zombie.remove();

					ZombieChickenEntity zombieChicken = CCEntities.ZOMBIE_CHICKEN.get().create(world.getWorld());
					zombieChicken.copyLocationAndAnglesFrom(chicken);
					zombieChicken.onInitialSpawn(world, world.getDifficultyForLocation(chicken.getPosition()), SpawnReason.JOCKEY, null, null);
					zombieChicken.setChickenJockey(true);
					if (event.getWorld() != null && !event.getWorld().getWorld().isRemote) {
						ModifiableAttributeInstance instance = zombieChicken.getAttribute(Attributes.MOVEMENT_SPEED);
						instance.applyNonPersistentModifier(BABY_SPEED_BOOST);
					}
					world.addEntity(zombieChicken);

					ZombieEntity newZombie = (ZombieEntity) zombie.getType().create(world.getWorld());
					newZombie.setChild(true);
					for(EquipmentSlotType slot : EquipmentSlotType.values())
						newZombie.setItemStackToSlot(slot, zombie.getItemStackFromSlot(slot));
					newZombie.copyLocationAndAnglesFrom(zombie);
					newZombie.onInitialSpawn(event.getWorld(), event.getWorld().getDifficultyForLocation(zombieChicken.getPosition()), SpawnReason.JOCKEY, null, null);
					newZombie.startRiding(zombieChicken, true);
					world.addEntity(newZombie);
				}
			}
		}
	}

	@SubscribeEvent
	public static void bonusXPBlock(BlockEvent.BreakEvent event) {
		if (event.getPlayer().getHeldItemMainhand().getItem().isIn(CCTags.Items.XP_BOOST_TOOLS)) {
			event.setExpToDrop(event.getExpToDrop() + (event.getExpToDrop() / 2));
		}
	}

	@SubscribeEvent
	public static void bonusXPMobs(LivingExperienceDropEvent event) {
		if (event.getAttackingPlayer() != null) {
			if (event.getAttackingPlayer().getHeldItemMainhand().getItem().isIn(CCTags.Items.XP_BOOST_TOOLS)) {
				event.setDroppedExperience(event.getDroppedExperience() + (event.getDroppedExperience() / 2));
			}
		}
	}

	@SubscribeEvent
	public static void potionAddedEvent(PotionEvent.PotionAddedEvent event) {
		if (event.getPotionEffect().getPotion() == CCEffects.REWIND.get()) {
			LivingEntity entity = event.getEntityLiving();
			CompoundNBT data = entity.getPersistentData();
			data.putDouble("RewindX", entity.getPosX());
			data.putDouble("RewindY", entity.getPosY());
			data.putDouble("RewindZ", entity.getPosZ());
		}
	}

	@SubscribeEvent
	public static void potionRemoveEvent(PotionEvent.PotionRemoveEvent event) {
		if (event.getPotionEffect().getPotion() == CCEffects.REWIND.get()) {
			LivingEntity entity = event.getEntityLiving();
			CompoundNBT data = entity.getPersistentData();
			if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
				entity.setPositionAndUpdate(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
				entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public static void potionExpireEvent(PotionEvent.PotionExpiryEvent event) {
		if (event.getPotionEffect().getPotion() == CCEffects.AFFLICTION.get()) {
			event.getEntityLiving().attackEntityFrom(CCDamageSources.AFFLICTION, (event.getPotionEffect().getAmplifier() + 1) * 3);
		}

		if (event.getPotionEffect().getPotion() == CCEffects.REWIND.get()) {
			LivingEntity entity = event.getEntityLiving();
			CompoundNBT data = entity.getPersistentData();
			if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
				entity.setPositionAndUpdate(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
				entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public static void handleAffliction(LivingDamageEvent event) {
		LivingEntity entity = event.getEntityLiving();
		float chance = 0.0F;
		Random rand = new Random();

		ItemStack head = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
		ItemStack chest = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);
		ItemStack legs = entity.getItemStackFromSlot(EquipmentSlotType.LEGS);
		ItemStack feet = entity.getItemStackFromSlot(EquipmentSlotType.FEET);

		if (event.getSource().getTrueSource() instanceof LivingEntity)
			if (head.getItem() == CCItems.SILVER_HELMET.get()) {
				chance += 0.25F;
				if (chest.getItem() == CCItems.SILVER_CHESTPLATE.get()) chance += 0.25F;
				if (legs.getItem() == CCItems.SILVER_LEGGINGS.get()) chance += 0.25F;
				if (feet.getItem() == CCItems.SILVER_BOOTS.get()) chance += 0.25F;
				if (rand.nextFloat() <= chance) {
					LivingEntity livingEntity = (LivingEntity) event.getSource().getTrueSource();
					livingEntity.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
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
}
