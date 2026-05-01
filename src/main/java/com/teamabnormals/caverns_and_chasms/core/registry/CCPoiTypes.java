package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.LightningRodBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;

public final class CCPoiTypes {
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<PoiType, PoiType> LIGHTNING_ROD = POI_TYPES.register("lightning_rod", () -> new PoiType(CCBlocks.BLOCKS.getDeferredRegister().getEntries().stream().filter(block -> block.get() instanceof LightningRodBlock).map(block -> block.get().getStateDefinition().getPossibleStates()).collect(HashSet::new, HashSet::addAll, HashSet::addAll), 0, 1));
}