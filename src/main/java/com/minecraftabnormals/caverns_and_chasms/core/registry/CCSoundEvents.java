package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.common.block.*;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCSoundEvents {
	public static final RegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER;

	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_DEATH = HELPER.createSoundEvent("entity.deeper.death");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_HURT = HELPER.createSoundEvent("entity.deeper.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_PRIMED = HELPER.createSoundEvent("entity.deeper.primed");
}