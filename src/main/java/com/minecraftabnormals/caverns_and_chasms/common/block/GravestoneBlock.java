package com.minecraftabnormals.caverns_and_chasms.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class GravestoneBlock extends HorizontalBlock {
	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 10);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.box(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D),
			Block.box(2.0D, 0.0D, 5.0D, 14.0D, 14.0D, 11.0D),
			Block.box(5.0D, 0.0D, 2.0D, 11.0D, 14.0D, 14.0D)};

	public GravestoneBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE).setValue(CHARGE, 0));
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canSupportCenter(worldIn, pos.below(), Direction.UP);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, CHARGE, POWERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(CHARGE, 0).setValue(POWERED, false);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.getValue(FACING)) {
			case NORTH:
			default:
				return SHAPES[0];
			case EAST:
				return SHAPES[1];
			case SOUTH:
				return SHAPES[2];
			case WEST:
				return SHAPES[3];
		}
	}

	public void powerBlock(BlockState state, World world, BlockPos pos) {
		world.setBlock(pos, state.setValue(POWERED, true).setValue(GravestoneBlock.CHARGE, state.getValue(CHARGE) < 10 ? state.getValue(CHARGE) + 1: state.getValue(CHARGE)), 3);
		this.updateNeighbors(state, world, pos);
		world.getBlockTicks().scheduleTick(pos, this, 20);
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.getValue(POWERED)) {
			worldIn.setBlock(pos, state.setValue(POWERED, false), 3);
			this.updateNeighbors(state, worldIn, pos);
		}
	}

	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(POWERED) ? 15 : 0;
	}

	public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return blockState.getValue(POWERED)  ? 15 : 0;
	}

	public boolean isSignalSource(BlockState state) {
		return true;
	}

	private void updateNeighbors(BlockState state, World worldIn, BlockPos pos) {
		worldIn.updateNeighborsAt(pos, this);
		worldIn.updateNeighborsAt(pos.below(), this);
	}
}