package com.minecraftabnormals.caverns_and_chasms.core.registry;

import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCSoundEvents {
	public static final SoundSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getSoundSubHelper();

	public static final RegistryObject<SoundEvent> EPILOGUE = HELPER.createSoundEvent("music.record.epilogue");

	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_DEATH = HELPER.createSoundEvent("entity.deeper.death");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_HURT = HELPER.createSoundEvent("entity.deeper.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_PRIMED = HELPER.createSoundEvent("entity.deeper.primed");
}