package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.item.CopperHornItem;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class InstrumentSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
	public static final InstrumentSubtypeInterpreter INSTANCE = new InstrumentSubtypeInterpreter();

	private InstrumentSubtypeInterpreter() {

	}

	@Override
	public String apply(ItemStack itemStack, UidContext context) {
		CompoundTag tag = itemStack.getTag();
		if (tag != null && tag.contains(CopperHornItem.HARMONY, 8)) {
			ResourceLocation harmonyLocation = ResourceLocation.tryParse(tag.getString(CopperHornItem.HARMONY));
			ResourceLocation melodyLocation = ResourceLocation.tryParse(tag.getString(CopperHornItem.MELODY));
			ResourceLocation bassLocation = ResourceLocation.tryParse(tag.getString(CopperHornItem.BASS));

			List<String> strings = new ArrayList<>();

			if (harmonyLocation != null && melodyLocation != null && bassLocation != null) {
				strings.add(BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, harmonyLocation)).map(holder -> holder.key().location().toString()).orElse(IIngredientSubtypeInterpreter.NONE));
				strings.add(BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, melodyLocation)).map(holder -> holder.key().location().toString()).orElse(IIngredientSubtypeInterpreter.NONE));
				strings.add(BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, bassLocation)).map(holder -> holder.key().location().toString()).orElse(IIngredientSubtypeInterpreter.NONE));
			}

			StringJoiner joiner = new StringJoiner(",", "[", "]");
			strings.sort(null);
			for (String s : strings) {
				joiner.add(s);
			}

			return joiner.toString();
		}


		return IIngredientSubtypeInterpreter.NONE;
	}
}