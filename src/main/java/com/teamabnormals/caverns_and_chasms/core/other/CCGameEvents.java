package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CCGameEvents {
	public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<GameEvent, GameEvent> TUNING_FORK_VIBRATE = register("tuning_fork_vibrate", 16);

	public static DeferredHolder<GameEvent, GameEvent> register(String name, int radius) {
		return GAME_EVENTS.register(name, () -> new GameEvent(radius));
	}
}