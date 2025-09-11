package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.teamabnormals.caverns_and_chasms.core.mixin.ItemStackAccessor;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

import java.util.Optional;
import java.util.function.Supplier;

public interface WeatheringCopperItem {
	Supplier<BiMap<Item, Item>> NEXT_BY_ITEM = Suppliers.memoize(() -> {
		return ImmutableBiMap.<Item, Item>builder()
				.put(CCItems.COPPER_HELMET.get(), CCItems.EXPOSED_COPPER_HELMET.get())
				.put(CCItems.EXPOSED_COPPER_HELMET.get(), CCItems.WEATHERED_COPPER_HELMET.get())
				.put(CCItems.WEATHERED_COPPER_HELMET.get(), CCItems.OXIDIZED_COPPER_HELMET.get())
				.put(CCItems.COPPER_CHESTPLATE.get(), CCItems.EXPOSED_COPPER_CHESTPLATE.get())
				.put(CCItems.EXPOSED_COPPER_CHESTPLATE.get(), CCItems.WEATHERED_COPPER_CHESTPLATE.get())
				.put(CCItems.WEATHERED_COPPER_CHESTPLATE.get(), CCItems.OXIDIZED_COPPER_CHESTPLATE.get())
				.put(CCItems.COPPER_LEGGINGS.get(), CCItems.EXPOSED_COPPER_LEGGINGS.get())
				.put(CCItems.EXPOSED_COPPER_LEGGINGS.get(), CCItems.WEATHERED_COPPER_LEGGINGS.get())
				.put(CCItems.WEATHERED_COPPER_LEGGINGS.get(), CCItems.OXIDIZED_COPPER_LEGGINGS.get())
				.put(CCItems.COPPER_BOOTS.get(), CCItems.EXPOSED_COPPER_BOOTS.get())
				.put(CCItems.EXPOSED_COPPER_BOOTS.get(), CCItems.WEATHERED_COPPER_BOOTS.get())
				.put(CCItems.WEATHERED_COPPER_BOOTS.get(), CCItems.OXIDIZED_COPPER_BOOTS.get())
				.put(CCItems.COPPER_SWORD.get(), CCItems.EXPOSED_COPPER_SWORD.get())
				.put(CCItems.EXPOSED_COPPER_SWORD.get(), CCItems.WEATHERED_COPPER_SWORD.get())
				.put(CCItems.WEATHERED_COPPER_SWORD.get(), CCItems.OXIDIZED_COPPER_SWORD.get())
				.put(CCItems.COPPER_PICKAXE.get(), CCItems.EXPOSED_COPPER_PICKAXE.get())
				.put(CCItems.EXPOSED_COPPER_PICKAXE.get(), CCItems.WEATHERED_COPPER_PICKAXE.get())
				.put(CCItems.WEATHERED_COPPER_PICKAXE.get(), CCItems.OXIDIZED_COPPER_PICKAXE.get())
				.put(CCItems.COPPER_AXE.get(), CCItems.EXPOSED_COPPER_AXE.get())
				.put(CCItems.EXPOSED_COPPER_AXE.get(), CCItems.WEATHERED_COPPER_AXE.get())
				.put(CCItems.WEATHERED_COPPER_AXE.get(), CCItems.OXIDIZED_COPPER_AXE.get())
				.put(CCItems.COPPER_SHOVEL.get(), CCItems.EXPOSED_COPPER_SHOVEL.get())
				.put(CCItems.EXPOSED_COPPER_SHOVEL.get(), CCItems.WEATHERED_COPPER_SHOVEL.get())
				.put(CCItems.WEATHERED_COPPER_SHOVEL.get(), CCItems.OXIDIZED_COPPER_SHOVEL.get())
				.put(CCItems.COPPER_HOE.get(), CCItems.EXPOSED_COPPER_HOE.get())
				.put(CCItems.EXPOSED_COPPER_HOE.get(), CCItems.WEATHERED_COPPER_HOE.get())
				.put(CCItems.WEATHERED_COPPER_HOE.get(), CCItems.OXIDIZED_COPPER_HOE.get())
				.build();
	});

	Supplier<BiMap<Item, Item>> PREVIOUS_BY_ITEM = Suppliers.memoize(() -> NEXT_BY_ITEM.get().inverse());

	WeatherState getAge();

	default void updateOxidation(ItemStack stack, Level level) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.getBoolean("waxed") && this.getNext(stack).isPresent() && level.getGameTime() % 20 == 0) {
			for (int i = 0; i < level.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get(); i++) {
				Optional<ItemStack> next = this.getNext(stack);
				if (next.isPresent()) {
					RandomSource random = level.getRandom();
					float chance = 0.05688889F * 0.01F;
					if (this.getAge() == WeatherState.UNAFFECTED) {
						chance *= 0.75F;
					}

					if (random.nextFloat() < chance) {
						((ItemStackAccessor) (Object) stack).setDelegate(((ItemStackAccessor) (Object) next.get()).getDelegate());
					}
				}
			}
		}
	}

	default String getOrCreateDescriptionId(ItemStack stack) {
		return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).withPrefix(stack.getOrCreateTag().getBoolean("waxed") ? "waxed_" : ""));
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
