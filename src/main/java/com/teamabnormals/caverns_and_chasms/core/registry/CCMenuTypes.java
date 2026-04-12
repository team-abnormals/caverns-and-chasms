package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.AtoningScreen;
import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.BejeweledAnvilScreen;
import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.DismantlingScreen;
import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.ToolboxScreen;
import com.teamabnormals.caverns_and_chasms.common.inventory.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, value = Dist.CLIENT)
public class CCMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<MenuType<?>, MenuType<ToolboxMenu>> TOOLBOX = MENUS.register("toolbox", () -> new MenuType<>(ToolboxMenu::new, FeatureFlags.VANILLA_SET));
	public static final DeferredHolder<MenuType<?>, MenuType<StorageDuctMenu>> STORAGE_DUCT = MENUS.register("storage_duct", () -> new MenuType<>(StorageDuctMenu::new, FeatureFlags.VANILLA_SET));
	public static final DeferredHolder<MenuType<?>, MenuType<DismantlingMenu>> DISMANTLING = MENUS.register("dismantling", () -> new MenuType<>(DismantlingMenu::new, FeatureFlags.VANILLA_SET));
	public static final DeferredHolder<MenuType<?>, MenuType<BejeweledAnvilMenu>> BEJEWELED_ANVIL = MENUS.register("bejeweled_anvil", () -> new MenuType<>(BejeweledAnvilMenu::new, FeatureFlags.VANILLA_SET));
	public static final DeferredHolder<MenuType<?>, MenuType<AtoningMenu>> ATONING = MENUS.register("atoning", () -> new MenuType<>(AtoningMenu::new, FeatureFlags.VANILLA_SET));

	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(TOOLBOX.get(), ToolboxScreen::new);
		event.register(DISMANTLING.get(), DismantlingScreen::new);
		event.register(BEJEWELED_ANVIL.get(), BejeweledAnvilScreen::new);
		event.register(ATONING.get(), AtoningScreen::new);
	}
}