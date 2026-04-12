package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.SplurterBlock;
import com.teamabnormals.caverns_and_chasms.common.block.entity.SplurterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.VanillaInventoryCodeHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = VanillaInventoryCodeHooks.class, remap = false)
public abstract class VanillaInventoryCodeHooksMixin {

	@Shadow
	protected static Optional<Pair<IItemHandler, Object>> getAttachedItemHandler(Level level, BlockPos pos, Direction direction) {
		return null;
	}

	@Inject(method = "dropperInsertHook", at = @At("HEAD"), cancellable = true)
	private static void dropperInsertHook(Level level, BlockPos pos, DispenserBlockEntity dropper, int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (dropper instanceof SplurterBlockEntity) {
			cir.setReturnValue(splurterInsertHook(level, pos, dropper, slot, stack));
		}
	}


	@Unique
	private static boolean splurterInsertHook(Level level, BlockPos pos, DispenserBlockEntity splurter, int slot, @NotNull ItemStack stack) {
		Direction facing = level.getBlockState(pos).getValue(SplurterBlock.FACING);
		return getAttachedItemHandler(level, pos, facing).map(destinationResult -> {
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
		}).orElse(true);
	}

	@Unique
	private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack) {
		for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
			stack = insertStack(source, destination, destInventory, stack, slot);
		}
		return stack;
	}

	@Unique
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
