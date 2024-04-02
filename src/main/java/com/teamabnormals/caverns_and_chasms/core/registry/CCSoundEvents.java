package com.teamabnormals.caverns_and_chasms.core.registry;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.IntStream;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CCSoundEvents {
	public static final SoundSubRegistryHelper HELPER = CavernsAndChasms.REGISTRY_HELPER.getSoundSubHelper();

	public static final RegistryObject<SoundEvent> EPILOGUE = HELPER.createSoundEvent("music.record.epilogue");

	public static final RegistryObject<SoundEvent> ROCKY_DIRT_BREAK = HELPER.createSoundEvent("block.rocky_dirt.break");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_FALL = HELPER.createSoundEvent("block.rocky_dirt.fall");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_HIT = HELPER.createSoundEvent("block.rocky_dirt.hit");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_PLACE = HELPER.createSoundEvent("block.rocky_dirt.place");
	public static final RegistryObject<SoundEvent> ROCKY_DIRT_STEP = HELPER.createSoundEvent("block.rocky_dirt.step");

	public static final RegistryObject<SoundEvent> TOOLBOX_OPEN = HELPER.createSoundEvent("block.toolbox.open");
	public static final RegistryObject<SoundEvent> TOOLBOX_CLOSE = HELPER.createSoundEvent("block.toolbox.close");

	public static final RegistryObject<SoundEvent> TUNING_FORK_VIBRATE = HELPER.createSoundEvent("item.tuning_fork.vibrate");

	public static final RegistryObject<SoundEvent> TETHER_POTION_EQUIP = HELPER.createSoundEvent("item.tether_potion.equip");
	public static final RegistryObject<SoundEvent> TETHER_POTION_BREAK = HELPER.createSoundEvent("item.tether_potion.break");

	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_DEATH = HELPER.createSoundEvent("entity.deeper.death");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_HURT = HELPER.createSoundEvent("entity.deeper.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_DEEPER_PRIMED = HELPER.createSoundEvent("entity.deeper.primed");

	public static final RegistryObject<SoundEvent> ENTITY_MIME_DEATH = HELPER.createSoundEvent("entity.mime.death");
	public static final RegistryObject<SoundEvent> ENTITY_MIME_HURT = HELPER.createSoundEvent("entity.mime.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_MIME_MIME = HELPER.createSoundEvent("entity.mime.mime");
	public static final RegistryObject<SoundEvent> ENTITY_MIME_IMPERSONATE = HELPER.createSoundEvent("entity.mime.impersonate");

	public static final RegistryObject<SoundEvent> ENTITY_COPPER_GOLEM_DEATH = HELPER.createSoundEvent("entity.copper_golem.death");
	public static final RegistryObject<SoundEvent> ENTITY_COPPER_GOLEM_HURT = HELPER.createSoundEvent("entity.copper_golem.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_COPPER_GOLEM_REPAIR = HELPER.createSoundEvent("entity.copper_golem.repair");
	public static final RegistryObject<SoundEvent> ENTITY_COPPER_GOLEM_DAMAGE = HELPER.createSoundEvent("entity.copper_golem.damage");
	public static final RegistryObject<SoundEvent> ENTITY_COPPER_GOLEM_GEAR = HELPER.createSoundEvent("entity.copper_golem.gear");
	public static final RegistryObject<SoundEvent> ENTITY_COPPER_GOLEM_STEP = HELPER.createSoundEvent("entity.copper_golem.step");

	public static final RegistryObject<SoundEvent> ENTITY_GLARE_DEATH = HELPER.createSoundEvent("entity.glare.death");
	public static final RegistryObject<SoundEvent> ENTITY_GLARE_ANGRY = HELPER.createSoundEvent("entity.glare.angry");
	public static final RegistryObject<SoundEvent> ENTITY_GLARE_EAT = HELPER.createSoundEvent("entity.glare.eat");
	public static final RegistryObject<SoundEvent> ENTITY_GLARE_HURT = HELPER.createSoundEvent("entity.glare.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_GLARE_AMBIENT = HELPER.createSoundEvent("entity.glare.ambient");
	public static final RegistryObject<SoundEvent> ENTITY_GLARE_TAME = HELPER.createSoundEvent("entity.glare.tame");
	public static final RegistryObject<SoundEvent> ENTITY_GLARE_UNTAME = HELPER.createSoundEvent("entity.glare.untame");

	public static final RegistryObject<SoundEvent> PARROT_IMITATE_DEEPER = HELPER.createSoundEvent("entity.parrot.imitate.deeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_PEEPER = HELPER.createSoundEvent("entity.parrot.imitate.peeper");
	public static final RegistryObject<SoundEvent> PARROT_IMITATE_MIME = HELPER.createSoundEvent("entity.parrot.imitate.mime");

	public static final ImmutableList<RegistryObject<SoundEvent>> LOST_GOAT_HORN_SOUND_VARIANTS = registerGoatHornSoundVariants();

	private static ImmutableList<RegistryObject<SoundEvent>> registerGoatHornSoundVariants() {
		return IntStream.range(0, 2).mapToObj((suffix) -> HELPER.createSoundEvent("item.lost_goat_horn.sound." + suffix)).collect(ImmutableList.toImmutableList());
	}

	public static class CCSoundTypes {
		public static final ForgeSoundType ROCKY_DIRT = new ForgeSoundType(1.0F, 1.0F, CCSoundEvents.ROCKY_DIRT_BREAK, CCSoundEvents.ROCKY_DIRT_STEP, CCSoundEvents.ROCKY_DIRT_PLACE, CCSoundEvents.ROCKY_DIRT_HIT, CCSoundEvents.ROCKY_DIRT_FALL);
	}
}