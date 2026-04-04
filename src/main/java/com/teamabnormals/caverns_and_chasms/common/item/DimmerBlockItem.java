package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.Map;

public class DimmerBlockItem extends BlockItem {
	protected final Block wallBlock;

	public DimmerBlockItem(Block block, Block wallBlock, Item.Properties properties) {
		super(block, properties);
		this.wallBlock = wallBlock;
	}

	protected boolean canPlace(LevelReader level, BlockState state, BlockPos pos) {
		return state.canSurvive(level, pos);
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		BlockState blockstate = this.wallBlock.getStateForPlacement(context);
		BlockState blockstate1 = null;
		LevelReader levelreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();

		for (Direction direction : context.getNearestLookingDirections()) {
			BlockState blockstate2 = direction.getAxis() == Axis.Y ? this.getBlock().getStateForPlacement(context) : blockstate;
			if (blockstate2 != null && this.canPlace(levelreader, blockstate2, blockpos)) {
				blockstate1 = blockstate2;
				break;
			}
		}

		return blockstate1 != null && levelreader.isUnobstructed(blockstate1, blockpos, CollisionContext.empty()) ? blockstate1 : null;
	}

	@Override
	public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
		super.registerBlocks(blockToItemMap, item);
		blockToItemMap.put(this.wallBlock, item);
	}

	@Override
	public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item item) {
		super.removeFromBlockToItemMap(blockToItemMap, item);
		blockToItemMap.remove(this.wallBlock);
	}
}