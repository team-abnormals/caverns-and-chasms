package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.Mime;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;

public class Grazer extends AbstractGrazer implements Enemy {

	public Grazer(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	public static boolean checkGrazerSpawnRules(EntityType<Grazer> grazer, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		if (Mime.checkUndergroundMonsterSpawnRules(grazer, level, spawnType, pos, random) && level instanceof WorldGenLevel wgLevel && pos.getY() <= CCConfig.COMMON.grazerMaxSpawnHeight.get()) {
			int length = 256;
			int phase = level.dimensionType().moonPhase(level.dayTime());
			int zPos = pos.getZ() + ((int) (wgLevel.getSeed() % 10) * length);
			int dividedZ = Math.floorMod(zPos, length * 8);
			if (length * phase <= dividedZ && dividedZ <= length * (phase + 1)) {
				int center = length * phase + length / 2;
				int distance = Math.abs(dividedZ - center);
				float chance = Math.min(1.0F, 0.3F + 1.0F - (float) distance / center);
				return random.nextFloat() < chance;
			}
		}

		return false;
	}
}