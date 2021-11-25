package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.common.entity.*;
import com.teamabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonCat;
import com.teamabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonParrot;
import com.teamabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonWolf;
import com.teamabnormals.caverns_and_chasms.common.entity.zombie.ZombieCat;
import com.teamabnormals.caverns_and_chasms.common.entity.zombie.ZombieParrot;
import com.teamabnormals.caverns_and_chasms.common.entity.zombie.ZombieWolf;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
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

	public static final RegistryObject<EntityType<Cavefish>> CAVEFISH = HELPER.createLivingEntity("cavefish", Cavefish::new, MobCategory.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<Deeper>> DEEPER = HELPER.createLivingEntity("deeper", Deeper::new, MobCategory.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<Spiderling>> SPIDERLING = HELPER.createLivingEntity("spiderling", Spiderling::new, MobCategory.MONSTER, 0.35F, 0.25F);
	public static final RegistryObject<EntityType<SilverArrow>> SILVER_ARROW = HELPER.createEntity("silver_arrow", SilverArrow::new, SilverArrow::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<Kunai>> KUNAI = HELPER.createEntity("kunai", Kunai::new, Kunai::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<Fly>> FLY = HELPER.createLivingEntity("fly", Fly::new, MobCategory.CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<Mime>> MIME = HELPER.createLivingEntity("mime", Mime::new, MobCategory.MONSTER, 0.6F, 2.1F);
	public static final RegistryObject<EntityType<Rat>> RAT = HELPER.createLivingEntity("rat", Rat::new, MobCategory.CREATURE, 0.4F, 0.45F);
	public static final RegistryObject<EntityType<ThrownSpinelPearl>> SPINEL_PEARL = HELPER.createEntity("spinel_pearl", ThrownSpinelPearl::new, ThrownSpinelPearl::new, MobCategory.MISC, 0.25F, 0.25F);

	public static final RegistryObject<EntityType<ZombieWolf>> ZOMBIE_WOLF = HELPER.createLivingEntity("zombie_wolf", ZombieWolf::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<ZombieCat>> ZOMBIE_CAT = HELPER.createLivingEntity("zombie_cat", ZombieCat::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<ZombieParrot>> ZOMBIE_PARROT = HELPER.createLivingEntity("zombie_parrot", ZombieParrot::new, MobCategory.CREATURE, 0.5F, 0.9F);

	public static final RegistryObject<EntityType<SkeletonWolf>> SKELETON_WOLF = HELPER.createLivingEntity("skeleton_wolf", SkeletonWolf::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<SkeletonCat>> SKELETON_CAT = HELPER.createLivingEntity("skeleton_cat", SkeletonCat::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<SkeletonParrot>> SKELETON_PARROT = HELPER.createLivingEntity("skeleton_parrot", SkeletonParrot::new, MobCategory.CREATURE, 0.5F, 0.9F);

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(CAVEFISH.get(), Cavefish.registerAttributes().build());
		event.put(DEEPER.get(), Deeper.createAttributes().build());
		event.put(SPIDERLING.get(), Spiderling.registerAttributes().build());
		event.put(FLY.get(), Fly.registerAttributes().build());
		event.put(MIME.get(), Mime.registerAttributes().build());
		event.put(RAT.get(), Rat.registerAttributes().build());

		event.put(ZOMBIE_WOLF.get(), Wolf.createAttributes().build());
		event.put(ZOMBIE_CAT.get(), Cat.createAttributes().build());
		event.put(ZOMBIE_PARROT.get(), Parrot.createAttributes().build());

		event.put(SKELETON_WOLF.get(), Wolf.createAttributes().build());
		event.put(SKELETON_CAT.get(), Cat.createAttributes().build());
		event.put(SKELETON_PARROT.get(), Parrot.createAttributes().build());
	}

	public static void registerEntitySpawns() {
		SpawnPlacements.register(CAVEFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Cavefish::canCavefishSpawn);
		SpawnPlacements.register(DEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(MIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mime::canMimeSpawn);
	}
}
