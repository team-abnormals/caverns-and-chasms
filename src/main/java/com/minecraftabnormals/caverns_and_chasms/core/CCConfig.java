package com.minecraftabnormals.caverns_and_chasms.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class CCConfig {

	public static class Common {
		public final ConfigValue<Boolean> creeperExplosionsDestroyBlocks;
		public final ConfigValue<Integer> deeperStartingSpawnHeight;

		public Common(ForgeConfigSpec.Builder builder) {
			creeperExplosionsDestroyBlocks = builder
					.translation(makeTranslation("creeperExplosionsDestroyBlocks"))
					.define("Creeper explosions destroy blocks", true);
			deeperStartingSpawnHeight = builder
					.translation(makeTranslation("deeperStartingSpawnHeight"))
					.defineInRange("Deeper starting spawn height", 60, 0, 255);
		}
	}

	private static String makeTranslation(String name) {
		return CavernsAndChasms.MODID + ".config." + name;
	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();
	}
}