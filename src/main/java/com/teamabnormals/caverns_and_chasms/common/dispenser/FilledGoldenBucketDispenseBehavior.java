package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.item.GoldenBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class FilledGoldenBucketDispenseBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

	public ItemStack execute(BlockSource source, ItemStack stack) {
		DispensibleContainerItem dispensibleItem = (DispensibleContainerItem) stack.getItem();
		BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		Level level = source.getLevel();
		BlockState state = level.getBlockState(pos);
		ItemStack filled = GoldenBucketItem.getFilledBucket(state);
		if (state.getBlock() instanceof BucketPickup bucketPickup && filled != null && stack.is(filled.getItem()) && stack.getOrCreateTag().getInt("FluidLevel") < 3) {
			bucketPickup.pickupBlock(level, pos, state);
			level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
			ItemStack returnItem = stack.copy();
			GoldenBucketItem.increaseFluidLevel(returnItem);
			stack.shrink(1);
			if (stack.isEmpty()) {
				return returnItem;
			} else {
				if (source.<DispenserBlockEntity>getEntity().addItem(returnItem) < 0) {
					this.defaultDispenseItemBehavior.dispense(source, returnItem);
				}
				return stack;
			}
		} else if (dispensibleItem.emptyContents(null, level, pos, null)) {
			dispensibleItem.checkExtraContent(null, level, stack, pos);
			return GoldenBucketItem.getEmptySuccessItem(stack, null);
		} else {
			return this.defaultDispenseItemBehavior.dispense(source, stack);
		}
	}
}