package com.teamabnormals.caverns_and_chasms.integration.boatload;

import com.teamabnormals.boatload.common.item.ChestBoatItem;
import com.teamabnormals.boatload.common.item.FurnaceBoatItem;
import com.teamabnormals.boatload.common.item.LargeBoatItem;
import com.teamabnormals.boatload.core.api.BoatloadBoatType;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CCBoatTypes {
	public static final BoatloadBoatType AZALEA = BoatloadBoatType.register(BoatloadBoatType.create(new ResourceLocation(CavernsAndChasms.MOD_ID, "azalea"), () -> CCBlocks.AZALEA_PLANKS.get().asItem(), () -> CCItems.AZALEA_BOAT.get(), () -> CCItems.AZALEA_CHEST_BOAT.get(), () -> CCItems.AZALEA_FURNACE_BOAT.get(), () -> CCItems.LARGE_AZALEA_BOAT.get()));

	public static final Supplier<Item> AZALEA_CHEST_BOAT = () -> new ChestBoatItem(AZALEA);
	public static final Supplier<Item> AZALEA_FURNACE_BOAT = () -> new FurnaceBoatItem(AZALEA);
	public static final Supplier<Item> LARGE_AZALEA_BOAT = () -> new LargeBoatItem(AZALEA);
}