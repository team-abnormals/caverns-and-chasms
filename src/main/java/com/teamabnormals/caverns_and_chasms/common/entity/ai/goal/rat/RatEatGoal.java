package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class RatEatGoal extends Goal {
	private final Rat rat;
	private ItemStack stack;
	private int eatTime;

	public RatEatGoal(Rat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.rat.canSit() && this.rat.getHealth() < this.rat.getMaxHealth()) {
			ItemStack itemStack = this.rat.getMainHandItem();
			if (!itemStack.isEmpty() && this.canBeEaten(itemStack) && this.rat.getRandom().nextInt(reducedTickDelay(50)) == 0) {
				this.stack = itemStack;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.rat.canSit()) {
			return false;
		} else if (this.stack != this.rat.getMainHandItem()) {
			return false;
		} else if (this.eatTime <= 0) {
			this.rat.heal((float) this.stack.getItem().getFoodProperties().getNutrition());
			ItemStack itemStack = this.stack.finishUsingItem(this.rat.level(), this.rat);
			this.rat.playSound(CCSoundEvents.RAT_HAPPY.get(), 0.5F, this.rat.getRandom().nextFloat() * 0.1F + 0.9F);
			if (!itemStack.isEmpty()) {
				this.rat.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
			}
			return false;
		}
		return this.rat.onGround();
	}

	@Override
	public void start() {
		this.rat.getNavigation().stop();
		this.rat.setEating(true);
		this.eatTime = reducedTickDelay(80);
	}

	@Override
	public void stop() {
		this.rat.setEating(false);
	}

	@Override
	public void tick() {
		this.eatTime--;
	}

	private boolean canBeEaten(ItemStack stack) {
		return stack.getItem().isEdible();
	}
}