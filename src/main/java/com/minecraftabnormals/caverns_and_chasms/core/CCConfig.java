package com.minecraftabnormals.caverns_and_chasms.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class CCConfig {

	public static class Common {
		public final ConfigValue<Boolean> creeperExplosionsDestroyBlocks;
		public final ConfigValue<Integer> deeperMaxSpawnHeight;
		public final ConfigValue<Boolean> largeEmeraldVeins;
		public final ConfigValue<Boolean> railScaffoldingBehavior;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("entities");
			creeperExplosionsDestroyBlocks = builder
					.translation(makeTranslation("creeperExplosionsDestroyBlocks"))
					.define("Creeper explosions destroy blocks", false);
			deeperMaxSpawnHeight = builder
					.translation(makeTranslation("deeperMaxSpawnHeight"))
					.defineInRange("Deeper max spawn height", 60, 0, 255);
			builder.pop();
			builder.push("worldgen");
			largeEmeraldVeins = builder
					.translation(makeTranslation("largeEmeraldVeins"))
					.define("Large emerald veins", true);
			railScaffoldingBehavior = builder
					.comment("Rails can be placed in the direction you're looking at by clicking on another rail, similar to scaffolding")
					.translation(makeTranslation("railScaffoldingBehavior"))
					.define("Rail scaffolding behavior", true);
			builder.pop();
		}
	}

	private static String makeTranslation(String name) {
		return CavernsAndChasms.MOD_ID + ".config." + name;
	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();
	}
}