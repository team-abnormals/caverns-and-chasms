package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.inventory.DismantlingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DismantlingTableBlock extends CraftingTableBlock {
	private static final Component CONTAINER_TITLE = Component.translatable("container.caverns_and_chasms.dismantle");

	public DismantlingTableBlock(BlockBehaviour.Properties p_56420_) {
		super(p_56420_);
	}

	@Override
	public MenuProvider getMenuProvider(BlockState p_56435_, Level p_56436_, BlockPos p_56437_) {
		return new SimpleMenuProvider((p_277304_, p_277305_, p_277306_) -> {
			return new DismantlingMenu(p_277304_, p_277305_, ContainerLevelAccess.create(p_56436_, p_56437_));
		}, CONTAINER_TITLE);
	}

	@Override
	public InteractionResult use(BlockState state, Level p_56429_, BlockPos p_56430_, Player p_56431_, InteractionHand p_56432_, BlockHitResult p_56433_) {
		if (p_56429_.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			p_56431_.openMenu(state.getMenuProvider(p_56429_, p_56430_));
			p_56431_.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
			return InteractionResult.CONSUME;
		}
	}
}