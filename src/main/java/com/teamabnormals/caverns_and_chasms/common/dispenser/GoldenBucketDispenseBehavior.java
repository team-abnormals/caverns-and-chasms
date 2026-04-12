package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class GoldenBucketDispenseBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		LevelAccessor level = source.level();
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof BucketPickup pickup) {
			ItemStack pickupStack = GoldenBucketItem.getFilledBucket(state);
			if (!GoldenBucketItem.isEmpty(stack) && GoldenBucketItem.canBeFilled(stack)) {
				GoldenBucketItem.setFluidLevel(pickupStack, GoldenBucketItem.getFluidLevel(stack) + 1);
			}
			pickup.pickupBlock(null, level, pos, state);
			if (pickupStack.isEmpty()) {
				return super.execute(source, stack);
			} else {
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
				return this.consumeWithRemainder(source, stack, pickupStack.copy());
			}
		} else {
			return super.execute(source, stack);
		}
	}
}