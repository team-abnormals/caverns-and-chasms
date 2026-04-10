package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.Nullable;

public class PotionSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final PotionSubtypeInterpreter INSTANCE = new PotionSubtypeInterpreter();

	private PotionSubtypeInterpreter() {
	}

	@Override
	@Nullable
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		PotionContents contents = ingredient.get(DataComponents.POTION_CONTENTS);
		if (contents == null) {
			return null;
		}
		return new PotionSubtypeData(contents.potion().orElse(null), ingredient.has(CCDataComponents.SUBTLE));
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

		return itemDescriptionId + ".effect_id." + potionEffectId + (itemStack.has(CCDataComponents.SUBTLE) ? ".subtle" : "");
	}

	record PotionSubtypeData(Holder<Potion> potion, boolean subtle) {
		@Override
		public String toString() {
			return String.format("potion=%s, subtle=%s", potion, subtle);
		}
	}
}