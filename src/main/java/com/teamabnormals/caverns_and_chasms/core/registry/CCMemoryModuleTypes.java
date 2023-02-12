package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCMemoryModuleTypes {
	public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_DARK_BLOCK = MEMORY_MODULE_TYPES.register("nearest_dark_block", () -> new MemoryModuleType<>(Optional.empty()));
	public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_SPORE_BLOSSOM = MEMORY_MODULE_TYPES.register("nearest_spore_blossom", () -> new MemoryModuleType<>(Optional.empty()));
	public static final RegistryObject<MemoryModuleType<UUID>> DISLIKED_PLAYER = MEMORY_MODULE_TYPES.register("disliked_player", () -> new MemoryModuleType<>(Optional.of(UUIDUtil.CODEC)));
}