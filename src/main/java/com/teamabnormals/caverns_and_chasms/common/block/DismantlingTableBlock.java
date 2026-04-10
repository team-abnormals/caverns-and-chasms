package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.inventory.DismantlingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SmithingTableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DismantlingTableBlock extends SmithingTableBlock {
	private static final Component CONTAINER_TITLE = Component.translatable("container.caverns_and_chasms.dismantle");

	public DismantlingTableBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider((i, inventory, player) -> new DismantlingMenu(i, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
	}
}