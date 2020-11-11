package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CCConfig;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
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
		CompoundNBT tag = stack.getOrCreateTag();
		Hand hand = event.getHand();
		if (event.getTarget() instanceof CowEntity && !((CowEntity) event.getTarget()).isChild()) {
			if (stack.getItem() == CCItems.GOLDEN_BUCKET.get()) {
				player.swingArm(hand);
				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
				ItemStack milkBucket = DrinkHelper.func_241445_a_(stack, player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
				player.setHeldItem(hand, milkBucket);
			} else if (stack.getItem() == CCItems.GOLDEN_MILK_BUCKET.get() && tag.getInt("FluidLevel") < 2) {
				player.swingArm(hand);
				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
				ItemStack milkBucket = DrinkHelper.func_241445_a_(stack, player, CCItems.GOLDEN_MILK_BUCKET.get().getDefaultInstance());
				if (!player.abilities.isCreativeMode)
					milkBucket.getOrCreateTag().putInt("FluidLevel", tag.getInt("FluidLevel") + 1);
				player.setHeldItem(hand, milkBucket);
			}
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		if (event.getExplosion().getExplosivePlacedBy().getType() == EntityType.CREEPER) {
			if (!CCConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
				event.getAffectedBlocks().clear();
			}
		}
	}

	@SubscribeEvent
	public static void onEvent(LivingSpawnEvent.CheckSpawn event) {
		Entity entity = event.getEntity();
		IWorld world = event.getWorld();
		boolean validSpawn = event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.CHUNK_GENERATION;
		if (event.getResult() != Event.Result.DENY && validSpawn && entity.getType() == EntityType.CREEPER && event.getY() < 60) {
			CreeperEntity creeper = (CreeperEntity) event.getEntity();
			if (world.getBlockState(creeper.getPosition().down()).isIn(CCTags.DEEPER_SPAWN_BLOCKS)) {
				DeeperEntity deeper = CCEntities.DEEPER.get().create(world.getWorld());
				deeper.setLocationAndAngles(creeper.getPosX(), creeper.getPosY(), creeper.getPosZ(), creeper.rotationYaw, creeper.rotationPitch);
				world.addEntity(deeper);
				entity.remove();
			}
		}
	}
}
