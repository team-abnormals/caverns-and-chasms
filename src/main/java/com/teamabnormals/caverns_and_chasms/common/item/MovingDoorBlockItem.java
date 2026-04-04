package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.common.item.BEWLRBlockItem;
import com.teamabnormals.caverns_and_chasms.client.renderer.block.RollerDoorBlockEntityWithoutLevelRenderer;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.AbstractMovingDoorBlock;
import com.teamabnormals.caverns_and_chasms.common.block.holdable.MovingDoorType;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class MovingDoorBlockItem extends BEWLRBlockItem {
	private final MovingDoorType doorType;
	private String descriptionId;

	public MovingDoorBlockItem(Block block, MovingDoorType doorType, Properties properties) {
		super(block, properties, () -> () -> BEWLR(doorType));
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
			this.descriptionId = Util.makeDescriptionId("block", ForgeRegistries.ITEMS.getKey(this));
		}

		return this.descriptionId;
	}

	@Override
	public void registerBlocks(Map<Block, Item> map, Item item) {
		if (this.getBlock() instanceof AbstractMovingDoorBlock doorBlock && this.doorType == doorBlock.getDefaultDoorType()) {
			super.registerBlocks(map, item);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static BEWLRBlockItem.LazyBEWLR BEWLR(MovingDoorType doorType) {
		return new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> {
			MovingDoorHeaderBlockEntity blockEntity = new MovingDoorHeaderBlockEntity(BlockPos.ZERO, CCBlocks.ROLLER_DOOR_HEADER.get().defaultBlockState());
			blockEntity.setDoorType(doorType);
			blockEntity.syncVisuals();
			return new RollerDoorBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, blockEntity);
		});
	}
}