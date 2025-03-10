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
import net.minecraft.world.level.block.state.BlockState;

public class FilledGoldenBucketDispenseBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
	private final GoldenBucketDispenseBehavior goldenBucketDispenseItemBehavior = new GoldenBucketDispenseBehavior();

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		DispensibleContainerItem container = (DispensibleContainerItem) stack.getItem();
		BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		Level level = source.getLevel();
		BlockState state = level.getBlockState(pos);
		ItemStack filled = GoldenBucketItem.getFilledBucket(state);

		if (state.getBlock() instanceof BucketPickup && !filled.isEmpty() && stack.is(filled.getItem()) && GoldenBucketItem.canBeFilled(stack)) {
			return this.goldenBucketDispenseItemBehavior.dispense(source, stack);
		} else if (container.emptyContents(null, level, pos, null, stack)) {
			container.checkExtraContent(null, level, stack, pos);
			return GoldenBucketItem.getEmptySuccessItem(stack, null);
		} else {
			return this.defaultDispenseItemBehavior.dispense(source, stack);
		}
	}
}