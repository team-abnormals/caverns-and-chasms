package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class InstrumentSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final InstrumentSubtypeInterpreter INSTANCE = new InstrumentSubtypeInterpreter();

	private InstrumentSubtypeInterpreter() {

	}

	@Override
	public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
		return List.of(ingredient.get(CCDataComponents.HARMONY_INSTRUMENT), ingredient.get(CCDataComponents.MELODY_INSTRUMENT), ingredient.get(CCDataComponents.BASS_INSTRUMENT));
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
		if (stack.has(CCDataComponents.HARMONY_INSTRUMENT)) {
			ResourceLocation harmonyLocation = stack.get(CCDataComponents.HARMONY_INSTRUMENT).getKey().location();
			ResourceLocation melodyLocation = stack.get(CCDataComponents.MELODY_INSTRUMENT).getKey().location();
			ResourceLocation bassLocation = stack.get(CCDataComponents.BASS_INSTRUMENT).getKey().location();

			List<String> strings = new ArrayList<>();
			strings.add(harmonyLocation.toString());
			strings.add(melodyLocation.toString());
			strings.add(bassLocation.toString());
			StringJoiner joiner = new StringJoiner(",", "[", "]");
			strings.sort(null);
			for (String s : strings) {
				joiner.add(s);
			}

			return joiner.toString();
		}


		return "";
	}
}