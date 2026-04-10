package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractMovingDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class MovingDoorBlockItem extends BlockItem {
	private final MovingDoorType doorType;
	private String descriptionId;

	public MovingDoorBlockItem(Block block, MovingDoorType doorType, Properties properties) {
		super(block, properties);
		this.doorType = doorType;
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		if (result.consumesAction()) {
			Level level = context.getLevel();
			BlockPos blockpos = context.getClickedPos();
			if (level.getBlockEntity(blockpos) instanceof MovingDoorBlockEntity blockEntity) {
				blockEntity.onPlace(this.doorType);
			}
		}

		return result;
	}

	@Override
	public String getDescriptionId() {
		if (this.descriptionId == null) {
			this.descriptionId = Util.makeDescriptionId("block", BuiltInRegistries.ITEM.getKey(this));
		}

		return this.descriptionId;
	}

	@Override
	public void registerBlocks(Map<Block, Item> map, Item item) {
		if (this.getBlock() instanceof AbstractMovingDoorBlock doorBlock && this.doorType == doorBlock.getDefaultDoorType()) {
			super.registerBlocks(map, item);
		}
	}
}