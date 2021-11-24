package com.teamabnormals.caverns_and_chasms.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class CCConfig {

	public static class Common {
		public final ConfigValue<Boolean> creeperExplosionsDestroyBlocks;
		public final ConfigValue<Integer> deeperMaxSpawnHeight;
		public final ConfigValue<Boolean> largeEmeraldVeins;

		public final ConfigValue<Boolean> chainmailArmorBuff;

		public final ConfigValue<Boolean> betterRailPlacement;
		public final ConfigValue<Integer> betterRailPlacementRange;

		public Common(ForgeConfigSpec.Builder builder) {

			builder.push("mobs");
			creeperExplosionsDestroyBlocks = builder.define("Creeper explosions destroy blocks", false);
			deeperMaxSpawnHeight = builder.defineInRange("Deeper max spawn height", 60, 0, 255);
			builder.pop();

			builder.push("world");
			builder.push("generation");
			largeEmeraldVeins = builder.define("Large emerald veins", true);
			builder.pop();
			builder.pop();

			builder.push("tweaks");
			chainmailArmorBuff = builder.comment("Chainmail armor increases the user's attack damage").define("Chainmail armor buff", true);
			builder.push("rails");
			betterRailPlacement = builder.comment("Rails can be placed in the direction you're looking at by clicking on another rail, similar to scaffolding").define("Better rail placement", true);
			betterRailPlacementRange = builder.comment("The range in blocks that better rail placement can reach").define("Placement range", 7);
			builder.pop();
			builder.pop();
		}
	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();
	}
}