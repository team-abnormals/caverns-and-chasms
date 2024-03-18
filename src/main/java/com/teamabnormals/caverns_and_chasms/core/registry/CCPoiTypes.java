package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;

public final class CCPoiTypes {
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<PoiType> LIGHTNING_ROD = POI_TYPES.register("beehive", () -> new PoiType(CCBlocks.HELPER.getDeferredRegister().getEntries().stream().filter(block -> block.get() instanceof LightningRodBlock).map(block -> block.get().getStateDefinition().getPossibleStates()).collect(HashSet::new, HashSet::addAll, HashSet::addAll), 0, 1));
}