package com.minecraftabnormals.caverns_and_chasms.core.other;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.abnormals_core.common.dispenser.FishBucketDispenseBehavior;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class CCCompat {

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerDispenseBehavior(CCItems.CAVEFISH_BUCKET.get(), new FishBucketDispenseBehavior());
	}

	public static void registerRenderLayers() {
		RenderTypeLookup.setRenderLayer(CCBlocks.GOLDEN_BARS.get(), RenderType.getCutout());
	}
}
