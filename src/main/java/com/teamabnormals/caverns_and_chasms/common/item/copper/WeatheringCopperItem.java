package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.OxidizableItem;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataMaps.WaxableItem;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

import java.util.Optional;

public interface WeatheringCopperItem {

	static Optional<Item> getWaxed(Item item) {
		WaxableItem waxable = item.builtInRegistryHolder().getData(CCDataMaps.WAXABLES);
		return Optional.ofNullable(waxable.waxed());
	}

	static Optional<Item> getUnwaxed(Item item) {
		return Optional.ofNullable(CCDataMaps.INVERSE_WAXABLES_DATAMAP.get(item));
	}

	WeatherState getAge();

	default void updateOxidation(LivingEntity entity, EquipmentSlot slot, ItemStack stack, Level level) {
		if (!level.isClientSide() && this.getNext(stack).isPresent() && level.getGameTime() % 60 == 0) {
			for (int i = 0; i < level.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get(); i++) {
				Optional<ItemStack> next = this.getNext(stack);
				if (next.isPresent()) {
					RandomSource random = level.getRandom();
					float chance = 0.05688889F * 0.01F;
					if (this.getAge() == WeatherState.UNAFFECTED) {
						chance *= 0.75F;
					}

					if (random.nextFloat() < chance) {
						copyStackToNewItem(entity, slot, stack, next.get());
					}
				}
			}
		}
	}

	static void copyStackToNewItem(LivingEntity entity, EquipmentSlot slot, ItemStack original, ItemStack newItem) {
		newItem.applyComponents(original.getComponentsPatch());
		entity.setItemSlot(slot, newItem);
	}

	default Optional<ItemStack> getNext(ItemStack stack) {
		return getNext(stack.getItem()).map((nextItem) -> new ItemStack(Holder.direct(nextItem), stack.getCount(), stack.getComponentsPatch()));
	}

	static Optional<ItemStack> getPrevious(ItemStack stack) {
		return getPrevious(stack.getItem()).map((prevStack) -> new ItemStack(Holder.direct(prevStack), stack.getCount(), stack.getComponentsPatch()));
	}

	static ItemStack getFirst(ItemStack stack) {
		return new ItemStack(Holder.direct(getFirst(stack.getItem())), stack.getCount(), stack.getComponentsPatch());
	}

	static Optional<Item> getNext(Item item) {
		OxidizableItem oxidizable = item.builtInRegistryHolder().getData(CCDataMaps.OXIDIZABLES);
		return Optional.ofNullable(oxidizable.nextOxidationStage());
	}

	static Optional<Item> getPrevious(Item item) {
		return Optional.ofNullable(CCDataMaps.INVERSE_OXIDIZABLES_DATAMAP.get(item));
	}

	static Item getFirst(Item item) {
		Item firstItem = item;

		for (Item previousItem = CCDataMaps.INVERSE_OXIDIZABLES_DATAMAP.get(item); previousItem != null; previousItem = CCDataMaps.INVERSE_OXIDIZABLES_DATAMAP.get(previousItem)) {
			firstItem = previousItem;
		}

		return firstItem;
	}

}
