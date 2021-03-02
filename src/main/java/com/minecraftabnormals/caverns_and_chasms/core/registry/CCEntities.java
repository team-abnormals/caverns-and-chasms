package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.caverns_and_chasms.client.render.*;
import com.minecraftabnormals.caverns_and_chasms.common.entity.*;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCEntities {
	public static final EntitySubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<CavefishEntity>> CAVEFISH = HELPER.createLivingEntity("cavefish", CavefishEntity::new, EntityClassification.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<DeeperEntity>> DEEPER = HELPER.createLivingEntity("deeper", DeeperEntity::new, EntityClassification.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<SpiderlingEntity>> SPIDERLING = HELPER.createLivingEntity("spiderling", SpiderlingEntity::new, EntityClassification.MONSTER, 0.35F, 0.25F);
	public static final RegistryObject<EntityType<RottenEggEntity>> ROTTEN_EGG = HELPER.createEntity("rotten_egg", RottenEggEntity::new, RottenEggEntity::new, EntityClassification.MISC, 0.25F, 0.25F);
	public static final RegistryObject<EntityType<SilverArrowEntity>> SILVER_ARROW = HELPER.createEntity("silver_arrow", SilverArrowEntity::new, SilverArrowEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<KunaiEntity>> THROWING_KNIFE = HELPER.createEntity("kunai", KunaiEntity::new, KunaiEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<ZombieChickenEntity>> ZOMBIE_CHICKEN = HELPER.createLivingEntity("zombie_chicken", ZombieChickenEntity::new, EntityClassification.MONSTER, 0.4F, 0.7F);
	public static final RegistryObject<EntityType<FlyEntity>> FLY = HELPER.createLivingEntity("fly", FlyEntity::new, EntityClassification.CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<MimeEntity>> MIME = HELPER.createLivingEntity("mime", MimeEntity::new, EntityClassification.MONSTER, 0.6F, 2.1F);

	public static void registerAttributes() {
		GlobalEntityTypeAttributes.put(CAVEFISH.get(), CavefishEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(DEEPER.get(), DeeperEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(SPIDERLING.get(), SpiderlingEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(ZOMBIE_CHICKEN.get(), ZombieChickenEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(FLY.get(), FlyEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(MIME.get(), MimeEntity.registerAttributes().create());
	}

	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(CAVEFISH.get(), CavefishRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(DEEPER.get(), DeeperRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SPIDERLING.get(), SpiderlingRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ROTTEN_EGG.get(), RottenEggRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SILVER_ARROW.get(), SilverArrowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(THROWING_KNIFE.get(), KunaiRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ZOMBIE_CHICKEN.get(), ZombieChickenRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(FLY.get(), FlyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MIME.get(), MimeRenderer::new);
	}

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(CAVEFISH.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CavefishEntity::canCavefishSpawn);
		EntitySpawnPlacementRegistry.register(DEEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(MIME.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MimeEntity::canMimeSpawn);
	}
}
