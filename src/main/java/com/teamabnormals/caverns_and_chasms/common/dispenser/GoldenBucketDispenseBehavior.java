package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class GoldenBucketDispenseBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		LevelAccessor level = source.getLevel();
		BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof BucketPickup pickup) {
			ItemStack pickupStack = GoldenBucketItem.getFilledBucket(state);
			if (!GoldenBucketItem.isEmpty(stack) && GoldenBucketItem.canBeFilled(stack)) {
				GoldenBucketItem.setFluidLevel(pickupStack, GoldenBucketItem.getFluidLevel(stack) + 1);
			}
			pickup.pickupBlock(level, pos, state);
			if (pickupStack.isEmpty()) {
				return super.execute(source, stack);
			} else {
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
				ItemStack returnItem = pickupStack.copy();
				stack.shrink(1);
				if (stack.isEmpty()) {
					return returnItem;
				} else {
					if (source.<DispenserBlockEntity>getEntity().addItem(returnItem) < 0) {
						this.defaultDispenseItemBehavior.dispense(source, returnItem);
					}

					return stack;
				}
			}
		} else {
			return super.execute(source, stack);
		}
	}
}