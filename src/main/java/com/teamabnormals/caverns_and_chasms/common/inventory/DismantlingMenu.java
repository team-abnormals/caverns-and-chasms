package com.teamabnormals.caverns_and_chasms.common.inventory;

import com.teamabnormals.caverns_and_chasms.core.other.CCCriteriaTriggers;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCMenuTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class DismantlingMenu extends CCItemCombinerMenu {
	private final Level level;

	public DismantlingMenu(int p_40245_, Inventory p_40246_) {
		this(p_40245_, p_40246_, ContainerLevelAccess.NULL);
	}

	public DismantlingMenu(int p_40248_, Inventory p_40249_, ContainerLevelAccess p_40250_) {
		super(CCMenuTypes.DISMANTLING.get(), p_40248_, p_40249_, p_40250_);
		this.level = p_40249_.player.level();
	}

	protected CCItemCombinerMenuSlotDefinition createSlotDefinitions() {
		return CCItemCombinerMenuSlotDefinition.create()
				.withSlot(0, 8, 48, (stack) -> this.areResultsEmpty() && !this.getResultContainer(stack).isEmpty())
				.withSlot(1, 26, 48, (stack) -> stack.is(CCItems.SPINEL.get()))
				.withResultSlot(0, 80, 48)
				.withResultSlot(1, 98, 48)
				.withResultSlot(2, 116, 48)
				.build();
	}

	protected boolean isValidBlock(BlockState p_40266_) {
		return p_40266_.is(CCBlocks.DISMANTLING_TABLE.get());
	}

	protected boolean mayPickup(Player p_40268_, boolean p_40269_) {
		return true;
	}

	protected void onTake(Player player, ItemStack stack) {
		stack.onCraftedBy(player.level(), player, stack.getCount());

		boolean slot0 = this.resultSlots.getItem(0).isEmpty();
		boolean slot1 = this.resultSlots.getItem(1).isEmpty();
		boolean slot2 = this.resultSlots.getItem(2).isEmpty();

		boolean firstTake = (slot0 ^ slot1 ^ slot2) ^ (slot0 && slot1 && slot2);

		this.shrinkStackInSlot(0);
		if (firstTake) {
			this.shrinkStackInSlot(1);
			this.access.execute((level, pos) -> level.levelEvent(1044, pos, 0));
			if (player instanceof ServerPlayer serverPlayer) {
				CCCriteriaTriggers.DISMANTLED_ITEM.trigger(serverPlayer);
			}
		}
	}


	private void shrinkStackInSlot(int p_40271_) {
		ItemStack itemstack = this.inputSlots.getItem(p_40271_);
		if (!itemstack.isEmpty()) {
			itemstack.shrink(1);
			this.inputSlots.setItem(p_40271_, itemstack);
		}
	}

	public void createResult() {
		Container container = this.getResultContainer(this.inputSlots.getItem(0));
		List<SmithingRecipe> list = this.level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, container, this.level);

		if ((list.isEmpty() || this.inputSlots.getItem(0).isEmpty() || this.inputSlots.getItem(1).isEmpty()) && this.areResultsFull()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			this.resultSlots.setItem(1, ItemStack.EMPTY);
			this.resultSlots.setItem(2, ItemStack.EMPTY);
		} else if (!this.inputSlots.getItem(1).isEmpty() && !list.isEmpty()) {
			this.resultSlots.setItem(0, container.getItem(0));
			this.resultSlots.setItem(1, container.getItem(1));
			this.resultSlots.setItem(2, container.getItem(2));
		}
	}

	public Container getResultContainer(ItemStack stack) {
		Container container = new SimpleContainer(3);
		ItemStack armor = stack.copy();

		Optional<ArmorTrim> trim = ArmorTrim.getTrim(level.registryAccess(), armor);
		if (trim.isPresent()) {
			ItemStack item = trim.get().material().get().ingredient().get().getDefaultInstance();
			ItemStack template = trim.get().pattern().get().templateItem().get().getDefaultInstance();

			template.getOrCreateTag().putBoolean("EmissiveTrim", armor.getOrCreateTag().getBoolean("EmissiveTrim"));
			template.getOrCreateTag().putBoolean("FadedTrim", armor.getOrCreateTag().getBoolean("FadedTrim"));

			armor.getOrCreateTag().remove("Trim");
			armor.getOrCreateTag().remove("EmissiveTrim");
			armor.getOrCreateTag().remove("FadedTrim");

			container.setItem(0, template);
			container.setItem(1, armor);
			container.setItem(2, item);
		} else {
			List<SmithingRecipe> list = this.level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING);
			for (SmithingRecipe recipe : list) {
				if (recipe instanceof SmithingTransformRecipe transform && transform.getResultItem(level.registryAccess()).is(armor.getItem())) {
					container.setItem(0, transform.template.getItems()[0].copy());
					container.setItem(1, transform.base.getItems()[0].copy());
					container.setItem(2, transform.addition.getItems()[0].copy());
				}
			}
		}

		return container;
	}

	@Override
	public int getSlotToQuickMoveTo(ItemStack p_266739_) {
		return this.findSlotMatchingIngredient(p_266739_).orElse(0);
	}

	private Optional<Integer> findSlotMatchingIngredient(ItemStack ingredient) {
		return this.areResultsEmpty() && !this.getResultContainer(ingredient).getItem(1).isEmpty() ? Optional.of(0) :
				ingredient.is(CCItems.SPINEL.get()) ? Optional.of(1) : Optional.empty();
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack p_40257_, Slot p_40258_) {
		return p_40258_.container != this.resultSlots && super.canTakeItemForPickAll(p_40257_, p_40258_);
	}

	@Override
	public boolean canMoveIntoInputSlots(ItemStack p_266846_) {
		return this.findSlotMatchingIngredient(p_266846_).isPresent();
	}
}