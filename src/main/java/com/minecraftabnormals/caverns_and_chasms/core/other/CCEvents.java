package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.FlyEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.SpiderlingEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.ZombieChickenEntity;
import com.minecraftabnormals.caverns_and_chasms.common.item.necromium.NecromiumHorseArmorItem;
import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Random;

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
	public static void onEvent(LivingSpawnEvent.CheckSpawn event) {
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
		if (event.getPotionEffect().getPotion() == CCEffects.REWIND.get()) {
			LivingEntity entity = event.getEntityLiving();
			CompoundNBT data = entity.getPersistentData();
			data.putString("RewindDimension", entity.getEntityWorld().getDimensionKey().getLocation().toString());
			data.putDouble("RewindX", entity.getPosX());
			data.putDouble("RewindY", entity.getPosY());
			data.putDouble("RewindZ", entity.getPosZ());
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
							entity.changeDimension(dimension);
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
							entity.changeDimension(dimension);
					}
					entity.setPositionAndUpdate(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"));
					entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		LivingEntity target = event.getEntityLiving();
		Random rand = new Random();

		if (event.getSource().getTrueSource() instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
			float afflictionChance = 0;
			float weaknessAmount = 0;

			for (EquipmentSlotType slot : EquipmentSlotType.values()) {
				ItemStack stack = target.getItemStackFromSlot(slot);

				Collection<AttributeModifier> afflictionModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.AFFLICTION_CHANCE.get());
				if (!afflictionModifiers.isEmpty())
					afflictionChance += afflictionModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();

				Collection<AttributeModifier> weaknessModifiers = stack.getAttributeModifiers(slot).get(CCAttributes.WEAKNESS_AURA.get());
				if (!weaknessModifiers.isEmpty()) {
					weaknessAmount += weaknessModifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				}
			}

			if (rand.nextFloat() < afflictionChance) {
				if (attacker.isEntityUndead())
					attacker.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
			}

			if (weaknessAmount != 0) {
				for (LivingEntity entity : target.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(weaknessAmount, 0.0D, weaknessAmount))) {
					if (entity != target)
						entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 60));
				}
			}

			if (target.getActivePotionEffect(CCEffects.AFFLICTION.get()) != null) {
				EffectInstance affliction = target.getActivePotionEffect(CCEffects.AFFLICTION.get());
				affliction.amplifier += 1;
				affliction.duration += 10;
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
}
