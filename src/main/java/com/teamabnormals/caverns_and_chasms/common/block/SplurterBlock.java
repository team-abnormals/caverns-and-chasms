package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.SplurterBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.dispenser.SplurterDispenseItemBehavior;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.VanillaInventoryCodeHooks;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SplurterBlock extends ScattererBlock {
	private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new SplurterDispenseItemBehavior();

	public SplurterBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected DispenseItemBehavior getDispenseMethod(Level level, ItemStack p_52947_) {
		return DISPENSE_BEHAVIOUR;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SplurterBlockEntity(pos, state);
	}

	@Override
	protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
		DispenserBlockEntity dispenser = level.getBlockEntity(pos, CCBlockEntityTypes.SPLURTER.get()).orElse(null);
		BlockSource source = new BlockSource(level, pos, state, dispenser);

		List<Integer> slots = IntStream.range(0, SplurterBlockEntity.CONTAINER_SIZE).boxed().collect(Collectors.toList());
		Collections.shuffle(slots);

		boolean success = false;
		for (int i : slots) {
			ItemStack stack = dispenser.getItem(i);
			if (!stack.isEmpty() && VanillaInventoryCodeHooks.dropperInsertHook(level, pos, dispenser, i, stack)) {
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

}
