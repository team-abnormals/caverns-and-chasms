package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCGameEvents {
	public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<GameEvent> TUNING_FORK_VIBRATE = register("tuning_fork_vibrate", 16);

	public static RegistryObject<GameEvent> register(String name, int radius) {
		return GAME_EVENTS.register(name, () -> new GameEvent(new ResourceLocation(CavernsAndChasms.MOD_ID, name).toString(), radius));
	}
}