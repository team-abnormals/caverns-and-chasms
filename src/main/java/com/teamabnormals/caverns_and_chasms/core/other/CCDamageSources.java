package com.teamabnormals.caverns_and_chasms.core.other;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.Kunai;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class CCDamageSources {
	public static final DamageSource SPIKED_RAIL = new DamageSource(CavernsAndChasms.MOD_ID + ".spiked_rail").setMagic().bypassArmor();
	public static final DamageSource LAVA_LAMP = new DamageSource(CavernsAndChasms.MOD_ID + ".lava_lamp").setIsFire();

	public static DamageSource causeKunaiDamage(Kunai kunai, @Nullable Entity indirectEntity) {
		return (new IndirectEntityDamageSource(CavernsAndChasms.MOD_ID + ".kunai", kunai, indirectEntity)).setProjectile().setMagic().bypassArmor();
	}
}
