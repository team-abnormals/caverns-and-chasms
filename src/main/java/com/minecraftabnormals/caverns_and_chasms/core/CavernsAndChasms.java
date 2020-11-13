package com.minecraftabnormals.caverns_and_chasms.core;

import com.minecraftabnormals.caverns_and_chasms.client.DeeperSpriteUploader;
import com.minecraftabnormals.caverns_and_chasms.core.other.CCCompat;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCFeatures;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CavernsAndChasms.MODID)
public class CavernsAndChasms {
	public static final String MODID = "caverns_and_chasms";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MODID);

	public CavernsAndChasms() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.getDeferredBlockRegister().register(bus);
		REGISTRY_HELPER.getDeferredItemRegister().register(bus);
		REGISTRY_HELPER.getDeferredTileEntityRegister().register(bus);
		REGISTRY_HELPER.getDeferredEntityRegister().register(bus);
		REGISTRY_HELPER.getDeferredSoundRegister().register(bus);

		CCEffects.POTIONS.register(bus);
		CCEffects.EFFECTS.register(bus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CCConfig.COMMON_SPEC);

		bus.addListener(this::commonSetup);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::clientSetup);
			bus.addListener(this::registerItemColors);
			DeeperSpriteUploader.init(bus);
		});
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			CCEntities.registerAttributes();
			CCEntities.registerEntitySpawns();
			CCFeatures.registerFeatures();
			CCEffects.registerBrewingRecipes();
			CCCompat.registerDispenserBehaviors();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			CCCompat.registerRenderLayers();
			CCItems.registerItemProperties();
			CCEntities.registerRenderers();
		});
	}

	private void registerItemColors(ColorHandlerEvent.Item event) {
		REGISTRY_HELPER.processSpawnEggColors(event);
	}
}
