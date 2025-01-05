package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.DismantlingScreen;
import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.ToolboxScreen;
import com.teamabnormals.caverns_and_chasms.common.inventory.DismantlingMenu;
import com.teamabnormals.caverns_and_chasms.common.inventory.ToolboxMenu;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CCMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<MenuType<ToolboxMenu>> TOOLBOX = MENU_TYPES.register("toolbox", () -> new MenuType<>(ToolboxMenu::new, FeatureFlags.VANILLA_SET));
	public static final RegistryObject<MenuType<DismantlingMenu>> DISMANTLING = MENU_TYPES.register("dismantling", () -> new MenuType<>(DismantlingMenu::new, FeatureFlags.VANILLA_SET));

	public static void registerScreenFactories() {
		MenuScreens.register(CCMenuTypes.TOOLBOX.get(), ToolboxScreen::new);
		MenuScreens.register(CCMenuTypes.DISMANTLING.get(), DismantlingScreen::new);
	}
}