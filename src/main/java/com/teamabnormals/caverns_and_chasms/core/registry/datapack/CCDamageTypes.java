package com.teamabnormals.caverns_and_chasms.core.registry.datapack;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CCDamageTypes {
	public static final ResourceKey<DamageType> SPIKED_RAIL = createKey("spiked_rail");
	public static final ResourceKey<DamageType> LAVA_LAMP = createKey("lava_lamp");
	public static final ResourceKey<DamageType> KUNAI = createKey("kunai");
	public static final ResourceKey<DamageType> DRAINING = createKey("draining");
	public static final ResourceKey<DamageType> GRAZER = createKey("grazer");
	public static final ResourceKey<DamageType> RIDING_GRAZER = createKey("riding_grazer");

	public static void bootstrap(BootstapContext<DamageType> context) {
		context.register(SPIKED_RAIL, new DamageType(CavernsAndChasms.MOD_ID + ".spiked_rail", 0.1F));
		context.register(LAVA_LAMP, new DamageType(CavernsAndChasms.MOD_ID + ".lava_lamp", 0.1F));
		context.register(KUNAI, new DamageType(CavernsAndChasms.MOD_ID + ".kunai", 0.1F));
		context.register(DRAINING, new DamageType(CavernsAndChasms.MOD_ID + ".draining", 0.1F));
		context.register(GRAZER, new DamageType(CavernsAndChasms.MOD_ID + ".grazer", 0.1F));
		context.register(RIDING_GRAZER, new DamageType(CavernsAndChasms.MOD_ID + ".riding_grazer", 0.1F));
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

	public static DamageSource draining(Level level, LivingEntity entity) {
		return level.damageSources().source(DRAINING, entity);
	}

	public static DamageSource grazer(Level level, LivingEntity entity) {
		return level.damageSources().source(GRAZER, entity);
	}

	public static DamageSource ridingGrazer(Level level, LivingEntity mount, @Nullable LivingEntity rider) {
		return level.damageSources().source(RIDING_GRAZER, mount, rider);
	}

	public static ResourceKey<DamageType> createKey(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, CavernsAndChasms.location(name));
	}
}