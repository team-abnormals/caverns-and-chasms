package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.caverns_and_chasms.client.render.*;
import com.minecraftabnormals.caverns_and_chasms.client.render.layer.UndeadParrotLayer;
import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonCatRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.skeleton.SkeletonWolfRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieCatRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieChickenRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieParrotRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.zombie.ZombieWolfRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.*;
import com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonCatEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonParrotEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.skeleton.SkeletonWolfEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieCatEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieChickenEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieParrotEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.zombie.ZombieWolfEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCEntities {
	public static final EntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<CavefishEntity>> CAVEFISH = HELPER.createLivingEntity("cavefish", CavefishEntity::new, EntityClassification.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<DeeperEntity>> DEEPER = HELPER.createLivingEntity("deeper", DeeperEntity::new, EntityClassification.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<SpiderlingEntity>> SPIDERLING = HELPER.createLivingEntity("spiderling", SpiderlingEntity::new, EntityClassification.MONSTER, 0.35F, 0.25F);
	public static final RegistryObject<EntityType<RottenEggEntity>> ROTTEN_EGG = HELPER.createEntity("rotten_egg", RottenEggEntity::new, RottenEggEntity::new, EntityClassification.MISC, 0.25F, 0.25F);
	public static final RegistryObject<EntityType<SilverArrowEntity>> SILVER_ARROW = HELPER.createEntity("silver_arrow", SilverArrowEntity::new, SilverArrowEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<KunaiEntity>> KUNAI = HELPER.createEntity("kunai", KunaiEntity::new, KunaiEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<FlyEntity>> FLY = HELPER.createLivingEntity("fly", FlyEntity::new, EntityClassification.CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<MimeEntity>> MIME = HELPER.createLivingEntity("mime", MimeEntity::new, EntityClassification.MONSTER, 0.6F, 2.1F);
	public static final RegistryObject<EntityType<RatEntity>> RAT = HELPER.createLivingEntity("rat", RatEntity::new, EntityClassification.CREATURE, 0.4F, 0.45F);

	public static final RegistryObject<EntityType<ZombieChickenEntity>> ZOMBIE_CHICKEN = HELPER.createLivingEntity("zombie_chicken", ZombieChickenEntity::new, EntityClassification.MONSTER, 0.4F, 0.7F);
	public static final RegistryObject<EntityType<ZombieWolfEntity>> ZOMBIE_WOLF = HELPER.createLivingEntity("zombie_wolf", ZombieWolfEntity::new, EntityClassification.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<ZombieCatEntity>> ZOMBIE_CAT = HELPER.createLivingEntity("zombie_cat", ZombieCatEntity::new, EntityClassification.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<ZombieParrotEntity>> ZOMBIE_PARROT = HELPER.createLivingEntity("zombie_parrot", ZombieParrotEntity::new, EntityClassification.CREATURE, 0.5F, 0.9F);

	public static final RegistryObject<EntityType<SkeletonWolfEntity>> SKELETON_WOLF = HELPER.createLivingEntity("skeleton_wolf", SkeletonWolfEntity::new, EntityClassification.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<SkeletonCatEntity>> SKELETON_CAT = HELPER.createLivingEntity("skeleton_cat", SkeletonCatEntity::new, EntityClassification.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<SkeletonParrotEntity>> SKELETON_PARROT = HELPER.createLivingEntity("skeleton_parrot", SkeletonParrotEntity::new, EntityClassification.CREATURE, 0.5F, 0.9F);

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(CAVEFISH.get(), CavefishEntity.registerAttributes().build());
		event.put(DEEPER.get(), DeeperEntity.createAttributes().build());
		event.put(SPIDERLING.get(), SpiderlingEntity.registerAttributes().build());
		event.put(FLY.get(), FlyEntity.registerAttributes().build());
		event.put(MIME.get(), MimeEntity.registerAttributes().build());
		event.put(RAT.get(), RatEntity.registerAttributes().build());

		event.put(ZOMBIE_CHICKEN.get(), ZombieChickenEntity.registerAttributes().build());
		event.put(ZOMBIE_WOLF.get(), WolfEntity.createAttributes().build());
		event.put(ZOMBIE_CAT.get(), CatEntity.createAttributes().build());
		event.put(ZOMBIE_PARROT.get(), ParrotEntity.createAttributes().build());

		event.put(SKELETON_WOLF.get(), WolfEntity.createAttributes().build());
		event.put(SKELETON_CAT.get(), CatEntity.createAttributes().build());
		event.put(SKELETON_PARROT.get(), ParrotEntity.createAttributes().build());
	}

	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(CAVEFISH.get(), CavefishRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(DEEPER.get(), DeeperRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SPIDERLING.get(), SpiderlingRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ROTTEN_EGG.get(), RottenEggRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SILVER_ARROW.get(), SilverArrowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(KUNAI.get(), KunaiRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(FLY.get(), FlyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MIME.get(), MimeRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(RAT.get(), RatRenderer::new);

		RenderingRegistry.registerEntityRenderingHandler(ZOMBIE_CHICKEN.get(), ZombieChickenRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ZOMBIE_WOLF.get(), ZombieWolfRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ZOMBIE_CAT.get(), ZombieCatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ZOMBIE_PARROT.get(), ZombieParrotRenderer::new);

		RenderingRegistry.registerEntityRenderingHandler(SKELETON_WOLF.get(), SkeletonWolfRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SKELETON_CAT.get(), SkeletonCatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SKELETON_PARROT.get(), SkeletonParrotRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayers(FMLClientSetupEvent event) {
		event.getMinecraftSupplier().get().getEntityRenderDispatcher().getSkinMap().forEach(((s, playerRenderer) -> {
			playerRenderer.addLayer(new UndeadParrotLayer<>(playerRenderer));
		}));
	}

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(CAVEFISH.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CavefishEntity::canCavefishSpawn);
		EntitySpawnPlacementRegistry.register(DEEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(MIME.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MimeEntity::canMimeSpawn);
	}
}
