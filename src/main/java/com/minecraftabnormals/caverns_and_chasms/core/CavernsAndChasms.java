package com.minecraftabnormals.caverns_and_chasms.core;

import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import com.minecraftabnormals.caverns_and_chasms.client.DeeperSpriteUploader;
import com.minecraftabnormals.caverns_and_chasms.core.other.CCCompat;
import com.minecraftabnormals.caverns_and_chasms.core.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CavernsAndChasms.MOD_ID)
public class CavernsAndChasms {
	public static final String MOD_ID = "caverns_and_chasms";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	public CavernsAndChasms() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);
		CCAttributes.ATTRIBUTES.register(bus);
		CCEffects.POTIONS.register(bus);
		CCEffects.EFFECTS.register(bus);
		CCEnchantments.ENCHANTMENTS.register(bus);
		CCParticles.PARTICLES.register(bus);
		CCRecipes.Serailizers.RECIPE_SERIALIZERS.register(bus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CCConfig.COMMON_SPEC);

		bus.addListener(this::commonSetup);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::clientSetup);
			DeeperSpriteUploader.init(bus);
		});
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CCEntities.registerAttributes();
			CCEntities.registerEntitySpawns();
			CCFeatures.Configured.registerConfiguredFeatures();
			CCEffects.registerBrewingRecipes();
			CCCompat.registerDispenserBehaviors();
			CCEnchantments.registerEnchantmentTypes();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		CCTileEntities.registerRenderers();
		CCEntities.registerRenderers();
		event.enqueueWork(() -> {
			CCCompat.registerRenderLayers();
			CCItems.registerItemProperties();
		});
	}
}
