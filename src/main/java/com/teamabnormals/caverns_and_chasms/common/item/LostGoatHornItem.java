package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;

public class LostGoatHornItem extends InstrumentItem {
	private final TagKey<Instrument> instruments;

	public LostGoatHornItem(Properties properties, TagKey<Instrument> instruments) {
		super(properties, instruments);
		this.instruments = instruments;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowedIn(tab)) {
			for (Holder<Instrument> holder : Registry.INSTRUMENT.getTagOrEmpty(this.instruments)) {
				items.add(create(CCItems.LOST_GOAT_HORN.get(), holder));
			}
		}
	}
}