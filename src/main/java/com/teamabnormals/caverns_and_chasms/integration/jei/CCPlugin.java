package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCItemTiers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.stream.Stream;

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

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(RecipeTypes.ANVIL, getRepairRecipes(registration.getVanillaRecipeFactory()).toList());
		registration.addRecipes(RecipeTypes.CRAFTING, ToolboxWaxingRecipeMaker.createRecipes());
	}

	private static Stream<RepairData> getRepairData() {
		return Stream.of(
				new RepairData(CCArmorMaterials.SANGUINE.getRepairIngredient(),
						new ItemStack(CCItems.SANGUINE_HELMET.get()),
						new ItemStack(CCItems.SANGUINE_CHESTPLATE.get()),
						new ItemStack(CCItems.SANGUINE_LEGGINGS.get()),
						new ItemStack(CCItems.SANGUINE_BOOTS.get())),
				new RepairData(CCItemTiers.SILVER.getRepairIngredient(),
						new ItemStack(CCItems.SILVER_SWORD.get()),
						new ItemStack(CCItems.SILVER_PICKAXE.get()),
						new ItemStack(CCItems.SILVER_AXE.get()),
						new ItemStack(CCItems.SILVER_SHOVEL.get()),
						new ItemStack(CCItems.SILVER_HOE.get())),
				new RepairData(CCArmorMaterials.SILVER.getRepairIngredient(),
						new ItemStack(CCItems.SILVER_HELMET.get()),
						new ItemStack(CCItems.SILVER_CHESTPLATE.get()),
						new ItemStack(CCItems.SILVER_LEGGINGS.get()),
						new ItemStack(CCItems.SILVER_BOOTS.get())),
				new RepairData(CCItemTiers.NECROMIUM.getRepairIngredient(),
						new ItemStack(CCItems.NECROMIUM_SWORD.get()),
						new ItemStack(CCItems.NECROMIUM_AXE.get()),
						new ItemStack(CCItems.NECROMIUM_HOE.get()),
						new ItemStack(CCItems.NECROMIUM_SHOVEL.get()),
						new ItemStack(CCItems.NECROMIUM_PICKAXE.get())),
				new RepairData(CCArmorMaterials.NECROMIUM.getRepairIngredient(),
						new ItemStack(CCItems.NECROMIUM_BOOTS.get()),
						new ItemStack(CCItems.NECROMIUM_HELMET.get()),
						new ItemStack(CCItems.NECROMIUM_LEGGINGS.get()),
						new ItemStack(CCItems.NECROMIUM_CHESTPLATE.get())));
	}

	private static Stream<IJeiAnvilRecipe> getRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory) {
		return getRepairData().flatMap(repairData -> getRepairRecipes(repairData, vanillaRecipeFactory));
	}

	private static Stream<IJeiAnvilRecipe> getRepairRecipes(RepairData repairData, IVanillaRecipeFactory vanillaRecipeFactory) {
		Ingredient repairIngredient = repairData.getRepairIngredient();
		List<ItemStack> repairables = repairData.getRepairables();

		List<ItemStack> repairMaterials = List.of(repairIngredient.getItems());

		return repairables.stream().mapMulti((itemStack, consumer) -> {
			ItemStack damagedThreeQuarters = itemStack.copy();
			damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
			ItemStack damagedHalf = itemStack.copy();
			damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);

			IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
			consumer.accept(repairWithSame);

			if (!repairMaterials.isEmpty()) {
				ItemStack damagedFully = itemStack.copy();
				damagedFully.setDamageValue(damagedFully.getMaxDamage());
				IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
				consumer.accept(repairWithMaterial);
			}
		});
	}

	private static class RepairData {
		private final Ingredient repairIngredient;
		private final List<ItemStack> repairables;

		public RepairData(Ingredient repairIngredient, ItemStack... repairables) {
			this.repairIngredient = repairIngredient;
			this.repairables = List.of(repairables);
		}

		public Ingredient getRepairIngredient() {
			return repairIngredient;
		}

		public List<ItemStack> getRepairables() {
			return repairables;
		}
	}
}
