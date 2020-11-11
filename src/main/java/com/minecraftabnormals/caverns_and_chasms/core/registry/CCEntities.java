package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.client.render.BabySpiderRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.CavefishRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.DeeperRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.BabySpiderEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.CavefishEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCEntities {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;

	public static final RegistryObject<EntityType<CavefishEntity>> CAVEFISH = HELPER.createLivingEntity("cavefish", CavefishEntity::new, EntityClassification.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<DeeperEntity>> DEEPER = HELPER.createLivingEntity("deeper", DeeperEntity::new, EntityClassification.MONSTER, 0.6F, 1.7F);
	public static final RegistryObject<EntityType<BabySpiderEntity>> BABY_SPIDER = HELPER.createLivingEntity("baby_spider", BabySpiderEntity::new, EntityClassification.MONSTER, 0.35F, 0.25F);

	public static void registerAttributes() {
		GlobalEntityTypeAttributes.put(CAVEFISH.get(), CavefishEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(DEEPER.get(), DeeperEntity.func_234278_m_().create());
		GlobalEntityTypeAttributes.put(BABY_SPIDER.get(), BabySpiderEntity.registerAttributes().create());
	}

	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(CAVEFISH.get(), CavefishRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(DEEPER.get(), DeeperRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BABY_SPIDER.get(), BabySpiderRenderer::new);
	}

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(CAVEFISH.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CavefishEntity::canCavefishSpawn);
		EntitySpawnPlacementRegistry.register(DEEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);

		ForgeRegistries.BIOMES.getValues().forEach(CCEntities::processSpawning);
	}

	private static void processSpawning(Biome biome) {
		if (biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.BEACH) {
			biome.getSpawns(EntityClassification.WATER_CREATURE).add(new Biome.SpawnListEntry(CAVEFISH.get(), 300, 3, 7));
		}
	}
}
