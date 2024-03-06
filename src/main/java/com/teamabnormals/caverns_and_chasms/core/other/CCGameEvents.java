package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Registry;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCGameEvents {
	public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registry.GAME_EVENT_REGISTRY, CavernsAndChasms.MOD_ID);

	public static final RegistryObject<GameEvent> TUNING_FORK_VIBRATE = GAME_EVENTS.register("tuning_fork_vibrate", () -> new GameEvent("tuning_fork_vibrate", 16));
}