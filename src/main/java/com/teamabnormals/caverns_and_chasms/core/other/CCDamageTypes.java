package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CCDamageTypes {
	public static final ResourceKey<DamageType> SPIKED_RAIL = createKey("spiked_rail");
	public static final ResourceKey<DamageType> LAVA_LAMP = createKey("lava_lamp");
	public static final ResourceKey<DamageType> KUNAI = createKey("kunai");

	public static void bootstrap(BootstapContext<DamageType> context) {
		context.register(SPIKED_RAIL, new DamageType(CavernsAndChasms.MOD_ID + ".spiked_rail", 0.1F));
		context.register(LAVA_LAMP, new DamageType(CavernsAndChasms.MOD_ID + ".lava_lamp", 0.1F));
		context.register(KUNAI, new DamageType(CavernsAndChasms.MOD_ID + ".kunai", 0.1F));
	}

	public static DamageSource spikedRail(Level level) {
		return level.damageSources().source(SPIKED_RAIL);
	}

	public static DamageSource lavaLamp(Level level) {
		return level.damageSources().source(LAVA_LAMP);
	}

	public static DamageSource kunai(Level level, Kunai kunai, @Nullable Entity indirectEntity) {
		return level.damageSources().source(KUNAI, kunai, indirectEntity);
	}

	public static ResourceKey<DamageType> createKey(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(CavernsAndChasms.MOD_ID, name));
	}

}
