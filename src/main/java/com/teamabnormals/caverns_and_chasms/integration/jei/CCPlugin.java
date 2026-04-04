package com.teamabnormals.caverns_and_chasms.integration.jei;

import com.teamabnormals.caverns_and_chasms.common.recipe.SmithingModifierRecipe;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCArmorMaterials;
import com.teamabnormals.caverns_and_chasms.core.other.CCTiers.CCItemTiers;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.extensions.IForgeItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class CCPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return CavernsAndChasms.location(CavernsAndChasms.MOD_ID);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(CCBlocks.CUPRIC_CAMPFIRE.get()), RecipeTypes.CAMPFIRE_COOKING);
		registration.addRecipeCatalyst(new ItemStack(CCBlocks.BEJEWELED_ANVIL.get()), RecipeTypes.ANVIL);
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		IExtendableSmithingRecipeCategory smithingCategory = registration.getSmithingCategory();
		smithingCategory.addExtension(SmithingModifierRecipe.class, new SmithingModifierCategoryExtension<>());
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(CCItems.COPPER_HORN.get(), InstrumentSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(CCItems.TETHER_POTION.get(), PotionSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(CCItems.IMPACT_POTION.get(), PotionSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(CCItems.TRAIL_POTION.get(), PotionSubtypeInterpreter.INSTANCE);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(RecipeTypes.ANVIL, getRepairRecipes(registration).toList());
		registration.addRecipes(RecipeTypes.CRAFTING, SubtleTippedArrowRecipe.createRecipes(registration));
		registration.addRecipes(RecipeTypes.CRAFTING, NBTWaxingRecipeMaker.createRecipes());
		registration.addRecipes(RecipeTypes.CRAFTING, MusicDiscCopyRecipe.createRecipes());
	}

	private static Stream<RepairData> getRepairData(IRecipeRegistration registration) {
		Stream<ItemStack> items = registration.getIngredientManager().getAllItemStacks().stream().filter(IForgeItemStack::isRepairable);
		return Stream.of(
				new RepairData(Ingredient.of(CCItems.ZIRCONIA.get()), items.collect(Collectors.toList())),
				new RepairData(Ingredient.of(CCItemTags.INGOTS_TIN), new ItemStack(CCItems.AEGIS.get())),
				new RepairData(CCArmorMaterials.COWL.getRepairIngredient(), new ItemStack(CCItems.COWL.get())),
				new RepairData(CCArmorMaterials.TOOLBELT.getRepairIngredient(), new ItemStack(CCItems.TOOLBELT.get())),
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
						new ItemStack(CCItems.NECROMIUM_CHESTPLATE.get()))
		);
	}

	private static Stream<IJeiAnvilRecipe> getRepairRecipes(IRecipeRegistration registration) {
		return getRepairData(registration).flatMap(repairData -> getRepairRecipes(repairData, registration));
	}

	private static Stream<IJeiAnvilRecipe> getRepairRecipes(RepairData repairData, IRecipeRegistration registration) {
		Ingredient repairIngredient = repairData.getRepairIngredient();
		List<ItemStack> repairables = repairData.getRepairables();

		List<ItemStack> repairMaterials = List.of(repairIngredient.getItems());

		return repairables.stream().mapMulti((itemStack, consumer) -> {
			ItemStack damagedThreeQuarters = itemStack.copy();
			damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
			ItemStack damagedHalf = itemStack.copy();
			damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);

			IJeiAnvilRecipe repairWithSame = registration.getVanillaRecipeFactory().createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
			consumer.accept(repairWithSame);

			if (!repairMaterials.isEmpty()) {
				ItemStack damagedFully = itemStack.copy();
				damagedFully.setDamageValue(damagedFully.getMaxDamage());
				IJeiAnvilRecipe repairWithMaterial = registration.getVanillaRecipeFactory().createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
				consumer.accept(repairWithMaterial);
			}
		});
	}

	private static class RepairData {
		private final Ingredient repairIngredient;
		private final List<ItemStack> repairables;

		public RepairData(Ingredient repairIngredient, ItemStack... repairables) {
			this(repairIngredient, List.of(repairables));
		}

		public RepairData(Ingredient repairIngredient, List<ItemStack> repairables) {
			this.repairIngredient = repairIngredient;
			this.repairables = repairables;
		}

		public Ingredient getRepairIngredient() {
			return repairIngredient;
		}

		public List<ItemStack> getRepairables() {
			return repairables;
		}
	}
}
