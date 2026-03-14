package com.teamabnormals.caverns_and_chasms.core.other;

import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.blueprint.common.advancement.EmptyTrigger;
import com.teamabnormals.caverns_and_chasms.common.advancement.*;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate.Type;
import net.minecraft.advancements.critereon.EntitySubPredicate.Types;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCCriteriaTriggers {
	public static final EmptyTrigger USE_TUNING_FORK = CriteriaTriggers.register(new EmptyTrigger(prefix("use_tuning_fork")));
	public static final EmptyTrigger SPOTTED_BY_PEEPER = CriteriaTriggers.register(new EmptyTrigger(prefix("spotted_by_peeper")));
	public static final EmptyTrigger DISMANTLED_ITEM = CriteriaTriggers.register(new EmptyTrigger(prefix("dismantled_item")));
	public static final AtonedItemTrigger ATONED_ITEM = CriteriaTriggers.register(new AtonedItemTrigger());
	public static final RepairedItemTrigger REPAIRED_ITEM = CriteriaTriggers.register(new RepairedItemTrigger());
	public static final PlayerHurtSelfTrigger PLAYER_HURT_SELF = CriteriaTriggers.register(new PlayerHurtSelfTrigger());
	public static final OpenStorageDuctTrigger OPEN_STORAGE_DUCT = CriteriaTriggers.register(new OpenStorageDuctTrigger());
	public static final RatKilledEntityTrigger RAT_KILLED_ENTITY = CriteriaTriggers.register(new RatKilledEntityTrigger());
	public static final HoopTrigger HOOP_ENTERED = CriteriaTriggers.register(new HoopTrigger());
	public static final EmptyTrigger HATCH_SADDLED_GRAZER = CriteriaTriggers.register(new EmptyTrigger(prefix("hatch_saddled_grazer")));

	public static final EntitySubPredicate.Type RICOCHETS = RicochetPredicate::fromJson;
	public static final EntitySubPredicate.Type COPPER_GOLEM = CopperGolemPredicate::fromJson;

	public static void registerPredicates() {
		ImmutableBiMap.Builder<String, Type> builder = ImmutableBiMap.builder();
		Types.TYPES.forEach(builder::put);
		builder.put("ricochets", RICOCHETS);
		builder.put("copper_golem", COPPER_GOLEM);
		Types.TYPES = builder.buildOrThrow();
	}

	private static ResourceLocation prefix(String name) {
		return CavernsAndChasms.location(name);
	}
}