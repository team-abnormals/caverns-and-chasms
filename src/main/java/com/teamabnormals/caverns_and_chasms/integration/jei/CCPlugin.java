package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

@JeiPlugin
public class CCPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(CavernsAndChasms.MOD_ID, CavernsAndChasms.MOD_ID);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(CCBlocks.CUPRIC_CAMPFIRE.get()), RecipeTypes.CAMPFIRE_COOKING);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(CCItems.TETHER_POTION.get(), (itemStack, context) -> {
			if (!itemStack.hasTag()) {
				return IIngredientSubtypeInterpreter.NONE;
			}
			Potion potionType = PotionUtils.getPotion(itemStack);
			String potionTypeString = potionType.getName("");
			StringBuilder stringBuilder = new StringBuilder(potionTypeString);
			List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemStack);
			for (MobEffectInstance effect : effects) {
				stringBuilder.append(";").append(effect);
			}

			return stringBuilder.toString();
		});
	}
}
