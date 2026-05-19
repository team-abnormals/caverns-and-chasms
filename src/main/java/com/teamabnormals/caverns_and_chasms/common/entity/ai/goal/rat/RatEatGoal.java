package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.Optional;

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
			if (!itemStack.isEmpty() && this.rat.isHealingItem(itemStack) && this.rat.getRandom().nextInt(reducedTickDelay(50)) == 0) {
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
			FoodProperties foodProperties = this.stack.getFoodProperties(this.rat);
			if (foodProperties != null) {
				this.rat.heal((float) foodProperties.nutrition());
				this.rat.eat(this.rat.level(), this.stack, foodProperties);
				foodProperties.usingConvertsTo().ifPresent(itemStack -> this.rat.spitOutItem(itemStack.copy()));
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
}