package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.inventory.BejeweledAnvilMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BejeweledAnvilBlock extends AnvilBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
	private static final VoxelShape X_LEG2 = Block.box(5.0D, 3.0D, 7.0D, 11.0D, 10.0D, 9.0D);
	private static final VoxelShape X_TOP = Block.box(1.0D, 10.0D, 4.0D, 15.0D, 16.0D, 12.0D);
	private static final VoxelShape Z_LEG2 = Block.box(7.0D, 3.0D, 5.0D, 9.0D, 10.0D, 11.0D);
	private static final VoxelShape Z_TOP = Block.box(4.0D, 10.0D, 1.0D, 12.0D, 16.0D, 15.0D);
	private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_LEG2, X_TOP);
	private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_LEG2, Z_TOP);
	private static final Component CONTAINER_TITLE = Component.translatable("container.repair");

	public BejeweledAnvilBlock(BlockBehaviour.Properties p_48777_) {
		super(p_48777_);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
	}

	@Nullable
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider((i, inventory, player) -> new BejeweledAnvilMenu(i, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
	}

	protected void falling(FallingBlockEntity p_48779_) {
		p_48779_.setHurtsEntities(2.0F, 40);
	}

	public void onLand(Level p_48793_, BlockPos p_48794_, BlockState p_48795_, BlockState p_48796_, FallingBlockEntity p_48797_) {
		if (!p_48797_.isSilent()) {
			p_48793_.levelEvent(1031, p_48794_, 0);
		}
	}

	public void onBrokenAfterFall(Level p_152053_, BlockPos p_152054_, FallingBlockEntity p_152055_) {
		if (!p_152055_.isSilent()) {
			p_152053_.levelEvent(1029, p_152054_, 0);
		}
	}
}