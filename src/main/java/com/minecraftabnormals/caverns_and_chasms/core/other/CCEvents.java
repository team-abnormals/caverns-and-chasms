package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.SpiderlingEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID)
public class CCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof IronGolemEntity && !CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			IronGolemEntity golem = (IronGolemEntity) event.getEntity();
			golem.targetSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(goal -> {
				golem.targetSelector.removeGoal(goal);
				golem.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(golem, MobEntity.class, 5, false, false, (entity) -> entity instanceof IMob));
			});
		}
		if (event.getEntity() instanceof CreeperEntity && !CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			CreeperEntity creeper = (CreeperEntity) event.getEntity();
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
		if (target instanceof SpiderEntity && stack.getItem() == Items.SPIDER_SPAWN_EGG) {
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
		if (event.getEntity().getType() == EntityType.CREEPER) {
			event.setResult(Event.Result.DENY);
		}
	}

	@SubscribeEvent
	public static void onEvent(LivingSpawnEvent.CheckSpawn event) {
		Entity entity = event.getEntity();
		IWorld world = event.getWorld();
		boolean validSpawn = event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION;
		if (event.getResult() != Event.Result.DENY && validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < CCConfig.COMMON.deeperMaxSpawnHeight.get()) {
			CreeperEntity creeper = (CreeperEntity) entity;
			if (world.getBlockState(creeper.getPosition().down()).isIn(CCTags.DEEPER_SPAWN_BLOCKS)) {
				DeeperEntity deeper = CCEntities.DEEPER.get().create(world.getWorld());
				if (deeper != null) {
					deeper.setLocationAndAngles(creeper.getPosX(), creeper.getPosY(), creeper.getPosZ(), creeper.rotationYaw, creeper.rotationPitch);
					world.addEntity(deeper);
					entity.remove();
				}
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
				entity.attemptTeleport(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"), true);
				entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public static void potionExpireEvent(PotionEvent.PotionExpiryEvent event) {
		if (event.getPotionEffect().getPotion() == CCEffects.REWIND.get()) {
			LivingEntity entity = event.getEntityLiving();
			CompoundNBT data = entity.getPersistentData();
			if (data.contains("RewindX") && data.contains("RewindY") && data.contains("RewindZ")) {
				entity.attemptTeleport(data.getDouble("RewindX"), data.getDouble("RewindY"), data.getDouble("RewindZ"), true);
				entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}
}
