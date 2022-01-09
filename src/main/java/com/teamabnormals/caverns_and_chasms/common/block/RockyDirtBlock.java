package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents.CCSoundTypes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class RockyDirtBlock extends Block {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.ROOTED_DIRT);

	public RockyDirtBlock(Properties properties) {
		super(properties);
	}

	@Override
	public SoundType getSoundType(BlockState state) {
		return CCSoundTypes.ROCKY_DIRT;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this.asItem(), group, items);
	}
}