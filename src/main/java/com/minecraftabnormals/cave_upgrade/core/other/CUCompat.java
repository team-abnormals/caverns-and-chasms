package com.minecraftabnormals.cave_upgrade.core.other;

import com.minecraftabnormals.cave_upgrade.core.registry.CUItems;
import com.teamabnormals.abnormals_core.common.dispenser.FishBucketDispenseBehavior;

import net.minecraft.block.DispenserBlock;

public class CUCompat {

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerDispenseBehavior(CUItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseBehavior());
	}
}
