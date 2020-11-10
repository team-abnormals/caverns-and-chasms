package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.abnormals_core.common.dispenser.FishBucketDispenseBehavior;

import net.minecraft.block.DispenserBlock;

public class CCCompat {

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerDispenseBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseBehavior());
	}
}
