package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.ScattererBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;


public class ScattererBlock extends DispenserBlock {
	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	public ScattererBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ScattererBlockEntity(pos, state);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block p_52703_, BlockPos p_52704_, boolean p_52705_) {
		boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
		boolean flag1 = state.getValue(TRIGGERED);
		if (flag && !flag1) {
			level.scheduleTick(pos, this, 4);
			level.setBlock(pos, state.setValue(TRIGGERED, true).setValue(POWER, level.getBestNeighborSignal(pos)), 3);
		} else if (!flag && flag1) {
			level.setBlock(pos, state.setValue(TRIGGERED, false).setValue(POWER, 0), 3);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (level.getBestNeighborSignal(pos) != state.getValue(POWER)) {
			level.setBlock(pos, state.setValue(TRIGGERED, true).setValue(POWER, level.getBestNeighborSignal(pos)), 3);
		}
		super.animateTick(state, level, pos, random);
	}

	@Override
	protected void dispenseFrom(ServerLevel level, BlockPos pos) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(level, pos);
		DispenserBlockEntity dispenserblockentity = blocksourceimpl.getEntity();
		for (int i = 0; i < dispenserblockentity.getContainerSize(); i++) {
			ItemStack itemstack = dispenserblockentity.getItem(i);
			DispenseItemBehavior dispenseitembehavior = this.getDispenseMethod(itemstack);
			if (dispenseitembehavior != DispenseItemBehavior.NOOP) {
				dispenserblockentity.setItem(i, dispenseitembehavior.dispense(blocksourceimpl, itemstack));
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWER, TRIGGERED, FACING);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return state.getValue(POWER);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
}
