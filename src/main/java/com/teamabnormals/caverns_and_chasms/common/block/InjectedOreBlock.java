package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import net.minecraft.core.NonNullList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Map;

public class InjectedOreBlock extends OreBlock {
	private static final Map<Item, TargetedItemCategoryFiller> FILLER_MAP = Maps.newHashMap();
	private final Item followItem;

	public InjectedOreBlock(Item followItem, Properties properties) {
		this(followItem, properties, UniformInt.of(0, 0));
	}

	public InjectedOreBlock(Item followItem, BlockBehaviour.Properties properties, UniformInt uniformInt) {
		super(properties, uniformInt);
		this.followItem = followItem;
		FILLER_MAP.put(followItem, new TargetedItemCategoryFiller(() -> followItem));
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER_MAP.get(this.followItem).fillItem(this.asItem(), group, items);
	}
}