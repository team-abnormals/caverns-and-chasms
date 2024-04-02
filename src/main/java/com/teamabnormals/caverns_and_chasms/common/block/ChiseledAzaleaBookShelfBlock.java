package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.common.block.BlueprintChiseledBookShelfBlock;
import net.minecraft.world.phys.Vec2;

public class ChiseledAzaleaBookShelfBlock extends BlueprintChiseledBookShelfBlock {

	public ChiseledAzaleaBookShelfBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getHitSlot(Vec2 vec2) {
		if (vec2.x < 0.3125F) {
			return vec2.y >= 0.5F ? 0 : 4;
		} else if (vec2.x >= 0.6875F) {
			return vec2.y >= 0.5F ? 1 : 5;
		} else {
			return vec2.x < 0.5F ? 2 : 3;
		}
	}
}
