package com.minecraftabnormals.cave_upgrade.core;

import com.minecraftabnormals.cave_upgrade.client.DeeperSpriteUploader;
import com.minecraftabnormals.cave_upgrade.core.registry.CUEntities;
import com.minecraftabnormals.cave_upgrade.core.registry.CUItems;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CaveUpgrade.MODID)
public class CaveUpgrade {
	public static final String MODID = "cave_upgrade";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MODID);

	public CaveUpgrade() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.getDeferredBlockRegister().register(bus);
		REGISTRY_HELPER.getDeferredItemRegister().register(bus);
		REGISTRY_HELPER.getDeferredTileEntityRegister().register(bus);
		REGISTRY_HELPER.getDeferredEntityRegister().register(bus);
		REGISTRY_HELPER.getDeferredSoundRegister().register(bus);

		bus.addListener(this::commonSetup);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::clientSetup);
			bus.addListener(this::registerItemColors);
			DeeperSpriteUploader.init(bus);
		});
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			CUEntities.registerAttributes();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			CUItems.registerItemProperties();
			CUEntities.registerRenderers();
		});
	}

	private void registerItemColors(ColorHandlerEvent.Item event) {
		REGISTRY_HELPER.processSpawnEggColors(event);
	}
}
