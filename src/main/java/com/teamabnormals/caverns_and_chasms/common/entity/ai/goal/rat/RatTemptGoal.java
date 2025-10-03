package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class RatTemptGoal extends TemptGoal {
	private final Rat rat;
	
	public RatTemptGoal(Rat rat) {
		super(rat, 1.0D, Ingredient.of(CCItemTags.RAT_FOOD), false);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.rat.trustsPlayers() && super.canUse();
	}
}