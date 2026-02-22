package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.RollerDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class RollerDoorBlockItem extends BEWLRBlockItem {
	public RollerDoorBlockItem(Block block, Properties properties, Supplier<Callable<LazyBEWLR>> bewlr) {
		super(block, properties, bewlr);
	}

	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		if (result.consumesAction()) {
			Level level = context.getLevel();
			BlockPos blockpos = context.getClickedPos();
			if (level.getBlockEntity(blockpos) instanceof RollerDoorBlockEntity blockentity)
				blockentity.onPlace();
		}

		return result;
	}
}