package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.caverns_and_chasms.core.mixin.ItemStackAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

import java.util.Optional;
import java.util.function.Supplier;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCItems.*;

public interface WeatheringCopperItem {
	Supplier<BiMap<Item, Item>> NEXT_BY_ITEM = Suppliers.memoize(() -> ImmutableBiMap.<Item, Item>builder()
			.put(Items.COPPER_INGOT, EXPOSED_COPPER_INGOT.get())
			.put(EXPOSED_COPPER_INGOT.get(), WEATHERED_COPPER_INGOT.get())
			.put(WEATHERED_COPPER_INGOT.get(), OXIDIZED_COPPER_INGOT.get())
			.put(COPPER_HELMET.get(), EXPOSED_COPPER_HELMET.get())
			.put(EXPOSED_COPPER_HELMET.get(), WEATHERED_COPPER_HELMET.get())
			.put(WEATHERED_COPPER_HELMET.get(), OXIDIZED_COPPER_HELMET.get())
			.put(COPPER_CHESTPLATE.get(), EXPOSED_COPPER_CHESTPLATE.get())
			.put(EXPOSED_COPPER_CHESTPLATE.get(), WEATHERED_COPPER_CHESTPLATE.get())
			.put(WEATHERED_COPPER_CHESTPLATE.get(), OXIDIZED_COPPER_CHESTPLATE.get())
			.put(COPPER_LEGGINGS.get(), EXPOSED_COPPER_LEGGINGS.get())
			.put(EXPOSED_COPPER_LEGGINGS.get(), WEATHERED_COPPER_LEGGINGS.get())
			.put(WEATHERED_COPPER_LEGGINGS.get(), OXIDIZED_COPPER_LEGGINGS.get())
			.put(COPPER_BOOTS.get(), EXPOSED_COPPER_BOOTS.get())
			.put(EXPOSED_COPPER_BOOTS.get(), WEATHERED_COPPER_BOOTS.get())
			.put(WEATHERED_COPPER_BOOTS.get(), OXIDIZED_COPPER_BOOTS.get())
			.put(COPPER_SWORD.get(), EXPOSED_COPPER_SWORD.get())
			.put(EXPOSED_COPPER_SWORD.get(), WEATHERED_COPPER_SWORD.get())
			.put(WEATHERED_COPPER_SWORD.get(), OXIDIZED_COPPER_SWORD.get())
			.put(COPPER_PICKAXE.get(), EXPOSED_COPPER_PICKAXE.get())
			.put(EXPOSED_COPPER_PICKAXE.get(), WEATHERED_COPPER_PICKAXE.get())
			.put(WEATHERED_COPPER_PICKAXE.get(), OXIDIZED_COPPER_PICKAXE.get())
			.put(COPPER_AXE.get(), EXPOSED_COPPER_AXE.get())
			.put(EXPOSED_COPPER_AXE.get(), WEATHERED_COPPER_AXE.get())
			.put(WEATHERED_COPPER_AXE.get(), OXIDIZED_COPPER_AXE.get())
			.put(COPPER_SHOVEL.get(), EXPOSED_COPPER_SHOVEL.get())
			.put(EXPOSED_COPPER_SHOVEL.get(), WEATHERED_COPPER_SHOVEL.get())
			.put(WEATHERED_COPPER_SHOVEL.get(), OXIDIZED_COPPER_SHOVEL.get())
			.put(COPPER_HOE.get(), EXPOSED_COPPER_HOE.get())
			.put(EXPOSED_COPPER_HOE.get(), WEATHERED_COPPER_HOE.get())
			.put(WEATHERED_COPPER_HOE.get(), OXIDIZED_COPPER_HOE.get())
			.build());

	Supplier<BiMap<Item, Item>> PREVIOUS_BY_ITEM = Suppliers.memoize(() -> NEXT_BY_ITEM.get().inverse());

	Supplier<BiMap<Item, Item>> WAXABLES = Suppliers.memoize(() -> ImmutableBiMap.<Item, Item>builder()
			.put(Items.COPPER_INGOT, WAXED_COPPER_INGOT.get()).put(EXPOSED_COPPER_INGOT.get(), WAXED_EXPOSED_COPPER_INGOT.get()).put(WEATHERED_COPPER_INGOT.get(), WAXED_WEATHERED_COPPER_INGOT.get()).put(OXIDIZED_COPPER_INGOT.get(), WAXED_OXIDIZED_COPPER_INGOT.get())

			.put(COPPER_HELMET.get(), WAXED_COPPER_HELMET.get()).put(EXPOSED_COPPER_HELMET.get(), WAXED_EXPOSED_COPPER_HELMET.get()).put(WEATHERED_COPPER_HELMET.get(), WAXED_WEATHERED_COPPER_HELMET.get()).put(OXIDIZED_COPPER_HELMET.get(), WAXED_OXIDIZED_COPPER_HELMET.get())
			.put(COPPER_CHESTPLATE.get(), WAXED_COPPER_CHESTPLATE.get()).put(EXPOSED_COPPER_CHESTPLATE.get(), WAXED_EXPOSED_COPPER_CHESTPLATE.get()).put(WEATHERED_COPPER_CHESTPLATE.get(), WAXED_WEATHERED_COPPER_CHESTPLATE.get()).put(OXIDIZED_COPPER_CHESTPLATE.get(), WAXED_OXIDIZED_COPPER_CHESTPLATE.get())
			.put(COPPER_LEGGINGS.get(), WAXED_COPPER_LEGGINGS.get()).put(EXPOSED_COPPER_LEGGINGS.get(), WAXED_EXPOSED_COPPER_LEGGINGS.get()).put(WEATHERED_COPPER_LEGGINGS.get(), WAXED_WEATHERED_COPPER_LEGGINGS.get()).put(OXIDIZED_COPPER_LEGGINGS.get(), WAXED_OXIDIZED_COPPER_LEGGINGS.get())
			.put(COPPER_BOOTS.get(), WAXED_COPPER_BOOTS.get()).put(EXPOSED_COPPER_BOOTS.get(), WAXED_EXPOSED_COPPER_BOOTS.get()).put(WEATHERED_COPPER_BOOTS.get(), WAXED_WEATHERED_COPPER_BOOTS.get()).put(OXIDIZED_COPPER_BOOTS.get(), WAXED_OXIDIZED_COPPER_BOOTS.get())

			.put(COPPER_SWORD.get(), WAXED_COPPER_SWORD.get()).put(EXPOSED_COPPER_SWORD.get(), WAXED_EXPOSED_COPPER_SWORD.get()).put(WEATHERED_COPPER_SWORD.get(), WAXED_WEATHERED_COPPER_SWORD.get()).put(OXIDIZED_COPPER_SWORD.get(), WAXED_OXIDIZED_COPPER_SWORD.get())
			.put(COPPER_PICKAXE.get(), WAXED_COPPER_PICKAXE.get()).put(EXPOSED_COPPER_PICKAXE.get(), WAXED_EXPOSED_COPPER_PICKAXE.get()).put(WEATHERED_COPPER_PICKAXE.get(), WAXED_WEATHERED_COPPER_PICKAXE.get()).put(OXIDIZED_COPPER_PICKAXE.get(), WAXED_OXIDIZED_COPPER_PICKAXE.get())
			.put(COPPER_AXE.get(), WAXED_COPPER_AXE.get()).put(EXPOSED_COPPER_AXE.get(), WAXED_EXPOSED_COPPER_AXE.get()).put(WEATHERED_COPPER_AXE.get(), WAXED_WEATHERED_COPPER_AXE.get()).put(OXIDIZED_COPPER_AXE.get(), WAXED_OXIDIZED_COPPER_AXE.get())
			.put(COPPER_SHOVEL.get(), WAXED_COPPER_SHOVEL.get()).put(EXPOSED_COPPER_SHOVEL.get(), WAXED_EXPOSED_COPPER_SHOVEL.get()).put(WEATHERED_COPPER_SHOVEL.get(), WAXED_WEATHERED_COPPER_SHOVEL.get()).put(OXIDIZED_COPPER_SHOVEL.get(), WAXED_OXIDIZED_COPPER_SHOVEL.get())
			.put(COPPER_HOE.get(), WAXED_COPPER_HOE.get()).put(EXPOSED_COPPER_HOE.get(), WAXED_EXPOSED_COPPER_HOE.get()).put(WEATHERED_COPPER_HOE.get(), WAXED_WEATHERED_COPPER_HOE.get()).put(OXIDIZED_COPPER_HOE.get(), WAXED_OXIDIZED_COPPER_HOE.get())
			.build());

	Supplier<BiMap<Item, Item>> WAX_OFF_BY_ITEM = Suppliers.memoize(() -> WAXABLES.get().inverse());

	static Optional<ItemStack> getWaxed(ItemStack stack) {
		return Optional.ofNullable(WAXABLES.get().get(stack.getItem())).map(ItemStack::new);
	}

	static Optional<ItemStack> getUnwaxed(ItemStack stack) {
		return Optional.ofNullable(WAX_OFF_BY_ITEM.get().get(stack.getItem())).map(ItemStack::new);
	}

	WeatherState getAge();

	default void updateOxidation(ItemStack stack, Level level) {
		if (this.getNext(stack).isPresent() && level.getGameTime() % 60 == 0) {
			for (int i = 0; i < level.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get(); i++) {
				Optional<ItemStack> next = this.getNext(stack);
				if (next.isPresent()) {
					RandomSource random = level.getRandom();
					float chance = 0.05688889F * 0.01F;
					if (this.getAge() == WeatherState.UNAFFECTED) {
						chance *= 0.75F;
					}

					if (random.nextFloat() < chance) {
						copyStackToNewItem(stack, next.get());
					}
				}
			}
		}
	}

	static void copyStackToNewItem(ItemStack original, ItemStack newItem) {
		((ItemStackAccessor) (Object) original).setDelegate(((ItemStackAccessor) (Object) newItem).getDelegate());
	}

	default Optional<ItemStack> getNext(ItemStack stack) {
		return getNext(stack.getItem()).map((nextItem) -> new ItemStack(nextItem, stack.getCount(), stack.getOrCreateTag().copy()));
	}

	static Optional<ItemStack> getPrevious(ItemStack stack) {
		return getPrevious(stack.getItem()).map((prevStack) -> new ItemStack(prevStack, stack.getCount(), stack.getOrCreateTag().copy()));
	}

	static ItemStack getFirst(ItemStack stack) {
		return new ItemStack(getFirst(stack.getItem()), stack.getCount(), stack.getOrCreateTag().copy());
	}

	static Optional<Item> getNext(Item item) {
		return Optional.ofNullable(NEXT_BY_ITEM.get().get(item));
	}

	static Optional<Item> getPrevious(Item item) {
		return Optional.ofNullable(PREVIOUS_BY_ITEM.get().get(item));
	}

	static Item getFirst(Item item) {
		Item firstItem = item;

		for (Item previousItem = PREVIOUS_BY_ITEM.get().get(item); previousItem != null; previousItem = PREVIOUS_BY_ITEM.get().get(previousItem)) {
			firstItem = previousItem;
		}

		return firstItem;
	}

}
