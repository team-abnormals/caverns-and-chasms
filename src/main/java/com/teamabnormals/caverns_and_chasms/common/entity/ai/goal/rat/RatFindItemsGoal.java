package com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.rat;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.rat.Rat;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class RatFindItemsGoal extends Goal {
	private final Rat rat;
	public final Predicate<ItemEntity> pickablePredicate;

	public RatFindItemsGoal(Rat rat) {
		this.rat = rat;
		this.pickablePredicate = entity -> !entity.hasPickUpDelay() && entity.isAlive() && entity.getItem().isEdible();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.isSittingBecauseOrdered()) {
			return false;
		} else {
			ItemStack currentItem = this.rat.getMainHandItem();
			if (currentItem.isEmpty() && currentItem.isEdible()) {
				return false;
			} else if (this.rat.getTarget() == null && this.rat.getLastHurtByMob() == null) {
				if (this.rat.getRandom().nextInt(10) != 0) {
					return false;
				} else {
					List<ItemEntity> list = this.rat.level().getEntitiesOfClass(ItemEntity.class, this.rat.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), this.pickablePredicate);
					return !list.isEmpty() && this.rat.getMainHandItem().isEmpty();
				}
			}
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.isSittingBecauseOrdered() && super.canContinueToUse();
	}

	@Override
	public void tick() {
		List<ItemEntity> list = this.rat.level().getEntitiesOfClass(ItemEntity.class, this.rat.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), this.pickablePredicate);
		ItemStack itemstack = this.rat.getMainHandItem();
		if (itemstack.isEmpty() && !list.isEmpty()) {
			this.rat.getNavigation().moveTo(list.get(0), 1.2F);
		}
	}

	@Override
	public void start() {
		List<ItemEntity> list = this.rat.level().getEntitiesOfClass(ItemEntity.class, this.rat.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), this.pickablePredicate);
		if (!list.isEmpty()) {
			this.rat.getNavigation().moveTo(list.get(0), 1.2F);
		}
	}
}