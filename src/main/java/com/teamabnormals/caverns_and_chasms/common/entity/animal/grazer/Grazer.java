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

	@Override
	public boolean removeWhenFarAway(double d) {
		return true;
	}

	public static boolean checkGrazerSpawnRules(EntityType<Grazer> grazer, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		if (Mime.checkUndergroundMonsterSpawnRules(grazer, level, spawnType, pos, random) && level instanceof WorldGenLevel wgLevel && pos.getY() <= CCConfig.COMMON.grazerMaxSpawnHeight.get()) {
			return random.nextFloat() < calculateGrazerMigrationChance(wgLevel, pos, level.getMoonPhase());
		}

		return false;
	}

	public static float calculateGrazerMigrationChance(WorldGenLevel wgLevel, BlockPos pos, int phase) {
		int length = 256;
		long seed = wgLevel.getSeed();
		int axisPos = (seed < 0 ? pos.getZ() : pos.getX()) + (4 * length);
		int dividedPos = Math.floorMod(axisPos, length * 8);
		if (seed % 2 == 0) {
			dividedPos = (8 * length) - dividedPos;
		}

		if (length * phase <= dividedPos && dividedPos <= length * (phase + 1)) {
			int center = length * phase + length / 2;
			int distance = Math.abs(dividedPos - center);
			return Math.min(1.0F, 0.3F + 1.0F - (float) distance / center);
		} else {
			return 0.0F;
		}
	}

	public static int calculateOptimalMoonPhaseForPos(WorldGenLevel wgLevel, BlockPos pos) {
		float maxChance = 0.0F;
		int maxPhase = 0;
		for (int i = 0; i < 8; i++) {
			float chance = calculateGrazerMigrationChance(wgLevel, pos, i);
			if (chance > maxChance) {
				maxChance = chance;
				maxPhase = i;
			}
		}
		return maxPhase;
	}
}