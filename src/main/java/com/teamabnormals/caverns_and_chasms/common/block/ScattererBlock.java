package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.ScattererBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.dispenser.FireChargeScattererBehavior;
import com.teamabnormals.caverns_and_chasms.common.dispenser.FireworkScattererBehavior;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;

public class ScattererBlock extends DispenserBlock {
	public ScattererBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ScattererBlockEntity(pos, state);
	}

	public int powerLevel;

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block p_52703_, BlockPos p_52704_, boolean p_52705_) {
		boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
		boolean flag1 = state.getValue(TRIGGERED);
		if (flag && !flag1) {
			level.scheduleTick(pos, this, 4);
			powerLevel = level.getBestNeighborSignal(pos);
			level.setBlock(pos, state.setValue(TRIGGERED, true), 3);
		} else if (!flag && flag1) {
			level.setBlock(pos, state.setValue(TRIGGERED, false), 3);
		}
	}

	@Override
	protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
		ScattererBlockEntity scatterer = level.getBlockEntity(pos, CCBlockEntityTypes.SCATTERER.get()).orElse(null);
		if (scatterer != null) {
			BlockSource source = new BlockSource(level, pos, state, scatterer);
			boolean success = false;
			for (int i = 0; i < scatterer.getContainerSize(); i++) {
				ItemStack stack = scatterer.getItem(i);
				DispenseItemBehavior behavior = getScatterMethod(level, stack);
				if (behavior != DispenseItemBehavior.NOOP && !stack.isEmpty()) {
					scatterer.setItem(i, behavior.dispense(source, stack));
					success = true;
				}
			}

			if (!success) {
				level.levelEvent(1001, pos, 0);
				level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(scatterer.getBlockState()));
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TRIGGERED, FACING);
	}

	public DispenseItemBehavior getScatterMethod(Level level, ItemStack stack) {
		if (stack.is(Items.FIREWORK_ROCKET)) {
			return new FireworkScattererBehavior(stack.getItem());
		} else if (stack.is(Items.FIRE_CHARGE)) {
			return new FireChargeScattererBehavior();
		} else {
			return this.getDispenseMethod(level, stack);
		}
	}
}