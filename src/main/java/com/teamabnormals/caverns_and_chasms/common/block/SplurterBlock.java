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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SplurterBlock extends DispenserBlock {

	private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new SplurterDispenseItemBehavior();

	public SplurterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, Boolean.valueOf(false)));
	}
	protected DispenseItemBehavior getDispenseMethod(ItemStack p_52947_) {
		return DISPENSE_BEHAVIOUR;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SplurterBlockEntity(pos, state);
	}

	@Override
	public void neighborChanged(BlockState p_52700_, Level p_52701_, BlockPos p_52702_, Block p_52703_, BlockPos p_52704_, boolean p_52705_) {
		boolean flag = p_52701_.hasNeighborSignal(p_52702_) || p_52701_.hasNeighborSignal(p_52702_.above());
		boolean flag1 = p_52700_.getValue(TRIGGERED);
		if (flag && !flag1) {
			p_52701_.scheduleTick(p_52702_, this, 4);
			p_52701_.setBlock(p_52702_, p_52700_.setValue(TRIGGERED, Boolean.valueOf(true)), 3);
		} else if (!flag && flag1) {
			p_52701_.setBlock(p_52702_, p_52700_.setValue(TRIGGERED, Boolean.valueOf(false)), 3);
		}

	}

	protected void dispenseFrom(ServerLevel p_52944_, BlockPos p_52945_) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(p_52944_, p_52945_);
		DispenserBlockEntity dispenserblockentity = blocksourceimpl.getEntity();

		List<Integer> slots = IntStream.range(0, SplurterBlockEntity.CONTAINER_SIZE).boxed().collect(Collectors.toList());
		Collections.shuffle(slots);

		for (int i : slots) {
			if (i < 0) {
				p_52944_.levelEvent(1001, p_52945_, 0);
			} else {
				ItemStack itemstack = dispenserblockentity.getItem(i);
				if (!itemstack.isEmpty() && splurterInsertHook(p_52944_, p_52945_, dispenserblockentity, i, itemstack)) {
					Direction direction = p_52944_.getBlockState(p_52945_).getValue(FACING);
					Container container = HopperBlockEntity.getContainerAt(p_52944_, p_52945_.relative(direction));
					ItemStack itemstack1;
					if (container == null) {
						itemstack1 = DISPENSE_BEHAVIOUR.dispense(blocksourceimpl, itemstack);
					} else {
						itemstack1 = HopperBlockEntity.addItem(dispenserblockentity, container, itemstack.copy(), direction.getOpposite());
					}
					dispenserblockentity.setItem(i, ItemStack.EMPTY);
				}
			}
		}
	}


	public static boolean splurterInsertHook(Level level, BlockPos pos, DispenserBlockEntity splurter, int slot, @NotNull ItemStack stack)
	{
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
						stack.shrink(transferredAmount);
					}

					splurter.setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : stack); // Update slot correctly
					return false;
				})
				.orElse(true);
	}

	private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack)
	{
		for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++)
		{
			stack = insertStack(source, destination, destInventory, stack, slot);
		}
		return stack;
	}

	private static ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot)
	{
		ItemStack slotStack = destInventory.getStackInSlot(slot);

		if (slotStack.isEmpty()) {
			ItemStack remainder = destInventory.insertItem(slot, stack, false);
			return remainder;
		}

		if (ItemHandlerHelper.canItemStacksStack(slotStack, stack)) {
			int spaceAvailable = slotStack.getMaxStackSize() - slotStack.getCount();

			if (spaceAvailable > 0) {
				int toMove = Math.min(spaceAvailable, stack.getCount());
				ItemStack tempStack = stack.copy();
				tempStack.setCount(toMove);

				stack.shrink(toMove);
			}
		}
		return stack;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TRIGGERED, FACING);
	}
}
