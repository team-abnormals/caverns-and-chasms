package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.entity.*;
import com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonCatEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonParrotEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonWolfEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieCatEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieParrotEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieWolfEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCEntityTypes {
	public static final EntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<CavefishEntity>> CAVEFISH = HELPER.createLivingEntity("cavefish", CavefishEntity::new, MobCategory.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<DeeperEntity>> DEEPER = HELPER.createLivingEntity("deeper", DeeperEntity::new, MobCategory.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<SpiderlingEntity>> SPIDERLING = HELPER.createLivingEntity("spiderling", SpiderlingEntity::new, MobCategory.MONSTER, 0.35F, 0.25F);
	public static final RegistryObject<EntityType<SilverArrowEntity>> SILVER_ARROW = HELPER.createEntity("silver_arrow", SilverArrowEntity::new, SilverArrowEntity::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<KunaiEntity>> KUNAI = HELPER.createEntity("kunai", KunaiEntity::new, KunaiEntity::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<FlyEntity>> FLY = HELPER.createLivingEntity("fly", FlyEntity::new, MobCategory.CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<MimeEntity>> MIME = HELPER.createLivingEntity("mime", MimeEntity::new, MobCategory.MONSTER, 0.6F, 2.1F);
	public static final RegistryObject<EntityType<RatEntity>> RAT = HELPER.createLivingEntity("rat", RatEntity::new, MobCategory.CREATURE, 0.4F, 0.45F);

	public static final RegistryObject<EntityType<ZombieWolfEntity>> ZOMBIE_WOLF = HELPER.createLivingEntity("zombie_wolf", ZombieWolfEntity::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<ZombieCatEntity>> ZOMBIE_CAT = HELPER.createLivingEntity("zombie_cat", ZombieCatEntity::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<ZombieParrotEntity>> ZOMBIE_PARROT = HELPER.createLivingEntity("zombie_parrot", ZombieParrotEntity::new, MobCategory.CREATURE, 0.5F, 0.9F);

	public static final RegistryObject<EntityType<SkeletonWolfEntity>> SKELETON_WOLF = HELPER.createLivingEntity("skeleton_wolf", SkeletonWolfEntity::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<SkeletonCatEntity>> SKELETON_CAT = HELPER.createLivingEntity("skeleton_cat", SkeletonCatEntity::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<SkeletonParrotEntity>> SKELETON_PARROT = HELPER.createLivingEntity("skeleton_parrot", SkeletonParrotEntity::new, MobCategory.CREATURE, 0.5F, 0.9F);

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(CAVEFISH.get(), CavefishEntity.registerAttributes().build());
		event.put(DEEPER.get(), DeeperEntity.createAttributes().build());
		event.put(SPIDERLING.get(), SpiderlingEntity.registerAttributes().build());
		event.put(FLY.get(), FlyEntity.registerAttributes().build());
		event.put(MIME.get(), MimeEntity.registerAttributes().build());
		event.put(RAT.get(), RatEntity.registerAttributes().build());

		event.put(ZOMBIE_WOLF.get(), Wolf.createAttributes().build());
		event.put(ZOMBIE_CAT.get(), Cat.createAttributes().build());
		event.put(ZOMBIE_PARROT.get(), Parrot.createAttributes().build());

		event.put(SKELETON_WOLF.get(), Wolf.createAttributes().build());
		event.put(SKELETON_CAT.get(), Cat.createAttributes().build());
		event.put(SKELETON_PARROT.get(), Parrot.createAttributes().build());
	}

	public static void registerEntitySpawns() {
		SpawnPlacements.register(CAVEFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CavefishEntity::canCavefishSpawn);
		SpawnPlacements.register(DEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MimeEntity::canMimeSpawn);
	}
}
