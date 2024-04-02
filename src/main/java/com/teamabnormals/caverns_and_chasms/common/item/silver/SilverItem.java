package com.teamabnormals.caverns_and_chasms.common.item.silver;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.rmi.Remote;

public class SilverItem {

	public static void causeMagicDamageParticles(LivingEntity target) {
		causeMagicParticles(target, false);
	}

	public static void causeMagicProtectionParticles(LivingEntity defender) {
		causeMagicParticles(defender, true);
	}

	public static void causeMagicParticles(LivingEntity entity, boolean defensive) {
		RandomSource random = entity.getRandom();
		int count = 3 + random.nextInt(2) + random.nextInt(2);
		if (defensive) {
			count += 3 + random.nextInt(2);
		}
		if (entity.level().isClientSide()) {
			ParticleOptions particle = defensive ? CCParticleTypes.SILVER_SPARK.get() : CCParticleTypes.SILVER_HIT.get();
			for (int i = 0; i < count; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				entity.level().addParticle(particle, entity.getRandomX(0.75D), entity.getRandomY() + (defensive ? 0.3F : 0.0F), entity.getRandomZ(0.75D), d0, d1, d2);
			}

		} else {
			String particle = CavernsAndChasms.MOD_ID + ":silver_" + (defensive ? "spark" : "hit");
			for (int i = 0; i < count; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				NetworkUtil.spawnParticle(particle, entity.getRandomX(0.75D), entity.getRandomY() + (defensive ? 0.3F : 0.0F), entity.getRandomZ(0.75D), d0, d1, d2);
			}
		}


	}
}