package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.CCSkullBlockEntity;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CCWallSkullBlock extends WallSkullBlock {

	public CCWallSkullBlock(SkullBlock.Type type, Properties props) {
		super(type, props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CCSkullBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return !level.isClientSide || !state.is(CCBlocks.PEEPER_HEAD.get()) && !state.is(CCBlocks.PEEPER_WALL_HEAD.get()) && !state.is(CCBlocks.MIME_HEAD.get()) && !state.is(CCBlocks.MIME_WALL_HEAD.get()) ? null : createTickerHelper(type, CCBlockEntityTypes.SKULL.get(), SkullBlockEntity::dragonHeadAnimation);
	}
}
