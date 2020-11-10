package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.client.render.CavefishRenderer;
import com.minecraftabnormals.caverns_and_chasms.client.render.DeeperRenderer;
import com.minecraftabnormals.caverns_and_chasms.common.entity.CavefishEntity;
import com.minecraftabnormals.caverns_and_chasms.common.entity.DeeperEntity;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCEntities {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;
	
	public static final RegistryObject<EntityType<CavefishEntity>> CAVEFISH = HELPER.createLivingEntity("cavefish", CavefishEntity::new, EntityClassification.WATER_CREATURE, 0.4F, 0.4F);
	public static final RegistryObject<EntityType<DeeperEntity>> DEEPER = HELPER.createLivingEntity("deeper", DeeperEntity::new, EntityClassification.MONSTER, 0.6F, 1.7F);
	
	public static void registerAttributes() {
		GlobalEntityTypeAttributes.put(CAVEFISH.get(), CavefishEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(DEEPER.get(), DeeperEntity.func_234278_m_().create());
	}
	
	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(CAVEFISH.get(), CavefishRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(DEEPER.get(), DeeperRenderer::new);
	}
}
