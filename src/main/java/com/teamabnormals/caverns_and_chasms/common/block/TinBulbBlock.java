package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TinBulbBlock extends Block {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	public TinBulbBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0).setValue(POWERED, false));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel serverlevel) {
			this.checkAndFlip(state, serverlevel, pos);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (level instanceof ServerLevel serverlevel) {
			this.checkAndFlip(state, serverlevel, pos);
		}
	}

	public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
		boolean flag = level.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			BlockState newState = state;
			if (!state.getValue(POWERED)) {
				newState = state.cycle(POWER);
				int power = newState.getValue(POWER);
				level.playSound(null, pos, power > 0 ? CCSoundEvents.TIN_BULB_TURN_ON.get() : CCSoundEvents.TIN_BULB_TURN_OFF.get(), SoundSource.BLOCKS, 1.0F, 1.0F - power * 0.01F);
			}

			level.setBlock(pos, newState.setValue(POWERED, flag), 3);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWER, POWERED);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return level.getBlockState(pos).getValue(POWER);
	}
}