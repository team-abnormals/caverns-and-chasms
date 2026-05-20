package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.entity.CCSkullBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class CCWallSkullBlock extends WallSkullBlock {
	private static final Map<Direction, VoxelShape> PEEPER_AABBS = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(3.0, 3.0, 6.0, 13.0, 13.0, 16.0),
			Direction.SOUTH, Block.box(3.0, 3.0, 0.0, 13.0, 13.0, 10.0),
			Direction.EAST, Block.box(0.0, 3.0, 3.0, 10.0, 13.0, 13.0),
			Direction.WEST, Block.box(6.0, 3.0, 3.0, 16.0, 13.0, 13.0))
	);

	public CCWallSkullBlock(SkullBlock.Type type, Properties props) {
		super(type, props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CCSkullBlockEntity(pos, state);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.is(CCBlocks.PEEPER_WALL_HEAD) ? PEEPER_AABBS.get(state.getValue(FACING)) : super.getShape(state, level, pos, context);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return !level.isClientSide || !state.is(CCBlocks.PEEPER_WALL_HEAD.get()) && !state.is(CCBlocks.MIME_WALL_HEAD.get()) ? null : createTickerHelper(type, CCBlockEntityTypes.SKULL.get(), CCSkullBlockEntity::animation);
	}
}
