package com.teamabnormals.caverns_and_chasms.integration.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

public class PotionSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final PotionSubtypeInterpreter INSTANCE = new PotionSubtypeInterpreter();

	private PotionSubtypeInterpreter() {
	}

	@Override
	@Nullable
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		return ingredient.get(DataComponents.POTION_CONTENTS);
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		return getStringName(ingredient);
	}

	public String getStringName(ItemStack itemStack) {
		if (itemStack.getComponentsPatch().isEmpty()) {
			return "";
		}
		PotionContents contents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		String itemDescriptionId = itemStack.getItem().getDescriptionId();
		String potionEffectId = contents.potion().map(Holder::getRegisteredName).orElse("none");
		return itemDescriptionId + ".effect_id." + potionEffectId;
	}
}