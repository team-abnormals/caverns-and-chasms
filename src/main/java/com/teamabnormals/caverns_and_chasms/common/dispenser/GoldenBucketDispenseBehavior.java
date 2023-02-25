package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class GoldenBucketDispenseBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

	public ItemStack execute(BlockSource source, ItemStack stack) {
		LevelAccessor level = source.getLevel();
		BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof BucketPickup bucketPickup) {
			ItemStack itemstack = GoldenBucketItem.getFilledBucket(state);
			bucketPickup.pickupBlock(level, pos, state);
			if (itemstack == null) {
				return super.execute(source, stack);
			} else {
				level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
				Item item = itemstack.getItem();
				stack.shrink(1);
				if (stack.isEmpty()) {
					return new ItemStack(item);
				} else {
					if (source.<DispenserBlockEntity>getEntity().addItem(new ItemStack(item)) < 0) {
						this.defaultDispenseItemBehavior.dispense(source, new ItemStack(item));
					}

					return stack;
				}
			}
		} else {
			return super.execute(source, stack);
		}
	}
}