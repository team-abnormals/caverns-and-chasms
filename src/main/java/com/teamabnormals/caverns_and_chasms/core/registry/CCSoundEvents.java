package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCSoundEvents {
	public static final SoundSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getSoundSubHelper();

	public static final RegistryObject<SoundEvent> EPILOGUE = HELPER.createSoundEvent("music.record.epilogue");

	public static final RegistryObject<SoundEvent> ROCKY_DIRT_BREAK = HELPER.createSoundEvent("block.rocky_dirt.break");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_FALL = HELPER.createSoundEvent("block.rocky_dirt.fall");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_HIT = HELPER.createSoundEvent("block.rocky_dirt.hit");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_PLACE = HELPER.createSoundEvent("block.rocky_dirt.place");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_STEP = HELPER.createSoundEvent("block.rocky_dirt.step");

	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_DEATH = HELPER.createSoundEvent("entity.deeper.death");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_HURT = HELPER.createSoundEvent("entity.deeper.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_PRIMED = HELPER.createSoundEvent("entity.deeper.primed");

	public static class CCSoundTypes {
		public static final SoundType ROCKY_DIRT = new SoundType(1.0F, 1.0F, CCSoundEvents.ROCKY_DIRT_BREAK.get(), CCSoundEvents.ROCKY_DIRT_STEP.get(), CCSoundEvents.ROCKY_DIRT_PLACE.get(), CCSoundEvents.ROCKY_DIRT_HIT.get(), CCSoundEvents.ROCKY_DIRT_FALL.get());
	}
}