package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.block.entity.AtoningTableBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.inventory.AtoningMenu;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AtoningTableBlock extends EnchantingTableBlock {

	public AtoningTableBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AtoningTableBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? createTickerHelper(type, CCBlockEntityTypes.ATONING_TABLE.get(), AtoningTableBlockEntity::bookAnimationTick) : null;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity instanceof AtoningTableBlockEntity table) {
			return new SimpleMenuProvider((i, inventory, player) -> new AtoningMenu(i, inventory, ContainerLevelAccess.create(level, pos)), table.getDisplayName());
		} else {
			return null;
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
	}
}