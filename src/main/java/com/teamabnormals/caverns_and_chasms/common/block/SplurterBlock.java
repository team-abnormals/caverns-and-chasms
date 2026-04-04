package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.SplurterBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.dispenser.SplurterDispenseItemBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SplurterBlock extends ScattererBlock {
	private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new SplurterDispenseItemBehavior();

	public SplurterBlock(Properties properties) {
		super(properties);
	}

	protected DispenseItemBehavior getDispenseMethod(ItemStack p_52947_) {
		return DISPENSE_BEHAVIOUR;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SplurterBlockEntity(pos, state);
	}

	protected void dispenseFrom(ServerLevel level, BlockPos pos) {
		BlockSourceImpl source = new BlockSourceImpl(level, pos);
		DispenserBlockEntity dispenser = source.getEntity();

		List<Integer> slots = IntStream.range(0, SplurterBlockEntity.CONTAINER_SIZE).boxed().collect(Collectors.toList());
		Collections.shuffle(slots);

		boolean success = false;
		for (int i : slots) {
			ItemStack stack = dispenser.getItem(i);
			if (!stack.isEmpty() && splurterInsertHook(level, pos, dispenser, i, stack)) {
				success = true;
				Direction direction = level.getBlockState(pos).getValue(FACING);
				Container container = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
				ItemStack itemstack1;
				if (container == null) {
					itemstack1 = DISPENSE_BEHAVIOUR.dispense(source, stack);
				} else {
					itemstack1 = HopperBlockEntity.addItem(dispenser, container, stack.copy(), direction.getOpposite());
				}
				dispenser.setItem(i, itemstack1);
			}
		}

		if (!success) {
			level.levelEvent(1001, pos, 0);
		}
	}

	public static boolean splurterInsertHook(Level level, BlockPos pos, DispenserBlockEntity splurter, int slot, @NotNull ItemStack stack) {
		Direction enumfacing = level.getBlockState(pos).getValue(SplurterBlock.FACING);
		BlockPos blockpos = pos.relative(enumfacing);
		return VanillaInventoryCodeHooks.getItemHandler(level, blockpos.getX(), blockpos.getY(), blockpos.getZ(), enumfacing.getOpposite())
				.map(destinationResult -> {
					IItemHandler itemHandler = destinationResult.getKey();
					Object destination = destinationResult.getValue();

					ItemStack originalStack = stack.copy();
					ItemStack remainder = putStackInInventoryAllSlots(splurter, destination, itemHandler, originalStack);

					int transferredAmount = stack.getCount() - remainder.getCount();

					if (transferredAmount > 0) {
						remainder = stack.copy();
						remainder.shrink(transferredAmount);
					}

					splurter.setItem(slot, remainder);
					return false;
				})
				.orElse(true);
	}

	private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack) {
		for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
			stack = insertStack(source, destination, destInventory, stack, slot);
		}
		return stack;
	}

	private static ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot) {
		ItemStack simulatedStack = destInventory.insertItem(slot, stack, true);

		if (simulatedStack.getCount() == stack.getCount()) {
			return stack;
		}

		int insertedAmount = stack.getCount() - simulatedStack.getCount();

		if (insertedAmount > 0) {
			ItemStack toInsert = stack.copy();
			toInsert.setCount(insertedAmount);

			ItemStack remainder = destInventory.insertItem(slot, toInsert, false);

			stack.shrink(insertedAmount - remainder.getCount());
		}
		return stack;
	}
}
